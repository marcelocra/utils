#!/usr/bin/env -S deno run -A

/**
 * Creates a new vite project using the `react-swc-ts` template, adding some extra devtools to it.
 */
import { execSync } from "node:child_process";
import fs from "node:fs";
import path from "node:path";
import process from "node:process";

const devDependencies = [
  "@tailwindcss/typography",
  "@trivago/prettier-plugin-sort-imports",
  "@types/node",
  "autoprefixer",
  "cssnano",
  "daisyui",
  "eslint-config-google",
  "eslint-config-prettier",
  "postcss",
  "prettier",
  "prettier-plugin-tailwindcss",
  "tailwindcss",
];

// Kept as reference. To be installed only if required.
const extraDevDependencies = ["@types/shelljs", "shelljs", "shx", "zx"];

// Get config templates from the templates folder.
const __filename = new URL(import.meta.url).pathname;
const templatesPath = path.resolve(
  path.dirname(path.dirname(path.dirname(path.dirname(__filename)))),
  "src",
  "templates"
);
const prettierTemplateConfig = fs.readFileSync(path.resolve(templatesPath, ".prettierrc.json"), "utf8");
const eslintTemplateConfig = fs.readFileSync(path.resolve(templatesPath, ".eslintrc.json"), "utf8");

// Install stuff before loading the package.json.
execSync(`npm install --save-dev ${devDependencies.join(" ")}`, {
  stdio: "inherit",
});

// Load the package.json file, to apply updates.
const currentDir = process.cwd();
const packageJsonPath = `${currentDir}/package.json`;
const { default: packageJson } = await import(packageJsonPath, {
  with: { type: "json" },
});

console.log(packageJsonPath);
console.log(packageJson);

packageJson.eslintConfig = JSON.parse(eslintTemplateConfig);
packageJson.prettier = JSON.parse(prettierTemplateConfig);

packageJson.engines = { node: ">=20" };
packageJson.engineStrict = true;
packageJson.packageManager = "npm@10.5.0";
packageJson.author = { name: "Marcelo Almeida", email: "npm@marcelocra.com" };
packageJson.repository = { type: "git", url: "PACKAGE_REPO_URL" };

// Write the applied updates to the file.
fs.writeFileSync(packageJsonPath, JSON.stringify(packageJson, null, 2));

// Print the result.
console.log(packageJson);

