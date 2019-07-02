package tech.simter.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author RJ
 */
class IdTest {
  @Test
  void integerId() {
    Id c = Id.with(1);
    assertEquals(1, c.getId());
  }

  @Test
  void longId() {
    Id c = Id.with(1L);
    assertEquals(1L, c.getId());
  }

  @Test
  void stringId() {
    Id c = Id.with("1");
    assertEquals("1", c.getId());
  }

  @Test
  void nullId() {
    assertNull(Id.with(null).getId());
  }
}