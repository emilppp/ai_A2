import java.util.StringTokenizer;
import java.util.Vector;
import java.lang.Integer;

/**
 * Encapsulates a move
 *
 * In general, you should regard this as an opaque structure that exists
 * in the GameState you obtain from findPossibleMoves.
 *
 * The functions isNormal() and isEOG() might be useful.
 *
 * You can probably ignore the rest of the interface.
 */
public class Move {
  /**
   * Constants for different types of moves.
   *
   */
  public static final int MOVE_NORMAL   =  0;   ///< a normal move
  public static final int MOVE_BOG      = -1;   ///< beginning of game
  public static final int MOVE_XW       = -2;   ///< X wins => end of game
  public static final int MOVE_OW       = -3;   ///< O wins => end of game
  public static final int MOVE_DRAW     = -4;   ///< draw => end of game
  public static final int MOVE_NULL     = -5;   ///< a null move

  public static final int SPECIAL_NONE  = 0;    ///< not a special move
  public static final int SPECIAL_WIN   = 1;    ///< special move - win
  public static final int SPECIAL_DRAW  = 2;    ///< special move - draw

  private static final String DELIMITER = "_";

  private int type;
  public Vector<Integer> data = new Vector<Integer>();

  /**
   * Constructs a specific type of move.
   *
   * @param moveType
   *      moveType should be one of MOVE_BOG, MOVE_RW, MOVE_WW or MOVE_DRAW
   * @return
   */
  public Move(int moveType) {
    setType(moveType);
  }

  /**
   * Constructs a MOVE_BOG move.
   */
  public Move() {
    this(MOVE_BOG);
  }

  /**
   * Constructs a normal move.
   *
   * @param p1 a destination cell
   * @param p2 a player symbol
   */

  public Move(int p1, int p2) {
    this(MOVE_NORMAL);
    this.data.setSize(0);
    this.data.add(p1);
    this.data.add(p2);
  }

  /**
   * Constructs a Special move (Win or Draw) for player.
   *
   * @param p1 a destination cell
   * @param p2 a player symbol
   * @param specialMove a special move type
   */

  public Move(int p1, int p2, int specialMove) {
    this(p1, p2);
    if (specialMove == SPECIAL_DRAW) {
      setType(MOVE_DRAW);
    }
    if (specialMove == SPECIAL_WIN) {
      if (p2 == Constants.CELL_O)
        setType(MOVE_OW);
      if (p2 == Constants.CELL_X)
        setType(MOVE_XW);
    }
  }

  /**
   * Reconstructs the move from a string.
   *
   * @param string a string, which should have been previously generated by
   *        ToString(), or obtained from the server.
   */
  public Move(final String string) {
    StringTokenizer st = new StringTokenizer(string, DELIMITER);
    String str = st.nextToken();

    setType(Integer.parseInt(str));

    int length = 0;

    if (type == MOVE_NORMAL) {
      length = 2;
    } else if(type > 0) {
        length = type + 1;
    }

    if (type == MOVE_OW) {
      length = 2;
    }

    if (type == MOVE_XW) {
      length = 2;
    }

    if (type == MOVE_DRAW) {
      length = 2;
    }

    if ((length > 12) || (type < MOVE_NULL)) {
      setType(MOVE_NULL);
      return;
    }

    this.data.setSize(length);

    for (int i = 0; i < length; ++i) {
      str = st.nextToken();
      int cell = Integer.parseInt(str);
      this.data.set(i, cell);
    }
  }

  /**
   * Checks if the movement is null or invalid.
   */
  public boolean isNull() {
    return (this.type == MOVE_NULL);
  }

  /**
   * Checks if the movement marks beginning of game.
   */
  public boolean isBOG() {
    return (this.type == MOVE_BOG);
  }

  /**
   * Checks if the movement marks end of game.
   */
  public boolean isEOG() {
    return (this.type < MOVE_BOG);
  }

  /**
   * Checks if the game ended in X win.
   */
  public boolean isXWin() {
    return (this.type == MOVE_XW);
  }

  /**
   * Checks if the game ended in O win.
   */
  public boolean isOWin() {
    return (this.type == MOVE_OW);
  }

  /**
   * Checks if the game ended in draw.
   */
  public boolean isDraw() {
    return (this.type == MOVE_DRAW);
  }

  /**
   * Checks if the movement is a normal move.
   */
  public boolean isNormal() {
    return (this.type == MOVE_NORMAL);
  }

  /**
   * Gets the type of the move.
   */
  public int getType() {
    return this.type;
  }

  private void setType(int moveType) {
    this.type = moveType;
  }

  /**
   * Gets the length of the move sequence.
   */
  public int getLength() {
    return this.data.size();
  }

  /**
   * Gets the pN-th cell in the move sequence.
   */
  public int at(int pN) {
    return this.data.elementAt(pN);
  }

  /**
   * Converts the move to a string so that it can be sent to the other player.
   */
  public String toMessage() {
    String str = new String();
    str = str + type;

    for (int i = 0; i < this.data.size(); ++i) {
      str = str + DELIMITER + this.data.elementAt(i);
    }

    return str;
  }

  @Override
  public String toString() {
    if (this.type == MOVE_XW) {
      return "XW";
    }
    if (this.type == MOVE_OW) {
      return "OW";
    }
    if (this.type == MOVE_DRAW) {
      return "DRAW";
    }
    if (this.type == MOVE_BOG) {
      return "BOG";
    }
    if (this.isNull()) {
      return "NULL";
    }

    String str = new String();
    String delimiter = isNormal() ? "-" : "x";

    assert(this.data.size() > 0);

    // Concatenate all the cell numbers
    str = str + this.data.elementAt(0);

    for (int i = 1; i < this.data.size(); ++i) {
      str = str + delimiter + this.data.elementAt(i);
    }

    return str;
  }

  /**
   * Checks if the two objects represent the same move.
   */
  public boolean equals(final Move otherMove) {
    if (this.type != otherMove.type) {
      return false;
    }

    if (this.data.size() != otherMove.data.size()) {
      return false;
    }

    for (int i = 0; i < this.data.size(); ++i) {
      if (this.data.elementAt(i) != otherMove.data.elementAt(i)) {
        return false;
      }
    }

    return true;
  }
}
