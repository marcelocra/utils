# Algorithms and data structures checklist

## Hash tables

### In general

- associative array, meaning that there's a value associated to each key
- usually the key is passed through a hash function, to avoid

is applied to the provided key. It is unlikely that two keys will have the same
hash value, but if they do, the objects will be places in an array-like
structure

- in some languages (e.g. Java) you can define your own hash function
- keys can be a lot of different types of object

### In JavaScript

- from the MDN docs:
  > The Map object holds key-value pairs and remembers the original insertion
  > order of the keys. Any value (both objects and primitive values) may be used
  > as either a key or a value.
- in TypeScript, `Map<Type1, Type2>`. Many times we use `string` to `object`
- iteration happens in insertion order
- according to the
  [mdn documentation](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Map#description)
  > The specification requires maps to be implemented "that, on average, provide
  > access times that are sublinear on the number of elements in the
  > collection". Therefore, it could be represented internally as a hash table
  > (with O(1) lookup), a search tree (with O(log(N)) lookup), or any other data
  > structure, as long as the complexity is better than O(N).
- api
  - size: `size() -> number`
  - add: `set(key, value)`
  - check: `has(key) -> boolean`
  - get: `get(key) -> value | undefined`
  - remove: `delete(key) -> boolean`

## Sets

## Dynamically resizing arrays

## VERY weird things

These are some weird things I discovered while studying JavaScript.

- if a `Map` has `NaN` as the key to a value, it will return the value when
  `get` with `NaN`, even though `NaN !== NaN`
- `Object.is(-0, +0)` is false. But `-0 === +0` is true
