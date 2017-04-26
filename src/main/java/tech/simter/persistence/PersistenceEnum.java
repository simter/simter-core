package tech.simter.persistence;

/**
 * The persistence interface with persistence value.
 *
 * @author RJ 2017-04-26
 */
public interface PersistenceEnum<V> {
  /**
   * The persistence value
   *
   * @return The persistence value
   */
  V value();
}