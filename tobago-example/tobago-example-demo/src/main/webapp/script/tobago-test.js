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

function testFrameQuerySelectorFn(expression) {
  return function () {
    return document.getElementById("page:testframe").contentWindow.document.querySelector(expression);
  }
}

function testFrameQuerySelectorAllFn(expression) {
  return function () {
    return document.getElementById("page:testframe").contentWindow.document.querySelectorAll(expression);
  }
}

export {testFrameQuerySelectorFn, testFrameQuerySelectorAllFn};

QUnit.test("wait for test", function (assert) {
  let done = assert.async();

  let startTime = new Date().getTime();
  let contentWindowName = "";
  let waitingDone = false;
  let interval = setInterval(function () {
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
    let duplicatedIDs = [];
    let iFrame = document.getElementById("page:testframe").contentWindow.document.querySelectorAll("[id]");
    iFrame.forEach(element => {
      let sameIdElements = document.getElementById("page:testframe").contentWindow.document
          .querySelectorAll("[id='" + element.id + "']");
      if (sameIdElements.length > 1) {
        duplicatedIDs.push(element.id);
      }
    });
    return duplicatedIDs;
  }

  let duplicatedIDs = getDuplicatedIDs();
  assert.equal(duplicatedIDs.length, 0, "duplicated IDs are: " + duplicatedIDs);
});

QUnit.test("test '???'", function (assert) {
  assert.ok(testFrameQuerySelectorFn("html")().textContent.indexOf("???") <= -1,
      "There must no '???' on the site.");
});
