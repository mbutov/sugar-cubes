![You take sugar?](sugar.jpg?raw=true "No thank you, Turkish. I'm sweet enough.")

## Sugar Cubes Library    
    
Sugar Cubes are pieces of java code which make your everyday development sweet enough.
The library requires Java 8 or higher.    
    
### Including into your project

Gradle:

    compile 'org.sugarcubes:sugar-cubes-library:0.1.0-SNAPSHOT'

Maven:
    
    <dependency>
        ...
    </dependency>

## Packages
    
### org.sugarcubes.builder
    
Provides generic builder pattern as well as some builders for collections.

Generic builder:
```java
    Map<String, Integer> map = Builders.<Map<String, Integer>>of(HashMap::new)
        .apply(Invocations.call(Map::put, "teeth", 32))
        .apply(Invocations.call(Map::put, "fingers", 8))
        .transform(Collections::unmodifiableMap)
        .build();
```
    
Same thing done with MapBuilder:    
```java
    Map<String, Integer> map = MapBuilder.<String, Integer>hashMap()
        .put("teeth", 32)
        .put("fingers", 8)
        .transform(Collections::unmodifiableMap)
        .build();
```

Set builder:
```java
    Set<String> set = SetBuilder.<String>hashSet()
        .addAll("ugly", "coyote")
        .transform(Collections::unmodifiableSet)
        .build();
```
    
### org.sugarcubes.cloner          
    
Tools for faster deep cloning of different kind of objects.

### org.sugarcubes.reflection          

Set of wrappers for Java Reflection classes. Simplifies working with Reflection API.
    
### org.sugarcubes.rex          
    
REx means "runtime exceptions". This module helps to handle checked exceptions
and translate them into unchecked.

### org.sugarcubes.tuple
    
Simple (but enough for most cases) java tuple implementation.
    
## Sandbox

Sandbox is a place for sweet candydates.
The code here is mostly experimental. 

### org.sugarcubes.proxy

Generic API for proxy creation via various providers.
    
