package tech.simter.annotation;

import java.lang.annotation.*;
import java.time.format.DateTimeFormatter;

/**
 * Can be used on {@link java.util.Date}, {@link java.util.Calendar} and java8DateTime to
 * indicate the pattern or lang to use when working with this date or time field.
 * The pattern format are the standard ones from {@link DateTimeFormatter}.
 *
 * @author RJ
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Format {
  /**
   * The pattern to use.
   *
   * @return the pattern
   */
  String value() default "";

  /**
   * The lang.
   *
   * @return the lang
   */
  String lang() default "";
}