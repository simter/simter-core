package tech.simter.persistence;

/**
 * The Sex Enum
 *
 * @author RJ 2017-04-26
 */
public enum Sex implements PersistenceEnum<Integer> {
  Undefined(1),
  Male(2),
  Female(4);

  private final int value;

  Sex(Integer value) {
    this.value = value;
  }

  @Override
  public Integer value() {
    return value;
  }

  /**
   * Get the Sex with the specified value.
   *
   * @param value The value
   * @return The Sex of the specified value
   * @throws IllegalArgumentException If the value is unsupported
   */
  public static Sex valueOf(Integer value) {
    for (Sex status : Sex.values()) {
      if (status.value().equals(value)) return status;
    }
    throw new IllegalArgumentException("unsupported sex value: " + value);
  }
}