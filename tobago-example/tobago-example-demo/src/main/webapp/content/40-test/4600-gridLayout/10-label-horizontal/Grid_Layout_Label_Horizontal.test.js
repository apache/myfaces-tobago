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
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("test CSS of the fields and labels of 'first1'", function (done) {
  let fieldFn = querySelectorFn("#page\\:mainForm\\:first1");
  let labelFn = querySelectorFn("#page\\:mainForm\\:first1\\:\\:label");

  JasmineTestTool.checkGridCss(done, fieldFn(), "2", "auto", "3", "auto");
  JasmineTestTool.checkGridCss(done, labelFn(), "1", "auto", "3", "auto");
});

it("test CSS of the fields and labels of 'last1'", function (done) {
  let fieldFn = querySelectorFn("#page\\:mainForm\\:last1");
  let labelFn = querySelectorFn("#page\\:mainForm\\:last1\\:\\:label");

  JasmineTestTool.checkGridCss(done, fieldFn(), "3", "auto", "3", "auto");
  JasmineTestTool.checkGridCss(done, labelFn(), "4", "auto", "3", "auto");
});

it("test CSS of the fields and labels of 'last1'", function (done) {
  let fieldFn = querySelectorFn("#page\\:mainForm\\:last1");
  let labelFn = querySelectorFn("#page\\:mainForm\\:last1\\:\\:label");

  JasmineTestTool.checkGridCss(done, fieldFn(), "3", "auto", "3", "auto");
  JasmineTestTool.checkGridCss(done, labelFn(), "4", "auto", "3", "auto");
});

it("test CSS of the fields and labels of 'first2'", function (done) {
  let fieldFn = querySelectorFn("#page\\:mainForm\\:first2");
  let labelFn = querySelectorFn("#page\\:mainForm\\:first2\\:\\:label");

  JasmineTestTool.checkGridCss(done, fieldFn(), "1", "auto", "5", "auto");
  JasmineTestTool.checkGridCss(done, labelFn(), "2", "auto", "5", "auto");
});

it("test CSS of the fields and labels of 'last2'", function (done) {
  let fieldFn = querySelectorFn("#page\\:mainForm\\:last2");
  let labelFn = querySelectorFn("#page\\:mainForm\\:last2\\:\\:label");

  JasmineTestTool.checkGridCss(done, fieldFn(), "4", "auto", "5", "auto");
  JasmineTestTool.checkGridCss(done, labelFn(), "3", "auto", "5", "auto");
});

it("test CSS of the fields and labels of 'first3'", function (done) {
  let fieldFn = querySelectorFn("#page\\:mainForm\\:first3");
  let labelFn = querySelectorFn("#page\\:mainForm\\:first3\\:\\:label");

  JasmineTestTool.checkGridCss(done, fieldFn(), "2", "span 3", "7", "auto");
  JasmineTestTool.checkGridCss(done, labelFn(), "1", "auto", "7", "auto");
});

it("test CSS of the fields and labels of 'last3'", function (done) {
  let fieldFn = querySelectorFn("#page\\:mainForm\\:last3");
  let labelFn = querySelectorFn("#page\\:mainForm\\:last3\\:\\:label");

  JasmineTestTool.checkGridCss(done, fieldFn(), "1", "span 3", "8", "auto");
  JasmineTestTool.checkGridCss(done, labelFn(), "4", "auto", "8", "auto");
});

it("test CSS of the fields and labels of 'first4'", function (done) {
  let fieldFn = querySelectorFn("#page\\:mainForm\\:first4");
  let labelFn = querySelectorFn("#page\\:mainForm\\:first4\\:\\:label");

  JasmineTestTool.checkGridCss(done, fieldFn(), "2", "span 2", "10", "auto");
  JasmineTestTool.checkGridCss(done, labelFn(), "1", "auto", "10", "auto");
});

it("test CSS of the fields and labels of 'last4'", function (done) {
  let fieldFn = querySelectorFn("#page\\:mainForm\\:last4");
  let labelFn = querySelectorFn("#page\\:mainForm\\:last4\\:\\:label");

  JasmineTestTool.checkGridCss(done, fieldFn(), "1", "span 2", "11", "auto");
  JasmineTestTool.checkGridCss(done, labelFn(), "3", "auto", "11", "auto");
});

it("test CSS of the fields and labels of 'first5'", function (done) {
  let fieldFn = querySelectorFn("#page\\:mainForm\\:first5");
  let labelFn = querySelectorFn("#page\\:mainForm\\:first5\\:\\:label");

  JasmineTestTool.checkGridCss(done, fieldFn(), "3", "span 2", "13", "auto");
  JasmineTestTool.checkGridCss(done, labelFn(), "2", "auto", "13", "auto");
});

it("test CSS of the fields and labels of 'last5'", function (done) {
  let fieldFn = querySelectorFn("#page\\:mainForm\\:last5");
  let labelFn = querySelectorFn("#page\\:mainForm\\:last5\\:\\:label");

  JasmineTestTool.checkGridCss(done, fieldFn(), "2", "span 2", "14", "auto");
  JasmineTestTool.checkGridCss(done, labelFn(), "4", "auto", "14", "auto");
});

it("test CSS of the fields and labels of 'first6'", function (done) {
  let fieldFn = querySelectorFn("#page\\:mainForm\\:first6");
  let labelFn = querySelectorFn("#page\\:mainForm\\:first6\\:\\:label");

  JasmineTestTool.checkGridCss(done, fieldFn(), "2", "span 4", "16", "auto");
  JasmineTestTool.checkGridCss(done, labelFn(), "1", "auto", "16", "auto");
});

it("test CSS of the fields and labels of 'last6'", function (done) {
  let fieldFn = querySelectorFn("#page\\:mainForm\\:last6");
  let labelFn = querySelectorFn("#page\\:mainForm\\:last6\\:\\:label");

  JasmineTestTool.checkGridCss(done, fieldFn(), "1", "span 4", "17", "auto");
  JasmineTestTool.checkGridCss(done, labelFn(), "5", "auto", "17", "auto");
});
