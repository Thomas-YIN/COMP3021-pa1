package hk.ust.cse.comp3021.pa1.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link java.util.Stack}-like data structure to track all valid moves made by a player.
 *
 * <p>A stack is a data structure which enforces Last-In First-Out (LIFO) ordering of its elements.</p>
 *
 * <p>You can read more about stacks <a href="https://en.wikipedia.org/wiki/Stack_(abstract_data_type)">here</a>.</p>
 */
public class MoveStack {

    @NotNull
    private final List<MoveResult> moves = new ArrayList<>();

    private int popCount = 0;

    /**
     * Pushes a move to this stack.
     *
     * <p>
     * In our testing, we will only push instances of {@link MoveResult.Valid.Alive}; How you handle other
     * sealed-subclasses of {@link MoveResult} is entirely up to you.
     * </p>
     *
     * @param move The move to push into this stack.
     */
    public void push(@NotNull final MoveResult move) {
        // TODO done
        //First handle the valid alive moves
        if(move instanceof MoveResult.Valid.Alive){
            moves.add(move);
        }
        //TODO: Handle other type of moves by throwing exceptions?
        else if(move instanceof MoveResult.Invalid){
            throw new IllegalArgumentException("Invalid move!");
        }
        else if(move instanceof MoveResult.Valid.Dead){
            //moves.add(move);
            throw new IllegalArgumentException("You died!");
        }
    }

    /**
     * @return Whether the stack is currently empty.
     */
    public boolean isEmpty() {
        // TODO done
        return moves.isEmpty();
    }

    /**
     * Pops a move from this stack.
     *
     * @return The instance of {@link MoveResult} last performed by the player.
     */
    @NotNull
    public MoveResult pop() {
        // TODO done
        if(!moves.isEmpty()){
            var top = moves.get(moves.size()-1);
            moves.remove(moves.size()-1);
            this.popCount++;
            return top;
        }else{
            throw new IllegalArgumentException("No moves have been performed yet!");
        }
    }

    /**
     * @return The number of {@link MoveStack#pop} calls invoked.
     */
    public int getPopCount() {
        // TODO done
        return this.popCount;
    }

    /**
     * Peeks the topmost of the element of the stack.
     *
     * <p>
     * This method is intended for internal use only, although we will not stop you if you want to use this method in
     * your implementation.
     * </p>
     *
     * @return The instance of {@link MoveResult} at the top of the stack, corresponding to the last move performed by
     * the player.
     */
    @NotNull
    public MoveResult peek() {
        // TODO done
        if(!moves.isEmpty()){
            return moves.get(moves.size()-1);
        }else{
            throw new IllegalArgumentException("No moves have been performed yet!");
        }
    }
}
