# reflective
Utility classes that use ASM for generating metaclasses as if it were standard java reflection, but without the performance overhead.


__nl.sander.reflective.compare.Compare__
* universal (deep) compare tool
* compares [apple] to [orange] recursively and shows the diff
* compiles to bytecode version jdk11
* but also handles records, if you run jdk16+
* Can optionally do 'structural comparison' (as opposed to _nominal_ like in the respective types of polymorphism). Let's say you have class Apple with property _color_ and a class Orange, also with property _color_. `Compare` provides `any` method with which you can compare the values disregarding the type that contains them.

__nl.sander.reflective.tomap.ToMap__
* turn any bean/record into a Map<String, Object>

Now working on capabilities that mimick java.lang.reflect
* not going to create something like setAccessible(true), since that's likely impossible without jdk support, and probably not wanted either
* I do plan to substitute java.lang.reflect.Array, because of it's VERY poor performance
* a read model for methods, fields etc
* invocation capabilities
 