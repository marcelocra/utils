/** @type {import('tailwindcss').Config} */
export default {
  content: ["./public/index.html", "./src/**/*.{html,js,cljs}"],
  theme: {
    extend: {},
  },
  plugins: [require("daisyui")],
};
