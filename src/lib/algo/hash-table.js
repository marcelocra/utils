import { expect, test } from "@jest/globals";
import { zip } from "../zip";

/**
 *
 * @param {{keys: string[], values: string[]}} params the params
 * @returns {Map<string, string>}
 */
export function hashTable(params) {
  return new Map(zip(params.keys, params.values));
}

test(
  "corrct map is built",
  expect(hashTable({ keys: ["a", "b"], values: [1, 2] })).toBe(
    new Map([
      ["a", 1],
      ["b", 2],
    ])
  )
);
