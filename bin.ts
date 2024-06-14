#!/usr/bin/env bun

import { $ } from "zx";
import { expect, test } from "bun:test";
import path from "node:path";
import { parseArgs } from "node:util";
import fs from "node:fs";

function error(msg: any, opts?: { exitCode: null | number }) {
  console.error(msg);

  if (!opts || Bun.env.NODE_ENV === "test") {
    return;
  }

  process.exit(opts.exitCode ?? 1);
}

function checkValidEnv() {
  console.log(Bun.env.NODE_ENV);
  if (!Bun || !Bun.env.NODE_ENV || !Bun.env["HOME"]) {
    error("Please, provide both NODE_ENV and HOME environment variables.");
    process.exit(1);
  }

  return {
    nodeEnv: Bun.env.NODE_ENV,
    isTest: Bun.env.NODE_ENV === "test",
    home: Bun.env["HOME"],
  };
}

console.log({ arg: Bun.argv, env: Bun.env });

enum LogMode {
  log,
  debug,
}

function l(msg: any, opts = { hide: true, mode: LogMode.debug }) {
  if (opts.hide) {
    return;
  }

  let fun;
  switch (opts.mode) {
    case LogMode.debug:
      fun = console.debug;
      break;

    case LogMode.log:
    default:
      fun = console.log;
      break;
  }

  fun(JSON.stringify(msg, null, 2));
}

function d(msg: any, opts = { hide: true, mode: LogMode.debug }) {
  l(msg, opts);
}

const ENVS = checkValidEnv();
const DEFAULT_LOGFILE_PATH = path.resolve(ENVS.home, ".sync-repo.log.txt");

// Create file if it doesn't exist.
if (!fs.existsSync(DEFAULT_LOGFILE_PATH)) {
  fs.writeFileSync(DEFAULT_LOGFILE_PATH, "");
}

const HELPERS = {
  now: () => {
    return new Date().toLocaleString("en-UK", {
      day: "numeric",
      weekday: "short",
      month: "short",
      year: "numeric",
      hour: "2-digit",
      hour12: false,
      minute: "2-digit",
      second: "2-digit",
      timeZone: "America/Sao_Paulo",
    });
  },
};

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

d(
  {
    values,
    positionals,
    currDate: HELPERS.now(),
  },
  // { hide: false },
);

// Check if provided directory is valid and exists.
if (!values.dir || !fs.lstatSync(values.dir).isDirectory()) {
  error(`Invalid directory: '${values.dir}'`);
}

let state = (await $`git status --porcelain`).stdout;
if (state === "") {
  l("Nothing to do.");
  process.exit(0);
}

if (fs)
  if (ENVS.isTest) {
    test("state", () => {
      expect(state).toBe("hey");
    });
  }
