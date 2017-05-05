package tech.simter.data;

import java.time.OffsetDateTime;

/**
 * The Created info object. It holds the identity and timestamp info.
 * <p>
 * This interface is target for the return object of the create rest api, such as POST method.
 *
 * @param <ID> the identity type
 * @author RJ
 */
public interface Created<ID> extends Id<ID>, Ts {
  /**
   * Create a {@link Created} instance with a specific identity
   *
   * @param id  the identity value
   * @param <T> the identity type
   * @return the instance
   */
  static <T> Created<T> with(T id) {
    return new Created<T>() {
      final String ts = OffsetDateTime.now().format(Ts.formatter);

      @Override
      public T getId() {
        return id;
      }

      @Override
      public String getTs() {
        return ts;
      }
    };
  }
}