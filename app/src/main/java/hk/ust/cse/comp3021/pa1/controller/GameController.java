package hk.ust.cse.comp3021.pa1.controller;

import hk.ust.cse.comp3021.pa1.model.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for {@link hk.ust.cse.comp3021.pa1.InertiaTextGame}.
 *
 * <p>
 * All game state mutations should be performed by this class.
 * </p>
 */
public class GameController {

    @NotNull
    private final GameState gameState;

    /**
     * Creates an instance.
     *
     * @param gameState The instance of {@link GameState} to control.
     */
    public GameController(@NotNull final GameState gameState) {
        // TODO done
        this.gameState = gameState;
    }

    /**
     * Processes a Move action performed by the player.
     *
     * @param direction The direction the player wants to move to.
     * @return An instance of {@link MoveResult} indicating the result of the action.
     */
    public MoveResult processMove(@NotNull final Direction direction) {
        // TODO Q: Same implementation by copying code??
        if(this.gameState.getGameBoard().getPlayer().getOwner() == null){
            throw new IllegalArgumentException("The player seems not on the game board!");
        }
        final Position OrigPos = this.gameState.getGameBoard().getPlayer().getOwner().getPosition();
        //The immediate new position of the move.
        Position newPos = OrigPos.offsetByOrNull(direction.getOffset(),
                this.gameState.getGameBoard().getNumRows(),
                this.gameState.getGameBoard().getNumCols());
        //Invalid cases
        if(newPos == null || this.gameState.getGameBoard().getCell(newPos) instanceof Wall){
            return new MoveResult.Invalid(OrigPos);
        }
        //Valid cases
        List<Position> gems = new ArrayList<>();
        List<Position> extralives = new ArrayList<>();
        Entity entityOnNewPos; //Stores the entity on the new cell
        Position adjPos = OrigPos; //Keeps track of the last cell position
        this.gameState.incrementNumMoves();
        while(true){
            //Termination#1: The new cell is out of bounds
            if(newPos == null){

                return new MoveResult.Valid.Alive(OrigPos, adjPos, gems, extralives);
            }else {
                entityOnNewPos = ((EntityCell) this.gameState.getGameBoard().getCell(newPos)).getEntity();
                //Termination#2: The new cell contains a mine --> valid dead move
                if (entityOnNewPos instanceof Mine) {
                    return new MoveResult.Valid.Dead(adjPos, newPos);
                }
                //Termination#3: The new cell is a Wall
                else if (this.gameState.getGameBoard().getCell(newPos) instanceof Wall) {
                    return new MoveResult.Valid.Alive(OrigPos, adjPos, gems, extralives);
                }
                //Termination#4: The new cell is a stop cell
                else if (this.gameState.getGameBoard().getCell(newPos) instanceof StopCell) {
                    return new MoveResult.Valid.Alive(OrigPos, newPos, gems, extralives);
                }
                //not terminate yet
                else {
                    //picks up gems
                    if (entityOnNewPos instanceof Gem) {
                        gems.add(newPos);
                    }
                    //picks up extra lives
                    else if (entityOnNewPos instanceof ExtraLife) {
                        extralives.add(newPos);
                    }
                    //empty cell (nothing to do)
                }
                //Update newPos and adjPos to check the next cell
                this.gameState.incrementNumMoves();
                adjPos = newPos;
                newPos = newPos.offsetByOrNull(direction.getOffset(), this.gameState.getGameBoard().getNumRows(), this.gameState.getGameBoard().getNumCols());
            }
        }
        //return null;
    }

    /**
     * Processes an Undo action performed by the player.
     *
     * @return {@code false} if there are no steps to undo.
     */
    public boolean processUndo() {
        // TODO
        return false;
    }
}
