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

// todo: migrate to typescript

function testAll() {
  let count = 1;
  const maxCount = document.querySelectorAll(".testframe-wrapper").length;

  function waitForTest(waitingDone, executeWhenDone) {
    var stillWaiting = true;
    var interval = setInterval(function () {
      if (stillWaiting) {
        stillWaiting = !waitingDone();
      } else {
        executeWhenDone();
        clearInterval(interval);
      }
    }, 500);
  }

  function cycle() {
    const iframe = document.getElementById("page:tp" + count);
    const url = iframe.getAttribute("name");
    iframe.setAttribute("src", url);

    const tpWindow = document.getElementById("page:tp" + count).contentWindow;

    const timeout = Date.now() + 30000;

    waitForTest(function () {
      const banner = tpWindow.document.querySelector(".jasmine-overall-result");
      return banner !== null || Date.now() > timeout;
    }, function () {
      if (count < maxCount) {
        count++;
        tpWindow.scrollTo(0, Number.MAX_SAFE_INTEGER)
        cycle();
      }
    });
  }

  cycle();
}

document.addEventListener("DOMContentLoaded", function (event) {
  testAll();
});
