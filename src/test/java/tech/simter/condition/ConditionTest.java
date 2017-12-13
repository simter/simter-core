package tech.simter.condition;

import org.junit.Test;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

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
  public void toQL_IsNull() {
    String alias = "t.id_";
    Condition c = Condition.of("id", ComparisonOperator.IsNull);

    assertThat(c.toQL(), is("id is null"));
    assertThat(c.toQL(alias, null), is(alias + " is null"));

    Map<String, Object> namedParamsValues = new HashMap<>();
    assertThat(c.toQL(null, namedParamsValues), is("id is null"));
    assertThat(namedParamsValues.isEmpty(), is(true));
    assertThat(c.toQL(alias, namedParamsValues), is(alias + " is null"));

    List<Object> paramsValues = new ArrayList<>();
    assertThat(c.toQL(null, paramsValues), is("id is null"));
    assertThat(paramsValues.isEmpty(), is(true));
    assertThat(c.toQL(null, null), is("id is null"));
    assertThat(c.toQL(alias, paramsValues), is(alias + " is null"));
  }

  @Test
  public void toQL_IsNotNull() {
    String alias = "t.id_";
    Condition c = Condition.of("id", ComparisonOperator.IsNotNull);

    assertThat(c.toQL(), is("id is not null"));
    assertThat(c.toQL(alias, null), is(alias + " is not null"));

    Map<String, Object> namedParamsValues = new HashMap<>();
    assertThat(c.toQL(null, namedParamsValues), is("id is not null"));
    assertThat(namedParamsValues.isEmpty(), is(true));
    assertThat(c.toQL(alias, namedParamsValues), is(alias + " is not null"));

    List<Object> paramsValues = new ArrayList<>();
    assertThat(c.toQL(null, paramsValues), is("id is not null"));
    assertThat(paramsValues.isEmpty(), is(true));
    assertThat(c.toQL(null, null), is("id is not null"));
    assertThat(c.toQL(alias, paramsValues), is(alias + " is not null"));
  }

  @Test
  public void toQL_In() {
    String id = "id";
    String alias = "t.id_";
    List<Integer> singleValues = Collections.singletonList(1);
    List<Integer> multiValues = Arrays.asList(1, 2);

    // without NamedParam
    Condition c = Condition.of(id, ComparisonOperator.In, singleValues);
    assertThat(c.toQL(), is("id in (?)"));
    assertThat(c.toQL(null, null), is("id in (?)"));
    assertThat(c.toQL(alias, null), is(alias + " in (?)"));
    c = Condition.of(id, ComparisonOperator.In, multiValues);
    assertThat(c.toQL(), is("id in (?, ?)"));
    assertThat(c.toQL(null, null), is("id in (?, ?)"));
    assertThat(c.toQL(alias, null), is(alias + " in (?, ?)"));

    // with one specified NamedParam
    Map<String, Object> namedParamsValues = new HashMap<>();
    c = Condition.of(id, ComparisonOperator.In, singleValues);
    assertThat(c.toQL(null, namedParamsValues, "ids"), is("id in (:ids)"));
    assertThat(namedParamsValues.size(), is(1));
    assertThat(namedParamsValues.get("ids"), is(singleValues));
    assertThat(c.toQL(alias, namedParamsValues, "ids"), is(alias + " in (:ids)"));

    // with more specified NamedParams
    namedParamsValues = new HashMap<>();
    c = Condition.of(id, ComparisonOperator.In, multiValues);
    assertThat(c.toQL(null, namedParamsValues, "id1", "id2"), is("id in (:id1, :id2)"));
    assertThat(namedParamsValues.size(), is(2));
    assertThat(namedParamsValues.get("id1"), is(multiValues.get(0)));
    assertThat(namedParamsValues.get("id2"), is(multiValues.get(1)));
    assertThat(c.toQL(alias, namedParamsValues, "id1", "id2"), is(alias + " in (:id1, :id2)"));

    // with one default NamedParam
    namedParamsValues = new HashMap<>();
    c = Condition.of(id, ComparisonOperator.In, singleValues);
    assertThat(c.toQL(null, namedParamsValues), is("id in (:id)"));
    assertThat(namedParamsValues.size(), is(1));
    assertThat(namedParamsValues.get("id"), is(singleValues.get(0)));
    assertThat(c.toQL(alias, namedParamsValues), is(alias + " in (:id)"));

    // with more default NamedParams
    namedParamsValues = new HashMap<>();
    c = Condition.of(id, ComparisonOperator.In, multiValues);
    assertThat(c.toQL(null, namedParamsValues), is("id in (:id0, :id1)"));
    assertThat(namedParamsValues.size(), is(2));
    assertThat(namedParamsValues.get("id0"), is(multiValues.get(0)));
    assertThat(namedParamsValues.get("id1"), is(multiValues.get(1)));
    assertThat(c.toQL(alias, namedParamsValues), is(alias + " in (:id0, :id1)"));
  }

  @Test
  public void toQL_NotIn() {
    String id = "id";
    String alias = "t.id_";
    List<Integer> singleValues = Collections.singletonList(1);
    List<Integer> multiValues = Arrays.asList(1, 2);

    // without NamedParam
    Condition c = Condition.of(id, ComparisonOperator.NotIn, singleValues);
    assertThat(c.toQL(), is("id not in (?)"));
    assertThat(c.toQL(null, null), is("id not in (?)"));
    assertThat(c.toQL(alias, null), is(alias + " not in (?)"));
    c = Condition.of(id, ComparisonOperator.NotIn, multiValues);
    assertThat(c.toQL(), is("id not in (?, ?)"));
    assertThat(c.toQL(null, null), is("id not in (?, ?)"));
    assertThat(c.toQL(alias, null), is(alias + " not in (?, ?)"));

    // with one specified NamedParam
    Map<String, Object> namedParamsValues = new HashMap<>();
    c = Condition.of(id, ComparisonOperator.NotIn, singleValues);
    assertThat(c.toQL(null, namedParamsValues, "ids"), is("id not in (:ids)"));
    assertThat(namedParamsValues.size(), is(1));
    assertThat(namedParamsValues.get("ids"), is(singleValues));
    assertThat(c.toQL(alias, namedParamsValues, "ids"), is(alias + " not in (:ids)"));

    // with more specified NamedParams
    namedParamsValues = new HashMap<>();
    c = Condition.of(id, ComparisonOperator.NotIn, multiValues);
    assertThat(c.toQL(null, namedParamsValues, "id1", "id2"), is("id not in (:id1, :id2)"));
    assertThat(namedParamsValues.size(), is(2));
    assertThat(namedParamsValues.get("id1"), is(multiValues.get(0)));
    assertThat(namedParamsValues.get("id2"), is(multiValues.get(1)));
    assertThat(c.toQL(alias, namedParamsValues, "id1", "id2"), is(alias + " not in (:id1, :id2)"));

    // with one default NamedParam
    namedParamsValues = new HashMap<>();
    c = Condition.of(id, ComparisonOperator.NotIn, singleValues);
    assertThat(c.toQL(null, namedParamsValues), is("id not in (:id)"));
    assertThat(namedParamsValues.size(), is(1));
    assertThat(namedParamsValues.get("id"), is(singleValues.get(0)));
    assertThat(c.toQL(alias, namedParamsValues), is(alias + " not in (:id)"));

    // with more default NamedParams
    namedParamsValues = new HashMap<>();
    c = Condition.of(id, ComparisonOperator.NotIn, multiValues);
    assertThat(c.toQL(null, namedParamsValues), is("id not in (:id0, :id1)"));
    assertThat(namedParamsValues.size(), is(2));
    assertThat(namedParamsValues.get("id0"), is(multiValues.get(0)));
    assertThat(namedParamsValues.get("id1"), is(multiValues.get(1)));
    assertThat(c.toQL(alias, namedParamsValues), is(alias + " not in (:id0, :id1)"));
  }

  @Test
  public void toQL_Ranges() {
    String id = "id";
    String alias = "t.id_";
    // {0} - startSymbol, {1} - endSymbol, {2} - alias
    Stream.of(Range, RangeGTEAndLT, RangeGTAndLTE, RangeGTAndLT).forEach(operator -> {
      List<Integer> singleValues = Collections.singletonList(1);
      List<Integer> twoValues = Arrays.asList(1, 2);
      String startSymbol = operator.symbol().startsWith("[") ? ">=" : ">";
      String endSymbol = operator.symbol().endsWith("]") ? "<=" : "<";

      // 1. without NamedParam
      Condition c = Condition.of(id, operator, twoValues);
      String expected = MessageFormat.format("id {0} ? and id {1} ?", startSymbol, endSymbol);
      assertThat(c.toQL(), is(expected));
      assertThat(c.toQL(null, null), is(expected));

      expected = MessageFormat.format("{2} {0} ? and {2} {1} ?", startSymbol, endSymbol, alias); // with alias
      assertThat(c.toQL(alias, null), is(expected));

      c = Condition.of(id, operator, singleValues);
      expected = MessageFormat.format("id {0} ?", startSymbol);
      assertThat(c.toQL(), is(expected));
      assertThat(c.toQL(null, null), is(expected));
      expected = MessageFormat.format("{1} {0} ?", startSymbol, alias); // with alias
      assertThat(c.toQL(alias, null), is(expected));

      c = Condition.of(id, operator, Arrays.asList(null, 2));
      expected = MessageFormat.format("id {0} ?", endSymbol);
      assertThat(c.toQL(), is(expected));
      assertThat(c.toQL(null, null), is(expected));

      expected = MessageFormat.format("{1} {0} ?", endSymbol, alias); // with alias
      assertThat(c.toQL(alias, null), is(expected));

      // 2. two value with default NamedParams
      Map<String, Object> namedParamsValues = new HashMap<>();
      c = Condition.of(id, operator, twoValues);
      expected = MessageFormat.format("id {0} :id0 and id {1} :id1", startSymbol, endSymbol);
      assertThat(c.toQL(null, namedParamsValues), is(expected));
      assertThat(namedParamsValues.size(), is(2));
      assertThat(namedParamsValues.get("id0"), is(twoValues.get(0)));
      assertThat(namedParamsValues.get("id1"), is(twoValues.get(1)));

      expected = MessageFormat.format("{2} {0} :id0 and {2} {1} :id1", startSymbol, endSymbol, alias); // with alias
      assertThat(c.toQL(alias, namedParamsValues), is(expected));
      assertThat(namedParamsValues.size(), is(2));
      assertThat(namedParamsValues.get("id0"), is(twoValues.get(0)));
      assertThat(namedParamsValues.get("id1"), is(twoValues.get(1)));

      // 3. one value with default NamedParam
      namedParamsValues = new HashMap<>();
      c = Condition.of(id, operator, singleValues);
      expected = MessageFormat.format("id {0} :id0", startSymbol);
      assertThat(c.toQL(null, namedParamsValues), is(expected));
      assertThat(namedParamsValues.size(), is(1));
      assertThat(namedParamsValues.get("id0"), is(singleValues.get(0)));

      expected = MessageFormat.format("{1} {0} :id0", startSymbol, alias); // with alias
      assertThat(c.toQL(alias, namedParamsValues), is(expected));
      assertThat(namedParamsValues.size(), is(1));
      assertThat(namedParamsValues.get("id0"), is(singleValues.get(0)));

      namedParamsValues = new HashMap<>();
      c = Condition.of(id, operator, Arrays.asList(null, 2));
      expected = MessageFormat.format("id {0} :id1", endSymbol);
      assertThat(c.toQL(null, namedParamsValues), is(expected));
      assertThat(namedParamsValues.size(), is(1));
      assertThat(namedParamsValues.get("id1"), is(2));

      expected = MessageFormat.format("{1} {0} :id1", endSymbol, alias); // with alias
      assertThat(c.toQL(alias, namedParamsValues), is(expected));
      assertThat(namedParamsValues.size(), is(1));
      assertThat(namedParamsValues.get("id1"), is(2));

      // 4. two value with specified NamedParams
      namedParamsValues = new HashMap<>();
      c = Condition.of(id, operator, twoValues);
      expected = MessageFormat.format("id {0} :k1 and id {1} :k2", startSymbol, endSymbol);
      assertThat(c.toQL(null, namedParamsValues, "k1", "k2"), is(expected));
      assertThat(namedParamsValues.size(), is(2));
      assertThat(namedParamsValues.get("k1"), is(twoValues.get(0)));
      assertThat(namedParamsValues.get("k2"), is(twoValues.get(1)));

      expected = MessageFormat.format("{2} {0} :k1 and {2} {1} :k2", startSymbol, endSymbol, alias); // with alias
      assertThat(c.toQL(alias, namedParamsValues, "k1", "k2"), is(expected));
      assertThat(namedParamsValues.size(), is(2));
      assertThat(namedParamsValues.get("k1"), is(twoValues.get(0)));
      assertThat(namedParamsValues.get("k2"), is(twoValues.get(1)));

      // 5. onw value with specified NamedParams
      namedParamsValues = new HashMap<>();
      c = Condition.of(id, operator, singleValues);
      expected = MessageFormat.format("id {0} :k1", startSymbol);
      assertThat(c.toQL(null, namedParamsValues, "k1"), is(expected));
      assertThat(namedParamsValues.size(), is(1));
      assertThat(namedParamsValues.get("k1"), is(singleValues.get(0)));

      expected = MessageFormat.format("{1} {0} :k1", startSymbol, alias); // with alias
      assertThat(c.toQL(alias, namedParamsValues, "k1"), is(expected));
      assertThat(namedParamsValues.size(), is(1));
      assertThat(namedParamsValues.get("k1"), is(singleValues.get(0)));

      namedParamsValues = new HashMap<>();
      c = Condition.of(id, operator, Arrays.asList(null, 2));
      expected = MessageFormat.format("id {0} :k2", endSymbol);
      assertThat(c.toQL(null, namedParamsValues, null, "k2"), is(expected));
      assertThat(namedParamsValues.size(), is(1));
      assertThat(namedParamsValues.get("k2"), is(2));

      expected = MessageFormat.format("{1} {0} :k2", endSymbol, alias); // with alias
      assertThat(c.toQL(alias, namedParamsValues, null, "k2"), is(expected));
      assertThat(namedParamsValues.size(), is(1));
      assertThat(namedParamsValues.get("k2"), is(2));
    });
  }

  @Test
  public void toQL_SingleValueOperator() {
    String id = "id";
    Integer value = 1;
    List<ComparisonOperator> excludes = Arrays.asList(
      IsNull, IsNotNull, In, NotIn, Range, RangeGTEAndLT, RangeGTAndLTE, RangeGTAndLT
    );

    Arrays.stream(ComparisonOperator.values()).filter(o1 -> excludes.stream().noneMatch(o2 -> o1 == o2))
      .forEach(operator -> {
        // without NamedParam
        Condition c = Condition.of(id, operator, value);
        assertThat(c.toQL(), is(String.format("id %s ?", operator.symbol())));
        assertThat(c.toQL(null, null), is(String.format("id %s ?", operator.symbol())));

        // with default NamedParam
        Map<String, Object> namedParamsValues = new HashMap<>();
        c = Condition.of(id, operator, value);
        assertThat(c.toQL(null, namedParamsValues), is(String.format("id %s :%s", operator.symbol(), id)));
        assertThat(namedParamsValues.size(), is(1));

        if (operator == Like || operator == iLike) {
          assertThat(namedParamsValues.get(id), is("%" + value + "%"));
        } else if (operator == LikeLeft || operator == iLikeLeft) {
          assertThat(namedParamsValues.get(id), is(value + "%"));
        } else if (operator == LikeRight || operator == iLikeRight) {
          assertThat(namedParamsValues.get(id), is("%" + value));
        } else assertThat(namedParamsValues.get(id), is(value));

        // with specified NamedParam
        namedParamsValues = new HashMap<>();
        c = Condition.of(id, operator, value);
        assertThat(c.toQL(null, namedParamsValues, "k"), is(String.format("id %s :%s", operator.symbol(), "k")));
        assertThat(namedParamsValues.size(), is(1));

        if (operator == Like || operator == iLike) {
          assertThat(namedParamsValues.get("k"), is("%" + value + "%"));
        } else if (operator == LikeLeft || operator == iLikeLeft) {
          assertThat(namedParamsValues.get("k"), is(value + "%"));
        } else if (operator == LikeRight || operator == iLikeRight) {
          assertThat(namedParamsValues.get("k"), is("%" + value));
        } else assertThat(namedParamsValues.get("k"), is(value));
      });
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
