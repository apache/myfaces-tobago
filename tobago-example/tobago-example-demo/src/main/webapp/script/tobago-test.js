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

/**
 * Wait for ajax requests. Can be used with PhantomJs.
 * @param waitingDone return false if still waiting, true if waiting done
 * @param executeWhenDone is executed after waiting
 */
function waitForAjax(waitingDone, executeWhenDone) {
  var startTime = new Date().getTime();
  var maxWait = 20000;
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
