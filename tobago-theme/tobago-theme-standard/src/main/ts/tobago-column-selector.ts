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

import {Sheet} from "./tobago-sheet";
import {Selectable} from "./tobago-selectable";
import {Css} from "./tobago-css";

export class ColumnSelector {
  private sheet: Sheet;

  constructor(sheet: Sheet) {
    this.sheet = sheet;
  }

  public get headerElement(): HTMLInputElement {
    return this.sheet.columnSelectorToggle;
  }

  public get selectable(): Selectable {
    return Selectable[this.headerElement.dataset.tobagoSelectionMode];
  }

  public rowElement(element: HTMLTableRowElement): HTMLInputElement {
    return element.querySelector(`td.${Css.TOBAGO_COLUMN_SELECTOR} > input.tobago-selected`);
  }

  public get disabled(): boolean {
    const rowElement = this.sheet
        .querySelector<HTMLInputElement>("tr[row-index] input[name^='" + this.sheet.id + "_data_row_selector']");
    return rowElement && rowElement.disabled;
  }
}
