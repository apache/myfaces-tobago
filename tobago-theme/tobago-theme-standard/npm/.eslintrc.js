module.exports = {
  parser: '@typescript-eslint/parser',
  plugins: ['@typescript-eslint'],
  extends: [
    'plugin:@typescript-eslint/recommended',
    'plugin:@typescript-eslint/eslint-recommended',
    'plugin:@typescript-eslint/recommended'
  ],
  parserOptions: {
    project: './tsconfig.json',
    ecmaVersion: 2018,
    sourceType: 'module',
  },
  rules: {
    "prefer-const": "warn",
    "prefer-rest-params": "warn",
    "no-case-declarations": "warn",
    "no-irregular-whitespace": "warn",
    "no-constant-condition": "warn",
    "no-inner-declarations": "warn",
    "@typescript-eslint/ban-ts-comment": "warn",
    "@typescript-eslint/no-implied-eval": "warn",
    "@typescript-eslint/unbound-method": "warn",
    "@typescript-eslint/no-unsafe-call": "warn",
    "@typescript-eslint/no-unsafe-assignment": "warn",
    "@typescript-eslint/no-unsafe-member-access": "warn",
    "@typescript-eslint/restrict-template-expressions": "warn",
    "@typescript-eslint/indent": [
      "warn",
      2
    ],
    "@typescript-eslint/member-delimiter-style": [
      "warn",
      {
        "multiline": {
          "delimiter": "semi",
          "requireLast": true
        },
        "singleline": {
          "delimiter": "semi",
          "requireLast": false
        }
      }
    ],
    "@typescript-eslint/member-ordering": "warn",
    "@typescript-eslint/no-unnecessary-type-assertion": ["warn"],
    "@typescript-eslint/no-empty-function": "warn",
    "@typescript-eslint/no-inferrable-types": "warn",
    "@typescript-eslint/restrict-plus-operands": "warn",
    "@typescript-eslint/no-unsafe-return": "warn",
    "@typescript-eslint/quotes": [
      "warn",
      "double"
    ],
    "@typescript-eslint/semi": [
      "error",
      "always"
    ],
    "max-len": [
      "warn",
      {
        "code": 120
      }
    ],
    "no-multiple-empty-lines": "error",
    "no-var": "warn"
  }
}
