package tech.simter.persistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author RJ
 */
class CommonStateTest {
  @Test
  void valueOf() {
    assertEquals(CommonState.Draft, CommonState.valueOf(1));
    assertEquals(CommonState.Enabled, CommonState.valueOf(2));
    assertEquals(CommonState.Disabled, CommonState.valueOf(4));
    assertEquals(CommonState.Deleted, CommonState.valueOf(8));
  }

  @Test
  void valueOfFailed_0() {
    assertThrows(IllegalArgumentException.class, () -> CommonState.valueOf(0));
  }

  @Test
  void valueOfFailed_3() {
    assertThrows(IllegalArgumentException.class, () -> CommonState.valueOf(3));
  }

  @Test
  void valueOfFailed_5() {
    assertThrows(IllegalArgumentException.class, () -> CommonState.valueOf(5));
  }

  @Test
  void valueOfFailed_6() {
    assertThrows(IllegalArgumentException.class, () -> CommonState.valueOf(6));
  }

  @Test
  void valueOfFailed_7() {
    assertThrows(IllegalArgumentException.class, () -> CommonState.valueOf(7));
  }

  @Test
  void valueOfFailed_9() {
    assertThrows(IllegalArgumentException.class, () -> CommonState.valueOf(9));
  }
}
