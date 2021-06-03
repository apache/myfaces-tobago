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

import {BehaviorMode} from "./tobago-behavior-mode";

test("enum values to strings", () => {
  expect(BehaviorMode[BehaviorMode.client]).toBe("client");
  expect(BehaviorMode[BehaviorMode.ajax]).toBe("ajax");
  expect(BehaviorMode[BehaviorMode.full]).toBe("full");
});

test("strings to enum values", () => {
  expect(BehaviorMode["client"]).toBe(BehaviorMode.client);
  expect(BehaviorMode["ajax"]).toBe(BehaviorMode.ajax);
  expect(BehaviorMode["full"]).toBe(BehaviorMode.full);
});
