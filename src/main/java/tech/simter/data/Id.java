package tech.simter.data;

/**
 * A ID holder.
 *
 * @author RJ
 */
public class Id<T> {
  protected T id;

  Id(T id) {
    this.id = id;
  }

  /**
   * Get the identity
   *
   * @return the identity
   */
  public T getId() {
    return id;
  }

  /**
   * Create a Id instance.
   *
   * @param <TT> the identity type
   * @param id   the identity value
   * @return the instance
   */
  public static <TT> Id with(TT id) {
    return new Id<>(id);
  }
}