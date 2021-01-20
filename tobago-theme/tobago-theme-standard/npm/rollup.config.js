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

import resolve from "@rollup/plugin-node-resolve"
import replace from '@rollup/plugin-replace';

export default {
  input: 'dist/js/tobago-all.js',
  output: {
    file: 'dist/js/tobago.js',
    format: 'umd', /* tbd: check if "iife" is better? */
    sourcemap: true,
    name: 'tobago'
  },
  plugins: [
    resolve(),
    replace({
      // XXX workaround for popper2 included by bootstrap, otherwise be get an error: process is not defined at runtime
      'process.env.NODE_ENV': JSON.stringify('production')
    }),  ]
};
