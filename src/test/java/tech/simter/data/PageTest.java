package tech.simter.data;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author RJ
 */
class PageTest {
  @Test
  void empty() {
    Page<Object> page = Page.build(0, 0, null, 0);
    assertEquals(1, page.getNumber());
    assertEquals(Page.DEFAULT_CAPACITY, page.getCapacity());
    assertNotNull(page.getRows());
    assertEquals(0L, page.getTotalCount());

    assertEquals(0, page.getPageCount());
    assertEquals(0, page.getSize());
    assertEquals(0, page.getOffset());
    assertTrue(page.isEmpty());
    assertTrue(page.isFirst());
    assertTrue(page.isLast());
  }

  @Test
  void firstPage() {
    List<String> items = new ArrayList<>();
    items.add("1");
    Page<String> page = Page.build(1, 25, items, 101);
    assertEquals(1, page.getNumber());
    assertEquals(25, page.getCapacity());
    assertNotNull(page.getRows());
    assertEquals(101L, page.getTotalCount());

    assertEquals(5, page.getPageCount());
    assertEquals(1, page.getSize());
    assertEquals(0, page.getOffset());
    assertFalse(page.isEmpty());
    assertTrue(page.isFirst());
    assertFalse(page.isLast());
  }

  @Test
  void secondPage() {
    List<String> items = new ArrayList<>();
    items.add("1");
    Page<String> page = Page.build(2, 25, items, 101);
    assertEquals(2, page.getNumber());
    assertEquals(25, page.getCapacity());
    assertNotNull(page.getRows());
    assertEquals(101L, page.getTotalCount());

    assertEquals(5, page.getPageCount());
    assertEquals(1, page.getSize());
    assertEquals(25, page.getOffset());
    assertFalse(page.isEmpty());
    assertFalse(page.isFirst());
    assertFalse(page.isLast());

    // another
    page = Page.build(3, 25, items, 101);
    assertEquals(3, page.getNumber());
    assertEquals(25, page.getCapacity());
    assertNotNull(page.getRows());
    assertEquals(101L, page.getTotalCount());

    assertEquals(5, page.getPageCount());
    assertEquals(1, page.getSize());
    assertEquals(50, page.getOffset());
    assertFalse(page.isEmpty());
    assertFalse(page.isFirst());
    assertFalse(page.isLast());
  }

  @Test
  void lastPage() {
    List<String> items = new ArrayList<>();
    items.add("1");
    Page<String> page = Page.build(5, 25, items, 101);
    assertEquals(5, page.getNumber());
    assertEquals(25, page.getCapacity());
    assertNotNull(page.getRows());
    assertEquals(101L, page.getTotalCount());

    assertEquals(5, page.getPageCount());
    assertEquals(1, page.getSize());
    assertEquals(100, page.getOffset());
    assertFalse(page.isEmpty());
    assertFalse(page.isFirst());
    assertTrue(page.isLast());
  }

  @Test
  void canNotModifyPageData() {
    assertThrows(UnsupportedOperationException.class, () -> {
      List<String> items = new ArrayList<>();
      items.add("1");
      Page<String> page = Page.build(1, 25, items, 101);
      page.getRows().add("something");
    });
  }

  @Test
  void toValidCapacity() {
    assertEquals(Page.DEFAULT_CAPACITY, Page.toValidCapacity(-1));
    assertEquals(Page.DEFAULT_CAPACITY, Page.toValidCapacity(0));
    assertEquals(1, Page.toValidCapacity(1));
    assertEquals(10, Page.toValidCapacity(10));
    assertEquals(100, Page.toValidCapacity(100));
  }

  @Test
  void calculateOffset() {
    assertEquals(0, Page.calculateOffset(1, 1));
    assertEquals(1, Page.calculateOffset(2, 1));

    assertEquals(0, Page.calculateOffset(1, 25));
    assertEquals(25, Page.calculateOffset(2, 25));

    assertEquals(0, Page.calculateOffset(1, 100));
    assertEquals(100, Page.calculateOffset(2, 100));

    assertEquals(0, Page.calculateOffset(-1, -1));
    assertEquals(0, Page.calculateOffset(-1, 0));
    assertEquals(0, Page.calculateOffset(0, 0));
    assertEquals(0, Page.calculateOffset(1, 0));
    assertEquals(25, Page.calculateOffset(2, 0));
  }

  @Test
  void iterator() {
    List<String> items = new ArrayList<>();
    for (int i = 0; i < 10; i++) items.add(String.valueOf(i));
    Page<String> page = Page.build(1, 25, items, 101);
    int i = 0;
    for (String row : page) {
      assertEquals(String.valueOf(i), row);
      i++;
    }
  }
}
