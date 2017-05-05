package tech.simter.data;

import com.owlike.genson.Genson;
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
    System.out.println(new Genson().serialize(Ts.now()));
  }
}