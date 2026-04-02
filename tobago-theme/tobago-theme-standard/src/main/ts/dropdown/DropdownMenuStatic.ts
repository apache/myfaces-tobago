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

import {DropdownMenuAlignment} from "../tobago-dropdown-menu";

export class DropdownMenuStatic {

  /**
   * If this function is called, the dropdownMenu element must have style "left: 0px, right: 0px" set and
   * "width, max-width" must be not set.
   * Alignment=start and extend=false: left side align with left side of the reference element;
   *                                   max-width goes to the right page-border
   * Alignment=start and extend=true: right side starts at the right page-border;
   *                                  max-width goes to the left page-border
   * Alignment=centerFullWidth, extend is ignored: left/right align with left/right of the reference element
   * Alignment=end and extend=false: right side align width the right side of the reference element;
   *                                 max-width goes to the left page-border
   * Alignment=end and extend=true: left side starts at the left page-border;
   *                                max-width goes to the right page-border
   *
   * @param alignment start, centerFullWidth, end
   * @param extend use extended width for start/end alignment
   * @param refElementLeft referenceElement.getBoundingClientRect().left
   * @param refElementRight referenceElement.getBoundingClientRect().right
   * @param dropdownLeft dropdownMenu.getBoundingClientRect().left
   * @param dropdownRight dropdownMenu.getBoundingClientRect().right
   */
  static getHorizontalPositionAndSize(alignment: DropdownMenuAlignment, extend: boolean,
                                      refElementLeft: number, refElementRight: number,
                                      dropdownLeft: number, dropdownRight: number
  ): { left: number, right: number, maxWidth: number } {
    let left = null;
    let right = null;
    let maxWidth = null;

    const borderLeft = Math.min(refElementLeft, dropdownLeft);
    const borderRight = Math.max(refElementRight, dropdownRight);

    switch (alignment) {
      case DropdownMenuAlignment.start:
        if (extend) {
          right = Math.min(dropdownRight - refElementRight, 0);
          maxWidth = borderRight - borderLeft;
        } else {
          left = refElementLeft - dropdownLeft;
          maxWidth = borderRight - refElementLeft;
        }
        break;
      case DropdownMenuAlignment.centerFullWidth:
        left = refElementLeft - dropdownLeft;
        right = dropdownRight - refElementRight;
        break;
      case DropdownMenuAlignment.end:
        if (extend) {
          left = Math.min(refElementLeft - dropdownLeft, 0);
          maxWidth = borderRight - borderLeft;
        } else {
          right = dropdownRight - refElementRight;
          maxWidth = refElementRight - borderLeft;
        }
        break;
    }

    return {left: left, right: right, maxWidth: maxWidth};
  }
}
