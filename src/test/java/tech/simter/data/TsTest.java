package tech.simter.data;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author RJ
 */
class TsTest {
  @Test
  void now() {
    assertEquals(OffsetDateTime.now().format(Ts.formatter), Ts.now().getTs());
  }
}