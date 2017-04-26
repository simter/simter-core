package tech.simter.persistence;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author RJ 2017-04-26
 */
public class CommonStateTest {
  @Test
  public void valueOf() throws Exception {
    assertThat(CommonState.valueOf(1), is(CommonState.Draft));
    assertThat(CommonState.valueOf(2), is(CommonState.Enabled));
    assertThat(CommonState.valueOf(4), is(CommonState.Disabled));
    assertThat(CommonState.valueOf(8), is(CommonState.Deleted));
  }

  @Test(expected = IllegalArgumentException.class)
  public void valueOfFailed_0() throws Exception {
    CommonState.valueOf(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void valueOfFailed_3() throws Exception {
    CommonState.valueOf(3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void valueOfFailed_5() throws Exception {
    CommonState.valueOf(5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void valueOfFailed_6() throws Exception {
    CommonState.valueOf(6);
  }

  @Test(expected = IllegalArgumentException.class)
  public void valueOfFailed_7() throws Exception {
    CommonState.valueOf(7);
  }

  @Test(expected = IllegalArgumentException.class)
  public void valueOfFailed_9() throws Exception {
    CommonState.valueOf(9);
  }
}
