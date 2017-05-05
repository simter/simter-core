package tech.simter.data;

/**
 * A ID interface.
 *
 * @author RJ
 */
public interface Id<T> {
  /**
   * Get the identity
   *
   * @return the identity
   */
  T getId();

  /**
   * Create a Id instance.
   *
   * @param id  the identity value
   * @param <T> the identity type
   * @return the instance
   */
  static <T> Id<T> with(T id) {
    return () -> id;
  }
}