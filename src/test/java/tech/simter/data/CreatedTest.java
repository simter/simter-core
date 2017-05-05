package tech.simter.data;

import org.junit.Test;

import java.time.OffsetDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author RJ
 */
public class CreatedTest {
  @Test
  public void integerId() {
    Created<Integer> c = Created.with(1);
    assertThat(c.getId(), is(1));
    assertThat(c.getTs(), is(OffsetDateTime.now().format(Ts.formatter)));
  }

  @Test
  public void longId() {
    Created<Long> c = Created.with(1L);
    assertThat(c.getId(), is(1L));
    assertThat(c.getTs(), is(OffsetDateTime.now().format(Ts.formatter)));
  }

  @Test
  public void stringId() {
    Created<String> c = Created.with("1");
    assertThat(c.getId(), is("1"));
    assertThat(c.getTs(), is(OffsetDateTime.now().format(Ts.formatter)));
  }

  @Test
  public void nullId() {
    assertThat(Created.with((Integer) null).getId(), nullValue());
    assertThat(Created.with((Long) null).getId(), nullValue());
    assertThat(Created.with((String) null).getId(), nullValue());
  }
}