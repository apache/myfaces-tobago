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

const {spawnSync} = require("child_process");

/* The "baseDir" must be used for lightningcss to get the sourceMappingURL correctly. */
const baseDir = process.argv[2];

/* The "demo.min.css" must be generated before "demo.css", because using an already prefixed "demo.css" as an
   input for the "demo.min.css" may result in an incorrect result. */

// prefix and minify
spawnSync("lightningcss", ["--minify", "--targets", ">= 0.1%", "--sourcemap", "--output-file", "demo.min.css", "demo.css"], {
  cwd: baseDir
});

// prefix only
spawnSync("lightningcss", ["--targets", ">= 0.1%", "--sourcemap", "--output-file", "demo.css", "demo.css"], {
  cwd: baseDir
});
