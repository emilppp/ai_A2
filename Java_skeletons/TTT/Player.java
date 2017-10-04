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

     private int index = 0;

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
        index = 0;

        //Random random = new Random();
       // return nextStates.elementAt(random.nextInt(nextStates.size()));

       int val = minmax(gameState, nextStates, depth, alpha, beta, gameState.getNextPlayer());
       return nextStates.elementAt(index);

    }

    public int minmax(GameState gameState, Vector<GameState> nextStates, int depth, int alpha, int beta, int player) {
        //Random random = new Random();
       // return nextStates.elementAt(random.nextInt(nextStates.size()));
        int v = 0;

        if(depth == 0 || nextStates.isEmpty()) {
            v = eval(gameState, player);
            //player a
        } else if (player == 1) {
            v = -Integer.MAX_VALUE;
            for(GameState g : nextStates) {
                v = Math.max(v, minmax(g, nextStates, depth-1, alpha, beta, 2));
                if(v > alpha) {
                  alpha = v;
                  index = nextStates.indexOf(g);
                }
                if(beta <= alpha)
                    break;
            }
            //player b
        } else {
            v = Integer.MAX_VALUE;
            for(GameState g : nextStates) {
              v = Math.min(v, minmax(g, nextStates, depth-1, alpha, beta, 1));
              if(v < beta) {
                beta = v;
                index = nextStates.indexOf(g);
              }
                if(beta <= alpha)
                    break;
            }
        }
        return v;
    }

    public int eval(GameState gameState, int player) {
        final int n = gameState.BOARD_SIZE;
        int[] combinations = new int[10];

        // check rows
        for(int i = 0; i < n; i++) {
          int power = 1;
            for(int j = 0; j < n; j++) {
                combinations[i] += checkCell(gameState, player, i, j);
                combinations[4 + i] += checkCell(gameState, player, j, i);
            }
            combinations[8] += checkCell(gameState, player, i, i);
            combinations[9] += checkCell(gameState, player, n - i, n - i);
        }
        int max = 0;
        //System.err.println("New state");
        for(int i : combinations) {
          //System.err.println("Comb: " + i);
          if(i > max) {
            max = i;
          }
        }
        //return Math.max(Math.max(row, col), Math.max(row, diag));
        return max;
    }

    private int checkCell(GameState gameState, int player, int i, int j) {
      if(gameState.at(i, j) == player) {
        return 10;
      }
      else if(gameState.at(i, j) == 0) {
        return 0;
      }
      else {
        return -10;
      }
    }
}
