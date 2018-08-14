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

TobagoTestTool = {};

TobagoTestTool.stepType = {
  ACTION: 1,
  WAIT_RESPONSE: 2,
  WAIT_MS: 3,
  ASSERTS: 4
};

TobagoTestTool.msie = navigator.userAgent.indexOf("MSIE") > -1 || navigator.userAgent.indexOf("Trident") > -1;

TobagoTestTool.COLUMN_START = TobagoTestTool.msie ? "-ms-grid-column" : "grid-column-start";
TobagoTestTool.COLUMN_END = TobagoTestTool.msie ? "-ms-grid-column-span" : "grid-column-end";
TobagoTestTool.ROW_START = TobagoTestTool.msie ? "-ms-grid-row" : "grid-row-start";
TobagoTestTool.ROW_END = TobagoTestTool.msie ? "-ms-grid-row-span" : "grid-row-end";

TobagoTestTool.checkGridCss = function (assert, $element, columnStart, columnEnd, rowStart, rowEnd) {

  columnEnd = TobagoTestTool.convertGridCss(columnEnd);
  rowEnd = TobagoTestTool.convertGridCss(rowEnd);

  assert.equal($element.css(TobagoTestTool.COLUMN_START), columnStart);
  assert.equal($element.css(TobagoTestTool.COLUMN_END), columnEnd);
  assert.equal($element.css(TobagoTestTool.ROW_START), rowStart);
  assert.equal($element.css(TobagoTestTool.ROW_END), rowEnd);
};

TobagoTestTool.convertGridCss = function (end) {
  if (TobagoTestTool.msie) {
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

function TobagoTestTool(assert) {
  this.assert = assert;
  this.steps = [];
}

TobagoTestTool.prototype = {
  action: function (func) {
    this.steps.push({
      type: TobagoTestTool.stepType.ACTION,
      func: func
    });
  },
  waitForResponse: function () {
    this.steps.push({
      type: TobagoTestTool.stepType.WAIT_RESPONSE
    });
  },
  waitMs: function (ms) {
    this.steps.push({
      type: TobagoTestTool.stepType.WAIT_MS,
      ms: ms ? ms : 0
    });
  },
  asserts: function (numOfAssertions, func) {
    this.steps.push({
      type: TobagoTestTool.stepType.ASSERTS,
      numOfAssertions: numOfAssertions ? numOfAssertions : 0,
      func: func
    });
  },
  startTest: function () {
    var steps = this.steps.slice(0);
    var stepStarted = 0;
    var stepFinished = 0;
    var responses = 0;
    var timeoutTimestamp = Date.now();

    function getAssertExpect() {
      var expect = 0;
      steps.forEach(function (step) {
        if (step.type === TobagoTestTool.stepType.ASSERTS) {
          expect += step.numOfAssertions;
        }
      });
      return expect;
    }

    function getAssertAsync() {
      var async = 0;
      steps.forEach(function (step) {
        if (step.type === TobagoTestTool.stepType.ASSERTS) {
          async++;
        }
      });
      return async;
    }

    this.assert.expect(getAssertExpect());
    var done = this.assert.async(getAssertAsync());
    var assert = this.assert;

    function isFinished() {
      return stepStarted >= steps.length && stepFinished >= steps.length;
    }

    function isTimeout() {
      return Date.now() > timeoutTimestamp + 20000;
    }

    function startNextPhase() {
      return stepStarted === stepFinished;
    }

    function resetTimeout() {
      timeoutTimestamp = Date.now();
    }

    function increaseStepFinished() {
      stepFinished++;
      resetTimeout();
    }

    function detectAjaxResponse() {
      if (!isFinished() && !isTimeout()) {
        responses++;
      }
    }

    function TobagoFrame() {
      return document.getElementById("page:testframe").contentWindow.Tobago;
    }

    TobagoFrame().registerListener(detectAjaxResponse, TobagoFrame().Phase.AFTER_UPDATE);

    jQuery("#page\\:testframe").on("load", function () {
      if (!isFinished() && !isTimeout()) {

        /**
         * we need to wait for the fully loaded DOM, otherwise an action function may executed to early and some events
         * like 'componentFn().click()' cannot triggered correctly.
         */
        jQuery("#page\\:testframe").ready(function () {
          responses++;
        });

        // we need to re-initiate the ajax listener
        TobagoFrame().registerListener(detectAjaxResponse, TobagoFrame().Phase.AFTER_UPDATE);
      }
    });

    function cycle() {
      if (!isFinished() && !isTimeout()) {
        if (steps[stepFinished].type === TobagoTestTool.stepType.ACTION) {
          if (startNextPhase()) {
            stepStarted++;
            responses = 0; // maybe next step is 'wait for response'
            steps[stepFinished].func();
            increaseStepFinished();
          }

          if (startNextPhase()) {
            cycle();
          } else {
            setTimeout(cycle, 50);
          }
        } else if (steps[stepFinished].type === TobagoTestTool.stepType.WAIT_RESPONSE) {
          if (startNextPhase()) {
            stepStarted++;
          }

          if (responses > 0) {
            responses = 0;
            increaseStepFinished();
          }

          if (startNextPhase()) {
            cycle();
          } else {
            setTimeout(cycle, 50);
          }
        } else if (steps[stepFinished].type === TobagoTestTool.stepType.WAIT_MS) {
          if (startNextPhase()) {
            stepStarted++;
            setTimeout(function () {
              increaseStepFinished();
              cycle();
            }, steps[stepFinished].ms);
          } else {
            setTimeout(cycle, 50);
          }
        } else if (steps[stepFinished].type === TobagoTestTool.stepType.ASSERTS) {
          if (startNextPhase()) {
            stepStarted++;
            steps[stepFinished].func();
            increaseStepFinished();
            done();
          }

          if (startNextPhase()) {
            cycle();
          } else {
            setTimeout(cycle, 50);
          }
        }
      } else if (isTimeout()) {
        assert.ok(false, "Timeout!");
      }
    }

    cycle();
  }
};
