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

import {defineConfig} from "eslint/config";
import eslint from "@eslint/js";
import tseslint from "typescript-eslint";
import stylistic from '@stylistic/eslint-plugin'

export default defineConfig([
  {
    ignores: [
      "**/tobago-polyfill.ts"
    ]
  },
  eslint.configs.recommended,
  tseslint.configs.recommended,
  tseslint.configs.stylistic,
  {
    plugins: {
      '@stylistic': stylistic,
    },
    languageOptions: {
      ecmaVersion: 2018,
      sourceType: "module",
      parserOptions: {
        project: "./tsconfig.json",
      },
    },
    rules: {
      "@stylistic/semi": "error",
      "@typescript-eslint/no-explicit-any": "off",
      "@typescript-eslint/no-unsafe-function-type": "off",
      "@typescript-eslint/no-unused-expressions": "off",
      "@typescript-eslint/no-unused-vars": "off",
      "max-len": ["error", {code: 120,}],
      "no-fallthrough": "off",
      "no-irregular-whitespace": "off",
      "no-multiple-empty-lines": "error",
      "no-useless-assignment": "off"
    }
  },
]);
