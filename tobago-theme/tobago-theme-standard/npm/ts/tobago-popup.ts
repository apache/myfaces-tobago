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

import {DomUtils} from "./tobago-utils";

const ESCAPE_KEYCODE = 27; // KeyboardEvent.which value for Escape (Esc) key

const Default = {
  backdrop: true,
  keyboard: true,
  focus: true,
  show: true
};

const DefaultType = {
  backdrop: "(boolean|string)",
  keyboard: "boolean",
  focus: "boolean",
  show: "boolean"
};

const Event = {
  HIDE: "hide.bs.modal",
  HIDE_PREVENTED: "hidePrevented.bs.modal",
  HIDDEN: "hidden.bs.modal",
  SHOW: "show.bs.modal",
  SHOWN: "shown.bs.modal",
  FOCUSIN: "focusin.bs.modal",
  RESIZE: "resize.bs.modal",
  CLICK_DISMISS: "click.dismiss.bs.modal",
  KEYDOWN_DISMISS: "keydown.dismiss.bs.modal",
  MOUSEUP_DISMISS: "mouseup.dismiss.bs.modal",
  MOUSEDOWN_DISMISS: "mousedown.dismiss.bs.modal",
  CLICK_DATA_API: "click.bs.modal.data-api",
  TRANSITION_END: "bsTransitionEnd"
};

const ClassName = {
  SCROLLABLE: "modal-dialog-scrollable",
  SCROLLBAR_MEASURER: "modal-scrollbar-measure",
  BACKDROP: "modal-backdrop",
  OPEN: "modal-open",
  FADE: "fade",
  SHOW: "show",
  STATIC: "modal-static"
};

const Selector = {
  DIALOG: ".modal-dialog",
  MODAL_BODY: ".modal-body",
  DATA_TOGGLE: "[data-toggle='modal']",
  DATA_DISMISS: "[data-dismiss='modal']",
  FIXED_CONTENT: ".fixed-top, .fixed-bottom, .is-fixed, .sticky-top",
  STICKY_CONTENT: ".sticky-top"
};

export class Popup extends HTMLElement {

  private emulateTransitionEndCalled: boolean = false;
  private _dialog: HTMLElement;
  private _backdrop: HTMLDivElement;
  private _isShown: boolean;
  private _isBodyOverflowing: boolean;
  private _ignoreBackdropClick: boolean;
  // _isTransitioning;
  private _scrollbarWidth : number;
  private _clickDismiss: (event: Event) => void;

  constructor() {
    super();
    this._dialog = this.querySelector(Selector.DIALOG);
    this._backdrop = null;
    this._isShown = false;
    this._isBodyOverflowing = false;
    this._ignoreBackdropClick = false;
    // this._isTransitioning = false;
    this._scrollbarWidth = 0;
  }

  connectedCallback(): void {
    const hidden = Collapse.findHidden(this);
    if (hidden.value === "false") {
      // XXX hack: this is needed for popups open by AJAX.
      // XXX currently the DOM replacement done by Tobago doesn't remove the modal-backdrop
      for (const backdrop of document.querySelectorAll(".modal-backdrop")) {
        backdrop.parentNode.removeChild(backdrop);
      }
      this.show(); // inits and opens the popup
    } else {
      this.hide(); // inits and hides the popup
    }
  }

  // Public

  // toggle(relatedTarget) {
  //   return this._isShown ? this.hide() : this.show(relatedTarget)
  // }

  show():void {
    if (this._isShown /*|| this._isTransitioning*/) {
      return;
    }

/*
    if (this.classList.contains(ClassName.FADE)) {
      this._isTransitioning = true;
    }
*/

    const showEvent = new CustomEvent(Event.SHOW, /*{*/
      // detail: relatedTarget // TBD: detail or anything other
    /*}*/);

    this.dispatchEvent(showEvent);

    if (this._isShown || showEvent.defaultPrevented) {
      return;
    }

    this._isShown = true;

    this._checkScrollbar();
    this._setScrollbar();

    // this._adjustDialog();
    //
    // this._setEscapeEvent();
    // this._setResizeEvent();

    this._clickDismiss = (event: Event) => {this.hide(/*event*/);};
    if (this.classList.contains(Selector.DATA_DISMISS)) {
      this.addEventListener(Event.CLICK_DISMISS, this._clickDismiss);
    }

    this._dialog.addEventListener(Event.MOUSEDOWN_DISMISS, () => {
      // $(this._element).one(Event.MOUSEUP_DISMISS, (event) => {  // XXX not implemented yet
      //   if ($(event.target).is(this._element)) { // XXX not implemented yet
      this._ignoreBackdropClick = true;
      // }
      // })
    });

    // this._showBackdrop(() => this._showElement(relatedTarget))
    this._showBackdrop(() => this._showElement());
  }

  hide(/*event*/):void {
    // if (event) {
    //   event.preventDefault()
    // }

    if (!this._isShown/* || this._isTransitioning*/) {
      return;
    }

    const hideEvent = new CustomEvent(Event.HIDE);

    this.dispatchEvent(hideEvent);

    if (!this._isShown || hideEvent.defaultPrevented) {
      return;
    }

    this._isShown = false;
    // const transition = this.classList.contains(ClassName.FADE);

    // if (transition) {
    //   this._isTransitioning = true
    // }

    // this._setEscapeEvent();
    // this._setResizeEvent();

    // $(document).off(Event.FOCUSIN);

    this.classList.remove(ClassName.SHOW);

    this.removeEventListener(Event.CLICK_DISMISS, this._clickDismiss);
    // $(this._dialog).off(Event.MOUSEDOWN_DISMISS);

    // if (transition) {
    //   const transitionDuration = this.getTransitionDuration();
    //
    //   this.addEventListener(Popup.TRANSITION_END, (event:Event) => this._hideModal(event));
    //   this.emulateTransitionEnd(transitionDuration)
    // } else {
       this._hideModal();
    // }
  }

  // dispose() {
  //   [window, this._element, this._dialog]
  //       .forEach((htmlElement) => $(htmlElement).off(`.bs.modal`));

    /**
     * `document` has 2 events `Event.FOCUSIN` and `Event.CLICK_DATA_API`
     * Do not move `document` in `htmlElements` array
     * It will remove `Event.CLICK_DATA_API` event that should remain
     */
    // $(document).off(Event.FOCUSIN);
    //
    // $.removeData(this._element, 'bs.modal');
    //
    // this._config = null;
    // this._element = null;
    // this._dialog = null;
    // this._backdrop = null;
    // this._isShown = null;
    // this._isBodyOverflowing = null;
    // this._ignoreBackdropClick = null;
    // this._isTransitioning = null;
    // this._scrollbarWidth = null;
  // }

  // handleUpdate() {
  //   this._adjustDialog();
  // }

  // Private

  /*_getConfig(config) {
    config = {
      ...Default,
      ...config
    }
    Util.typeCheckConfig(NAME, config, DefaultType)
    return config
  }*/
/*
  _triggerBackdropTransition() {
    if (this._config.backdrop === 'static') {
      const hideEventPrevented = $.Event(Event.HIDE_PREVENTED);

      $(this._element).trigger(hideEventPrevented);
      if (hideEventPrevented.defaultPrevented) {
        return;
      }

      this._element.classList.add(ClassName.STATIC);

      const modalTransitionDuration = DomUtils.getTransitionTime(this._element)

      $(this._element).one(Util.TRANSITION_END, () => {
        this._element.classList.remove(ClassName.STATIC)
      })
          .emulateTransitionEnd(modalTransitionDuration);
      this._element.focus();
    } else {
      this.hide();
    }
  }
*/
  _showElement(/*relatedTarget*/):void {
    // const transition = $(this._element).hasClass(ClassName.FADE)
    const modalBody = this._dialog ? this._dialog.querySelector(Selector.MODAL_BODY) : null;

    if (!this.parentNode ||
        this.parentNode.nodeType !== Node.ELEMENT_NODE) {
      // Don't move modal's DOM position
      document.body.appendChild(this);
    }

    this.style.display = "block";
    this.removeAttribute("aria-hidden");
    this.setAttribute("aria-modal", "true");

    if (this._dialog.classList.contains(ClassName.SCROLLABLE) && modalBody) {
      modalBody.scrollTop = 0;
    } else {
      this.scrollTop = 0;
    }

    // if (transition) {
    //   Util.reflow(this._element)
    // }

    this.classList.add(ClassName.SHOW);

    // const shownEvent = $.Event(Event.SHOWN, {
    //   relatedTarget
    // })

    // const transitionComplete = () => {
      // if (this._config.focus) {
      //   this._element.focus()
      // }
      // this._isTransitioning = false
      // $(this._element).trigger(shownEvent)
    // };

    // if (transition) {
    //   const transitionDuration = DomUtils.getTransitionTime(this._dialog)
    //
    //   $(this._dialog)
    //       .one(Util.TRANSITION_END, transitionComplete)
    //       .emulateTransitionEnd(transitionDuration)
    // } else {
    //   transitionComplete()
    // }

    this.dispatchEvent(new CustomEvent(Event.SHOWN));
    const autofocusElement = this.querySelector("[autofocus]") as HTMLElement;
    if (autofocusElement) {
      autofocusElement.focus();
    }
  }
/*
  _enforceFocus() {
    $(document)
        .off(Event.FOCUSIN) // Guard against infinite focus loop
        .on(Event.FOCUSIN, (event) => {
          if (document !== event.target &&
              this._element !== event.target &&
              $(this._element).has(event.target).length === 0) {
            this._element.focus()
          }
        })
  }

  _setEscapeEvent() {
    if (this._isShown && this._config.keyboard) {
      $(this._element).on(Event.KEYDOWN_DISMISS, (event) => {
        if (event.which === ESCAPE_KEYCODE) {
          this._triggerBackdropTransition()
        }
      })
    } else if (!this._isShown) {
      $(this._element).off(Event.KEYDOWN_DISMISS)
    }
  }

  _setResizeEvent() {
    if (this._isShown) {
      $(window).on(Event.RESIZE, (event) => this.handleUpdate(event))
    } else {
      $(window).off(Event.RESIZE)
    }
  }
*/
  _hideModal(): void {
    this.style.display = "none";
    this.setAttribute("aria-hidden", "true");
    this.removeAttribute("aria-modal");
    // this._isTransitioning = false;
    this._showBackdrop(() => {
      document.body.classList.remove(ClassName.OPEN);
      // this._resetAdjustments();
      this._resetScrollbar();
      // $(this._element).trigger(Event.HIDDEN)
    });
  }

  _removeBackdrop() : void {
    if (this._backdrop) {
      this._backdrop.remove();
      this._backdrop = null;
    }
  }

  _showBackdrop(callback) : void {
    const animate = this.classList.contains(ClassName.FADE) ? ClassName.FADE : "";

    if (this._isShown /*&& this._config.backdrop*/) {
      this._backdrop = document.createElement("div");
      this._backdrop.classList.add(ClassName.BACKDROP);

      if (animate) {
        this._backdrop.classList.add(animate);
      }

      document.body.append(this._backdrop);

      /*$(this._element).on(Event.CLICK_DISMISS, (event) => {
        if (this._ignoreBackdropClick) {
          this._ignoreBackdropClick = false
          return
        }
        if (event.target !== event.currentTarget) {
          return
        }

        this._triggerBackdropTransition()
      })*/

      /*if (animate) {
        Util.reflow(this._backdrop)
      }*/

      this._backdrop.classList.add(ClassName.SHOW);

      if (!callback) {
        return;
      }

      if (!animate) {
        callback();
        return;
      }

      const backdropTransitionDuration: number = DomUtils.getTransitionTime(this._backdrop);

      this.addOnetimeEventListener(this._backdrop, Event.TRANSITION_END, callback);
      this.emulateTransitionEnd(this._backdrop, backdropTransitionDuration);
    } else if (!this._isShown && this._backdrop) {
      this._backdrop.classList.remove(ClassName.SHOW);

      const callbackRemove = () => {
        this._removeBackdrop();
        if (callback) {
          callback();
        }
      };

      if (this.classList.contains(ClassName.FADE)) {
        const backdropTransitionDuration = DomUtils.getTransitionTime(this._backdrop);

        this.addOnetimeEventListener(this._backdrop, Event.TRANSITION_END, callbackRemove);
        this.emulateTransitionEnd(this._backdrop, backdropTransitionDuration);
      } else {
        callbackRemove();
      }
    } else if (callback) {
      callback();
    }
  }

  // ----------------------------------------------------------------------
  // the following methods are used to handle overflowing modals
  // todo (fat): these should probably be refactored out of modal.js
  // ----------------------------------------------------------------------

  /*_adjustDialog() {
    const isModalOverflowing =
        this._element.scrollHeight > document.documentElement.clientHeight

    if (!this._isBodyOverflowing && isModalOverflowing) {
      this._element.style.paddingLeft = `${this._scrollbarWidth}px`
    }

    if (this._isBodyOverflowing && !isModalOverflowing) {
      this._element.style.paddingRight = `${this._scrollbarWidth}px`
    }
  }*/

  /*_resetAdjustments() {
    this._element.style.paddingLeft = ''
    this._element.style.paddingRight = ''
  }*/

  _checkScrollbar(): void {
    const rect = document.body.getBoundingClientRect();
    this._isBodyOverflowing = rect.left + rect.right < window.innerWidth;
    this._scrollbarWidth = this._getScrollbarWidth();
  }

  _setScrollbar(): void {
    if (this._isBodyOverflowing) {
      // Note: DOMNode.style.paddingRight returns the actual value or '' if not set
      //   while $(DOMNode).css('padding-right') returns the calculated value or 0 if not set
      const fixedContent = [].slice.call(document.querySelectorAll(Selector.FIXED_CONTENT));
      const stickyContent = [].slice.call(document.querySelectorAll(Selector.STICKY_CONTENT));

      // Adjust fixed content padding
      /*$(fixedContent).each((index, element) => {
        const actualPadding = element.style.paddingRight
        const calculatedPadding = $(element).css('padding-right')
        $(element)
            .data('padding-right', actualPadding)
            .css('padding-right', `${parseFloat(calculatedPadding) + this._scrollbarWidth}px`)
      })*/

      // Adjust sticky content margin
      /*$(stickyContent).each((index, element) => {
        const actualMargin = element.style.marginRight
        const calculatedMargin = $(element).css('margin-right')
        $(element)
            .data('margin-right', actualMargin)
            .css('margin-right', `${parseFloat(calculatedMargin) - this._scrollbarWidth}px`)
      })*/

      // Adjust body padding
      const actualPadding = document.body.style.paddingRight;
      // const calculatedPadding = $(document.body).css('padding-right');
      /*$(document.body)
          .data('padding-right', actualPadding)
          .css('padding-right', `${parseFloat(calculatedPadding) + this._scrollbarWidth}px`)*/
    }

    document.body.classList.add(ClassName.OPEN);
  }

  _resetScrollbar(): void {
    // Restore fixed content padding
    const fixedContent = [].slice.call(document.querySelectorAll(Selector.FIXED_CONTENT));
    /*$(fixedContent).each((index, element) => {
      const padding = $(element).data('padding-right')
      $(element).removeData('padding-right')
      element.style.paddingRight = padding ? padding : ''
    })*/

    // Restore sticky content
    const elements = [].slice.call(document.querySelectorAll(`${Selector.STICKY_CONTENT}`));
    /*$(elements).each((index, element) => {
      const margin = $(element).data('margin-right')
      if (typeof margin !== 'undefined') {
        $(element).css('margin-right', margin).removeData('margin-right')
      }
    })*/

    // Restore body padding
    /*const padding = $(document.body).data('padding-right')
    $(document.body).removeData('padding-right')
    document.body.style.paddingRight = padding ? padding : '';*/
  }

  _getScrollbarWidth(): number { // thx d.walsh
    const scrollDiv = document.createElement("div");
    scrollDiv.className = ClassName.SCROLLBAR_MEASURER;
    document.body.appendChild(scrollDiv);
    const scrollbarWidth = scrollDiv.getBoundingClientRect().width - scrollDiv.clientWidth;
    document.body.removeChild(scrollDiv);
    return scrollbarWidth;
  }

  // Static

  /*static _jQueryInterface(config, relatedTarget) {
    return this.each(function () {
      let data = $(this).data(DATA_KEY)
      const _config = {
        ...Default,
        ...$(this).data(),
        ...typeof config === 'object' && config ? config : {}
      }

      if (!data) {
        data = new Modal(this, _config)
        $(this).data(DATA_KEY, data)
      }

      if (typeof config === 'string') {
        if (typeof data[config] === 'undefined') {
          throw new TypeError(`No method named "${config}"`)
        }
        data[config](relatedTarget)
      } else if (_config.show) {
        data.show(relatedTarget)
      }
    })
  }*/

  private emulateTransitionEnd(element: HTMLElement, duration: number): void {
    this.emulateTransitionEndCalled = false;

    element.addEventListener(Event.TRANSITION_END, () => {
      this.emulateTransitionEndCalled = true;
    });

    setTimeout(() => {
      if (!this.emulateTransitionEndCalled) {
        element.dispatchEvent(new CustomEvent(Event.TRANSITION_END));
      }
    }, duration);
  }

  private addOnetimeEventListener(element: HTMLElement, event: string, listener): void {
    function listenerWrapper(): void {
      listener();
      element.removeEventListener(event, listenerWrapper);
    }

    element.addEventListener(event, listenerWrapper);
  }
}

document.addEventListener("tobago.init", function (event: Event): void {
  if (window.customElements.get("tobago-popup") == null) {
    window.customElements.define("tobago-popup", Popup);
  }
});

export class Collapse {

  static findHidden(element: HTMLElement): HTMLInputElement {
    const rootNode = element.getRootNode() as ShadowRoot | Document;
    return rootNode.getElementById(element.id + "::collapse") as HTMLInputElement;
  }

  static execute = function (action: string, target: HTMLElement): void {
    const hidden = Collapse.findHidden(target);
    let newCollapsed;
    switch (action) {
      case "hide":
        newCollapsed = true;
        break;
      case "show":
        newCollapsed = false;
        break;
      default:
        console.error("unknown action: '" + action + "'");
    }
    if (newCollapsed) {
      if (target instanceof Popup) {
        target.hide();
      } else {
        target.classList.add("tobago-collapsed");
      }
    } else {
      if (target instanceof Popup) {
        target.show();
      } else {
        target.classList.remove("tobago-collapsed");
      }
    }
    hidden.value = newCollapsed;
  };
}
