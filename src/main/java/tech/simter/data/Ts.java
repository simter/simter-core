package tech.simter.data;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A timestamp holder.
 *
 * @author RJ
 */
public class Ts {
  public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssx");
  private final String ts;

  private Ts() {
    ts = OffsetDateTime.now().format(formatter);
  }

  /**
   * Get the now timestamp. The format is 'yyyy-MM-dd'T'HH:mm:ssx', such as '2017-01-01T08:00:00+08'
   *
   * @return the now timestamp
   */
  public String getTs() {
    return ts;
  }

  /**
   * Create a {@link Ts} instance with now.
   *
   * @return the instance
   */
  public static Ts now() {
    return new Ts();
  }
}