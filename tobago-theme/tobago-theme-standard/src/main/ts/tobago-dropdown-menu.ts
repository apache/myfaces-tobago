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

import {Css} from "./tobago-css";

export enum DropdownMenuAlignment {
  start,
  centerFullWidth,
  end
}

export class DropdownMenu {

  private dropdownMenuElementId: string;
  private referenceElement: HTMLElement;
  private parent: HTMLElement; //initial parent of the dropdown menu
  private localMenu: boolean;
  private alignment: DropdownMenuAlignment;
  private resizeEventListener: EventListenerOrEventListenerObject = this.resizeEventListenerEvent.bind(this);
  private scrollEventListener: EventListenerOrEventListenerObject = this.scrollEventListenerEvent.bind(this);

  /**
   * @param dropdownMenuElement dropdown menu element
   * @param referenceElement the element which the dropdown menu is aligned for
   * @param localMenu if true, do not use the menu store
   * @param alignment on the 'start' or 'end' of the reference element or in the center and the min-width is the same as
   * the reference element width
   */
  constructor(dropdownMenuElement: HTMLElement, referenceElement: HTMLElement,
              localMenu: boolean, alignment: DropdownMenuAlignment) {
    this.dropdownMenuElementId = dropdownMenuElement.getAttribute("name");
    this.referenceElement = referenceElement;
    this.parent = dropdownMenuElement.parentElement;
    this.localMenu = localMenu;
    this.alignment = alignment;
  }

  private resizeEventListenerEvent(event: Event): void {
    this.updatePosition();
  }

  private scrollEventListenerEvent(event: Event): void {
    const target = event.target as HTMLElement;
    if (!target.classList || !target.classList.contains(Css.TOBAGO_DROPDOWN_MENU)
        || target.getAttribute("name") !== this.dropdownMenuElementId) {
      this.hide();
    }
  }

  /**
   * Call this in the disconnectedCallback() method.
   */
  disconnect(): void {
    this.hide();
    if (this.dropdownMenuElement?.parentElement.classList.contains(Css.TOBAGO_PAGE_MENU_STORE)) {
      this.dropdownMenuElement?.remove();
    }

    delete this.dropdownMenuElementId;
    delete this.referenceElement;
    delete this.parent;
    delete this.localMenu;
    delete this.alignment;
  }

  show(): void {
    if (this.dropdownMenuElement && !this.dropdownMenuElement.classList.contains(Css.SHOW)) {
      window.addEventListener("resize", this.resizeEventListener);
      window.addEventListener("scroll", this.scrollEventListener, true);

      if (!this.localMenu) {
        this.menuStore.appendChild(this.dropdownMenuElement);
      }
      this.dropdownMenuElement.classList.add(Css.SHOW);
      this.updatePosition();
    }
  }

  hide(): void {
    if (this.dropdownMenuElement && this.dropdownMenuElement.classList.contains(Css.SHOW)) {
      window.removeEventListener("resize", this.resizeEventListener);
      window.removeEventListener("scroll", this.scrollEventListener, true);

      this.dropdownMenuElement.classList.remove(Css.SHOW);
      if (!this.localMenu) {
        this.parent?.appendChild(this.dropdownMenuElement);
      }
    }
  }

  private updatePosition(): void {
    const offset = 2; // 2px offset to every border
    const windowWidth = window.innerWidth;
    const windowHeight = window.innerHeight;
    const refElementRect = this.referenceElement.getBoundingClientRect();

    //calc width
    this.dropdownMenuElement.style.width = null;
    const naturalWidth = this.dropdownMenuElement.offsetWidth;
    const maxWidth = windowWidth - offset - offset;
    switch (this.alignment) {
      case DropdownMenuAlignment.start:
      case DropdownMenuAlignment.end:
        if (naturalWidth > maxWidth) {
          this.dropdownMenuElement.style.width = maxWidth + "px";
        }
        break;
      case DropdownMenuAlignment.centerFullWidth:
        if (refElementRect.width <= maxWidth) {
          this.dropdownMenuElement.style.width = refElementRect.width + "px";
        }
        break;
    }

    //calc height
    const topSpace = refElementRect.top - offset - offset;
    const bottomSpace = windowHeight - refElementRect.bottom - offset - offset;
    this.dropdownMenuElement.style.maxHeight = bottomSpace >= topSpace ? bottomSpace + "px" : topSpace + "px";

    //calc top
    if (bottomSpace >= topSpace) {
      this.dropdownMenuElement.style.top = refElementRect.bottom + offset + "px";
    } else {
      this.dropdownMenuElement.style.top = offset + "px";
    }

    //calc left
    const currentWidth = this.dropdownMenuElement.offsetWidth;
    if (this.alignment === DropdownMenuAlignment.start) {
      const naturalLeft = refElementRect.left;
      const alternateLeft = window.innerWidth - offset - currentWidth;
      this.dropdownMenuElement.style.left = naturalLeft <= alternateLeft ? naturalLeft + "px" : alternateLeft + "px";
    } else if (this.alignment === DropdownMenuAlignment.centerFullWidth) {
      const refElementCenter = refElementRect.left + (refElementRect.width / 2);
      this.dropdownMenuElement.style.left = refElementCenter - (currentWidth / 2) + "px";
    } else if (this.alignment === DropdownMenuAlignment.end) {
      const naturalLeft = refElementRect.right - currentWidth;
      this.dropdownMenuElement.style.left = naturalLeft >= offset ? naturalLeft + "px" : offset + "px";
    }
  }

  private get menuStore(): HTMLDivElement {
    const root = document.getRootNode() as ShadowRoot | Document;
    return root.querySelector<HTMLDivElement>(`.${Css.TOBAGO_PAGE_MENU_STORE}`);
  }

  private get dropdownMenuElement(): HTMLElement {
    const root = document.getRootNode() as ShadowRoot | Document;
    return root.querySelector(`.${Css.TOBAGO_DROPDOWN_MENU}[name='${this.dropdownMenuElementId}']`);
  }
}
