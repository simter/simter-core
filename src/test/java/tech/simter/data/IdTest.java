package tech.simter.data;

import com.owlike.genson.Genson;
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
    Id<Integer> c = Id.with(1);
    assertThat(c.getId(), is(1));
    System.out.println(new Genson().serialize(c));
  }

  @Test
  public void longId() {
    Id<Long> c = Id.with(1L);
    assertThat(c.getId(), is(1L));
  }

  @Test
  public void stringId() {
    Id<String> c = Id.with("1");
    assertThat(c.getId(), is("1"));
  }

  @Test
  public void nullId() {
    assertThat(Id.with((Integer) null).getId(), nullValue());
    assertThat(Id.with((Long) null).getId(), nullValue());
    assertThat(Id.with((String) null).getId(), nullValue());
  }
}