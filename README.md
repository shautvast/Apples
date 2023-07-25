# Apples
* universal (deep) compare tool
* compares [apple] to [orange] recursively and shows the diff
* no reflection
* compiles to bytecode version jdk11
* but also handles records, if you run jdk16+

* I have one more wish for this and that is 'structural comparison'. Let's say you have class Apple with property _color_ and a class Orange, also with property _color_. Right now `Apples` does the sensible thing, and that is saying: "classes don't match". But what what if you could compare these apples and oranges? Should be possible. 