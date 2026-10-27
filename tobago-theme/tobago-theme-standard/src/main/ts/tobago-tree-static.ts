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

export class TreeStatic {

  /**
   * Bestimmt den Tabindex-Wert für einen Tree-Node-Container.
   * Tree-Nodes selbst sollten nicht im Tab-Fokus liegen (tabindex="-1"),
   * nur die Sub-Elemente (Toggle, Checkbox, Radio).
   * @returns -1 für den Node-Container
   */
  static getTabindexForNode(): number {
    return -1;
  }

  /**
   * Bestimmt den Tabindex-Wert für Sub-Elemente eines Nodes (Toggle, Checkbox, Radio).
   * Diese sind normalerweise tabbbar und erhalten tabindex="0".
   * @returns 0 für sub-Elemente, die tabbbar sein sollen
   */
  static getTabindexForSubElement(): number {
    return 0;
  }

  /**
   * Bestimmt, ob eine Checkbox/Radio bei der Navigation fokussiert werden soll.
   * Dies hängt davon ab, ob Checkboxen im Tree vorhanden sind,
   * welches Element aktuell fokussiert ist (Toggle oder Input),
   * und ob der nächste Node ein Toggle hat.
   *
   * @param hasCheckboxes - ob der Tree Checkboxes/Radios enthält
   * @param currentTargetIsToggle - ob das aktuelle fokussierte Element ein Toggle ist
   * @param nextNodeHasToggle - ob der nächste Node ein Toggle hat
   * @returns true, wenn die Checkbox fokussiert werden soll, false für Toggle
   */
  static shouldFocusCheckbox(
      hasCheckboxes: boolean,
      currentTargetIsToggle: boolean,
      nextNodeHasToggle: boolean
  ): boolean {
    // Wenn keine Checkboxen existieren -> Toggle fokussieren (return false)
    if (!hasCheckboxes) {
      return false;
    }

    // Wenn das nächste Node kein Toggle hat -> Checkbox fokussieren (return true)
    if (!nextNodeHasToggle) {
      return true;
    }

    // Wenn aktuell das Toggle fokussiert war und das nächste Node auch ein Toggle hat -> Checkbox fokussieren
    if (currentTargetIsToggle && nextNodeHasToggle) {
      return true;
    }

    return false;
  }

  /**
   * Bestimmt den Index des nächsten sichtbaren Nodes für die Pfeil-Oben-Taste.
   * Gibt -1 zurück, wenn es keinen vorherigen sichtbaren Node gibt.
   *
   * @param currentNodeIndex - Index des aktuellen Nodes im sichtbaren Array
   * @param visibleNodesCount - Gesamtanzahl der sichtbaren Nodes
   * @returns Index des vorherigen Nodes oder -1
   */
  static getNextNodeIndexOnArrowUp(currentNodeIndex: number, visibleNodesCount: number): number {
    if (currentNodeIndex <= 0 || visibleNodesCount === 0) {
      return -1;
    }
    return currentNodeIndex - 1;
  }

  /**
   * Bestimmt den Index des nächsten sichtbaren Nodes für die Pfeil-Unten-Taste.
   * Gibt -1 zurück, wenn es keinen nächsten sichtbaren Node gibt.
   *
   * @param currentNodeIndex - Index des aktuellen Nodes im sichtbaren Array
   * @param visibleNodesCount - Gesamtanzahl der sichtbaren Nodes
   * @returns Index des nächsten Nodes oder -1
   */
  static getNextNodeIndexOnArrowDown(currentNodeIndex: number, visibleNodesCount: number): number {
    if (currentNodeIndex < 0 || currentNodeIndex >= visibleNodesCount - 1) {
      return -1;
    }
    return currentNodeIndex + 1;
  }

  /**
   * Bestimmt, ob die linke Pfeiltaste das Node einklappen soll oder zum Parent springen.
   * - Wenn expandiert: einklappen
   * - Wenn zugeklappt: zum Parent springen
   *
   * @param isExpanded - ob der aktuelle Node expandiert ist
   * @returns "collapse" um einzuklappen, "parent" um zum Parent zu springen
   */
  static getActionOnArrowLeft(isExpanded: boolean): "collapse" | "parent" {
    return isExpanded ? "collapse" : "parent";
  }

  /**
   * Bestimmt, ob die rechte Pfeiltaste das Node expandieren soll oder ein Kind besuchen.
   * - Wenn zugeklappt und expandierbar: expandieren
   * - Wenn expandiert: zum ersten Kind springen
   *
   * @param isExpandable - ob der aktuelle Node expandierbar ist
   * @param isExpanded - ob der aktuelle Node expandiert ist
   * @returns "expand" zum Expandieren, "child" um zum ersten Kind zu springen, oder null
   */
  static getActionOnArrowRight(isExpandable: boolean, isExpanded: boolean): "expand" | "child" | null {
    if (isExpandable && !isExpanded) {
      return "expand";
    }
    if (isExpanded) {
      return "child";
    }
    return null;
  }

  /**
   * Bestimmt, ob ein Toggle Click ereignis (ArrowRight oder ArrowLeft) ausgelöst werden sollte.
   * Dies ist der Fall, wenn:
   * - ArrowRight: Node ist expandierbar und nicht expandiert
   * - ArrowLeft: Node ist expandiert
   *
   * @param isExpandable - ob der aktuelle Node expandierbar ist
   * @param isExpanded - ob der aktuelle Node expandiert ist
   * @param action - die geplante Aktion ("collapse", "parent", "expand", "child", etc.)
   * @returns true, wenn ein Toggle-Click ausgelöst werden sollte
   */
  static shouldClickToggleOnArrowKeys(
      isExpandable: boolean,
      isExpanded: boolean,
      action: string | null
  ): boolean {
    return (isExpandable && !isExpanded && action === "expand") ||
           (isExpanded && action === "collapse");
  }

  /**
   * Bestimmt, ob bei Space oder Enter die Checkbox/Radio toggled werden sollte.
   * Dies sollte immer der Fall sein, wenn der Focus auf der aktuellen Node liegt.
   *
   * @returns true, um die Checkbox zu togglen
   */
  static shouldToggleCheckboxOnSpaceOrEnter(): boolean {
    return true;
  }
}
