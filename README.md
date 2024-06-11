# Utils

> Be more productive by automating tasks and avoiding code rewrites.

## Goal

The goal is for this package to be both a command line program (cli) and a
utility library.

**IMPORTANT:** Currently, 19may2024, we are not there yet!

### to do now, per version

v0.1.5

- need to prioritize what comes next

later

- [ ] display notification status at the system tray
- [ ] simplify adding to-do's for each project
- [ ] create a cli to avoid running possibly dangerous commands, like npx or
      bunx
- [ ] port code from `core.clj` to cljs
- [ ] create algorithm study section
- [ ] add tests
  - might be possible to add cljs tests in the same file as the code is,
    importing it later in a test file so that the testing framework recognize it

### ideas

- [ ] full `shadow-cljs` project, with Reagent, TailwindCSS and DaisyUI
      (Tailwind plugin)
- [ ] snippet program, to help insert code snippets in any editor. Should work
      like VSCode snippets, but powered by a cli, to work in any editor (like an
      lsp). Would allow me to have the same snippet system in Neovim, Sublime,
      etc
- [ ] migrate all `archive` code and script references to the snippet program
- [ ] download all nerdfonts listed in vscode settings directly from their site
- [ ] write a script to capture all functionality here and create an "api"
      section in this readme

### done

v0.1.4

- get bin.js to work only with js and make it similar to core.clj, to compare
  both languages

v0.1.3

- [x] get `bin.mjs` and `lib.mjs` to work without cljs
- [x] rename `js` files to `mjs`, to make it more clear
- [x] add `author` and `repository` fields in the `package.json` file

---

<!-- #region Thinking about the installation, for later. -->

## Install

Eventually, should work like this.

### cli

To use this package as a task automation cli, running like an isolated binary,
run:

```sh
pnpm add --global mcra-utils

```

I also alias it to `u`. If you like the alias, run

```sh
mcra-utils --create-alias
```

to add it both to `~/.bashrc` and `~/.zshrc`. Source those or reload your
terminal to use the alias, as usual.

### JavaScript utility library

To use this package as a JavaScript utility library, add it to your project
dependencies:

```sh
# In your project folder, run:
pnpm add mcra-utils
```

<!-- #endregion -->

## Templates

Simple way to get started in some projects.

### React

https://stackblitz.com/fork/react

### Preact

#### No build

```html
<script type="importmap">
  {
    "imports": {
      "preact": "https://esm.sh/preact@10.19.2",
      "preact/": "https://esm.sh/preact@10.19.2/",
      "htm/preact": "https://esm.sh/htm@3.1.1/preact?external=preact"
    }
  }
</script>

<script type="module">
  import { render } from "preact";
  import { useReducer } from "preact/hooks";
  import { html } from "htm/preact";

  export function App() {
    const [count, add] = useReducer((a, b) => a + b, 0);

    return html`
      <button onClick=${() => add(-1)}>Decrement</button>
      <input readonly size="4" value=${count} />
      <button onClick=${() => add(1)}>Increment</button>
    `;
  }

  render(html`<${App} />`, document.body);
</script>
```

### Eslint with Prettier for the Google JavaScript Style

```sh
bun i -D \
    eslint \
    eslint-config-google \
    eslint-config-prettier \
    prettier
```

```json
{
  "eslintConfig": {
    "extends": [
      "eslint:recommended",
      "google",
    ],
    "plugins": [
      "prettier"
    ]
  },
  "prettier": {
    "plugins": []
  },
}
```

#### With Preact and Tailwind

```sh
bun i -D \
    eslint \
    eslint-config-google \
    eslint-config-prettier \
    prettier \
    eslint-config-preact \
    prettier-plugin-tailwindcss
```

The ESLint order should have the highest priorities later, so they are applied over the previous ones.

```json
{
  "eslintConfig": {
    "extends": [
      "eslint:recommended",
      "google",
      "preact"
    ],
    "plugins": [
      "prettier"
    ]
  },
  "prettier": {
    "plugins": [
      "prettier-plugin-tailwindcss"
    ]
  },
}
```

## Tools