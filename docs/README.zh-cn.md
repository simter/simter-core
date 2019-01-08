# [simter-core](https://github.com/simter/simter-core) [[English]]

一些基础性的常用接口或类:

- 注解类 (tech.simter.annotation)
    - `Format`：属性值输出格式化注解
    - `Comment`：包、类、字段、方法的注释信息注解
- 持久化 (tech.simter.persistence)
    - `PersistenceEnum` 接口：带持久化值的持久化接口
    - `CommonState` 枚举：实现了 `PersistenceEnum` 接口的常用状态枚举，包括：Draft(1), Enabled(2), Disabled(4) and Deleted(8)
    - `Sex` 枚举：实现了 `PersistenceEnum` 接口的性别枚举, 包括：Undefined(1), Male(2) and Female(4).
- 通用数据接口/类 (tech.simter.data)
    - `Page<T>` 接口：分页数据持有者接口，含静态工具方法及默认实现
    - `Id<T>` 类：一个简单的 ID 值持有者，含单一的 id 属性
    - `Ts` 类：一个简单的时间挫值持有者，含单一的 ts 属性，可通过 put 方法扩展
    - `Created<T>` 类：继承于 `Id<T>`，用于持有创建信息，含 id、ts 两个属性
    - `Option` 类：一个简单的 text/value 数据持有者，类似于 'HTML DOM Option' 对象，有 text 和 value 两个属性
    - `Node` 类：树节点数据的封装
    - `ImportedResult` 类：导入结果的数据封装

## 1. 安装

```xml
<dependency>
  <groupId>tech.simter</groupId>
  <artifactId>simter-core</artifactId>
  <version>1.0.0</version>
</dependency>
```

## 2. 要求

- Java 8+

## 3. 使用

### 3.1. Format

```java
@Format("yyyy-MM-dd HH:mm")
private LocalDateTime createOn;
```

### 3.2. Comment

```java
@Comment("The document's create-time")
private LocalDateTime createOn;
```

### 3.3 Page\<T\>

通过便捷的静态方法 `build` 来构建默认的 `Page` 接口实现：

```java
List<String> rows = Arrays.asList("a", "b");
Page<String> page = Page.build(1, 25, rows, 100L);
assertThat(page.getNumber(), is(1));
assertThat(page.getCapacity(), is(25));
assertThat(page.getRows(), is(rows));
assertThat(page.getTotalCount(), is(100L));
```

## 4. 构建

```bash
mvn clean package
```

## 5. 发布

请先查看 [simter-parent] 的发布配置说明。

### 5.1. 发布到局域网 Nexus 仓库

```bash
mvn clean deploy -P lan
```

### 5.2. 发布到 Sonatype 仓库

```bash
mvn clean deploy -P sonatype
```

发布成功后登陆到 <https://oss.sonatype.org>，在 `Staging Repositories` 找到这个包，然后将其 close 和 release。
过几个小时后，就会自动同步到 [Maven 中心仓库](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22simter-core%22) 了。

### 5.3. 发布到 Bintray 仓库

```bash
mvn clean deploy -P bintray
```

发布之前要先在 Bintray 创建 package `https://bintray.com/simter/maven/tech.simter:simter-core`。
发布到的地址为 `https://api.bintray.com/maven/simter/maven/tech.simter:simter-core/;publish=1`。
发布成功后可以到 <https://jcenter.bintray.com/tech/simter/simter-core> 检查一下结果。


[simter-parent]: https://github.com/simter/simter-parent
[English]: https://github.com/simter/simter-core/blob/master/README.md