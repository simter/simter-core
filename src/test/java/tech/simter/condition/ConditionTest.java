package tech.simter.condition;

import org.junit.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static tech.simter.condition.ComparisonOperator.*;

/**
 * @author RJ
 */
public class ConditionTest {
  @Test
  public void of() {
    for (ComparisonOperator operator : ComparisonOperator.values()) {
      Condition c = Condition.of("id", operator, 1);
      assertThat(c.getId(), is("id"));
      assertThat(c.getOperator(), is(operator));
      assertThat(c.getValue(), is(1));
    }
  }

  @Test
  public void toQL() {
    String id = "id";
    Condition c;
    for (ComparisonOperator operator : ComparisonOperator.values()) {
      if (operator == IsNull) {
        c = Condition.of(id, operator);
        assertThat(c.toQL(), is("id is null"));
      } else if (operator == IsNotNull) {
        c = Condition.of(id, operator);
        assertThat(c.toQL(), is("id is not null"));
      } else if (operator == In) {
        // without NamedParam
        c = Condition.of(id, operator, Collections.singletonList(1));
        assertThat(c.toQL(), is("id in (?)"));
        c = Condition.of(id, operator, Arrays.asList(1, 2));
        assertThat(c.toQL(), is("id in (?, ?)"));

        // with one NamedParam
        c = Condition.of(id, operator, Collections.singletonList(1));
        assertThat(c.toQL(":ids"), is("id in (:ids)"));
        // with more NamedParams
        c = Condition.of(id, operator, Arrays.asList(1, 2));
        assertThat(c.toQL(":id1", ":id2"), is("id in (:id1, :id2)"));
      } else if (operator == NotIn) {
        // without NamedParam
        c = Condition.of(id, operator, Collections.singletonList(1));
        assertThat(c.toQL(), is("id not in (?)"));
        c = Condition.of(id, operator, Arrays.asList(1, 2));
        assertThat(c.toQL(), is("id not in (?, ?)"));

        // with one NamedParam
        c = Condition.of(id, operator, Collections.singletonList(1));
        assertThat(c.toQL(":ids"), is("id not in (:ids)"));
        // with more NamedParams
        c = Condition.of(id, operator, Arrays.asList(1, 2));
        assertThat(c.toQL(":id1", ":id2"), is("id not in (:id1, :id2)"));
      } else if (operator.isRange()) {
        if (operator == Range) {
          // without NamedParam
          c = Condition.of(id, operator, Arrays.asList(1, 2));
          assertThat(c.toQL(), is("id >= ? and id <= ?"));
          c = Condition.of(id, operator, Collections.singletonList(1));
          assertThat(c.toQL(), is("id >= ?"));
          c = Condition.of(id, operator, Arrays.asList(null, 2));
          assertThat(c.toQL(), is("id <= ?"));

          // with NamedParams
          c = Condition.of(id, operator, Arrays.asList(1, 2));
          assertThat(c.toQL(":id1", ":id2"), is("id >= :id1 and id <= :id2"));
          c = Condition.of(id, operator, Collections.singletonList(1));
          assertThat(c.toQL(":id1"), is("id >= :id1"));
          c = Condition.of(id, operator, Arrays.asList(null, 2));
          assertThat(c.toQL((String) null, ":id2"), is("id <= :id2"));
        } else if (operator == RangeGTAndLTE) {
          // without NamedParam
          c = Condition.of(id, operator, Arrays.asList(1, 2));
          assertThat(c.toQL(), is("id > ? and id <= ?"));
          c = Condition.of(id, operator, Collections.singletonList(1));
          assertThat(c.toQL(), is("id > ?"));
          c = Condition.of(id, operator, Arrays.asList(null, 2));
          assertThat(c.toQL(), is("id <= ?"));

          // with NamedParams
          c = Condition.of(id, operator, Arrays.asList(1, 2));
          assertThat(c.toQL(":id1", ":id2"), is("id > :id1 and id <= :id2"));
          c = Condition.of(id, operator, Collections.singletonList(1));
          assertThat(c.toQL(":id1"), is("id > :id1"));
          c = Condition.of(id, operator, Arrays.asList(null, 2));
          assertThat(c.toQL((String) null, ":id2"), is("id <= :id2"));
        } else if (operator == RangeGTEAndLT) {
          // without NamedParam
          c = Condition.of(id, operator, Arrays.asList(1, 2));
          assertThat(c.toQL(), is("id >= ? and id < ?"));
          c = Condition.of(id, operator, Collections.singletonList(1));
          assertThat(c.toQL(), is("id >= ?"));
          c = Condition.of(id, operator, Arrays.asList(null, 2));
          assertThat(c.toQL(), is("id < ?"));

          // with NamedParams
          c = Condition.of(id, operator, Arrays.asList(1, 2));
          assertThat(c.toQL(":id1", ":id2"), is("id >= :id1 and id < :id2"));
          c = Condition.of(id, operator, Collections.singletonList(1));
          assertThat(c.toQL(":id1"), is("id >= :id1"));
          c = Condition.of(id, operator, Arrays.asList(null, 2));
          assertThat(c.toQL((String) null, ":id2"), is("id < :id2"));
        } else if (operator == RangeGTAndLT) {
          // without NamedParam
          c = Condition.of(id, operator, Arrays.asList(1, 2));
          assertThat(c.toQL(), is("id > ? and id < ?"));
          c = Condition.of(id, operator, Collections.singletonList(1));
          assertThat(c.toQL(), is("id > ?"));
          c = Condition.of(id, operator, Arrays.asList(null, 2));
          assertThat(c.toQL(), is("id < ?"));

          // with NamedParams
          c = Condition.of(id, operator, Arrays.asList(1, 2));
          assertThat(c.toQL(":id1", ":id2"), is("id > :id1 and id < :id2"));
          c = Condition.of(id, operator, Collections.singletonList(1));
          assertThat(c.toQL(":id1"), is("id > :id1"));
          c = Condition.of(id, operator, Arrays.asList(null, 2));
          assertThat(c.toQL((String) null, ":id2"), is("id < :id2"));
        } else {
          c = Condition.of(id, operator, Arrays.asList(1, 2));
          throw new IllegalArgumentException("unsupported operator. ql=" + c.toQL());
        }
      } else {
        c = Condition.of(id, operator, 1);
        assertThat(c.toQL(), is(String.format("id %s ?", operator.symbol())));
      }
    }
  }

  @Test
  public void toCondition4Equals() throws Exception {
    Condition c = Condition.toCondition(createItem("id", "1", "Integer", "="));
    assertThat(c.getId(), is("id"));
    assertThat(c.getOperator(), is(Equals));
    assertThat(c.getValue(), is(1));

    c = Condition.toCondition(createItem("[\"id\", \"1\", \"Integer\", \"=\"]"));
    assertThat(c.getId(), is("id"));
    assertThat(c.getOperator(), is(Equals));
    assertThat(c.getValue(), is(1));

    c = Condition.toCondition(createItem("[\"id\", \"1\", \"Integer\"]"));
    assertThat(c.getId(), is("id"));
    assertThat(c.getOperator(), is(Equals));
    assertThat(c.getValue(), is(1));

    c = Condition.toCondition(createItem("[\"id\", \"1\"]"));
    assertThat(c.getId(), is("id"));
    assertThat(c.getOperator(), is(Equals));
    assertThat(c.getValue(), is("1"));
  }

  @Test
  public void toCondition4Range() throws Exception {
    Condition c = Condition.toCondition(createItem("[\"id\", [\"1\", \"2\"], \"Integer\", \"[]\"]"));
    assertThat(c.getId(), is("id"));
    assertThat(c.getOperator(), is(Range));
    assertThat(c.getValues().get(0), is(1));
    assertThat(c.getValues().get(1), is(2));

    // default value type is String
    c = Condition.toCondition(createItem("[\"id\", [\"1\", \"2\"], null, \"[]\"]"));
    assertThat(c.getId(), is("id"));
    assertThat(c.getOperator(), is(Range));
    assertThat(c.getValues().get(0), is("1"));
    assertThat(c.getValues().get(1), is("2"));

    c = Condition.toCondition(createItem("[\"id\", [\"1\"], \"Integer\", \"[]\"]"));
    assertThat(c.getId(), is("id"));
    assertThat(c.getOperator(), is(Range));
    assertThat(c.getValues().size(), is(1));
    assertThat(c.getValues().get(0), is(1));
  }

  @Test
  public void toConditions() throws Exception {
    JsonArray array = Json.createArrayBuilder()
      .add(createItem("[\"id\", \"1\", \"Integer\", \"=\"]"))
      .add(createItem("[\"money\", [\"1\", \"2.05\"], \"Decimal\", \"[]\"]"))
      .add(createItem("[\"startDate\", [\"2017-01-01\", \"2017-02-01\"], \"LocalDate\", \"[]\"]"))
      .build();
    List<Condition> cs = Condition.toConditions(array.toString());
    assertThat(cs.size(), is(3));

    // Equals
    assertThat(cs.get(0).getId(), is("id"));
    assertThat(cs.get(0).getOperator(), is(Equals));
    assertThat(cs.get(0).getValue(), is(1));

    // Decimal Range
    assertThat(cs.get(1).getId(), is("money"));
    assertThat(cs.get(1).getOperator(), is(Range));
    assertThat(cs.get(1).getValues().get(0), is(new BigDecimal("1")));
    assertThat(cs.get(1).getValues().get(1), is(new BigDecimal("2.05")));

    // LocalDate Range
    assertThat(cs.get(2).getId(), is("startDate"));
    assertThat(cs.get(2).getOperator(), is(Range));
    assertThat(cs.get(2).getValues().get(0), is(LocalDate.parse("2017-01-01")));
    assertThat(cs.get(2).getValues().get(1), is(LocalDate.parse("2017-02-01")));
  }

  // only can use for single value
  private JsonArray createItem(String id, String value, String type, String operator) {
    JsonArrayBuilder builder = Json.createArrayBuilder();
    return builder
      .add(id)
      .add(value)
      .add(type)
      .add(operator)
      .build();
  }

  // item must be standard JsonArray structure
  private JsonArray createItem(String item) throws UnsupportedEncodingException {
    return Json.createReader(new StringReader(URLDecoder.decode(item, "UTF-8")))
      .readArray();
  }
}
