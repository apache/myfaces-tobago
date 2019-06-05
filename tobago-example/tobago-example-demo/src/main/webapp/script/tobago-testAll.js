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

Listener.register(function () {

  var prefix = "page:tp";

  jQuery("iframe").each(function () {
    jQuery(this).on("load", function () {

      var counter = Number(jQuery(this).attr("id").substring(prefix.length));

      waitForTest(function () {
        var $thisFrame = jQuery(DomUtils.escapeClientId(prefix + counter));
        var $banner = $thisFrame.contents().find("#qunit-banner");
        return $banner.length > 0
            && $banner.attr("class") !== "";
      }, function () {
        var $nextFrame = jQuery(DomUtils.escapeClientId(prefix + (counter + 1)));
        runNextFrame($nextFrame);
      });
    });
  });

  var $firstFrame = jQuery(DomUtils.escapeClientId(prefix + 1));
  runNextFrame($firstFrame);
}, Phase.DOCUMENT_READY);

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

function runNextFrame($nextFrame) {
  var url = $nextFrame.attr("name");
  if (url) {
    $nextFrame.removeAttr("name");
    $nextFrame.attr("src", url);
  }
}
