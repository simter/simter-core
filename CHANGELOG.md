# [simter-core](https://github.com/simter/simter-core) changelog

## 0.3.0 - 2017-12-13
- Annotation (tech.simter.annotation)
    - The `Format` annotation class
- Query language encapsulation (tech.simter.condition)
    - The `Condition` interface
    - The `ValueConverter#convert(String type, String value)` util method
    - The `ComparisonOperator` Enum
- Persistence (tech.simter.persistence)
    - The `PersistenceEnum` Enum
    - The `CommonState` Enum implemented `PersistenceEnum`
    - The `Sex` Enum implemented `PersistenceEnum`
- Common data class/interface (tech.simter.data)
    - The `Page<T>` interface
    - The `Id<T>` class
    - The `Ts` class
    - The `Created<T>` class extend `Id<T>`
    - The `Option` class
    - The `Node` class
    - The `ImportedResult` class