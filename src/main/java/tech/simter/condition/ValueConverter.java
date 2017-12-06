package tech.simter.condition;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * The value converter.
 *
 * @author RJ
 */
final class ValueConverter {
  /**
   * The value separator.
   */
  private static final String SEPARATOR = ",";

  /**
   * Convert string value to actual type value
   *
   * @param type  Supported types are String|int|Integer|long|Long|float|Float|double|Double|Decimal|boolean|Boolean
   *              |String[]|int[]|Integer[]|long[]|Long[]|float[]|Float[]|double[]|Double[]|Decimal[]|boolean[]|Boolean[]
   *              |Date(only yyyy-MM-dd)|Calendar(only yyyy-MM-dd HH:mm:ss)
   *              |LocalDate|LocalDateTime|LocalTime|Year|YearMonth|Month|java.sql.Date
   * @param value The string value
   * @return The converted value with the specific type
   * @throws IllegalArgumentException If type is unsupported
   */
  @SuppressWarnings("unchecked")
  static <T> T convert(String type, String value) {
    if (type == null || type.length() == 0) return (T) value;

    if (type.equalsIgnoreCase("String")) return (T) toString(value);
    else if (type.equalsIgnoreCase("String[]")) return (T) toStringArray(value, SEPARATOR);
    else if (type.equals("int")) return (T) toInteger(value);
    else if (type.equals("int[]")) return (T) toIntArray(value, SEPARATOR);
    else if (type.equals("Integer")) return (T) toInteger(value);
    else if (type.equals("Integer[]")) return (T) toIntegerArray(value, SEPARATOR);
    else if (type.equals("long")) return (T) toLong(value);
    else if (type.equals("long[]")) return (T) toLong_Array(value, SEPARATOR);
    else if (type.equals("Long")) return (T) toLong(value);
    else if (type.equals("Long[]")) return (T) toLongArray(value, SEPARATOR);
    else if (type.equals("float")) return (T) toFloat(value);
    else if (type.equals("float[]")) return (T) toFloat_Array(value, SEPARATOR);
    else if (type.equals("Float")) return (T) toFloat(value);
    else if (type.equals("Float[]")) return (T) toFloatArray(value, SEPARATOR);
    else if (type.equals("double")) return (T) toDouble(value);
    else if (type.equals("double[]")) return (T) toDouble_Array(value, SEPARATOR);
    else if (type.equals("Double")) return (T) toDouble(value);
    else if (type.equals("Double[]")) return (T) toDoubleArray(value, SEPARATOR);
    else if (type.equals("boolean")) return (T) toBoolean(value);
    else if (type.equals("boolean[]")) return (T) toBoolean_Array(value, SEPARATOR);
    else if (type.equals("Boolean")) return (T) toBoolean(value);
    else if (type.equals("Boolean[]")) return (T) toBooleanArray(value, SEPARATOR);
    else if (type.equals("BigDecimal") || type.equalsIgnoreCase("Decimal")) return (T) toBigDecimal(value);
    else if (type.equals("BigDecimal[]") || type.equalsIgnoreCase("Decimal[]"))
      return (T) toBigDecimalArray(value, SEPARATOR);
    else if (type.equals("Date")) return (T) toSqlDate(value);
    else if (type.equals("Calendar")) return (T) toCalendar(value);
    else if (type.equals("LocalDate")) return (T) toLocalDate(value);
    else if (type.equals("LocalDate[]")) return (T) toLocalDateArray(value, SEPARATOR);
    else if (type.equals("LocalDateTime")) return (T) toLocalDateTime(value);
    else if (type.equals("LocalDateTime[]")) return (T) toLocalDateTimeArray(value, SEPARATOR);
    else if (type.equals("LocalTime")) return (T) toLocalTime(value);
    else if (type.equals("LocalTime[]")) return (T) toLocalTimeArray(value, SEPARATOR);
    else if (type.equals("Year")) return (T) toYear(value);
    else if (type.equals("YearMonth")) return (T) toYearMonth(value);
    else if (type.equals("Month")) return (T) toMonth(value);
      // 调用类的构造函数 Constructor(String) 或静态方法 valueOf(String)、parse(String) 来获取值
    else return valueOf(type, value);
    //else throw new IllegalArgumentException("unsupported value type: type=" + type + ", value=" + value);
  }

  @SuppressWarnings("unchecked")
  private static <T> T valueOf(String type, String value) {
    if (value == null || value.isEmpty()) return null;
    try {
      Class<?> clazz = Class.forName(type);
      try {
        // instance by single String param constructor
        Constructor constructor = clazz.getDeclaredConstructor(String.class);
        return (T) constructor.newInstance(value);
      } catch (NoSuchMethodException e) {
        try {
          // instance by single CharSequence param constructor
          Constructor constructor = clazz.getDeclaredConstructor(CharSequence.class);
          return (T) constructor.newInstance(value);
        } catch (NoSuchMethodException e1) {
          try {
            // instance by single String param static method with name 'valueOf'
            Method method = clazz.getDeclaredMethod("valueOf", String.class);
            return (T) method.invoke(clazz, value);
          } catch (NoSuchMethodException me) {
            try {
              // instance by single CharSequence param static method with name 'valueOf'
              Method method = clazz.getDeclaredMethod("valueOf", CharSequence.class);
              return (T) method.invoke(clazz, value);
            } catch (NoSuchMethodException me1) {
              try {
                // instance by single String param static method with name 'parse'
                Method method = clazz.getDeclaredMethod("parse", String.class);
                return (T) method.invoke(clazz, value);
              } catch (NoSuchMethodException me2) {
                try {
                  // instance by single CharSequence param static method with name 'parse'
                  Method method = clazz.getDeclaredMethod("parse", CharSequence.class);
                  return (T) method.invoke(clazz, value);
                } catch (NoSuchMethodException me3) {
                  throw new RuntimeException("At leas define one String param constructor" +
                    " or static method with name 'valueOf' or 'parse' for class '" + type + "'");
                }
              }
            }
          }
        }
      }
    } catch (Exception e) {
      throw new IllegalArgumentException(e.getMessage(), e);
    }
  }

  private static String toString(String value) {
    return value;
  }

  private static String[] toStringArray(String value, String separator) {
    String subStr = value.substring(1, value.length() - 1);
    String[] strArray = subStr.split(separator);
    for (int i = 0; i < strArray.length; i++)
      strArray[i] = strArray[i] == null ? strArray[i] :
        strArray[i].isEmpty() ? strArray[i].trim() : strArray[i].trim().replaceAll("\"", "");
    return strArray;
  }

  private static int toInt(String value) {
    return value == null || value.isEmpty() ? 0 : Integer.parseInt(value);
  }

  private static int[] toIntArray(String value, String separator) {
    String subStr = value.substring(1, value.length() - 1);
    String[] strArray = subStr.split(separator);
    int[] result = new int[strArray.length];
    for (int i = 0; i < strArray.length; i++) result[i] = toInt(strArray[i]);
    return result;
  }

  private static Integer toInteger(String value) {
    return value == null || value.isEmpty() ? null : new Integer(value.trim());
  }

  private static Integer[] toIntegerArray(String value, String separator) {
    String subStr = value.substring(1, value.length() - 1);
    String[] strArray = subStr.split(separator);
    Integer[] result = new Integer[strArray.length];
    for (int i = 0; i < strArray.length; i++) result[i] = toInteger(strArray[i]);
    return result;
  }

  private static long toLong_(String value) {
    return value == null || value.isEmpty() ? 0L : Long.parseLong(value);
  }

  private static long[] toLong_Array(String value, String separator) {
    String subStr = value.substring(1, value.length() - 1);
    String[] strArray = subStr.split(separator);
    long[] result = new long[strArray.length];
    for (int i = 0; i < strArray.length; i++) result[i] = toLong_(strArray[i]);
    return result;
  }

  private static Long toLong(String value) {
    return value == null || value.isEmpty() ? null : new Long(value.trim());
  }

  private static Long[] toLongArray(String value, String separator) {
    String subStr = value.substring(1, value.length() - 1);
    String[] strArray = subStr.split(separator);
    Long[] result = new Long[strArray.length];
    for (int i = 0; i < strArray.length; i++) result[i] = toLong(strArray[i]);
    return result;
  }

  private static float toFloat_(String value) {
    return value == null || value.isEmpty() ? 0F : Float.parseFloat(value);
  }

  private static float[] toFloat_Array(String value, String separator) {
    String subStr = value.substring(1, value.length() - 1);
    String[] strArray = subStr.split(separator);
    float[] result = new float[strArray.length];
    for (int i = 0; i < strArray.length; i++) result[i] = toFloat_(strArray[i]);
    return result;
  }

  private static Float toFloat(String value) {
    return value == null || value.isEmpty() ? null : new Float(value.trim());
  }

  private static Float[] toFloatArray(String value, String separator) {
    String subStr = value.substring(1, value.length() - 1);
    String[] strArray = subStr.split(separator);
    Float[] result = new Float[strArray.length];
    for (int i = 0; i < strArray.length; i++) result[i] = toFloat(strArray[i]);
    return result;
  }

  private static double toDouble_(String value) {
    return value == null || value.isEmpty() ? 0D : Double.parseDouble(value);
  }

  private static double[] toDouble_Array(String value, String separator) {
    String subStr = value.substring(1, value.length() - 1);
    String[] strArray = subStr.split(separator);
    double[] result = new double[strArray.length];
    for (int i = 0; i < strArray.length; i++) result[i] = toDouble_(strArray[i]);
    return result;
  }

  private static Double toDouble(String value) {
    return value == null || value.isEmpty() ? null : new Double(value.trim());
  }

  private static Double[] toDoubleArray(String value, String separator) {
    String subStr = value.substring(1, value.length() - 1);
    String[] strArray = subStr.split(separator);
    Double[] result = new Double[strArray.length];
    for (int i = 0; i < strArray.length; i++) result[i] = toDouble(strArray[i]);
    return result;
  }

  private static boolean toBoolean_(String value) {
    return value != null && !value.isEmpty() && Boolean.parseBoolean(value);
  }

  private static boolean[] toBoolean_Array(String value, String separator) {
    String subStr = value.substring(1, value.length() - 1);
    String[] strArray = subStr.split(separator);
    boolean[] result = new boolean[strArray.length];
    for (int i = 0; i < strArray.length; i++) result[i] = toBoolean_(strArray[i]);
    return result;
  }

  private static Boolean toBoolean(String value) {
    return value == null || value.isEmpty() ? null : Boolean.valueOf(value.trim());
  }

  private static Boolean[] toBooleanArray(String value, String separator) {
    String subStr = value.substring(1, value.length() - 1);
    String[] strArray = subStr.split(separator);
    Boolean[] result = new Boolean[strArray.length];
    for (int i = 0; i < strArray.length; i++) result[i] = toBoolean(strArray[i]);
    return result;
  }

  private static BigDecimal toBigDecimal(String value) {
    return value == null || value.isEmpty() ? null : new BigDecimal(value.trim().replace(",", ""));
  }

  private static BigDecimal[] toBigDecimalArray(String value, String separator) {
    String subStr = value.substring(1, value.length() - 1);
    String[] strArray = subStr.split(separator);
    BigDecimal[] result = new BigDecimal[strArray.length];
    for (int i = 0; i < strArray.length; i++) result[i] = toBigDecimal(strArray[i]);
    return result;
  }

  private static Date toSqlDate(String value) {
    return value == null || value.isEmpty() ? null : Date.valueOf(value.trim());
  }

  // only support 'yyyy-MM-dd HH:mm:ss' format
  private static Calendar toCalendar(String value) {
    if (value == null || value.isEmpty()) return null;
    LocalDateTime parse = LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    Calendar instance = Calendar.getInstance();
    instance.set(Calendar.YEAR, parse.getYear());
    instance.set(Calendar.MONTH, parse.getMonthValue() - 1);
    instance.set(Calendar.DAY_OF_MONTH, parse.getDayOfMonth());
    instance.set(Calendar.HOUR_OF_DAY, parse.getHour());
    instance.set(Calendar.MINUTE, parse.getMinute());
    instance.set(Calendar.SECOND, parse.getSecond());
    instance.set(Calendar.MILLISECOND, 0);
    return instance;
  }

  private static LocalDate toLocalDate(String value) {
    return value == null || value.isEmpty() ? null : LocalDate.parse(value.trim());
  }

  private static LocalDate[] toLocalDateArray(String value, String separator) {
    String subStr = value.substring(1, value.length() - 1);
    String[] strArray = subStr.split(separator);
    LocalDate[] result = new LocalDate[strArray.length];
    for (int i = 0; i < strArray.length; i++) result[i] = toLocalDate(strArray[i]);
    return result;
  }

  private static LocalDateTime toLocalDateTime(String value) {
    return value == null || value.isEmpty() ? null : LocalDateTime.parse(value.trim());
  }

  private static LocalDateTime[] toLocalDateTimeArray(String value, String separator) {
    String subStr = value.substring(1, value.length() - 1);
    String[] strArray = subStr.split(separator);
    LocalDateTime[] result = new LocalDateTime[strArray.length];
    for (int i = 0; i < strArray.length; i++) result[i] = toLocalDateTime(strArray[i]);
    return result;
  }

  private static LocalTime toLocalTime(String value) {
    return value == null || value.isEmpty() ? null : LocalTime.parse(value.trim());
  }

  private static LocalTime[] toLocalTimeArray(String value, String separator) {
    String subStr = value.substring(1, value.length() - 1);
    String[] strArray = subStr.split(separator);
    LocalTime[] result = new LocalTime[strArray.length];
    for (int i = 0; i < strArray.length; i++) result[i] = toLocalTime(strArray[i]);
    return result;
  }

  private static Year toYear(String value) {
    return value == null || value.isEmpty() ? null : Year.parse(value);
  }

  private static YearMonth toYearMonth(String value) {
    return value == null || value.isEmpty() ? null : YearMonth.parse(value);
  }

  private static Month toMonth(String value) {
    return value == null || value.isEmpty() ? null : Month.of(Integer.parseInt(value));
  }
}