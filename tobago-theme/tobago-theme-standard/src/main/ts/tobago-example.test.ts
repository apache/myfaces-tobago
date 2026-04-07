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

import {Example} from "./tobago-example";

test("Example how to test a upper case function: yyyy", () => {
  const result = Example.toUpperCase("yyyy");
  expect(result).toBe("YYYY");
});

test("Example how to use HTMLElement with DOM in a test", () => {
  const element: HTMLElement = document.createElement("div");
  element.textContent = "Hallo";

  expect(element.tagName).toBe("DIV");
  expect(element.textContent).toBe("Hallo");
});
