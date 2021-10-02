package hk.ust.cse.comp3021.pa1.controller;

import hk.ust.cse.comp3021.pa1.model.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for {@link GameBoard}.
 *
 * <p>
 * This class is responsible for providing high-level operations to mutate a {@link GameBoard}. This should be the only
 * class which mutates the game board; Other classes should use this class to mutate the game board.
 * </p>
 */
public class GameBoardController {

    @NotNull
    private final GameBoard gameBoard;

    /**
     * Creates an instance.
     *
     * @param gameBoard The instance of {@link GameBoard} to control.
     */
    public GameBoardController(@NotNull final GameBoard gameBoard) {
        // TODO done
        this.gameBoard = gameBoard;
    }

    /**
     * Moves the player in the given direction.
     *
     * <p>
     * You should ensure that the game board is only mutated if the move is valid and results in the player still being
     * alive. If the player dies after moving or the move is invalid, the game board should remain in the same state as
     * before this method was called.
     * </p>
     *
     * @param direction Direction to move the player in.
     * @return An instance of {@link MoveResult} representing the result of this action.
     */
    @NotNull
    public MoveResult makeMove(@NotNull final Direction direction) {
        // TODO done
        if(this.gameBoard.getPlayer().getOwner() == null){
            throw new IllegalArgumentException("The player seems not on the game board!");
        }
        final Position OrigPos = this.gameBoard.getPlayer().getOwner().getPosition();
        //The immediate new position of the move.
        Position newPos = OrigPos.offsetByOrNull(direction.getOffset(),
                                                 this.gameBoard.getNumRows(),
                                                 this.gameBoard.getNumCols());
        //Invalid cases
        if(newPos == null || this.gameBoard.getCell(newPos) instanceof Wall){
            return new MoveResult.Invalid(OrigPos);
        }
        //Valid cases
        List<Position> gems = new ArrayList<>();
        List<Position> extralives = new ArrayList<>();
        @Nullable Entity entityOnNewPos = null; //Stores the entity on the new cell
        Position adjPos = OrigPos; //Keeps track of the last cell position

        while(true){
            //Termination#1: The new cell is out of bounds
            if(newPos == null){
                //Move the player on the board
                ((EntityCell) this.gameBoard.getCell(OrigPos)).setEntity(null);
                ((EntityCell) this.gameBoard.getCell(adjPos)).setEntity(this.gameBoard.getPlayer());
                return new MoveResult.Valid.Alive(adjPos, OrigPos, gems, extralives);
            }else {
                if(this.gameBoard.getCell(newPos) instanceof EntityCell newEntityCell){
                    entityOnNewPos = newEntityCell.getEntity();
                }
                //Termination#2: The new cell contains a mine --> valid dead move
                if (entityOnNewPos instanceof Mine) {
                    //give back all the collected items(gems, extralives)
                    for(Position p_gem : gems){
                        ((EntityCell) this.gameBoard.getCell(p_gem)).setEntity(new Gem());
                    }
                    for(Position p_extralives : extralives){
                        ((EntityCell) this.gameBoard.getCell(p_extralives)).setEntity(new ExtraLife());
                    }
                    return new MoveResult.Valid.Dead(OrigPos, newPos);
                }
                //Termination#3: The new cell is a Wall
                else if (this.gameBoard.getCell(newPos) instanceof Wall) {
                    ((EntityCell) this.gameBoard.getCell(OrigPos)).setEntity(null);
                    ((EntityCell) this.gameBoard.getCell(adjPos)).setEntity(this.gameBoard.getPlayer());
                    return new MoveResult.Valid.Alive(adjPos, OrigPos, gems, extralives);
                }
                //Termination#4: The new cell is a stop cell
                else if (this.gameBoard.getCell(newPos) instanceof StopCell) {
                    ((EntityCell) this.gameBoard.getCell(OrigPos)).setEntity(null);
                    //((EntityCell) this.gameBoard.getCell(newPos)).setEntity(this.gameBoard.getPlayer());
                    ((StopCell) this.gameBoard.getCell(newPos)).setPlayer(this.gameBoard.getPlayer());
                    return new MoveResult.Valid.Alive(newPos, OrigPos, gems, extralives);
                }
                //not terminate yet
                else {
                    //picks up gems
                    if (entityOnNewPos instanceof Gem) {
                        //Remove the gem in the cell
                        ((EntityCell) this.gameBoard.getCell(newPos)).setEntity(null);
                        gems.add(newPos);
                    }
                    //picks up extra lives
                    else if (entityOnNewPos instanceof ExtraLife) {
                        ((EntityCell) this.gameBoard.getCell(newPos)).setEntity(null);
                        extralives.add(newPos);
                    }
                    //empty cell (nothing to do)
                }
                //Update newPos and adjPos to check the next cell
                adjPos = newPos;
                newPos = newPos.offsetByOrNull(direction.getOffset(), this.gameBoard.getNumRows(), this.gameBoard.getNumCols());
            }
        }
    }

    /**
     * Undoes a move by reverting all changes performed by the specified move.
     *
     * <p>
     * Hint: Undoing a move is effectively the same as reversing everything you have done to make a move.
     * </p>
     *
     * @param prevMove The {@link MoveResult} object to revert.
     */
    public void undoMove(@NotNull final MoveResult prevMove) {
        // TODO
        //Only when a valid alive move is made does the game board change
        if(prevMove instanceof MoveResult.Valid.Alive){
            Position OrigPos = ((MoveResult.Valid.Alive) prevMove).origPosition;
            Position newPos = prevMove.newPosition;
            //restore the player's position
            ((EntityCell) this.gameBoard.getCell(newPos)).setEntity(null);
            ((EntityCell) this.gameBoard.getCell(OrigPos)).setEntity(this.gameBoard.getPlayer());
            //restore the picked up items
            for(Position p_gem : ((MoveResult.Valid.Alive) prevMove).collectedGems){
                ((EntityCell) this.gameBoard.getCell(p_gem)).setEntity(new Gem());
            }
            for(Position p_extralives : ((MoveResult.Valid.Alive) prevMove).collectedExtraLives){
                ((EntityCell) this.gameBoard.getCell(p_extralives)).setEntity(new ExtraLife());
            }
        }
    }
}
