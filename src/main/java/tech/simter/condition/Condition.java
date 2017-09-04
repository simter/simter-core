package tech.simter.condition;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonString;
import javax.json.JsonValue;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static tech.simter.condition.ComparisonOperator.*;
import static tech.simter.condition.ValueConverter.convert;

/**
 * The common condition interface.
 * <p>
 * Only expose the condition's markup. Use to hide actual sql to avoid sql attack.
 *
 * @author RJ
 */
public interface Condition {
  /**
   * The identity markup.
   *
   * @return The id
   */
  String getId();

  /**
   * The comparison operator.
   *
   * @return The operator
   */
  ComparisonOperator getOperator();

  /**
   * The value
   *
   * @return The value
   */
  Object getValue();

  /**
   * The values.
   * <p>
   * This method only support for multiple value condition. At this situation,
   * this method only force convert the {@link #getValue()} to {@link List List&lt;Object&gt;} and return it.
   *
   * @return The values
   */
  List<Object> getValues();

  /**
   * Convert to query language.
   * <p>
   * Such as key="code", name=":code", this={@link ComparisonOperator#Equals},
   * then the return value should be "code = :code".
   *
   * @param namedParamNames The NamedParam name, use '?' instead if not specified
   * @return query language
   * @throws UnsupportedOperationException If is range enum
   */
  String toQL(String... namedParamNames);

  /**
   * Build a new Condition instance.
   *
   * @param id       the id
   * @param operator the comparison operator
   * @param value    the value
   * @return the Condition instance
   */
  static Condition of(String id, ComparisonOperator operator, Object value) {
    return new Impl(id, operator, value);
  }

  /**
   * Build a new Condition instance.
   *
   * @param id       the id
   * @param operator the comparison operator
   * @return the Condition instance
   */
  static Condition of(String id, ComparisonOperator operator) {
    return new Impl(id, operator, null);
  }

  /**
   * Convert search value to Condition list.
   * <p>
   * The search param value must has standard JsonArray structure.
   * Each array item holds the condition data.
   * It must also be a standard JsonArray structure with [id, value[, type, operator]].
   * The type and operator is optional.
   * When ignore, the default type is {@link String}, the default operator is {@link ComparisonOperator#Equals}.
   *
   * @param search The search value
   * @return The Condition instance list
   */
  static List<Condition> toConditions(String search) {
    if (search == null || search.isEmpty()) return null;
    try {
      List<Condition> list = new ArrayList<>();
      Json.createReader(new StringReader(URLDecoder.decode(search, "UTF-8")))
        .readArray()
        .forEach(item -> list.add(toCondition((JsonArray) item)));
      return list;
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Build a new Condition instance.
   * <p>
   * The item parameter must has the specific structure with [id, value[, type, operator]].
   * The type and operator is optional.
   *
   * @param item The JsonArray item hold the structure data
   * @return the Condition instance
   */
  static Condition toCondition(JsonArray item) {
    // verify JsonArray structure: [id, value[, type, operator]]
    if (item == null || item.size() < 2)
      throw new IllegalArgumentException("invalid array structure, at lease has two item in the array: jsonArray=" + item);

    // The id
    String id = item.getString(0);

    // The value
    Object value;
    String type = item.size() > 2 && item.get(2).getValueType() != JsonValue.ValueType.NULL ?
      item.getString(2) : "String"; // Default type is String
    JsonValue ItemValue = item.get(1);
    if (ItemValue.getValueType() == JsonValue.ValueType.STRING) {        // Single value
      value = convert(type, item.getString(1));
    } else if (ItemValue.getValueType() == JsonValue.ValueType.ARRAY) {  // Multiple value
      JsonArray array = item.getJsonArray(1);
      List<Object> values = new ArrayList<>();
      array.forEach(v ->
        values.add(v.getValueType() == JsonValue.ValueType.NULL ? null : convert(type, ((JsonString) v).getString())));
      value = values;
    } else throw new IllegalArgumentException(
      "unsupported value type: valueType=" + item.getValueType() + ", jsonArray=" + item
    );

    // The operator
    ComparisonOperator operator = item.size() > 3 ?
      ComparisonOperator.symbolOf(item.getString(3)) : ComparisonOperator.Equals; // Default operator is Equals

    return new Impl(id, operator, value);
  }

  /**
   * Convert the condition to a ql with the specific NamedParam names.
   * <p>
   * If NamedParam is not specified and the condition has a valid value in the value position,
   * use '?' as the default NamedParam name.
   * <p>
   * For examples: <br>
   * 1) id="code", operator={@link ComparisonOperator#Equals Equals}, namedParamNames=[":code"].
   * Then return value should be "code = :code".<br>
   * 2) id="code", operator={@link ComparisonOperator#RangeGTAndLTE (]}, namedParamNames=[":code1", ":code2"].
   * Then return value should be "code &gt; :code1 and code &lt;= :code2".
   *
   * @param condition       The condition
   * @param namedParamNames The NamedParam names
   * @return The query language
   */
  static String toQL(Condition condition, String... namedParamNames) {
    if (condition == null) throw new IllegalArgumentException("The condition should not be null.");
    if (condition.getId() == null || condition.getId().isEmpty())
      throw new IllegalArgumentException("The condition's id should not be null or empty.");
    if (condition.getOperator() == null)
      throw new IllegalArgumentException("The condition's operator should not be null.");

    int len = namedParamNames == null ? 0 : namedParamNames.length;
    if (condition.getOperator().isRange()) {
      List<Object> values = condition.getValues();
      String startName = len > 0 ? namedParamNames[0] : (values.get(0) == null ? null : "?");
      String endName = len > 1 ? namedParamNames[1] : (values.size() < 2 || values.get(1) == null ? null : "?");
      if ((startName == null || startName.isEmpty()) && (endName == null || endName.isEmpty()))
        throw new IllegalArgumentException(MessageFormat.format(
          "At lease supply one NamedParam: id={0}, operator={1}, values={2}, namedParamNames={3)",
          condition.getId(), condition.getOperator(), condition.getValue(), namedParamNames));

      StringBuffer q = new StringBuffer();
      if (startName != null && !startName.isEmpty())
        q.append(condition.getId())
          .append(" ")
          .append(condition.getOperator().symbol().startsWith("[") ? ">=" : ">")
          .append(" ")
          .append(startName);
      if (endName != null && !endName.isEmpty()) {
        if (q.length() > 0) q.append(" and ");
        q.append(condition.getId())
          .append(" ")
          .append(condition.getOperator().symbol().endsWith("]") ? "<=" : "<")
          .append(" ")
          .append(endName);
      }
      return q.toString();
    } else {
      // Use '?' instead if NamedParam is not specified
      // TODO : in, not in, like, ilike value with %
      if (condition.getOperator() == IsNull || condition.getOperator() == IsNotNull) {
        return condition.getId() + " " + condition.getOperator().symbol();
      } else if (condition.getOperator() == In || condition.getOperator() == NotIn) {
        String ql = condition.getId() + " " + condition.getOperator().symbol() + " (";
        if (len == 0) {                  // use '?' for each value in list values
          ql += condition.getValues().stream().map(v -> "?").collect(Collectors.joining(", "));
        } else if (len == 1) {           // such as jpa array param - "id in (:ids)"
          ql += namedParamNames[0];
        } else {                         // use NamedParam names
          ql += Arrays.stream(namedParamNames).collect(Collectors.joining(", "));
        }
        ql += ")";
        return ql;
      } else {
        return condition.getId() + " " + condition.getOperator().symbol() + " " + (len > 0 ? namedParamNames[0] : "?");
      }
    }
  }

  /**
   * The inner Condition implement.
   */
  class Impl implements Condition {
    protected String id;
    protected ComparisonOperator operator;
    protected Object value;

    Impl(String id, ComparisonOperator operator, Object value) {
      this.id = id;
      this.operator = operator;
      this.value = value;
    }

    @Override
    public String getId() {
      return this.id;
    }

    @Override
    public ComparisonOperator getOperator() {
      return this.operator;
    }

    @Override
    public Object getValue() {
      return this.value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> getValues() {
      return (List<Object>) getValue();
    }

    @Override
    public String toQL(String... namedParamNames) {
      return Condition.toQL(this, namedParamNames);
    }

    @Override
    public String toString() {
      return this.toQL();
    }
  }
}