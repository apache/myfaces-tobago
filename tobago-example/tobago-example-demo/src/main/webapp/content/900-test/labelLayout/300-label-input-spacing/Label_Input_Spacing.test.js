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

import {querySelectorFn} from "/script/tobago-test.js";

it("Spacing between input and label", function () {
  const labelInputLeft = querySelectorFn("label[for='page:mainForm:inputLeft::field']")
  const labelInputRight = querySelectorFn("label[for='page:mainForm:inputRight::field']")
  const labelSuggestLeft = querySelectorFn("label[for='page:mainForm:suggestLeft::field']")
  const labelSuggestRight = querySelectorFn("label[for='page:mainForm:suggestRight::field']")

  expect(getComputedStyle(labelInputLeft()).marginRight).toBe("8px");
  expect(getComputedStyle(labelInputRight()).marginLeft).toBe("8px");
  expect(getComputedStyle(labelSuggestLeft()).marginRight).toBe("8px");
  expect(getComputedStyle(labelSuggestRight()).marginLeft).toBe("8px");
});
