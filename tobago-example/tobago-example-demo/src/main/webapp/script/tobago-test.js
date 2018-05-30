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

function jQueryFrame(expression) {
  return document.getElementById("page:testframe").contentWindow.jQuery(expression);
}

function jQueryFrameFn(expression) {
  return function () {
    return jQueryFrame(expression);
  }
}

QUnit.test("wait for test", function (assert) {
  var done = assert.async();

  var startTime = new Date().getTime();
  var contentWindowName = "";
  var waitingDone = false;
  var interval = setInterval(function () {
    contentWindowName = document.getElementById("page:testframe").contentWindow.name;
    waitingDone = (contentWindowName !== "page:testframe" && contentWindowName !== "ds-tempWindowId")
        || new RegExp('[\?&]base=([^&#]*)').exec(window.location.href)[1].indexOf("error%2F") === 0;
    if (new Date().getTime() - startTime >= 20000 || waitingDone) {
      clearInterval(interval);
      assert.ok(waitingDone);
      done();
    }
  }, 50);
});

QUnit.test("duplicated IDs", function (assert) {
  function getDuplicatedIDs() {
    var duplicatedIDs = [];
    jQueryFrame('[id]').each(function () {
      var ids = jQueryFrame('[id="' + this.id + '"]');
      if (ids.length > 1 && ids[0] === this)
        duplicatedIDs.push(this.id);
    });
    return duplicatedIDs;
  }

  var duplicatedIDs = getDuplicatedIDs();
  assert.equal(duplicatedIDs.length, 0, "duplicated IDs are: " + duplicatedIDs);
});

function TobagoTestTools(assert) {
  this.assert = assert;
  this.steps = [];
}

TobagoTestTools.stepType = {
  ACTION: 1,
  WAIT_RESPONSE: 2,
  WAIT_MS: 3,
  ASSERTS: 4
};
TobagoTestTools.prototype = {
  action: function (func) {
    this.steps.push({
      type: TobagoTestTools.stepType.ACTION,
      func: func
    })
  },
  waitForResponse: function () {
    this.steps.push({
      type: TobagoTestTools.stepType.WAIT_RESPONSE
    })
  },
  waitMs: function (ms) {
    this.steps.push({
      type: TobagoTestTools.stepType.WAIT_MS,
      ms: ms ? ms : 0
    })
  },
  asserts: function (numOfAssertions, func) {
    this.steps.push({
      type: TobagoTestTools.stepType.ASSERTS,
      numOfAssertions: numOfAssertions ? numOfAssertions : 0,
      func: func
    })
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
        if (step.type === TobagoTestTools.stepType.ASSERTS) {
          expect += step.numOfAssertions;
        }
      });
      return expect;
    }

    function getAssertAsync() {
      var async = 0;
      steps.forEach(function (step) {
        if (step.type === TobagoTestTools.stepType.ASSERTS) {
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
        responses++;

        // we need to re-initiate the ajax listener
        TobagoFrame().registerListener(detectAjaxResponse, TobagoFrame().Phase.AFTER_UPDATE);
      }
    });

    function cycle() {
      if (!isFinished() && !isTimeout()) {
        if (steps[stepFinished].type === TobagoTestTools.stepType.ACTION) {
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
        } else if (steps[stepFinished].type === TobagoTestTools.stepType.WAIT_RESPONSE) {
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
        } else if (steps[stepFinished].type === TobagoTestTools.stepType.WAIT_MS) {
          if (startNextPhase()) {
            stepStarted++;
            setTimeout(function () {
              increaseStepFinished();
              cycle();
            }, steps[stepFinished].ms);
          } else {
            setTimeout(cycle, 50);
          }
        } else if (steps[stepFinished].type === TobagoTestTools.stepType.ASSERTS) {
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
