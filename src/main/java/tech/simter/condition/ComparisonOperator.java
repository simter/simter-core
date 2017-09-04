package tech.simter.condition;

/**
 * The comparison operator.
 *
 * @author RJ
 */
public enum ComparisonOperator {
  GreaterThan(">"),
  GreaterThanOrEquals(">="),
  LessThan("<"),
  LessThanOrEquals("<="),
  Equals("="),
  NotEquals("!="),
  Like("like"),
  LikeLeft("like-left", "like"),
  LikeRight("like-right", "like"),
  /**
   * Like with ignore case.
   */
  iLike("ilike", "ilike"),
  /**
   * Like left with ignore case.
   */
  iLikeLeft("ilike-left", "ilike"),
  /**
   * Like right with ignore case.
   */
  iLikeRight("ilike-right", "ilike"),
  In("in"),
  NotIn("not-in", "not in"),
  IsNull("is-null", "is null"),
  IsNotNull("is-not-null", "is not null"),
  /**
   * [X, Y].
   */
  Range("[]"),
  /**
   * [X, Y).
   * <p>
   * Greater then or equal X and less then Y.
   */
  RangeGTEAndLT("[)"),
  /**
   * (X, Y].
   * <p>
   * Greater then X and less then or equal Y.
   */
  RangeGTAndLTE("(]"),
  /**
   * (X, Y).
   * <p>
   * Greater then X and less then Y.
   */
  RangeGTAndLT("()");

  private final String id;
  private final String symbol;

  ComparisonOperator(String id) {
    this.id = id;
    this.symbol = id;
  }

  ComparisonOperator(String id, String symbol) {
    this.id = id;
    this.symbol = symbol;
  }

  public String symbol() {
    return symbol;
  }

  /**
   * Judge whether this enum is a Range enum.
   *
   * @return true if is a Range enum, otherwise return false
   */
  public boolean isRange() {
    return this == Range || this == RangeGTEAndLT || this == RangeGTAndLTE || this == RangeGTAndLT;
  }

  /**
   * Convert the symbol string to enum.
   *
   * @param symbol The symbol
   * @return The enum
   * @throws IllegalArgumentException If not exists
   */
  public static ComparisonOperator symbolOf(String symbol) {
    for (ComparisonOperator t : ComparisonOperator.values()) {
      if (t.symbol().equals(symbol)) return t;
    }
    throw new IllegalArgumentException("unsupported ComparisonOperator symbol: " + symbol);
  }
}