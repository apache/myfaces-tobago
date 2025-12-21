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

import {elementByIdFn, querySelectorFn,} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Images must be among themselves", function (done) {
  const image1 = querySelectorFn("#page\\:mainForm\\:image1 img");
  const image2 = querySelectorFn("#page\\:mainForm\\:image2 img");

  let test = new JasmineTestTool(done);
  test.do(() => expect(image2().offsetTop).toBeGreaterThan(image1().offsetTop + image1().height));
  test.start();
});

it("Image sizes", function (done) {
  const image1 = querySelectorFn("#page\\:mainForm\\:image1 img");
  const image2 = querySelectorFn("#page\\:mainForm\\:image2 img");
  const image3 = querySelectorFn("#page\\:mainForm\\:image3 img");
  const image4 = querySelectorFn("#page\\:mainForm\\:image4 img");
  const image5 = querySelectorFn("#page\\:mainForm\\:image5 img");
  const image6 = querySelectorFn("#page\\:mainForm\\:image6 img");
  const customClassIcon = elementByIdFn("page:mainForm:customClassIcon");
  const customClassImage = elementByIdFn("page:mainForm:customClassImage");

  let test = new JasmineTestTool(done);
  test.do(() => expect(image1().width).toBe(24));
  test.do(() => expect(image1().height).toBe(10));
  test.do(() => expect(image2().width).toBe(480));
  test.do(() => expect(image2().height).toBe(200));
  test.do(() => expect(image3().width).toBe(300));
  test.do(() => expect(image3().height).toBe(125));
  test.do(() => expect(image4().width).toBe(30));
  test.do(() => expect(image4().height).toBe(2));
  test.do(() => expect(image5().width).toBe(60));
  test.do(() => expect(image5().height).toBe(25));
  test.do(() => expect(image6().width).toBe(240));
  test.do(() => expect(image6().height).toBe(100));
  test.do(() => expect(customClassIcon().classList).toContain("my-custom-class"));
  test.do(() => expect(customClassImage().classList).toContain("my-custom-class"));
  test.start();
});
