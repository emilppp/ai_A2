import java.util.*;
import java.lang.Math;

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
        int alpha = -Integer.MAX_VALUE;
        int beta = Integer.MAX_VALUE;

        int depth = 1;

        //Random random = new Random();
       // return nextStates.elementAt(random.nextInt(nextStates.size()));

       int index = 0;
       int max = 0;

       for(int i = 0; i < nextStates.size(); i++) {
         int val = minmax(nextStates.get(i), depth, alpha, beta, gameState.getNextPlayer());
         if(val > max) {
           max = val;
           index = i;
         }
       }
       return nextStates.elementAt(index);
    }

    public int minmax(GameState gameState, int depth, int alpha, int beta, int player) {
      Vector<GameState> nextStates = new Vector<GameState>();
      gameState.findPossibleMoves(nextStates);

        //Random random = new Random();
       // return nextStates.elementAt(random.nextInt(nextStates.size()));
        int v = 0;

        if(depth == 0 || nextStates.isEmpty()) {
            v = eval(gameState, player);
            //player a
        } else if (player == 1) {
            v = Integer.MIN_VALUE;
            for(GameState g : nextStates) {
                v = Math.max(v, minmax(g, depth-1, alpha, beta, 2));
                alpha = Math.max(v, alpha);
                if(beta <= alpha)
                    break;
            }
            //player b
        } else {
            v = Integer.MAX_VALUE;
            for(GameState g : nextStates) {
              v = Math.min(v, minmax(g, depth-1, alpha, beta, 1));
              beta = Math.min(v, beta);
                if(beta <= alpha)
                    break;
            }
        }
        // System.err.println("Index: " + v );
        return v;
    }

    public int eval(GameState gameState, int player) {
        final int n = gameState.BOARD_SIZE;
        int[][] combinations = new int[10][2];

        // check rows
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                checkMarker(gameState, combinations[i], i, j);
                checkMarker(gameState, combinations[4 + i], j, i);
            }
            //diagonals
            checkMarker(gameState, combinations[8], i, i);
            checkMarker(gameState, combinations[9], n - i - 1, i);

        }

        int score = 0;
        int[] tup = new int[2];

        for(int[] tuple : combinations) {
          int val = (int)Math.pow(10,tuple[0]) - (int)Math.pow(10,tuple[1]);

          score += val;
        }

        if (player == 1) {
        }
        else {
        }
        // System.err.println("Player: " + player + " Tuple: [" + tup[0] +";" + tup[1] + "] Points: " + score);
        // System.err.println("-_-_-_-_-_-_-_-_");

        //return Math.max(Math.max(row, col), Math.max(row, diag));
        return score;
    }

    private void checkMarker(GameState gameState, int[] markers, int row, int col) {
      final int markerAtCell = gameState.at(row, col);

      if(markerAtCell == 1) {
        //X
        markers[0] ++;
      }
      else if(markerAtCell == 2) {
        //O
        markers[1] ++;
      }
    }
}
