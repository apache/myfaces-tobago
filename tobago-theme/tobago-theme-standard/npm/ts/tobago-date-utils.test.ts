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

import {DateUtils} from "./tobago-date-utils";

test('pattern format: yyyy', () => {
  const result = DateUtils.convertPatternJava2Js("yyyy");
  expect(result).toBe("yyyy");
});

test('pattern format: yy', () => {
  const result = DateUtils.convertPatternJava2Js("yy");
  expect(result).toBe("yy");
});

test('pattern format: dd', () => {
  const result = DateUtils.convertPatternJava2Js("dd");
  expect(result).toBe("dd");
});

test('pattern format: d', () => {
  const result = DateUtils.convertPatternJava2Js("d");
  expect(result).toBe("d");
});

test('pattern format: MM', () => {
  const result = DateUtils.convertPatternJava2Js("MM");
  expect(result).toBe("mm");
});

test('pattern format: M', () => {
  const result = DateUtils.convertPatternJava2Js("M");
  expect(result).toBe("m");
});
