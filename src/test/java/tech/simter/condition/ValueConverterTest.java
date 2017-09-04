package tech.simter.condition;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author RJ
 */
public class ValueConverterTest {
  @Test
  public void toStringValue() {
    assertThat(ValueConverter.convert(null, "v"), is("v"));
    assertThat(ValueConverter.convert("string", "v"), is("v"));
    assertThat(ValueConverter.convert("String", "v"), is("v"));
    assertThat(ValueConverter.convert("String", "v\""), is("v\""));
  }

  @Test
  public void toStringArray() {
    String[] array = ValueConverter.convert("String[]", "[v1, v2]");
    assertThat(array.length, is(2));
    assertThat(array[0], is("v1"));
    assertThat(array[1], is("v2"));

    array = ValueConverter.convert("String[]", "[\"v1\", \"v2\"]");
    assertThat(array.length, is(2));
    assertThat(array[0], is("v1"));
    assertThat(array[1], is("v2"));
  }

  @Test
  public void toInteger() {
    assertThat(ValueConverter.convert("Integer", "1"), is(1));
  }

  @Test
  public void toIntegerArray() {
    Integer[] array = ValueConverter.convert("Integer[]", "[1, 2]");
    assertThat(array.length, is(2));
    assertThat(array[0], is(1));
    assertThat(array[1], is(2));
  }

  @Test
  public void toLong() {
    assertThat(ValueConverter.convert("Long", "1"), is(1L));
  }

  @Test
  public void toLongArray() {
    Long[] array = ValueConverter.convert("Long[]", "[1, 2]");
    assertThat(array.length, is(2));
    assertThat(array[0], is(1L));
    assertThat(array[1], is(2L));
  }

  @Test
  public void toFloat() {
    assertThat(ValueConverter.convert("Float", "1"), is(1F));
  }

  @Test
  public void toFloatArray() {
    Float[] array = ValueConverter.convert("Float[]", "[1, 2.5]");
    assertThat(array.length, is(2));
    assertThat(array[0], is(1F));
    assertThat(array[1], is(2.5F));
  }

  @Test
  public void toDouble() {
    assertThat(ValueConverter.convert("Double", "1"), is(1D));
  }

  @Test
  public void toDoubleArray() {
    Double[] array = ValueConverter.convert("Double[]", "[1, 2.5]");
    assertThat(array.length, is(2));
    assertThat(array[0], is(1D));
    assertThat(array[1], is(2.5D));
  }

  @Test
  public void toBoolean() {
    assertThat(ValueConverter.convert("Boolean", "false"), is(false));
    assertThat(ValueConverter.convert("Boolean", "true"), is(true));
  }

  @Test
  public void toBooleanArray() {
    Boolean[] array = ValueConverter.convert("Boolean[]", "[false, true]");
    assertThat(array.length, is(2));
    assertThat(array[0], is(false));
    assertThat(array[1], is(true));
  }

  @Test
  public void toDecimal() {
    assertThat(ValueConverter.convert("Decimal", "1"), is(new BigDecimal("1")));
    assertThat(ValueConverter.convert("Decimal", "2.05"), is(new BigDecimal("2.05")));
  }

  @Test
  public void toDecimalArray() {
    BigDecimal[] array = ValueConverter.convert("Decimal[]", "[1, 2.05]");
    assertThat(array.length, is(2));
    assertThat(array[0], is(new BigDecimal("1")));
    assertThat(array[1], is(new BigDecimal("2.05")));
  }

  @Test
  public void toLocalDate() {
    assertThat(ValueConverter.convert("LocalDate", "2017-01-02"), is(LocalDate.parse("2017-01-02")));
  }

  @Test
  public void toLocalDateArray() {
    LocalDate[] array = ValueConverter.convert("LocalDate[]", "[2017-01-02, 2017-03-02]");
    assertThat(array.length, is(2));
    assertThat(array[0], is(LocalDate.parse("2017-01-02")));
    assertThat(array[1], is(LocalDate.parse("2017-03-02")));
  }

  @Test
  public void toLocalDateTime() {
    assertThat(ValueConverter.convert("LocalDateTime", "2017-01-02T13:10:20"),
      is(LocalDateTime.parse("2017-01-02T13:10:20")));
  }

  @Test
  public void toLocalDateTimeArray() {
    LocalDateTime[] array = ValueConverter.convert("LocalDateTime[]", "[2017-01-02T13:10:20, 2017-03-02T13:10:20]");
    assertThat(array.length, is(2));
    assertThat(array[0], is(LocalDateTime.parse("2017-01-02T13:10:20")));
    assertThat(array[1], is(LocalDateTime.parse("2017-03-02T13:10:20")));
  }

  @Test
  public void toLocalTime() {
    assertThat(ValueConverter.convert("LocalTime", "13:10:20"), is(LocalTime.parse("13:10:20")));
  }

  @Test
  public void toLocalTimeArray() {
    LocalTime[] array = ValueConverter.convert("LocalTime[]", "[13:10:20, 13:10:30]");
    assertThat(array.length, is(2));
    assertThat(array[0], is(LocalTime.parse("13:10:20")));
    assertThat(array[1], is(LocalTime.parse("13:10:30")));
  }

  @Test(expected = IllegalArgumentException.class)
  public void toUnsupported() {
    ValueConverter.convert("xx", "xxx");
  }
}