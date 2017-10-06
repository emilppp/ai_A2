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
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        int depth = 16;

        //Random random = new Random();
       // return nextStates.elementAt(random.nextInt(nextStates.size()));

       int index = 0;
       for(int i = 0; i < nextStates.size(); i++) {
        int nextPlayer = gameState.getNextPlayer();
        
         int val = minmax(nextStates.get(i), depth, alpha, beta, nextPlayer);
         if(nextPlayer == 1) {
           if(val > alpha) {
             alpha = val;
             index = i;
           }
         }
         else {
           if(val < beta) {
             beta = val;
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


        
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                // check rows
                if(gameState.at(i, j) == 1) {  
                    combinations[i][0]++;
                } else if (gameState.at(i, j) == 2){
                    combinations[i][1]++;
                }
                //check col
                if(gameState.at(j, i) == 1) {
                    combinations[4+i][0]++;
                }
                else if (gameState.at(j, i) == 2){
                    combinations[4+i][1]++;
                }
            }
            // check diag1
            if(gameState.at(i, i) == 1) {
                combinations[8][0]++;
            } else if (gameState.at(i, i) == 2){
                combinations[8][1]++;
            }
            // check diag2
            if(gameState.at(n-i-1, i) == 1) {
                combinations[9][0]++;
            } else if (gameState.at(n-i-1, i) == 2){
                combinations[9][1]++;
            }
        }
        int score = 0;

        for(int[] i : combinations) {
            if((i[0] > 1 && i[1] > 1) || (i[0] == 0 && i[1] == 0)) {
                // add nothing
                continue;
            }

            score += (player == 1) ? Math.pow(10, i[0]) :  -Math.pow(10, i[1]);       
        }

        return score;
    }

    
}
