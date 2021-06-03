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

import {CollapseOperation} from "./tobago-collapsible-operation";

test("enum values to strings", () => {
  expect(CollapseOperation[CollapseOperation.show]).toBe("show");
  expect(CollapseOperation[CollapseOperation.hide]).toBe("hide");
  expect(CollapseOperation[CollapseOperation.toggle]).toBe("toggle");
});

test("strings to enum values", () => {
  expect(CollapseOperation["show"]).toBe(CollapseOperation.show);
  expect(CollapseOperation["hide"]).toBe(CollapseOperation.hide);
  expect(CollapseOperation["toggle"]).toBe(CollapseOperation.toggle);
});
