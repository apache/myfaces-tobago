/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import {TreeStatic} from "./tobago-tree-static";

test("getTabindexForNode", () => {
  // Ein Tree-Node-Container sollte tabindex="-1" haben, damit er nicht fokussierbar ist
  expect(TreeStatic.getTabindexForNode()).toBe(-1);
});

test("getTabindexForSubElement", () => {
  // Sub-Elemente (Toggle, Checkbox, Radio) sollten tabindex="0" haben
  expect(TreeStatic.getTabindexForSubElement()).toBe(0);
});

test("shouldFocusCheckbox", () => {
  // Keine Checkboxen -> Toggle fokussieren
  expect(TreeStatic.shouldFocusCheckbox(false, true, true)).toBe(false);
  expect(TreeStatic.shouldFocusCheckbox(false, false, true)).toBe(false);
  expect(TreeStatic.shouldFocusCheckbox(false, true, false)).toBe(false);
  expect(TreeStatic.shouldFocusCheckbox(false, false, false)).toBe(false);

  // Mit Checkboxen, nächster Node hat kein Toggle -> Checkbox fokussieren
  expect(TreeStatic.shouldFocusCheckbox(true, true, false)).toBe(true);
  expect(TreeStatic.shouldFocusCheckbox(true, false, false)).toBe(true);

  // Mit Checkboxen, aktuell Toggle fokussiert und nächster Node hat Toggle -> Checkbox fokussieren
  expect(TreeStatic.shouldFocusCheckbox(true, true, true)).toBe(true);

  // Mit Checkboxen, aktuell Checkbox fokussiert und nächster Node hat Toggle -> Toggle fokussieren
  expect(TreeStatic.shouldFocusCheckbox(true, false, true)).toBe(false);
});

test("getNextNodeIndexOnArrowUp", () => {
  // Normaler Fall: von Index 2 nach oben -> Index 1
  expect(TreeStatic.getNextNodeIndexOnArrowUp(2, 5)).toBe(1);

  // Am Anfang: von Index 0 nach oben -> -1 (kein vorheriger Node)
  expect(TreeStatic.getNextNodeIndexOnArrowUp(0, 5)).toBe(-1);

  // Ungültiger Index: -1 nach oben -> -1
  expect(TreeStatic.getNextNodeIndexOnArrowUp(-1, 5)).toBe(-1);

  // Nur ein Node: von Index 0 nach oben -> -1
  expect(TreeStatic.getNextNodeIndexOnArrowUp(0, 1)).toBe(-1);

  // Keine Nodes: von Index 0 nach oben -> -1
  expect(TreeStatic.getNextNodeIndexOnArrowUp(0, 0)).toBe(-1);

  // Von Index 1 nach oben -> Index 0
  expect(TreeStatic.getNextNodeIndexOnArrowUp(1, 5)).toBe(0);

  // Großer Index
  expect(TreeStatic.getNextNodeIndexOnArrowUp(100, 101)).toBe(99);
});

test("getNextNodeIndexOnArrowDown", () => {
  // Normaler Fall: von Index 2 nach unten -> Index 3
  expect(TreeStatic.getNextNodeIndexOnArrowDown(2, 5)).toBe(3);

  // Am Ende: von Index 4 nach unten mit 5 Nodes -> -1 (kein nächster Node)
  expect(TreeStatic.getNextNodeIndexOnArrowDown(4, 5)).toBe(-1);

  // Ungültiger Index: -1 nach unten -> -1
  expect(TreeStatic.getNextNodeIndexOnArrowDown(-1, 5)).toBe(-1);

  // Nur ein Node: von Index 0 nach unten -> -1
  expect(TreeStatic.getNextNodeIndexOnArrowDown(0, 1)).toBe(-1);

  // Keine Nodes: von Index 0 nach unten -> -1
  expect(TreeStatic.getNextNodeIndexOnArrowDown(0, 0)).toBe(-1);

  // Von Index 0 zu 1 mit 5 Nodes
  expect(TreeStatic.getNextNodeIndexOnArrowDown(0, 5)).toBe(1);

  // Von Index 0 zu 1 mit 2 Nodes
  expect(TreeStatic.getNextNodeIndexOnArrowDown(0, 2)).toBe(1);

  // Großer Index
  expect(TreeStatic.getNextNodeIndexOnArrowDown(98, 100)).toBe(99);
});

test("getActionOnArrowLeft", () => {
  // Node ist expandiert -> einklappen
  expect(TreeStatic.getActionOnArrowLeft(true)).toBe("collapse");

  // Node ist zugeklappt -> zum Parent springen
  expect(TreeStatic.getActionOnArrowLeft(false)).toBe("parent");
});


test("shouldToggleCheckboxOnSpaceOrEnter", () => {
  // Space/Enter sollte immer die Checkbox toggeln
  expect(TreeStatic.shouldToggleCheckboxOnSpaceOrEnter()).toBe(true);
});

test("Navigation Szenarien - Fokus Management", () => {
  // Szenario 1: Durch ein Tree mit 3 Nodes navigieren (Up/Down)
  const visibleNodes = 3;

  // Start bei Node 0, Pfeil unten -> Node 1
  expect(TreeStatic.getNextNodeIndexOnArrowDown(0, visibleNodes)).toBe(1);

  // Bei Node 1, Pfeil oben -> Node 0
  expect(TreeStatic.getNextNodeIndexOnArrowUp(1, visibleNodes)).toBe(0);

  // Bei Node 1, Pfeil unten -> Node 2
  expect(TreeStatic.getNextNodeIndexOnArrowDown(1, visibleNodes)).toBe(2);

  // Bei Node 2, Pfeil unten -> -1 (Ende erreicht)
  expect(TreeStatic.getNextNodeIndexOnArrowDown(2, visibleNodes)).toBe(-1);

  // Bei Node 2, Pfeil oben -> Node 1
  expect(TreeStatic.getNextNodeIndexOnArrowUp(2, visibleNodes)).toBe(1);
});

test("Navigation Szenarien - Expand/Collapse mit Kindern", () => {
  // Szenario 2: Node mit expandierbaren Kindern navigieren

  // Ein zugeklappter, expandierbarer Node
  // Pfeil rechts -> "expand"
  const actionRight1 = TreeStatic.getActionOnArrowRight(true, false);
  expect(actionRight1).toBe("expand");
  expect(TreeStatic.shouldClickToggleOnArrowKeys(true, false, actionRight1)).toBe(true);

  // Nach dem Expandieren, Pfeil rechts -> "child" (zum ersten Kind springen)
  const actionRight2 = TreeStatic.getActionOnArrowRight(true, true);
  expect(actionRight2).toBe("child");
  expect(TreeStatic.shouldClickToggleOnArrowKeys(true, true, actionRight2)).toBe(false);

  // Beim Kind, Pfeil links -> "parent" (zum Parent springen)
  const actionLeft1 = TreeStatic.getActionOnArrowLeft(false);
  expect(actionLeft1).toBe("parent");
  expect(TreeStatic.shouldClickToggleOnArrowKeys(true, false, actionLeft1)).toBe(false);

  // Beim Parent, Pfeil links (jetzt expandiert) -> "collapse" (einklappen)
  const actionLeft2 = TreeStatic.getActionOnArrowLeft(true);
  expect(actionLeft2).toBe("collapse");
  expect(TreeStatic.shouldClickToggleOnArrowKeys(true, true, actionLeft2)).toBe(true);
});

test("Fokus-Verhalten mit und ohne Checkboxen", () => {
  // Szenario 3: Mit und ohne Checkboxen navigieren

  // Tree ohne Checkboxen: immer Toggle fokussieren
  expect(TreeStatic.shouldFocusCheckbox(false, true, true)).toBe(false);
  expect(TreeStatic.shouldFocusCheckbox(false, false, true)).toBe(false);
  expect(TreeStatic.shouldFocusCheckbox(false, true, false)).toBe(false);

  // Tree mit Checkboxen: intelligente Entscheidung
  // Wenn von Toggle navigiert wird und nächster Node hat auch Toggle -> Checkbox fokussieren
  expect(TreeStatic.shouldFocusCheckbox(true, true, true)).toBe(true);

  // Wenn vom Input navigiert wird und nächster Node hat Toggle -> Toggle fokussieren
  expect(TreeStatic.shouldFocusCheckbox(true, false, true)).toBe(false);

  // Wenn nächster Node kein Toggle hat -> Checkbox fokussieren
  expect(TreeStatic.shouldFocusCheckbox(true, false, false)).toBe(true);
});

test("Tabindex Management", () => {
  // Alle Tree-Nodes sollten tabindex="-1" haben
  const nodeTabindex = TreeStatic.getTabindexForNode();
  expect(nodeTabindex).toBe(-1);

  // Alle fokussierbaren Sub-Elemente sollten tabindex="0" haben
  const subElementTabindex = TreeStatic.getTabindexForSubElement();
  expect(subElementTabindex).toBe(0);

  // Das sollte für alle Nodes konsistent sein
  expect(TreeStatic.getTabindexForNode()).toBe(TreeStatic.getTabindexForNode());
  expect(TreeStatic.getTabindexForSubElement()).toBe(TreeStatic.getTabindexForSubElement());
});

