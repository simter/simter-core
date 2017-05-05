package tech.simter.data;

import org.junit.Test;

import java.time.OffsetDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author RJ
 */
public class TsTest {
  @Test
  public void now() {
    assertThat(Ts.now().getTs(), is(OffsetDateTime.now().format(Ts.formatter)));
  }
}