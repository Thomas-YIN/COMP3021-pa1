package hk.ust.cse.comp3021.pa1.model;

import org.jetbrains.annotations.NotNull;

/**
 * The main game board of the game.
 *
 * <p>
 * The top-left hand corner of the game board is the "origin" of the board (0, 0).
 * </p>
 */
public final class GameBoard {

    /**
     * Number of rows in the game board.
     */
    private final int numRows;
    /**
     * Number of columns in the game board.
     */
    private final int numCols;

    /**
     * 2D array representing each cell in the game board.
     */
    @NotNull
    private final Cell[][] board;

    /**
     * The instance of {@link Player} on this game board.
     */
    @NotNull
    private final Player player;

    /**
     * Creates an instance using the provided creation parameters.
     *
     * @param numRows The number of rows in the game board.
     * @param numCols The number of columns in the game board.
     * @param cells   The initial values of cells.
     * @throws IllegalArgumentException if any of the following are true:
     *                                  <ul>
     *                                      <li>{@code numRows} is not equal to {@code cells.length}</li>
     *                                      <li>{@code numCols} is not equal to {@code cells[0].length}</li>
     *                                      <li>There is no player or more than one player in {@code cells}</li>
     *                                      <li>There are no gems in {@code cells}</li>
     *                                      <li>There are some gems which cannot be reached by the player</li>
     *                                  </ul>
     */
    public GameBoard(final int numRows, final int numCols, @NotNull final Cell[][] cells) {
        // TODO
        //Inspect the passed-in cells
        int num_players = 0;
        int num_gems = 0;
        Player target = null;
        for(int r=0; r<numRows; r++){
            for(int c=0; c<numCols; c++){
                //EntityCell toEntityCell = (EntityCell) cells[r][c];
                if(cells[r][c] instanceof EntityCell toEntityCell){
                    if(toEntityCell.getEntity() instanceof Player) {
                        num_players++;
                        target = (Player) toEntityCell.getEntity();
                    }

                    if(toEntityCell.getEntity() instanceof Gem) num_gems++;
                }
            }
        }
        //Incompatibility of game board size
        if(numRows != cells.length || numCols != cells[0].length){
            throw new IllegalArgumentException("Game board size does not match!");
        }
        //Invalid number of players
        else if(num_players != 1){
            throw new IllegalArgumentException("Invalid number of players!");
        }
        //Invalid initialization of gems
        else if(num_gems == 0){
            throw new IllegalArgumentException("There are no gems in the game board!");
        }
        //TODO: Unreachable gems exist(flood fill algorithm)

        //Assign values to the GameBoard
        this.numRows = numRows;
        this.numCols = numCols;
        this.board = cells;
        this.player = target;
        //Prepare for FF
        char[][] paint = paint(cells);
        floodfill(paint, this.player.getOwner().getPosition().row(), this.player.getOwner().getPosition().col(), 'P');
        int reachable=0;
        for(int r=0; r<this.numRows; r++){
            for(int c=0; c<this.numCols; c++){
                if(paint[r][c] == 'P' && ((EntityCell) cells[r][c]).getEntity() instanceof Gem){
                    reachable++;
                }
            }
        }
        if(reachable != num_gems){
            throw new IllegalArgumentException("There are unreachable gems in the board!");
        }

    }

    //Label the cells with a "color"
    private char[][] paint(Cell[][] cells){
        char[][] painted = new char[numRows][numCols];
        for(int r=0; r<numRows; r++){
            for(int c=0; c<numCols; c++){
                if(cells[r][c] instanceof Wall){
                    painted[r][c] = 'W';
                }else if(((EntityCell) cells[r][c]).getEntity() instanceof Player){
                    painted[r][c] = 'P';
                }else{
                    painted[r][c] = 'O';
                }

            }
        }
        return painted;
    }

    private boolean isSafe(char[][] mat, int x, int y, char target){
        return x >= 0 && x < mat.length && y >= 0 && y < mat[0].length && mat[x][y] == target;
    }

    private void floodfill(char[][] mat, int x, int y, char replacement) {

        final int[] row = {-1, 0, 0, 1};
        final int[] col = {0, -1, 1, 0};
        // base case
        if (mat == null || mat.length == 0) {
            return;
        }

        // get the target color
        char target = mat[x][y];

        // target color is same as replacement
        if (target == replacement) {
            return;
        }

        // replace the current pixel color with that of replacement
        mat[x][y] = replacement;

        // process all eight adjacent pixels of the current pixel and
        // recur for each valid pixel
        for (int k = 0; k < 4; k++) {
            // if the adjacent pixel at position (x + row[k], y + col[k]) is
            // a valid pixel and has the same color as that of the current pixel
            if (isSafe(mat, x + row[k], y + col[k], target)) {
                floodfill(mat, x + row[k], y + col[k], replacement);
            }
        }
    }

    /**
     * Returns the {@link Cell}s of a single row of the game board.
     *
     * @param r Row index.
     * @return 1D array representing the row. The first element in the array corresponds to the leftmost element of the
     * row.
     */
    @NotNull
    public Cell[] getRow(final int r) {
        // TODO done
        return this.board[r];
    }

    /**
     * Returns the {@link Cell}s of a single column of the game board.
     *
     * @param c Column index.
     * @return 1D array representing the column. The first element in the array corresponds to the topmost element of
     * the row.
     */
    @NotNull
    public Cell[] getCol(final int c) {
        // TODO done
        Cell[] colToReturn = new Cell[numRows];
        for(int i=0; i<numRows; i++){
            colToReturn[i] = this.board[i][c];
        }
        return colToReturn;
    }

    /**
     * Returns a single cell of the game board.
     *
     * @param r Row index.
     * @param c Column index.
     * @return The {@link Cell} instance at the specified location.
     */
    @NotNull
    public Cell getCell(final int r, final int c) {
        // TODO done
        return this.board[r][c];
    }

    /**
     * Returns a single cell of the game board.
     *
     * @param position The position object representing the location of the cell.
     * @return The {@link Cell} instance at the specified location.
     */
    @NotNull
    public Cell getCell(@NotNull final Position position) {
        // TODO done
        return this.board[position.row()][position.col()];
    }

    /**
     * Returns an {@link EntityCell} on the game board.
     *
     * <p>
     * This method is a convenience method for getting a cell which is unconditionally known to be an entity cell.
     * </p>
     *
     * @param r Row index.
     * @param c Column index.
     * @return The {@link EntityCell} instance at the specified location.
     * @throws IllegalArgumentException if the cell at the specified position is not an instance of {@link EntityCell}.
     */
    @NotNull
    public EntityCell getEntityCell(final int r, final int c) {
        // TODO done(Q: What would be the value of entity once a Cell obj is converted to EntityCell obj?)
        Cell asCell = this.board[r][c];
        EntityCell asEntityCell = (EntityCell) asCell;
        if(asEntityCell.entity == null){
            throw new IllegalArgumentException("The given position is not an instance of EntityCell!");
        }
        return asEntityCell;
    }

    /**
     * Returns an {@link EntityCell} on the game board.
     *
     * <p>
     * This method is a convenience method for getting a cell which is unconditionally known to be an entity cell.
     * </p>
     *
     * @param position The position object representing the location of the cell.
     * @return The {@link EntityCell} instance at the specified location.
     * @throws IllegalArgumentException if the cell at the specified position is not an instance of {@link EntityCell}.
     */
    @NotNull
    public EntityCell getEntityCell(@NotNull final Position position) {
        // TODO
        Cell asCell = this.board[position.row()][position.col()];
        EntityCell asEntityCell = (EntityCell) asCell;
        if(asEntityCell.entity == null){
            throw new IllegalArgumentException("The given position is not an instance of EntityCell!");
        }
        return asEntityCell;
    }

    /**
     * @return The number of rows of this game board.
     */
    public int getNumRows() {
        // TODO done
        return this.numRows;
    }

    /**
     * @return The number of columns of this game board.
     */
    public int getNumCols() {
        // TODO done
        return this.numCols;
    }

    /**
     * @return The player instance.
     */
    @NotNull
    public Player getPlayer() {
        // TODO done
        return this.player;
    }

    /**
     * @return The number of gems still present in the game board.
     */
    public int getNumGems() {
        // TODO done
        int numOfGems = 0;
        for(int i=0; i<numRows; i++){
            for(int j=0; j<numCols; j++){
                EntityCell current = (EntityCell) this.board[i][j];
                if(current.getEntity() != null && current.getEntity().getClass() == Gem.class){
                    numOfGems++;
                }
            }
        }
        return numOfGems;
    }
}
