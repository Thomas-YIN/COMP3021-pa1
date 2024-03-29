package hk.ust.cse.comp3021.pa1.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link EntityCell} which stops the {@link Player} from sliding further.
 */
public final class StopCell extends EntityCell {

    /**
     * Creates an instance of {@link StopCell} at the given game board position.
     *
     * @param position The position where this cell belongs at.
     */
    public StopCell(@NotNull final Position position) {
        // TODO done
        super(position);
    }

    /**
     * Creates an instance of {@link StopCell} at the given game board position.
     *
     * @param position      The position where this cell belongs at.
     * @param initialEntity The initial entity present in this cell.
     */
    public StopCell(@NotNull final Position position, @Nullable final Entity initialEntity) {
        // TODO done
        super(position, initialEntity);

    }

    /**
     * Same as {@link EntityCell#setEntity(Entity)}, with additional checking.
     *
     * @throws IllegalArgumentException if the entity is not {@code null} and not an instance of {@link Player}.
     */
    @Nullable
    @Override
    public Entity setEntity(@Nullable final Entity newEntity) {
        // TODO done
        if(newEntity != null && !(newEntity instanceof Player)){
            throw new IllegalArgumentException();
        }else{
            var previousEntity = this.entity;
            if(this.entity != null){
                this.entity.setOwner(null);
            }
            this.entity = newEntity;
            if(newEntity != null){
                this.entity.setOwner(this);
            }
            return previousEntity;
        }
    }

    /**
     * Replaces the player on this {@link StopCell} with {@code newPlayer}.
     *
     * <p>
     * This method should perform the same action as {@link EntityCell#setEntity(Entity)}, except that the parameter and
     * return value are both changed to {@link Player}.
     * </p>
     *
     * @param newPlayer The new player of this cell.
     * @return The previous player occupying this cell, or {@code null} if no player was previously occupying this cell.
     */
    @Nullable
    public Player setPlayer(@Nullable final Player newPlayer) {
        // TODO done
        return (Player) this.setEntity(newPlayer);
    }

    @Override
    public char toUnicodeChar() {
        return getEntity() != null ? getEntity().toUnicodeChar() : '\u25A1';
    }

    @Override
    public char toASCIIChar() {
        return getEntity() != null ? getEntity().toASCIIChar() : '#';
    }
}
