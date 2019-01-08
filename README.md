# [simter-core](https://github.com/simter/simter-core) [[中文]]

Some base or common things. Includes:

- Annotation (tech.simter.annotation)
    - The `Format` annotation class: Use to config the output format of the property value. Special for data/time property
    - The `Comment` annotation class: Use to define a description on the target package, class, field or method
- Persistence (tech.simter.persistence)
    - The `PersistenceEnum` Enum: The persistence interface with persistence value.
    - The `CommonState` Enum implemented `PersistenceEnum`: Defined common state, such as Draft(1), Enabled(2), Disabled(4) and Deleted(8).
    - The `Sex` Enum implemented `PersistenceEnum`: Defined common state, such as Undefined(1), Male(2) and Female(4).
- Common data class/interface (tech.simter.data)
    - The `Page<T>` interface: A page is a sublist of a list of objects.
    - The `Id<T>` class: A simple ID holder with a id property.
    - The `Ts` class: A simple timestamp holder with a ts property.
    - The `Created<T>` class extend `Id<T>`: The Created info object. It holds the identity and timestamp info. It has id and ts property.
    - The `Option` class: An option for simple text/value holder. It's similar to 'HTML DOM Option' object. It has text and value property.
    - The `Node` class: A tree-node data holder.
    - The `ImportedResult` class: A result holder for imported.

## 1. Installation

```xml
<dependency>
  <groupId>tech.simter</groupId>
  <artifactId>simter-core</artifactId>
  <version>1.0.0</version>
</dependency>
```

## 2. Requirement

- Java 8+

## 3. Usage

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

```java
List<String> rows = Arrays.asList("a", "b");
Page<String> page = Page.build(1, 25, rows, 100L);
assertThat(page.getNumber(), is(1));
assertThat(page.getCapacity(), is(25));
assertThat(page.getRows(), is(rows));
assertThat(page.getTotalCount(), is(100L));
```

## 4. Build

```bash
mvn clean package
```

## 5. Deploy

First take a look at [simter-parent] deploy config.

### 5.1. Deploy to LAN Nexus Repository

```bash
mvn clean deploy -P lan
```

### 5.2. Deploy to Sonatype Repository

```bash
mvn clean deploy -P sonatype
```

After deployed, login into <https://oss.sonatype.org>. Through `Staging Repositories`, search this package,
then close and release it. After couple hours, it will be synced
to [Maven Central Repository](https://search.maven.org/#search%7Cga%7C1%7Ca%3A%22simter-core%22).

### 5.3. Deploy to Bintray Repository

```bash
mvn clean deploy -P bintray
```

Will deploy to `https://api.bintray.com/maven/simter/maven/tech.simter:simter-core/;publish=1`.
So first create a package `https://bintray.com/simter/maven/tech.simter:simter-core` on Bintray.
After deployed, check it from <https://jcenter.bintray.com/tech/simter/simter-core>.


[simter-parent]: https://github.com/simter/simter-parent
[中文]: https://github.com/simter/simter-core/blob/master/docs/README.zh-cn.md