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
import {ClientBehaviors} from "./tobago-client-behaviors";

export enum DropdownMenuAlignment {
  start,
  centerFullWidth,
  end
}

export class DropdownMenu {
  private referenceElement: HTMLElement;
  private dropdownMenuElementId: string;
  private eventElement: HTMLElement;
  private parent: HTMLElement; //initial parent of the dropdown menu
  private localMenu: boolean;
  private alignment: DropdownMenuAlignment;
  private resizeEventListener: EventListenerOrEventListenerObject = this.resizeEventListenerEvent.bind(this);
  private scrollEventListener: EventListenerOrEventListenerObject = this.scrollEventListenerEvent.bind(this);

  private popper;

  /**
   * @param dropdownMenuElement dropdown menu element
   * @param referenceElement the element which the dropdown menu is aligned for
   * @param eventElement the element for which the show/shown/hide/hidden event is dispatched, usually the custom
   * element
   * @param localMenu if true, do not use the menu store
   * @param alignment on the 'start' or 'end' of the reference element or in the center and the min-width is the same as
   * the reference element width
   */
  constructor(dropdownMenuElement: HTMLElement, referenceElement: HTMLElement, eventElement: HTMLElement,
              localMenu: boolean, alignment: DropdownMenuAlignment) {
    this.dropdownMenuElementId = dropdownMenuElement.getAttribute("name");
    this.referenceElement = referenceElement;
    this.eventElement = eventElement;
    this.parent = dropdownMenuElement.parentElement;
    this.localMenu = localMenu;
    this.alignment = alignment;

    /*this.popper = createPopper(this.referenceElement, this.dropdownMenuElement, {
      placement: this.dropdownMenuElement.classList.contains(Css.DROPDOWN_MENU_END) ? "bottom-end" : "bottom-start",
    });*/
  }

  private resizeEventListenerEvent(event: Event): void {
    this.updatePosition();
  }

  private scrollEventListenerEvent(event: Event): void {
    const target = event.target as HTMLElement;
    if (!this.insideDropdownMenuElement(target)) {
      this.hide();
    }
  }

  private insideDropdownMenuElement(element: HTMLElement): boolean {
    if (element && element.classList) {
      if (element.classList.contains(Css.TOBAGO_DROPDOWN_MENU)
          && element.getAttribute("name") === this.dropdownMenuElementId) {
        return true;
      } else {
        return this.insideDropdownMenuElement(element.parentElement);
      }
    } else {
      return false;
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
      this.eventElement.dispatchEvent(new CustomEvent(ClientBehaviors.DROPDOWN_SHOW));

      window.addEventListener("resize", this.resizeEventListener);
      window.addEventListener("scroll", this.scrollEventListener, true);

      if (!this.localMenu) {
        this.menuStore.appendChild(this.dropdownMenuElement);
      }
      this.referenceElement.classList.add(Css.SHOW);
      this.referenceElement.ariaExpanded = "true";
      this.dropdownMenuElement.classList.add(Css.SHOW);
      this.updatePosition();

      this.eventElement.dispatchEvent(new CustomEvent(ClientBehaviors.DROPDOWN_SHOWN));
    }
  }

  hide(): void {
    if (this.dropdownMenuElement && this.dropdownMenuElement.classList.contains(Css.SHOW)) {
      this.eventElement.dispatchEvent(new CustomEvent(ClientBehaviors.DROPDOWN_HIDE));

      window.removeEventListener("resize", this.resizeEventListener);
      window.removeEventListener("scroll", this.scrollEventListener, true);

      this.referenceElement.classList.remove(Css.SHOW);
      this.referenceElement.ariaExpanded = "false";
      this.dropdownMenuElement.classList.remove(Css.SHOW);
      if (!this.localMenu) {
        this.parent?.appendChild(this.dropdownMenuElement);
      }

      this.eventElement.dispatchEvent(new CustomEvent(ClientBehaviors.DROPDOWN_HIDDEN));
    }
  }

  private updatePosition(): void {
    const refElementRect = this.referenceElement.getBoundingClientRect();

    //calc horizontal positioning and max-width
    this.calcHorizontalPositioningAndMaxWidth(refElementRect, this.alignment);
    if (this.dropdownMenuElement.offsetWidth - 1 > parseInt(this.dropdownMenuElement.style.maxWidth)
        && (window.innerWidth / 2) <= refElementRect.left + (refElementRect.right - refElementRect.left)) {
      // Content of dropdown is too big and reference element is on the right side; do DropdownMenuAlignment.end
      this.calcHorizontalPositioningAndMaxWidth(refElementRect, DropdownMenuAlignment.end);
    }

    //calc width
    switch (this.alignment) {
      case DropdownMenuAlignment.start:
      case DropdownMenuAlignment.end:
        this.dropdownMenuElement.style.width = null;
        break;
      case DropdownMenuAlignment.centerFullWidth:
        this.dropdownMenuElement.style.width = refElementRect.width + "px";
        break;
    }

    //calc vertical positioning and max-height
    const upperBorder = this.stickyHeader ? this.stickyHeader.offsetHeight : 0;
    const lowerBorder = this.fixedFooter ? this.fixedFooter.offsetTop : window.innerHeight;
    const spaceAbove = refElementRect.top - upperBorder;
    const spaceBelow = lowerBorder - refElementRect.bottom;
    if (spaceBelow >= spaceAbove) {
      this.dropdownMenuElement.style.marginTop = "0px";
      this.dropdownMenuElement.style.top = "0px";
      this.dropdownMenuElement.style.top = (refElementRect.bottom - this.dropdownRect.top) + "px";
      this.dropdownMenuElement.style.bottom = null;
      this.dropdownMenuElement.style.marginTop = "var(--tobago-dropdown-menu-component-offset)";
      this.dropdownMenuElement.style.marginBottom = null;
      this.dropdownMenuElement.style.maxHeight = spaceBelow
          - parseFloat(getComputedStyle(this.dropdownMenuElement).marginBottom) + "px";
    } else {
      this.dropdownMenuElement.style.marginBottom = "0px";
      this.dropdownMenuElement.style.top = null;
      this.dropdownMenuElement.style.bottom = "0px";
      this.dropdownMenuElement.style.bottom = (this.dropdownRect.bottom - refElementRect.top) + "px";
      this.dropdownMenuElement.style.marginTop = null;
      this.dropdownMenuElement.style.marginBottom = "var(--tobago-dropdown-menu-component-offset)";
      this.dropdownMenuElement.style.maxHeight = spaceAbove
          - parseFloat(getComputedStyle(this.dropdownMenuElement).marginTop) + "px";
    }
  }

  private calcHorizontalPositioningAndMaxWidth(refElementRect: DOMRect, alignment: DropdownMenuAlignment): void {
    const rightBorder = this.body.offsetWidth;
    switch (alignment) {
      case DropdownMenuAlignment.start:
      case DropdownMenuAlignment.centerFullWidth:
        this.dropdownMenuElement.style.left = "0px";
        this.dropdownMenuElement.style.left = (refElementRect.left - this.dropdownRect.left) + "px";
        this.dropdownMenuElement.style.right = null;
        this.dropdownMenuElement.style.maxWidth = rightBorder - refElementRect.left
            - parseFloat(getComputedStyle(this.dropdownMenuElement).marginRight) + "px";
        break;
      case DropdownMenuAlignment.end:
        this.dropdownMenuElement.style.left = null;
        this.dropdownMenuElement.style.right = "0px";
        this.dropdownMenuElement.style.right = (this.dropdownRect.right - refElementRect.right) + "px";
        this.dropdownMenuElement.style.maxWidth = refElementRect.right
            - parseFloat(getComputedStyle(this.dropdownMenuElement).marginLeft) + "px";
        break;
    }
  }

  private get body(): HTMLElement {
    const root = document.getRootNode() as ShadowRoot | Document;
    return root.querySelector("body");
  }

  private get stickyHeader(): HTMLElement {
    const root = document.getRootNode() as ShadowRoot | Document;
    return root.querySelector("tobago-header.sticky-top");
  }

  private get menuStore(): HTMLDivElement {
    const root = document.getRootNode() as ShadowRoot | Document;
    return root.querySelector<HTMLDivElement>(`.${Css.TOBAGO_PAGE_MENU_STORE}`);
  }

  private get dropdownMenuElement(): HTMLElement {
    const root = document.getRootNode() as ShadowRoot | Document;
    return root.querySelector(`.${Css.TOBAGO_DROPDOWN_MENU}[name='${this.dropdownMenuElementId}']`);
  }

  private get dropdownRect(): DOMRect {
    return this.dropdownMenuElement.getBoundingClientRect();
  }

  private get fixedFooter(): HTMLElement {
    const root = document.getRootNode() as ShadowRoot | Document;
    return root.querySelector("tobago-footer.fixed-bottom");
  }

  get visible(): boolean {
    return this.dropdownMenuElement.classList.contains(Css.SHOW);
  }
}
