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
// XXX it might be nice, if this util was in tobago-date.ts, but in that case there are problems
// XXX with Jest (UnitTesting)
export class Example {
    /*
    example file to demonstrate TypeScript tests.
    */
    static toUpperCase(text) {
        return text === null || text === void 0 ? void 0 : text.toUpperCase();
    }
}
//# sourceMappingURL=tobago-example.js.map