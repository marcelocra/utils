import { zip } from "./zip.js";

import "./types.js";

/**
 *
 * @param {{keys: Keys, values: Values}} params the params
 * @returns {Map<string, string|number>}
 */
export function hashTable(params) {
  const zipped = zip(params.keys, params.values);
  if (zipped instanceof Error) {
    return new Map();
  }

  return new Map(zipped);
}
