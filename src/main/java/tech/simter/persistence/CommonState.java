package tech.simter.persistence;

import java.util.Objects;

/**
 * The common state with the persistence value that is 2 of the n power.
 *
 * @author RJ 2017-04-26
 */
public enum CommonState implements PersistenceEnum<Integer> {
  /**
   * The state indicate draft. It has a persistence value 1(2^0).
   */
  Draft(1),

  /**
   * The state indicate active or enabled. It has a persistence value 2(2^1).
   */
  Enabled(2),

  /**
   * The state indicate inactive or disabled. It has a persistence value 4(2^2).
   */
  Disabled(4),

  /**
   * The state indicate deleted. It has a persistence value 8(2^3).
   */
  Deleted(8);

  private final int value;

  CommonState(Integer value) {
    this.value = value;
  }

  @Override
  public Integer value() {
    return value;
  }

  /**
   * Get the state with the persistence value.
   *
   * @param value The value
   * @return The CommonState of the persistence value
   * @throws IllegalArgumentException If the value is unsupported
   */
  public static CommonState valueOf(Integer value) {
    Objects.requireNonNull(value);
    for (CommonState status : CommonState.values()) {
      if (status.value().equals(value)) return status;
    }
    throw new IllegalArgumentException("unsupported CommonState value: " + value);
  }
}