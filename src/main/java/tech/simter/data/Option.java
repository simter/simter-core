package tech.simter.data;

/**
 * An option for simple text/value holder. It's similar to 'HTML DOM Option' object.
 *
 * @author RJ
 */
public class Option {
  /**
   * The text.
   */
  public String text;
  /**
   * The value.
   */
  public String value;

  public Option() {
  }

  /**
   * Instance with only text. This also set value equals to text.
   *
   * @param text the text
   */
  public Option(String text) {
    this.text = text;
    this.value = text;
  }

  /**
   * Instance with text and value.
   *
   * @param text  the text
   * @param value the value
   */
  public Option(String text, String value) {
    this.text = text;
    this.value = value;
  }

  /**
   * Instance with text and value.
   *
   * @param text  the text
   * @param value the number value
   */
  public Option(String text, Number value) {
    this.text = text;
    this.value = value == null ? null : value.toString();
  }
}