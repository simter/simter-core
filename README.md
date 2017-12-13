# [simter-core](https://github.com/simter/simter-core) [[中文]]

Some base or common things. Includes:

- Annotation (tech.simter.annotation)
    - The `Format` annotation class: Use to config the output format of the property value. Special for data/time property
- Query language encapsulation (tech.simter.condition)
    - The `Condition` interface: Only expose the condition's markup. Use to hide actual sql to avoid sql attack.
    - The `ValueConverter#convert(String type, String value)` util method: Convert string value to a specific type.
    - The `ComparisonOperator` Enum: Defined some query language operator, such as =, >, <, \[\], \[\),....
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
  <version>0.3.0</version>
</dependency>
```

## 2. Requirement

- Java 8+

## 3. Usage

### 3.1. Condition

#### 3.1.1. Condition's `of` and `toQL` static method

```java
// Case 1: simple ql
Condition.of("id", IsNull).toQL(); // "id is null"
  
// Case 2: query language with position value
List<Object> values = new ArrayList<>();
Condition.of("id", Equals, "123").toQL(null, values);       // "id = ?" , values[0]="123"
Condition.of("id", Equals, "123").toQL("t.id", values);     // "t.id = ?" , values[0]="123"
Condition.of("age", GreaterThan, 18).toQL("t.age", values); // "t.age > ?", values[1]=18
  
// Case 3: query language with named param value
Map<String, Object> namedValues = new HashMap<>();
Condition.of("id", Equals, "123").toQL(null, namedValues);          // "id = :id" , values["id"]="123"
Condition.of("id", Equals, "123").toQL("t.id", namedValues);        // "t.id = :id" , values["id"]="123"
Condition.of("id", Equals, "123").toQL("t.id", namedValues, "id1"); // "t.id = :id1", values["id1"]="123"
Condition.of("age", GreaterThan, 18).toQL("t.age", namedValues);    // "t.age > ?", values["age"]=18
  
// Case 4: range condition
Map<String, Object> namedValues = new HashMap<>();
List<Object> v1v2 = Arrays.asList(18, 60);
//-- "age >= :age0 and age <= :age1", values["age0"]=18, values["age1"]=60
Condition.of("age", Range, v1v2).toQL(null, namedValues);
//-- "age >= :age0 and age <= :age1", values["a1"]=18, values["a2"]=60
Condition.of("age", Range, v1v2).toQL(null, namedValues, "a1", "a2");
```

#### 3.1.2. Condition's `toCondition(JsonArray)` static method

```java
// data structure: [id, stringValue, valueType, operator]
JsonArray jsonArray = Json.createArrayBuilder()
  .add("age")   // id
  .add("18")    // stringValue
  .add("int")   // valueType
  .add("=")     // operator symbol, the value of `tech.simter.condition.ComparisonOperator.symbol()`
  .build();
  
// equals to `Condition.of("id", Equals, 18)`
Condition.toCondition(jsonArray);
```

See `section 3.2` to find all supported valueTypes.

#### 3.1.3. Condition's `toConditions(String)` static method

```java
/* data structure: [[id, stringValue, valueType, operator], ...], such as:
   [
     ["id"       , "18"                        , "Integer"   , "=" ]
     ["money"    , ["10", "20.05"]             , "BigDecimal", "[]"]
     ["startDate", ["2017-01-01", "2017-02-01"], "LocalDate" , "[]"]
   ]
*/
String search = ...;
List<Condition> conditions = Condition.toConditions(search);
  
// equals to the bellow code:
List<Condition> conditions = new ArrayList<>();
conditions.add(Condition.of("id", Equals, 18));
conditions.add(Condition.of("money", Range, Arrays.asList(new BigDecimal(10), new BigDecimal(20.05))));
conditions.add(Condition.of("startDate", Range, Arrays.asList(LocalDate.of(2017, 1 , 1), LocalDate.of(2017, 2 , 1))));
```

### 3.2. ValueConverter

Convert string-value to specific type by `convert(String type, String value)` method. Bellow is the inner supported type :

 Sn | type          | Java Type               | Remark
----|---------------|-------------------------|----
  1 | string        | java.lang.String        | 
  2 | int           | int                     | 
  3 | Integer       | java.lang.Integer       | 
  4 | long          | long                    | 
  5 | Long          | java.lang.Long          | 
  6 | float         | float                   | 
  7 | Float         | java.lang.Float         | 
  8 | double        | double                  | 
  9 | Double        | java.lang.Double        | 
 10 | BigDecimal    | java.math.BigDecimal    | 
 11 | boolean       | boolean                 | 
 12 | Boolean       | java.lang.Boolean       | 
 13 | LocalDate     | java.time.LocalDate     | 
 14 | LocalDateTime | java.time.LocalDateTime | 
 15 | LocalTime     | java.time.LocalTime     | 
 16 | Year          | java.time.Year          | 
 17 | YearMonth     | java.time.YearMonth     | 
 18 | Month         | java.time.Month         | 
 19 | Date          | java.sql.Date           | Only `yyyy-MM-dd` format
 20 | Calendar      | java.util.Calendar      | Only `yyyy-MM-dd HH:mm:ss` format

If the `type` value out of the above list, treat it as a full class name and follow bellow order method to get the converted value :

1. Constructor with a single `java.lang.String` type, such as `java.util.Date#Date(java.lang.String)`
2. Constructor with a single `java.lang.CharSequence` type
3. Static method `valueOf(java.lang.String)`, such as `java.sql.Date#valueOf(java.lang.String)`
4. Static method `valueOf(java.lang.CharSequence)`
5. Static method `parse(java.lang.String)`
6. Static method `parse(java.lang.CharSequence)`, such as `java.time.LocalDate#parse(java.lang.CharSequence)`

### 3.3. ComparisonOperator Enum

 Sn | Enum                | SQL Symbol      | Remark
----|---------------------|-----------------|----
  1 | GreaterThan         | >               | 
  2 | GreaterThanOrEquals | >=              | 
  3 | LessThan            | <               | 
  4 | LessThanOrEquals    | <=              | 
  5 | Equals              | =               | 
  6 | NotEquals           | !=              | equivalent to `<>`
  7 | Like                | like            | %xxx%
  8 | LikeLeft            | like            | xxx%
  9 | LikeRight           | like            | %xxx
 10 | iLike               | ilike           | case-insensitive %xxx% 
 11 | iLikeLeft           | ilike           | case-insensitive xxx%
 12 | iLikeRight          | ilike           | case-insensitive %xxx
 13 | In                  | in              | 
 14 | NotIn               | not in          | 
 15 | IsNull              | is null         | 
 16 | IsNotNull           | is not null     | 
 17 | Range               | X <= VALUE <= Y | [X, Y], equivalent to `between`
 18 | RangeGTEAndLT       | X <= VALUE <  Y | [X, Y)
 19 | RangeGTAndLTE       | X <  VALUE <= Y | (X, Y]
 20 | RangeGTAndLT        | X <  VALUE <  Y | (X, Y)

RangeGTEAndLT, RangeGTAndLTE, RangeGTAndLT are the extentions of `between`.

### 3.4 Page\<T\>

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