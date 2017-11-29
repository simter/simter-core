package tech.simter.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A tree-node data holder.
 *
 * @author cjw
 * @author RJ
 */
public class Node implements Serializable {
  /**
   * The id.
   */
  private Object id;
  /**
   * The label.
   */
  private String label;
  private boolean leaf = true;
  private boolean collapsed = true;
  private boolean selected = false;
  private String icon;

  /**
   * The url to load children if not specified children explicit.
   */
  private String url;
  /**
   * The url param key that will append to the url when load children by url.
   */
  private String paramKey;
  /**
   * The child nodes.
   */
  private List<Node> children;

  public Node() {
  }

  /**
   * Instance with id.
   *
   * @param id the id
   */
  public Node(Object id) {
    this.id = id;
  }

  /**
   * Instance with id and label.
   *
   * @param id    the id
   * @param label the label
   */
  public Node(Object id, String label) {
    this.id = id;
    this.label = label;
  }

  public Object getId() {
    return id;
  }

  public Node setId(Object id) {
    this.id = id;
    return this;
  }

  public String getLabel() {
    return label;
  }

  public Node setLabel(String label) {
    this.label = label;
    return this;
  }

  public boolean isLeaf() {
    return leaf;
  }

  public Node setLeaf(boolean leaf) {
    this.leaf = leaf;
    return this;
  }

  public boolean isCollapsed() {
    return collapsed;
  }

  public Node setCollapsed(boolean collapsed) {
    this.collapsed = collapsed;
    return this;
  }

  public boolean isSelected() {
    return selected;
  }

  public Node setSelected(boolean selected) {
    this.selected = selected;
    return this;
  }

  public String getIcon() {
    return icon;
  }

  public Node setIcon(String icon) {
    this.icon = icon;
    return this;
  }

  public String getUrl() {
    return url;
  }

  public Node setUrl(String url) {
    this.url = url;
    this.leaf = (url == null || url.isEmpty());
    return this;
  }

  public String getParamKey() {
    return paramKey;
  }

  public Node setParamKey(String paramKey) {
    this.paramKey = paramKey;
    return this;
  }

  public List<Node> getChildren() {
    return children;
  }

  public Node setChildren(List<Node> children) {
    this.children = children;
    this.leaf = children == null;
    if (children != null) this.url = null;
    return this;
  }

  /**
   * Add one node to the children.
   *
   * @param child the child to add
   * @return this instance
   */
  public Node addChildren(Node child) {
    if (child == null) return this;
    if (this.children == null) this.children = new ArrayList<>();
    this.children.add(child);
    return this;
  }
}