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

QUnit.test("No label set", function (assert) {
  assert.expect(5);

  let sectionFn = querySelectorFn("#page\\:mainForm\\:sectionNoLabel");
  let rootDiv = sectionFn().querySelector(".tobago-section-content > tobago-in");
  let label = rootDiv.querySelector("label");
  let input = rootDiv.querySelector("input");

  assert.equal(rootDiv.id, "page:mainForm:inNoLabel");
  assert.equal(label, null);
  assert.equal(input.id, "page:mainForm:inNoLabel::field");
  assert.equal(input.name, "page:mainForm:inNoLabel");
  assert.ok(input.classList.contains("tobago-in-markup-testmarkup"));
});

QUnit.test("labelLayout=none", function (assert) {
  assert.expect(5);

  let sectionFn = querySelectorFn("#page\\:mainForm\\:sectionNone");
  let rootDiv = sectionFn().querySelector(".tobago-section-content > tobago-in");
  let label = rootDiv.querySelector("label");
  let input = rootDiv.querySelector("input");

  assert.equal(rootDiv.id, "page:mainForm:inNone");
  assert.equal(label, null);
  assert.equal(input.id, "page:mainForm:inNone::field");
  assert.equal(input.name, "page:mainForm:inNone");
  assert.ok(input.classList.contains("tobago-in-markup-testmarkup"));
});

QUnit.test("labelLayout=skip", function (assert) {
  assert.expect(3);

  let sectionFn = querySelectorFn("#page\\:mainForm\\:sectionSkip");
  let rootInput = sectionFn().querySelector(".tobago-section-content > input");
  let label = sectionFn().querySelector("label");

  assert.equal(label, null);
  assert.equal(rootInput.id, "page:mainForm:inSkip");
  assert.ok(rootInput.classList.contains("tobago-in-markup-testmarkup"));
});

QUnit.test("labelLayout=top", function (assert) {
  assert.expect(5);

  let sectionFn = querySelectorFn("#page\\:mainForm\\:sectionTop");
  let rootDiv = sectionFn().querySelector(".tobago-section-content > tobago-in");
  let label = rootDiv.querySelector("label");
  let input = rootDiv.querySelector("input");

  assert.equal(rootDiv.id, "page:mainForm:inTop");
  assert.equal(label.getAttribute('for'), "page:mainForm:inTop::field");
  assert.equal(input.id, "page:mainForm:inTop::field");
  assert.equal(input.name, "page:mainForm:inTop");
  assert.ok(input.classList.contains("tobago-in-markup-testmarkup"));
});

QUnit.test("labelLayout=flowLeft", function (assert) {
  assert.expect(5);

  let sectionFn = querySelectorFn("#page\\:mainForm\\:sectionFlowLeft");
  let rootDiv = sectionFn().querySelector(".tobago-section-content > tobago-in");
  let label = rootDiv.querySelector("label");
  let input = rootDiv.querySelector("input");

  assert.equal(rootDiv.id, "page:mainForm:inFlowLeft");
  assert.equal(label.getAttribute('for'), "page:mainForm:inFlowLeft::field");
  assert.equal(input.id, "page:mainForm:inFlowLeft::field");
  assert.equal(input.name, "page:mainForm:inFlowLeft");
  assert.ok(input.classList.contains("tobago-in-markup-testmarkup"));
});

QUnit.test("labelLayout=flowRight", function (assert) {
  assert.expect(5);

  let sectionFn = querySelectorFn("#page\\:mainForm\\:sectionFlowRight");
  let rootDiv = sectionFn().querySelector(".tobago-section-content > tobago-in");
  let label = rootDiv.querySelector("label");
  let input = rootDiv.querySelector("input");

  assert.equal(rootDiv.id, "page:mainForm:inFlowRight");
  assert.equal(label.getAttribute('for'), "page:mainForm:inFlowRight::field");
  assert.equal(input.id, "page:mainForm:inFlowRight::field");
  assert.equal(input.name, "page:mainForm:inFlowRight");
  assert.ok(input.classList.contains("tobago-in-markup-testmarkup"));
});

QUnit.test("labelLayout=flexLeft", function (assert) {
  assert.expect(5);

  let sectionFn = querySelectorFn("#page\\:mainForm\\:sectionFlexLeft");
  let rootDiv = sectionFn().querySelector(".tobago-section-content > tobago-in");
  let label = rootDiv.querySelector("label");
  let input = rootDiv.querySelector("input");

  assert.equal(rootDiv.id, "page:mainForm:inFlexLeft");
  assert.equal(label.getAttribute('for'), "page:mainForm:inFlexLeft::field");
  assert.equal(input.id, "page:mainForm:inFlexLeft::field");
  assert.equal(input.name, "page:mainForm:inFlexLeft");
  assert.ok(input.classList.contains("tobago-in-markup-testmarkup"));
});

QUnit.test("labelLayout=flexRight", function (assert) {
  assert.expect(5);

  let sectionFn = querySelectorFn("#page\\:mainForm\\:sectionFlexRight");
  let rootDiv = sectionFn().querySelector(".tobago-section-content > tobago-in");
  let label = rootDiv.querySelector("label");
  let input = rootDiv.querySelector("input");

  assert.equal(rootDiv.id, "page:mainForm:inFlexRight");
  assert.equal(label.getAttribute('for'), "page:mainForm:inFlexRight::field");
  assert.equal(input.id, "page:mainForm:inFlexRight::field");
  assert.equal(input.name, "page:mainForm:inFlexRight");
  assert.ok(input.classList.contains("tobago-in-markup-testmarkup"));
});

QUnit.test("labelLayout=segmentLeft", function (assert) {
  assert.expect(6);

  let sectionFn = querySelectorFn("#page\\:mainForm\\:sectionSegmentLeft");
  let segmentLayout = sectionFn().querySelector(".tobago-section-content > div");
  let labelSegment = segmentLayout.querySelector(".col-3 > tobago-in");
  let inputSegment = segmentLayout.querySelector(".col-9 > tobago-in");
  let label = labelSegment.querySelector("label");
  let input = inputSegment.querySelector("input");

  assert.equal(labelSegment.id, "page:mainForm:inSegmentLeft::label");
  assert.equal(label.getAttribute('for'), "page:mainForm:inSegmentLeft::field");
  assert.equal(inputSegment.id, "page:mainForm:inSegmentLeft");
  assert.equal(input.id, "page:mainForm:inSegmentLeft::field");
  assert.equal(input.name, "page:mainForm:inSegmentLeft");
  assert.ok(input.classList.contains("tobago-in-markup-testmarkup"));
});

QUnit.test("labelLayout=segmentRight", function (assert) {
  assert.expect(6);

  let sectionFn = querySelectorFn("#page\\:mainForm\\:sectionSegmentRight");
  let segmentLayout = sectionFn().querySelector(".tobago-section-content > div");
  let labelSegment = segmentLayout.querySelector(".col-3 > tobago-in");
  let inputSegment = segmentLayout.querySelector(".col-9 > tobago-in");
  let label = labelSegment.querySelector("label");
  let input = inputSegment.querySelector("input");

  assert.equal(labelSegment.id, "page:mainForm:inSegmentRight::label");
  assert.equal(label.getAttribute('for'), "page:mainForm:inSegmentRight::field");
  assert.equal(inputSegment.id, "page:mainForm:inSegmentRight");
  assert.equal(input.id, "page:mainForm:inSegmentRight::field");
  assert.equal(input.name, "page:mainForm:inSegmentRight");
  assert.ok(input.classList.contains("tobago-in-markup-testmarkup"));
});
