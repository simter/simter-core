package tech.simter.data;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author RJ
 */
class CreatedTest {
  @Test
  void integerId() {
    Created<Integer> c = Created.with(1);
    assertEquals(1, c.getId());
    assertEquals(OffsetDateTime.now().format(Ts.formatter), c.getTs());
  }

  @Test
  void longId() {
    Created<Long> c = Created.with(1L);
    assertEquals(1L, c.getId());
    assertEquals(OffsetDateTime.now().format(Ts.formatter), c.getTs());
  }

  @Test
  void stringId() {
    Created<String> c = Created.with("1");
    assertEquals("1", c.getId());
    assertEquals(OffsetDateTime.now().format(Ts.formatter), c.getTs());
  }

  @Test
  void nullId() {
    assertNull(Created.with((Integer) null).getId());
    assertNull(Created.with((Long) null).getId());
    assertNull(Created.with((String) null).getId());
  }
}