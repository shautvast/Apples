# reflective
Utility classes that use ASM for generating metaclasses as if it were standard java reflection, but without the performance overhead.


__nl.sander.reflective.compare.Compare__
* universal (deep) compare tool
* compares [apple] to [orange] recursively and shows the diff
* compiles to bytecode version jdk11
* but also handles records, if you run jdk16+
* Can optionally do 'structural comparison' (as opposed to _nominal_ like in the respective types of polymorphism). Let's say you have class Apple with property _color_ and a class Orange, also with property _color_. `Compare` provides `any` method with which you can compare the values disregarding the type that contains them.

__nl.sander.reflective.tomap.ToMap__
* turn any bean/record in a Map<String, Object>

