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

QUnit.test("ajax excecute", function (assert) {
  assert.expect(12);
  var done = assert.async(3);
  var step = 1;

  var in1Selector = "#page\\:mainForm\\:in1\\:\\:field";
  var in2Selector = "#page\\:mainForm\\:in2\\:\\:field";
  var in3Selector = "#page\\:mainForm\\:in3\\:\\:field";
  var in4Selector = "#page\\:mainForm\\:in4\\:\\:field";
  var clearButtonSelector = "#page\\:mainForm\\:clear";
  var submitButtonSelector = "#page\\:mainForm\\:submit";
  var reloadButtonSelector = "#page\\:mainForm\\:reload";
  var $in1 = jQueryFrame(in1Selector);
  var $in2 = jQueryFrame(in2Selector);
  var $in3 = jQueryFrame(in3Selector);
  var $in4 = jQueryFrame(in4Selector);
  var $clearButton = jQueryFrame(clearButtonSelector);
  var $submitButton = jQueryFrame(submitButtonSelector);
  var $reloadButton = jQueryFrame(reloadButtonSelector);

  $in1.val("a");
  $in2.val("b");
  $in3.val("c");
  $in4.val("d");
  $clearButton.click();

  jQuery("#page\\:testframe").on("load", function () {
    if (step === 1) {
      $in1 = jQueryFrame(in1Selector);
      $in2 = jQueryFrame(in2Selector);
      $in3 = jQueryFrame(in3Selector);
      $in4 = jQueryFrame(in4Selector);
      $submitButton = jQueryFrame(submitButtonSelector);

      assert.equal($in1.val(), "");
      assert.equal($in2.val(), "");
      assert.equal($in3.val(), "");
      assert.equal($in4.val(), "");

      $in1.val("a");
      $in2.val("b");
      $in3.val("c");
      $in4.val("d");

      $submitButton.click();

      step++;
      done();

      waitForAjax(function () {
        $in1 = jQueryFrame(in1Selector);
        $in2 = jQueryFrame(in2Selector);
        $in3 = jQueryFrame(in3Selector);
        $in4 = jQueryFrame(in4Selector);
        return step === 2
            && $in1.val() === "a"
            && $in2.val() === "b"
            && $in3.val() === "c"
            && $in4.val() === "";
      }, function () {
        $in1 = jQueryFrame(in1Selector);
        $in2 = jQueryFrame(in2Selector);
        $in3 = jQueryFrame(in3Selector);
        $in4 = jQueryFrame(in4Selector);
        $reloadButton = jQueryFrame(reloadButtonSelector);

        assert.equal($in1.val(), "a");
        assert.equal($in2.val(), "b");
        assert.equal($in3.val(), "c");
        assert.equal($in4.val(), "");

        $reloadButton.click();

        step++;
        done();
      });
    } else if (step === 3) {
      $in1 = jQueryFrame(in1Selector);
      $in2 = jQueryFrame(in2Selector);
      $in3 = jQueryFrame(in3Selector);
      $in4 = jQueryFrame(in4Selector);

      assert.equal($in1.val(), "a");
      assert.equal($in2.val(), "");
      assert.equal($in3.val(), "c");
      assert.equal($in4.val(), "");

      done();
    }
  });
});
