/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import typescriptEslint from "@typescript-eslint/eslint-plugin";
import tsParser from "@typescript-eslint/parser";
import path from "node:path";
import {fileURLToPath} from "node:url";
import js from "@eslint/js";
import {FlatCompat} from "@eslint/eslintrc";
import stylisticTs from '@stylistic/eslint-plugin-ts'

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const compat = new FlatCompat({
  baseDirectory: __dirname,
  recommendedConfig: js.configs.recommended,
  allConfig: js.configs.all
});

export default [{
  ignores: [
    "**/dist/",
    "**/lib/",
    "**/node_modules/",
    "**/target/",
    "**/*.js",
    "**/tobago-polyfill.ts",
  ],
}, ...compat.extends(
    "eslint:recommended",
    "plugin:@typescript-eslint/recommended",
    "plugin:@typescript-eslint/stylistic"
), {
  plugins: {
    '@stylistic/ts': stylisticTs,
    "@typescript-eslint": typescriptEslint
  },
  languageOptions: {
    parser: tsParser,
    ecmaVersion: 2018,
    sourceType: "module",

    parserOptions: {
      project: "./tsconfig.json",
    },
  },
  rules: {
    "@stylistic/ts/semi": "error",
    "@typescript-eslint/no-explicit-any": "off",
    "@typescript-eslint/no-unused-expressions": "off",
    "@typescript-eslint/no-unused-vars": "off",
    "max-len": ["error", {code: 120,}],
    "no-fallthrough": "off",
    "no-irregular-whitespace": "off",
    "no-multiple-empty-lines": "error",
  },
}];
