package tech.simter.data;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A timestamp interface.
 *
 * @author RJ
 */
public interface Ts {
  /**
   * The timestamp formatter.
   */
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssx");

  /**
   * Get the timestamp. The format is 'yyyy-MM-dd'T'HH:mm:ssx', such as '2017-01-01T08:00:00+08'
   *
   * @return the created dateTime
   */
  String getTs();

  /**
   * Create a now timestamp.
   *
   * @return now
   */
  static Ts now() {
    class TsImpl implements Ts {
      private final String ts = OffsetDateTime.now().format(formatter);

      @Override
      public String getTs() {
        return ts;
      }
    }
    return new TsImpl();
  }
}