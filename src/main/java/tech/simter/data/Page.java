package tech.simter.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A page is a sublist of a list of objects.
 * It allows gain information about the position of it in the containing entire list.
 * <p>
 * The main interface methods just 4 of below, all other method can be calculate from them:
 * <ul>
 * <li>int getNumber() - The page number, it's one of the page conditions</li>
 * <li>int getCapacity() - The page capacity, it's the maximal size of each page, it's one of the page conditions</li>
 * <li>int getRows() - All real row data of this page, it's one of the page results</li>
 * <li>int getTotalCount() - The total amount of all data store in the repository, it's one of the page results</li>
 * </ul>
 *
 * @param <T> the type of the row data
 * @author RJ 2017-04-27
 */
public interface Page<T> extends Serializable, Iterable<T> {
  /**
   * The number of this page. It's minimal value is 1.
   *
   * @return the number of this page
   */
  int getNumber();

  /**
   * The maximal size of each page. It's minimal value is 1.
   *
   * @return the maximal size of each page
   */
  int getCapacity();

  /**
   * All the row data of this page.
   * Returns {@link Collections#emptyList()} if this page has no data. Implementer should never return null.
   *
   * @return all the real row data of this page
   */
  List<T> getRows();

  /**
   * The total amount of all rows in the store.
   *
   * @return zero if the repository has no data
   */
  long getTotalCount();

  /**
   * The number of total pages. It's minimal value is 0.
   *
   * @return zero if the repository has no data
   */
  int getPageCount();

  /**
   * The offset to be taken according to the page number and capacity. It's minimal value is 0.
   *
   * @return The offset to be taken according to the page number and capacity
   */
  int getOffset();

  /**
   * Whether the page has data at all.
   *
   * @return true if this page has data, otherwise return false
   */
  boolean isEmpty();

  /**
   * The real data size of this page. It's minimal value is 0.
   *
   * @return the real data size of this page
   */
  int getSize();

  /**
   * Whether the current page is the first page.
   *
   * @return true if this page is the first page, otherwise return false
   */
  boolean isFirst();

  /**
   * Whether the current page is the last page.
   *
   * @return true if this page is the last page, otherwise return false
   */
  boolean isLast();

  /**
   * Build a new page instance.
   *
   * @param number     the page number
   * @param capacity   the page maximal size
   * @param rows       the page data
   * @param totalCount the total amount of all rows in the store
   * @param <T>        the type of the row data
   * @return the page instance holds the data
   */
  static <T> Page<T> build(int number, int capacity, List<T> rows, long totalCount) {
    int number_ = Math.max(1, number);
    int capacity_ = Math.max(1, capacity);
    List<T> rows_ = rows != null ? Collections.unmodifiableList(rows) : Collections.emptyList();
    long totalCount_ = Math.max(0, totalCount);
    return new Page<T>() {
      @Override
      public int getNumber() {
        return number_;
      }

      @Override
      public int getCapacity() {
        return capacity_;
      }

      @Override
      public List<T> getRows() {
        return rows_;
      }

      @Override
      public long getTotalCount() {
        return totalCount_;
      }

      @Override
      public int getPageCount() {
        return (int) Math.ceil((double) totalCount_ / (double) capacity_);
      }

      @Override
      public int getOffset() {
        return (number_ - 1) * capacity_;
      }

      @Override
      public boolean isEmpty() {
        return rows_.isEmpty();
      }

      @Override
      public int getSize() {
        return rows_.size();
      }

      @Override
      public boolean isFirst() {
        return number_ <= 1;
      }

      @Override
      public boolean isLast() {
        return number_ + 1 > getPageCount();
      }

      // implement the {@link Iterable<T>} interface

      @Override
      public Iterator<T> iterator() {
        return rows_.iterator();
      }

      @Override
      public void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (T t : this.getRows()) {
          action.accept(t);
        }
      }
    };
  }
}
