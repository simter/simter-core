package tech.simter.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The result of imported
 *
 * @author cjw
 */
public class ImportedResult implements Serializable {
  /**
   * The total of success imported.
   */
  public int successCount;
  /**
   * The total of ignore imported.
   */
  public int ignoreCount;
  private List<String> columnNames;
  private List<Error> errors;

  /**
   * Get the total of failure imported.
   *
   * @return the total of failure imported
   */
  public int getErrorCount() {
    return this.getErrors().size();
  }

  /**
   * Get the total of all data to be imported.
   *
   * @return the total of all data to be imported
   */
  public int getTotalCount() {
    return successCount + this.getErrorCount() + ignoreCount;
  }

  /**
   * Get the list contains column name.
   *
   * @return list contains column name
   */
  public List<String> getColumnNames() {
    if (null == columnNames) columnNames = new ArrayList<>();
    return columnNames;
  }

  /**
   * Set the column name with specified list.
   *
   * @param columnNames list to be stored
   */
  public void setColumnNames(List<String> columnNames) {
    this.columnNames = columnNames;
  }

  /**
   * Add column names.
   *
   * @param columnNames column name to be append
   * @return this instance
   */
  public ImportedResult addColumnNames(String... columnNames) {
    this.getColumnNames().addAll(Arrays.asList(columnNames));
    return this;
  }

  /**
   * Get the list contains detail of failure imported.
   *
   * @return list contains detail of failure imported
   */
  public List<Error> getErrors() {
    if (null == errors) errors = new ArrayList<>();
    return errors;
  }

  /**
   * Set the errors with specified list.
   *
   * @param errors list to be stored
   */
  public void setErrors(List<Error> errors) {
    this.errors = errors;
  }

  /**
   * Add errors.
   *
   * @param errors error to be append
   * @return this instance
   */
  public ImportedResult addErrors(Error... errors) {
    this.getErrors().addAll(Arrays.asList(errors));
    return this;
  }

  /**
   * The detail of failure imported.
   *
   * @author cjw
   */
  public static class Error implements Serializable {
    /**
     * The index of error row.
     */
    public int index;
    /**
     * The failure description.
     */
    public String msg;
    private List<Object> source;

    public Error() {
    }

    /**
     * Instance with index, msg and source.
     *
     * @param index  the index of error row
     * @param msg    the failure description
     * @param source the origin row data
     */
    public Error(int index, String msg, List source) {
      this.index = index;
      this.msg = msg;
      this.source = source;
    }

    /**
     * Get the source.
     *
     * @return the list contains origin row data
     */
    public List<Object> getSource() {
      if (null == source) source = new ArrayList();
      return source;
    }

    /**
     * Set the source with specified list.
     *
     * @param source list to be stored
     */
    public void setSource(List<Object> source) {
      this.source = source;
    }

    /**
     * Add sources.
     *
     * @param values source value to be append
     * @return this instance
     */
    public Error addSource(Object... values) {
      if (null != values && values.length > 0)
        this.getSource().addAll(Arrays.asList(values));
      return this;
    }
  }
}