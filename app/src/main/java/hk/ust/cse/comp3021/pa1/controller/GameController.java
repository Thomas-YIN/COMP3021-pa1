package hk.ust.cse.comp3021.pa1.controller;

import hk.ust.cse.comp3021.pa1.model.*;
import org.jetbrains.annotations.NotNull;


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
        // TODO
        MoveResult result = this.gameState.getGameBoardController().makeMove(direction);
        if(result instanceof MoveResult.Valid.Dead){
            this.gameState.incrementNumMoves();
            this.gameState.decrementNumLives();
            this.gameState.incrementNumDeaths();
            //this.gameState.getMoveStack().push(result);
        }else if(result instanceof MoveResult.Valid.Alive){
            this.gameState.incrementNumMoves();
            this.gameState.increaseNumLives(((MoveResult.Valid.Alive) result).collectedExtraLives.size());
            this.gameState.getMoveStack().push(result);
        }
        return result;
    }

    /**
     * Processes an Undo action performed by the player.
     *
     * @return {@code false} if there are no steps to undo.
     */
    public boolean processUndo() {
        // TODO
        if(this.gameState.getMoveStack().isEmpty())
            return false;
        else{
            //Note that prev can only be a Valid Alive move,
            //because everything pushed in the MoveStack is instance of MoveResult.Valid.Alive
            MoveResult prev = this.gameState.getMoveStack().pop();
            this.gameState.getGameBoardController().undoMove(prev);
            this.gameState.decreaseNumLives(((MoveResult.Valid.Alive) prev).collectedExtraLives.size());
            return true;
        }
    }
}
