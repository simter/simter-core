package tech.simter.data;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author RJ
 */
public class IdTest {
  @Test
  public void integerId() {
    Id c = Id.with(1);
    assertThat(c.getId(), is(1));
  }

  @Test
  public void longId() {
    Id c = Id.with(1L);
    assertThat(c.getId(), is(1L));
  }

  @Test
  public void stringId() {
    Id c = Id.with("1");
    assertThat(c.getId(), is("1"));
  }

  @Test
  public void nullId() {
    assertThat(Id.with(null).getId(), nullValue());
  }
}