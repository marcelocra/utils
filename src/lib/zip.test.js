import { expect, test } from "@jest/globals";
import { zip } from "./zip";

test("merge two arrays into an array of pairs", () => {
  expect(zip(["a", "b", "c"], [1, 2, 3])).toBe([
    ["a", 1],
    ["b", 2],
    ["c", 3],
  ]);
});
