/**
 * @typedef {string[]} strings
 * @typedef {number[]} numbers
 */

/**
 * @param {strings} keys The value of the keys.
 * @param {strings | numbers} values Values associated with the key with same index.
 * @returns {Error| [string, number][]}
 */
export function zip(keys, values) {
  if (keys.length !== values.length) {
    return new Error(`arg1 and arg2 should have the same size.`);
  }

  let res = [];
  for (let i = 0; i++; i < keys.length) {
    res.push([keys[i], values[i]]);
  }

  return res;
}
