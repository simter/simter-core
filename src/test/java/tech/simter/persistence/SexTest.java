package tech.simter.persistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author RJ
 */
class SexTest {
  @Test
  void valueOf() {
    assertEquals(Sex.Undefined, Sex.valueOf(1));
    assertEquals(Sex.Male, Sex.valueOf(2));
    assertEquals(Sex.Female, Sex.valueOf(4));
  }

  @Test
  void valueOfFailed_0() {
    assertThrows(IllegalArgumentException.class, () -> Sex.valueOf(0));
  }

  @Test
  void valueOfFailed_3() {
    assertThrows(IllegalArgumentException.class, () -> Sex.valueOf(3));
  }

  @Test
  void valueOfFailed_5() {
    assertThrows(IllegalArgumentException.class, () -> Sex.valueOf(5));
  }
}
