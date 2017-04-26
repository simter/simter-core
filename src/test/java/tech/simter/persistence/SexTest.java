package tech.simter.persistence;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author RJ 2017-04-26
 */
public class SexTest {
  @Test
  public void valueOf() throws Exception {
    assertThat(Sex.valueOf(1), is(Sex.Undefined));
    assertThat(Sex.valueOf(2), is(Sex.Male));
    assertThat(Sex.valueOf(4), is(Sex.Female));
  }

  @Test(expected = IllegalArgumentException.class)
  public void valueOfFailed_0() throws Exception {
    Sex.valueOf(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void valueOfFailed_3() throws Exception {
    Sex.valueOf(3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void valueOfFailed_5() throws Exception {
    Sex.valueOf(5);
  }
}
