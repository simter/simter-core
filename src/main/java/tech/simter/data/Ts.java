package tech.simter.data;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * A timestamp holder.
 *
 * @author RJ
 */
public class Ts {
  public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssx");
  private final String ts;
  private Map<String, Object> map;

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

  /**
   * Returns true if the map contains no key-value mappings.
   *
   * @return true if the map contains no key-value mappings.
   */
  public boolean isEmpty() {
    return map == null || map.isEmpty();
  }

  /**
   * The extra key-value mappings data
   *
   * @return the map
   */
  public Map<String, Object> map() {
    if (map == null) map = new HashMap<>();
    return map;
  }

  /**
   * Add extra key-value data.
   *
   * @param key   the key
   * @param value the value
   * @return the instance
   */
  public Ts put(String key, Object value) {
    map().put(key, value);
    return this;
  }
}