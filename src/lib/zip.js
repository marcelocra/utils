import "./types.js";

/**
 * @param {Keys} keys The value of the keys.
 * @param {Values} values Values associated with the key with same index.
 * @returns {ZipReturn}
 */
export function zip(keys, values) {
  if (keys.length !== values.length) {
    return new Error(`arg1 and arg2 should have the same size.`);
  }

  /** @type {Iterable<[string, string|number]>} */
  let res = [];
  for (let i = 0; i < keys.length; i++) {
    if (keys[i] === undefined || values[i] === undefined) {
      return new Error(`args should not have undefined values.`);
    }

    // @ts-expect-error - We know that keys[i] and values[i] are not undefined
    res.push([keys[i], values[i]]);
  }

  return res;
}

// example
// console.log(zip(["a", "b", "c"], [1, 2, 3])); // [["a", 1], ["b", 2], ["c", 3]]