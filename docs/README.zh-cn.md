# [simter-core](https://github.com/simter/simter-core) [[English]]

一些基础性的常用接口或类:

- 注解类 (tech.simter.annotation)
    - `Format`：属性值输出格式化注解
- 查询条件封装 (tech.simter.condition)
    - `Condition` 接口（含静态工具方法及默认实现）：仅暴露条件标记符，避免 SQL 攻击
    - `ValueConverter#convert(String type, String value)` 工具方法：转换字符串值为特定类型
    - `ComparisonOperator` 枚举：定义查询语言的操作符，如 =、>、<、\[\]、\[\)、...等
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
  <version>0.3.0</version>
</dependency>
```

## 2. 要求

- Java 8+

## 3. 使用

### 3.1. Condition 条件封装

#### 3.1.1. Condition 的 `of` and `toQL` 静态方法

使用 `of` 方法构建默认的 `Condition` 实现，用 `toQL` 方法生成查询语句及相应占位参数或命名参数的值。

```java
// Case 1: simple ql
Condition.of("id", IsNull).toQL(); // "id is null"
  
// Case 2: 带 ? 参数的查询语句
List<Object> values = new ArrayList<>();
Condition.of("id", Equals, "123").toQL(null, values);       // "id = ?" , values[0]="123"
Condition.of("id", Equals, "123").toQL("t.id", values);     // "t.id = ?" , values[0]="123"
Condition.of("age", GreaterThan, 18).toQL("t.age", values); // "t.age > ?", values[1]=18
  
// Case 3: 带命名参数的查询语句
Map<String, Object> namedValues = new HashMap<>();
Condition.of("id", Equals, "123").toQL(null, namedValues);          // "id = :id" , values["id"]="123"
Condition.of("id", Equals, "123").toQL("t.id", namedValues);        // "t.id = :id" , values["id"]="123"
Condition.of("id", Equals, "123").toQL("t.id", namedValues, "id1"); // "t.id = :id1", values["id1"]="123"
Condition.of("age", GreaterThan, 18).toQL("t.age", namedValues);    // "t.age > ?", values["age"]=18
  
// Case 4: 范围条件
Map<String, Object> namedValues = new HashMap<>();
List<Object> v1v2 = Arrays.asList(18, 60);
//-- "age >= :age0 and age <= :age1", values["age0"]=18, values["age1"]=60
Condition.of("age", Range, v1v2).toQL(null, namedValues);
//-- "age >= :age0 and age <= :age1", values["a1"]=18, values["a2"]=60
Condition.of("age", Range, v1v2).toQL(null, namedValues, "a1", "a2");
```

#### 3.1.2. Condition 的 `toCondition(JsonArray)` 静态方法

使用固定的字符数据结构来生成特定条件。

```java
// 数据结构为：[id, stringValue, valueType, operator]
JsonArray jsonArray = Json.createArrayBuilder()
  .add("age")   // id
  .add("18")    // stringValue
  .add("int")   // valueType
  .add("=")     // sql 比较符, `ComparisonOperator.symbol()` 的值
  .build();
  
// 等价于 `Condition.of("id", Equals, 18)`
Condition.toCondition(jsonArray);
```

跳到下面的 `3.2 章节` 查看所有支持的类型。

#### 3.1.3. Condition 的 `toConditions(String)` 静态方法

使用固定的字符数据结构批量生成特定条件。

```java
/* 数据结构为: [[id, stringValue, valueType, operator], ...]，如：
   [
     ["id"       , "18"                        , "Integer"   , "=" ]
     ["money"    , ["10", "20.05"]             , "BigDecimal", "[]"]
     ["startDate", ["2017-01-01", "2017-02-01"], "LocalDate" , "[]"]
   ]
*/
String search = ...;
List<Condition> conditions = Condition.toConditions(search);
  
// 等价于下面的代码：
List<Condition> conditions = new ArrayList<>();
conditions.add(Condition.of("id", Equals, 18));
conditions.add(Condition.of("money", Range, Arrays.asList(new BigDecimal(10), new BigDecimal(20.05))));
conditions.add(Condition.of("startDate", Range, Arrays.asList(LocalDate.of(2017, 1 , 1), LocalDate.of(2017, 2 , 1))));
```

### 3.2. ValueConverter 转换字符串值为特定类型

通过工具方法 `convert(String type, String value)` 转换字符串值为特定的类型，内置支持如下类型转换：

序号 | type         | 对应 Java 类型           | 备注
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
 19 | Date          | java.sql.Date           | 仅支持 yyyy-MM-dd 格式的值
 20 | Calendar      | java.util.Calendar      | 仅支持 yyyy-MM-dd HH:mm:ss 格式的值

如果 `type` 值不在上述范围内，则将 `type` 值当作类的全名，按如下顺序找相应的方法来构建：

1. 带单个 java.lang.String 类型参数的构造函数，如 `java.util.Date#Date(java.lang.String)`
2. 带单个 java.lang.CharSequence 类型参数的构造函数
3. 类的静态方法 valueOf(java.lang.String)，如 `java.sql.Date#valueOf(java.lang.String)`
4. 类的静态方法 valueOf(java.lang.CharSequence)
5. 类的静态方法 parse(java.lang.String)
6. 类的静态方法 parse(java.lang.CharSequence)，如 `java.time.LocalDate#parse(java.lang.CharSequence)`

### 3.3. ComparisonOperator 操作符枚举

序号 | 枚举                | SQL 比较符      | 备注
----|---------------------|-----------------|----
  1 | GreaterThan         | >               | 
  2 | GreaterThanOrEquals | >=              | 
  3 | LessThan            | <               | 
  4 | LessThanOrEquals    | <=              | 
  5 | Equals              | =               | 
  6 | NotEquals           | !=              | 等价于 `<>`
  7 | Like                | like            | %xxx%
  8 | LikeLeft            | like            | xxx%
  9 | LikeRight           | like            | %xxx
 10 | iLike               | ilike           | 忽略大小写的 %xxx% 
 11 | iLikeLeft           | ilike           | 忽略大小写的 xxx%
 12 | iLikeRight          | ilike           | 忽略大小写的 %xxx
 13 | In                  | in              | 
 14 | NotIn               | not in          | 
 15 | IsNull              | is null         | 
 16 | IsNotNull           | is not null     | 
 17 | Range               | X <= VALUE <= Y | [X, Y]，等价于 `between`
 18 | RangeGTEAndLT       | X <= VALUE <  Y | [X, Y)
 19 | RangeGTAndLTE       | X <  VALUE <= Y | (X, Y]
 20 | RangeGTAndLT        | X <  VALUE <  Y | (X, Y)

RangeGTEAndLT、RangeGTAndLTE、RangeGTAndLT 可以看成是 `between` 的扩展。

### 3.4. Page\<T\>

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