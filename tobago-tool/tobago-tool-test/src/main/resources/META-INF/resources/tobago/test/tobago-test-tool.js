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

class JasmineUtils {
  static isMsie() {
    return navigator.userAgent.indexOf("MSIE") > -1 || navigator.userAgent.indexOf("Trident") > -1;
  };

  static checkGridCss(element, columnStart, columnEnd, rowStart, rowEnd) {
    columnEnd = this.convertGridCss(columnEnd);
    rowEnd = this.convertGridCss(rowEnd);

    if (this.isMsie()) {
      expect(getComputedStyle(element).msGridColumn).toBe(columnStart);
      expect(getComputedStyle(element).msGridColumnSpan).toBe(columnEnd);
      expect(getComputedStyle(element).msGridRow).toBe(rowStart);
      expect(getComputedStyle(element).msGridRowSpan).toBe(rowEnd);
    } else {
      expect(getComputedStyle(element).gridColumnStart).toBe(columnStart);
      expect(getComputedStyle(element).gridColumnEnd).toBe(columnEnd);
      expect(getComputedStyle(element).gridRowStart).toBe(rowStart);
      expect(getComputedStyle(element).gridRowEnd).toBe(rowEnd);
    }
  };

  static convertGridCss(end) {
    if (JasmineTestTool.msie) {
      switch (end) {
        case "auto":
          return "1";
        case "span 2":
          return "2";
        case "span 3":
          return "3";
        case "span 4":
          return "4";
        default:
          return end;
      }
    } else {
      return end;
    }
  };
}

class JasmineTestTool {

  static ajaxReadyStateChangeEvent = "tobago.jtt.ajax.readyStateChange";
  static ajaxReadyState;
  steps = [];
  done;
  timeout;

  /**
   * @param done function from Jasmine; must called if all Steps done or timeout
   * @param timeout for a single step; default 20000ms
   */
  constructor(done, timeout) {
    this.done = done;
    this.timeout = timeout ? timeout : 20000;
  }

  /**
   * Setup a test.
   * @param startConditionFn the desired start condition
   * @param doFn may be null; if start condition is not fulfilled, doFn is executed
   * @param eventType if start condition is not fulfilled, an event is dispatched
   * @param eventElement if start condition is not fulfilled, an event is dispatched
   */
  setup(startConditionFn, doFn, eventType, eventElement) {
    let eventFn;
    if (typeof eventElement !== "function") {
      eventFn = () => fail("'eventElement' must be a function but was: " + eventElement);
    } else if (eventType === "click") {
      eventFn = () => {
        if (eventElement().getAttribute("href")) {
          eventElement().click(); //links are only executed with 'click()'
        } else {
          eventElement().dispatchEvent(new Event(eventType, {bubbles: true}))
        }
      }
    } else {
      eventFn = () => eventElement().dispatchEvent(new Event(eventType, {bubbles: true}));
    }

    this.steps.push({
      type: "setup",
      startConditionFunc: startConditionFn,
      doFunc: doFn,
      eventFunc: eventFn,
      substep: 4
    });
  }

  /**
   * Execute dispatchEvent() on the given element.
   * @param type of the event, e.g. 'click' or 'change'
   * @param element on which the event is dispatched, like a querySelector for a button element
   * @param result function to indicate if the event dispatched correctly. Must be return 'false' before the event is
   * dispatched. Must be return 'true' after the event is dispatched.
   */
  event(type, element, result) {
    let eventFn;
    if (typeof element !== "function") {
      eventFn = () => fail("'element' must be a function but was: " + element);
    } else if (type === "click") {
      eventFn = () => {
        if (element().getAttribute("href")) {
          element().click(); //links are only executed with 'click()'
        } else {
          element().dispatchEvent(new Event(type, {bubbles: true}))
        }
      }
    } else {
      eventFn = () => element().dispatchEvent(new Event(type, {bubbles: true}));
    }

    this.steps.push({
      type: "event",
      eventType: type,
      eventFunc: eventFn,
      resultFunc: result,
      substep: 4
    });
  }

  do(fn) {
    this.steps.push({
      type: "do",
      func: fn,
      substep: 1
    });
  }

  /**
   * Wait till the given function is 'true'.
   * @param fn given function
   */
  wait(fn) {
    this.steps.push({
      type: "wait",
      func: fn,
      substep: 1
    });
  }

  start() {
    const steps = this.steps;
    let done = this.done;
    const timeout = this.timeout;
    let lastStepExecution;

    console.debug("[JasmineTestTool] start");
    registerAjaxReadyStateListener();
    resetTimeout();
    cycle();

    function cycle() {
      const nextStep = getNextStep();

      if (isFinished()) {
        done();
        console.debug("[JasmineTestTool] finished");
      } else if (isTimeout()) {
        fail("Timeout of " + JSON.stringify(nextStep));
        nextStep.substep = 0;
        resetTimeout();
        window.setTimeout(cycle, 1);
      } else if (!isDocumentReady() || !isAjaxReady()) {
        console.debug("[JasmineTestTool] documentReady: " + isDocumentReady() + " - ajaxReady: " + isAjaxReady());
        window.setTimeout(cycle, 50);
      } else if (nextStep.type === "setup" && nextStep.substep === 4) {
        console.debug("[JasmineTestTool] setup/4-step: " + nextStep.startConditionFunc);
        if (nextStep.startConditionFunc()) {
          nextStep.substep = 0;
        } else {
          nextStep.substep--;
        }
        resetTimeout();
        window.setTimeout(cycle, 1);
      } else if (nextStep.type === "setup" && nextStep.substep === 3) {
        console.debug("[JasmineTestTool] setup/3-step: " + nextStep.doFunc);
        if (nextStep.doFunc === null) {
          nextStep.substep--;
          resetTimeout();
          window.setTimeout(cycle, 1);
        } else {
          execute(nextStep.doFunc, nextStep);
        }
      } else if (nextStep.type === "setup" && nextStep.substep === 2) {
        console.debug("[JasmineTestTool] setup/2-step: " + nextStep.eventFunc);
        execute(nextStep.eventFunc, nextStep);
      } else if (nextStep.type === "setup" && nextStep.substep === 1) {
        console.debug("[JasmineTestTool] setup/1-step: wait for " + nextStep.startConditionFunc);
        waitFor(nextStep.startConditionFunc, nextStep);
      } else if (nextStep.type === "event" && nextStep.substep === 4) {
        console.debug("[JasmineTestTool] event/4-step: wait for negative result of: " + nextStep.resultFunc);
        if (!nextStep.resultFunc()) {
          nextStep.substep--;
          resetTimeout();
        }
        window.setTimeout(cycle, 50);
      } else if (nextStep.type === "event" && nextStep.substep === 3) {
        console.debug("[JasmineTestTool] event/3-step: " + nextStep.resultFunc);
        if (nextStep.resultFunc()) {
          fail("The result function (" + nextStep.resultFunc + ") returns already 'true' BEFORE the '"
              + nextStep.eventType + "' event is dispatched. Please define a result function which return 'false' before"
              + " dispatch event and return 'true' after dispatch event.");
          nextStep.substep = 0;
          resetTimeout();
          window.setTimeout(cycle, 1);
        } else {
          nextStep.substep--;
          resetTimeout();
          window.setTimeout(cycle, 1);
        }
      } else if (nextStep.type === "event" && nextStep.substep === 2) {
        console.debug("[JasmineTestTool] event/2-step: " + nextStep.eventFunc);
        execute(nextStep.eventFunc, nextStep);
      } else if (nextStep.type === "event" && nextStep.substep === 1) {
        console.debug("[JasmineTestTool] event/1-step: wait for " + nextStep.resultFunc);
        waitFor(nextStep.resultFunc, nextStep);
      } else if (nextStep.type === "do") {
        console.debug("[JasmineTestTool] do-step: " + nextStep.func);
        execute(nextStep.func, nextStep);
      } else if (nextStep.type === "wait") {
        console.debug("[JasmineTestTool] wait-step: " + nextStep.func);
        waitFor(nextStep.func, nextStep);
      } else {
        fail("an unexpected error has occurred!");
        done();
      }
    }

    function getNextStep() {
      for (let step of steps) {
        if (step.substep > 0) {
          return step;
        }
      }
      return null;
    }

    function isFinished() {
      for (let step of steps) {
        if (step.substep > 0) {
          return false;
        }
      }
      return true;
    }

    function isDocumentReady() {
      return document.getElementById("page:testframe").contentWindow.document.readyState === "complete";
    }

    function registerAjaxReadyStateListener() {
      JasmineTestTool.ajaxReadyState = XMLHttpRequest.UNSENT;
      window.removeEventListener(JasmineTestTool.ajaxReadyStateChangeEvent, JasmineTestTool.changeAjaxReadyState);
      window.addEventListener(JasmineTestTool.ajaxReadyStateChangeEvent, JasmineTestTool.changeAjaxReadyState);
    }

    function registerCustomXmlHttpRequest() {
      class JasmineXMLHttpRequest extends XMLHttpRequest {
        constructor() {
          super();
          this.addEventListener("readystatechange", function () {
            window.dispatchEvent(new CustomEvent(JasmineTestTool.ajaxReadyStateChangeEvent,
                {detail: {readyState: this.readyState}}));
          });
        }
      }

      document.getElementById("page:testframe").contentWindow.XMLHttpRequest = JasmineXMLHttpRequest;
    }

    function isAjaxReady() {
      return JasmineTestTool.ajaxReadyState === XMLHttpRequest.UNSENT
          || JasmineTestTool.ajaxReadyState === XMLHttpRequest.DONE;
    }

    function isTimeout() {
      return Date.now() > (lastStepExecution + timeout);
    }

    function resetTimeout() {
      lastStepExecution = Date.now();
    }

    function execute(fn, nextStep) {
      registerCustomXmlHttpRequest();
      fn();
      nextStep.substep--;
      resetTimeout();
      window.setTimeout(cycle, 1);
    }

    function waitFor(fn, nextStep) {
      if (fn()) {
        nextStep.substep--;
        resetTimeout();
      }
      window.setTimeout(cycle, 50);
    }
  }

  static changeAjaxReadyState(event) {
    JasmineTestTool.ajaxReadyState = event.detail.readyState;
    console.debug("[JasmineTestTool] ajaxReadyState: " + JasmineTestTool.ajaxReadyState);
  }
}

export {JasmineUtils, JasmineTestTool};
