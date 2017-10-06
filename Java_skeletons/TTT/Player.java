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

        int depth = 4;

        //Random random = new Random();
       // return nextStates.elementAt(random.nextInt(nextStates.size()));

       int index = 0;
       int max = (gameState.getNextPlayer() == 1) ? alpha : beta;

       for(int i = 0; i < nextStates.size(); i++) {
         int val = minmax(nextStates.get(i), depth, alpha, beta, gameState.getNextPlayer());
         if(gameState.getNextPlayer() == 1) {
           if(val > max) {
             max = val;
             index = i;
           }
         }
         else {
           if(val < max) {
             max = val;
             index = i;
           }
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
                checkMarker(gameState, combinations[i], player, i, j);
                checkMarker(gameState, combinations[4 + i], player, j, i);
            }
            //diagonals
            checkMarker(gameState, combinations[8], player, i, i);
            checkMarker(gameState, combinations[9], player, n - i - 1, i);
        }

        int score = 0;

        for(int[] tuple : combinations) {

          int val = markersInLine(tuple, player);
          score += val;
        }
        // System.err.println("Player: " + player + " Tuple: [" + tup[0] +";" + tup[1] + "] Points: " + score);
        // System.err.println("-_-_-_-_-_-_-_-_");

        return score;
    }

    private int markersInLine(int[] markers, int player) {
      if((markers[0] > 0 && markers[1] > 0) || (markers[0] == 0 && markers[1] == 0)) {
        return 0;
      }
      if (player == 1){
        return (int)Math.pow(10, markers[0]);
      } else {
        return -(int)Math.pow(10, markers[1]);
      }
    }

    private void checkMarker(GameState gameState, int[] markers, int player, int row, int col) {
      final int markerAtCell = gameState.at(row, col);

      if(markerAtCell == 0) {

      }
      else if(markerAtCell == player) {
        //X
        markers[0] ++;
      }
      else {
        //O
        markers[1] ++;
      }
    }
}
