#!/usr/bin/env bun

import { $ } from "zx";
import { expect, test } from "bun:test";
import path from "node:path";
import { parseArgs } from "node:util";

function checkValidEnv() {
  if (!Bun || !Bun.env.NODE_ENV || !Bun.env["HOME"]) {
    console.error(
      "Please, provide both NODE_ENV and HOME environment variables.",
    );

    process.exit(1);
  }

  return {
    nodeEnv: Bun.env.NODE_ENV,
    isTest: Bun.env.NODE_ENV === "test",
    home: Bun.env["HOME"],
  };
}

function d(msg: any, opts = { hide: true }) {
  if (opts.hide) {
    return;
  }

  console.log(JSON.stringify(msg, null, 2));
}

const ENVS = checkValidEnv();
const DEFAULT_LOGFILE_PATH = path.resolve(ENVS.home, ".sync-repo.log.txt");

function helloWorld() {
  return "hello";
}

const { values, positionals } = parseArgs({
  args: Bun.argv,
  options: {
    dir: {
      type: "string",
    },
  },
  strict: true,
  allowPositionals: ENVS.isTest,
});

d({ values, positionals }, { hide: false });

if (ENVS.isTest) {
  test(helloWorld.name, () => {
    expect(helloWorld()).toBe("hello");
  });
}
