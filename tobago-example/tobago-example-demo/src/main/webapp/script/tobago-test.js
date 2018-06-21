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
  var elementById = document.getElementById("page:testframe");
  var contentWindow = elementById.contentWindow;
  console.log("expression=" + expression);
  return contentWindow.jQuery(expression);
}

/**
 * Wait for ajax requests. Can be used with PhantomJs.
 * @param waitingDone return false if still waiting, true if waiting done
 * @param executeWhenDone is executed after waiting
 * @param maxWait set the maximal waiting time in ms; default is 20000
 */
function waitForAjax(waitingDone, executeWhenDone, maxWait) {
  var startTime = new Date().getTime();
  maxWait = maxWait ? maxWait : 20000;
  var stillWaiting = true;
  var interval = setInterval(function () {
    if (new Date().getTime() - startTime < maxWait && stillWaiting) {
      stillWaiting = !waitingDone();
    } else {
      executeWhenDone();
      clearInterval(interval);
    }
  }, 50);
}

QUnit.test("wait for test", function (assert) {
  var done = assert.async();

  var startTime = new Date().getTime();
  var contentWindowName = "";
  var waitingDone = false;
  var interval = setInterval(function () {
    contentWindowName = document.getElementById("page:testframe").contentWindow.name;
    waitingDone = contentWindowName !== "page:testframe" && contentWindowName !== "ds-tempWindowId";
    if (new Date().getTime() - startTime >= 20000 || waitingDone) {
      clearInterval(interval);
      assert.ok(waitingDone);
      done();
    }
  }, 50);
});

/**
 * Possibility to check if a number is not the same but quite the same with a tolerance value.
 */
QUnit.assert.similar = function(actual, expected, tolerance, message) {
  QUnit.assert.ok(Math.abs(actual - expected) < tolerance,
      "actual=" + actual  + " expected=" + expected  + " tolerance=" + tolerance  + " " + message);
};
