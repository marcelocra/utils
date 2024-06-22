# Lib

This package contains utility functions, data structures and some algorithmic
studies I'm doing in JavaScript. I'm using this repo to solve some algorithms
and data structure questions using JavaScript.

## Algorithms and data structures checklist

- [ ] strings
- [ ] arrays
- [x] hash tables
- [ ] sets

## Strings

In JavaScript, the most common API operations are:

- `length`: check the length of the string
- `+` and `+=`: build and concatenate
- `indexOf(strToMatch)`: check existence or location of substring
- `substring(start, end)`: get the substring from index `start` to `end`, not
  including `end`. So `"abcd".substring(1, 2) === "b"`

## Arrays

## Hash tables

- associative array, meaning that there's a value associated to each key
- usually the key is passed through a hash function, to avoid having one key
  pointing to multiple values
- if one key points to multiple values, the performance would degrade from being
  constant to linear
- in some languages (e.g. Java) you can define your own hash function
- different languages support different types as keys and/or values

### JavaScript specifics

- from the MDN docs:
  > The Map object holds key-value pairs and remembers the original insertion
  > order of the keys. Any value (both objects and primitive values) may be used
  > as either a key or a value.
- a Map can be described using `Map<Type1, Type2>` (TypeScript syntax), with
  `Type1` frequently being some sort of object id and `Type2` being the object
- iteration happens in insertion order
- according to the
  [mdn documentation](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Map#description)
  > The specification requires maps to be implemented "that, on average, provide
  > access times that are sublinear on the number of elements in the
  > collection". Therefore, it could be represented internally as a hash table
  > (with O(1) lookup), a search tree (with O(log(N)) lookup), or any other data
  > structure, as long as the complexity is better than O(N).

### JavaScript API

- size: `size() -> number`
- add: `set(key, value)`
- check: `has(key) -> boolean`
- get: `get(key) -> value | undefined`
- remove: `delete(key) -> boolean`
- create:
  ```js
  new Map([
    [key1, value1],
    // ...
    [keyN, valueN],
  ]);
  ```

## Sets

## Arrays

## VERY weird JavaScript things

These are some weird things I discovered while studying JavaScript.

- if a `Map` has `NaN` as the key to a value, it will return the value when
  `get` with `NaN`, even though `NaN !== NaN`
- `Object.is(-0, +0)` is false. But `-0 === +0` is true
