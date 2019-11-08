import resolve from "rollup-plugin-node-resolve"

export default {
  input: 'js/tobago-all.js',
  output: {
    file: 'js/tobago-bundle.js',
    format: 'iife',
    name: 'tobago'
  },
  plugins: [
    resolve()
  ]
};
