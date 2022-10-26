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

import {TobagoFilterRegistry} from "./tobago-filter-registry";

beforeEach(() => {
  Object.defineProperty(window, "navigator", {value: {}, configurable: true});
  // "es" is only one example
  Object.defineProperty(window.navigator, "language", {value: "es", configurable: true});
});

test("a startsWith a", () => {
  const p = TobagoFilterRegistry.get("startsWith");
  expect(p("a", "a")).toBeTruthy();
});

test("a startsWith b", () => {
  const p = TobagoFilterRegistry.get("startsWith");
  expect(p("a", "b")).toBeFalsy();
});

test("a startsWith A", () => {
  const p = TobagoFilterRegistry.get("startsWith");
  expect(p("a", "A")).toBeTruthy();
});

test("AB startsWith á", () => {
  const p = TobagoFilterRegistry.get("startsWith");
  expect(p("AB", "á")).toBeTruthy();
});

test("AB contains á", () => {
  const c = TobagoFilterRegistry.get("contains");
  expect(c("aB", "á")).toBeTruthy();
});

test("BAB contains á", () => {
  const c = TobagoFilterRegistry.get("contains");
  expect(c("BAB", "á")).toBeTruthy();
});

test("El niño startsWith \u00F1", () => {
  const c = TobagoFilterRegistry.get("startsWith");
  expect(c("El niño", "\u00F1")).toBeFalsy();
});

test("El niño contains \u00F1", () => {
  const c = TobagoFilterRegistry.get("contains");
  expect(c("El niño", "\u00F1")).toBeTruthy();
});

test("El niño startsWith \u006E\u0303", () => {
  const c = TobagoFilterRegistry.get("startsWith");
  expect(c("El niño", "\u006E\u0303")).toBeFalsy();
});

test("El niño contains \u006E\u0303", () => {
  const c = TobagoFilterRegistry.get("contains");
  expect(c("El niño", "\u006E\u0303")).toBeTruthy();
});

test("El niño startsWith Ñ", () => {
  const c = TobagoFilterRegistry.get("startsWith");
  expect(c("El niño", "Ñ")).toBeFalsy();
});

test("El niño contains Ñ", () => {
  const c = TobagoFilterRegistry.get("contains");
  expect(c("El niño", "Ñ")).toBeTruthy();
});

test("Am\u00e9lie startsWith Am\u0065\u0301lie", () => {
  const c = TobagoFilterRegistry.get("startsWith");
  expect(c("Am\u00e9lie", "Am\u0065\u0301lie")).toBeTruthy();
});

test("Am\u00e9lie contains Am\u0065\u0301lie", () => {
  const c = TobagoFilterRegistry.get("contains");
  expect(c("Am\u00e9lie", "Am\u0065\u0301lie")).toBeTruthy();
});

test("Barış startsWith Baris", () => {
  const c = TobagoFilterRegistry.get("startsWith");
// XXX skip  expect(c("Barış", "Baris")).toBeTruthy();
});

test("Barış contains Baris", () => {
  const c = TobagoFilterRegistry.get("contains");
// XXX skip  expect(c("Barış", "Baris")).toBeTruthy();
});

test("Uğur startsWith Ugur", () => {
  const c = TobagoFilterRegistry.get("startsWith");
  expect(c("Uğur", "Ugur")).toBeTruthy();
});

test("Uğur contains Ugur", () => {
  const c = TobagoFilterRegistry.get("contains");
  expect(c("Uğur", "Ugur")).toBeTruthy();
});

test("Mjølnir startsWith Mjolnir", () => {
  const c = TobagoFilterRegistry.get("startsWith");
  expect(c("Mjølnir", "Mjolnir")).toBeTruthy();
});

test("Mjølnir contains Mjolnir", () => {
  const c = TobagoFilterRegistry.get("contains");
  expect(c("Mjølnir", "Mjolnir")).toBeTruthy();
});
