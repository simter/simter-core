package tech.simter.data;

import java.io.Serializable;

/**
 * An node for tree view.
 *
 * @author cjw
 */
public class Node implements Serializable {
  /**
   * The id
   */
  public String id;
  /**
   * The label
   */
  public String label;
  private boolean leaf;

  /**
   * Instance with only id. This also set label equals to id.
   *
   * @param id
   */
  public Node(String id, boolean leaf) {
    this.id = id;
    this.label = id;
    this.setLeaf(leaf);
  }

  /**
   * Instance with id and label.
   *
   * @param id    the id
   * @param label the label
   */
  public Node(String id, String label, boolean leaf) {
    this.id = id;
    this.label = label;
    this.setLeaf(leaf);
  }

  /**
   * Instance with id and label.
   *
   * @param id    the number id
   * @param label the label
   */
  public Node(Number id, String label, boolean leaf) {
    this.id = id.toString();
    this.label = label;
    this.setLeaf(leaf);
  }

  /**
   * Instance with id and label.
   *
   * @param id    the id
   * @param label the number label
   */
  public Node(String id, Number label, boolean leaf) {
    this.id = id;
    this.label = label.toString();
    this.setLeaf(leaf);
  }

  /**
   * Instance with id and label.
   *
   * @param id    the number id
   * @param label the number label
   */
  public Node(Number id, Number label, boolean leaf) {
    this.id = id.toString();
    this.label = label.toString();
    this.setLeaf(leaf);
  }

  /**
   * Whether the node has child node at all.
   */
  public boolean isLeaf() {
    return leaf;
  }

  /**
   * Set the leaf with specified list.
   *
   * @param leaf value to be set.
   */
  public void setLeaf(boolean leaf) {
    this.leaf = leaf;
  }
}
