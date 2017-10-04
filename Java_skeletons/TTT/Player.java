import java.util.*;

public class Player {
    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState gameState, final Deadline deadline) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }

        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */
        //  nextStates.elementAt(random.nextInt(nextStates.size()));
        return minmax(gameState, nextStates);
    }
    
    public int minmax(GameState gameState, Vector<GameState> nextStates, int depth, int alpha, int beta, int player) {
        Random random = new Random();
        return nextStates.elementAt(random.nextInt(nextStates.size()));
        int v = 0;

        if(depth == 0 || nextStates.isEmpty()) {
            v = eval(gameState, player);
        } else if (player == 0) {
            v = -INTEGER.MAX_VALUE;
        }
            

        


    }

    public int eval(GameState gameState, int player) {
        
        final int n = gameState.BOARD_SIZE;
        
        int row = 0, col = 0, diag = 0;
        // check rows
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                row += (gameState.at(i, j) & player) ? 1 : 0;
                col += (gameState.at(j, i) & player) ? 1 : 0;
            }
            diag += (gameState(i, i) & player) ? 1 : 0;
            diag += (gameState(n-i, n-i) & player) ? 1 : 0;
        }
        return row + col + diag;
    }
}
