package tech.simter.annotation;

import java.lang.annotation.*;

/**
 * Define a description on the target package, class, field or method.
 *
 * @author RJ
 */
@Target({
  ElementType.PACKAGE, ElementType.TYPE, ElementType.FIELD, ElementType.METHOD,
  ElementType.PARAMETER, ElementType.CONSTRUCTOR
})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Comment {
  /**
   * The description.
   *
   * @return the description
   */
  String value();
}