package tech.simter.data;

import java.time.OffsetDateTime;

import static tech.simter.data.Ts.formatter;

/**
 * The Created info object. It holds the identity and timestamp info.
 * <p>
 * This interface is target for the return object of the create rest api, such as POST method.
 *
 * @param <T> the identity type
 * @author RJ
 */
public class Created<T> extends Id<T> {
  private final String ts;

  private Created(T id) {
    super(id);
    ts = OffsetDateTime.now().format(formatter);
  }

  /**
   * Get the timestamp. The format is 'yyyy-MM-dd'T'HH:mm:ssx', such as '2017-01-01T08:00:00+08'
   *
   * @return the created dateTime
   */
  public String getTs() {
    return ts;
  }

  /**
   * Create a {@link Created} instance with a specific identity
   *
   * @param id   the identity value
   * @param <TT> the identity type
   * @return the instance
   */
  public static <TT> Created<TT> with(TT id) {
    return new Created<>(id);
  }
}