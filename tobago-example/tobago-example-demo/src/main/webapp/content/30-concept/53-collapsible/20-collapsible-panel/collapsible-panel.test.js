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

QUnit.test("Simple Panel", function (assert) {
  assert.expect(14);
  var done = assert.async(5);
  var step = 1;

  var m = "#page\\:messages .tobago-messages";
  var $messages = jQueryFrame(m);
  var $show = jQueryFrame("#\\page\\:mainForm\\:simple\\:showSimple");
  var h = "#\\page\\:mainForm\\:simple\\:hideSimple";
  var $hide = jQueryFrame(h);
  var s = "#\\page\\:mainForm\\:simple\\:submitSimple";
  var $submit = jQueryFrame(s);
  var pc = "#page\\:mainForm\\:simple\\:simplePanel\\:\\:collapse";
  var $panelCollapsed = jQueryFrame(pc);
  var i = "#page\\:mainForm\\:simple\\:inSimple\\:\\:field";
  var $in = jQueryFrame(i);

  $show.click();

  jQuery("#page\\:testframe").on("load", function () {
    if (step === 1) {
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);
      $submit = jQueryFrame(s);

      assert.equal($panelCollapsed.val(), "false");
      assert.equal($in.length, 1);
      $in.val("some text");
      $submit.click();

      step++;
      done();
    } else if (step === 2) {
      $messages = jQueryFrame(m);
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);
      $submit = jQueryFrame(s);

      assert.equal($messages.length, 0);
      assert.equal($panelCollapsed.val(), "false");
      assert.equal($in.length, 1);
      $in.val("");
      $submit.click();

      step++;
      done();
    } else if (step === 3) {
      $messages = jQueryFrame(m);
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);
      $hide = jQueryFrame(h);

      assert.equal($messages.length, 1);
      assert.equal($panelCollapsed.val(), "false");
      assert.equal($in.length, 1);
      $hide.click();

      step++;
      done();
    } else if (step === 4) {
      $messages = jQueryFrame(m);
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);
      $submit = jQueryFrame(s);

      assert.equal($messages.length, 0);
      assert.equal($panelCollapsed.val(), "true");
      assert.equal($in.length, 0);
      $submit.click();

      step++;
      done();
    } else if (step === 5) {
      $messages = jQueryFrame(m);
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);

      assert.equal($messages.length, 0);
      assert.equal($panelCollapsed.val(), "true");
      assert.equal($in.length, 0);

      done();
    }
  });
});

QUnit.test("Full Server Request", function (assert) {
  assert.expect(14);
  var done = assert.async(5);
  var step = 1;

  var m = "#page\\:messages .tobago-messages";
  var $messages = jQueryFrame(m);
  var $show = jQueryFrame("#\\page\\:mainForm\\:server\\:showServer");
  var h = "#\\page\\:mainForm\\:server\\:hideServer";
  var $hide = jQueryFrame(h);
  var s = "#\\page\\:mainForm\\:server\\:submitServer";
  var $submit = jQueryFrame(s);
  var pc = "#page\\:mainForm\\:server\\:serverPanel\\:\\:collapse";
  var $panelCollapsed = jQueryFrame(pc);
  var i = "#page\\:mainForm\\:server\\:inServer\\:\\:field";
  var $in = jQueryFrame(i);

  $show.click();

  jQuery("#page\\:testframe").on("load", function () {
    if (step === 1) {
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);
      $submit = jQueryFrame(s);

      assert.equal($panelCollapsed.val(), "false");
      assert.equal($in.length, 1);
      $in.val("some text");
      $submit.click();

      step++;
      done();
    } else if (step === 2) {
      $messages = jQueryFrame(m);
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);
      $submit = jQueryFrame(s);

      assert.equal($messages.length, 0);
      assert.equal($panelCollapsed.val(), "false");
      assert.equal($in.length, 1);
      $in.val("");
      $submit.click();

      step++;
      done();
    } else if (step === 3) {
      $messages = jQueryFrame(m);
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);
      $hide = jQueryFrame(h);

      assert.equal($messages.length, 1);
      assert.equal($panelCollapsed.val(), "false");
      assert.equal($in.length, 1);
      $hide.click();

      step++;
      done();
    } else if (step === 4) {
      $messages = jQueryFrame(m);
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);
      $submit = jQueryFrame(s);

      assert.equal($messages.length, 0);
      assert.equal($panelCollapsed.val(), "true");
      assert.equal($in.length, 0);
      $submit.click();

      step++;
      done();
    } else if (step === 5) {
      $messages = jQueryFrame(m);
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);

      assert.equal($messages.length, 0);
      assert.equal($panelCollapsed.val(), "true");
      assert.equal($in.length, 0);

      done();
    }
  });
});

QUnit.test("Client Sided", function (assert) {
  assert.expect(14);
  var done = assert.async(3);
  var step = 1;

  var m = "#page\\:messages .tobago-messages";
  var $messages = jQueryFrame(m);
  var $show = jQueryFrame("#\\page\\:mainForm\\:client\\:showClient");
  var h = "#\\page\\:mainForm\\:client\\:hideClient";
  var $hide = jQueryFrame(h);
  var s = "#\\page\\:mainForm\\:client\\:submitClient";
  var $submit = jQueryFrame(s);
  var pc = "#page\\:mainForm\\:client\\:clientPanel\\:\\:collapse";
  var $panelCollapsed = jQueryFrame(pc);
  var i = "#page\\:mainForm\\:client\\:inClient\\:\\:field";
  var $in = jQueryFrame(i);

  $show.click();
  assert.equal($panelCollapsed.val(), "false");
  assert.equal($in.length, 1);
  $in.val("some text");
  $submit.click();

  jQuery("#page\\:testframe").on("load", function () {
    if (step === 1) {
      $messages = jQueryFrame(m);
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);
      $submit = jQueryFrame(s);

      assert.equal($messages.length, 0);
      assert.equal($panelCollapsed.val(), "false");
      assert.equal($in.length, 1);
      $in.val("");
      $submit.click();

      step++;
      done();
    } else if (step === 2) {
      $messages = jQueryFrame(m);
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);
      $hide = jQueryFrame(h);
      $submit = jQueryFrame(s);

      assert.equal($messages.length, 1);
      assert.equal($panelCollapsed.val(), "false");
      assert.equal($in.length, 1);
      $hide.click();

      assert.equal($messages.length, 1);
      assert.equal($panelCollapsed.val(), "true");
      assert.equal($in.length, 1);
      $submit.click();

      step++;
      done();
    } else if (step === 3) {
      $messages = jQueryFrame(m);
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);

      assert.equal($messages.length, 1);
      assert.equal($panelCollapsed.val(), "false");
      assert.equal($in.length, 1);

      done();
    }
  });
});

QUnit.test("Ajax", function (assert) {
  assert.expect(13);
  var done = assert.async(5);
  var step = 1;

  var m = "#page\\:messages .tobago-messages";
  var $messages = jQueryFrame(m);
  var $show = jQueryFrame("#\\page\\:mainForm\\:ajax\\:showAjax");
  var h = "#\\page\\:mainForm\\:ajax\\:hideAjax";
  var $hide = jQueryFrame(h);
  var s = "#\\page\\:mainForm\\:ajax\\:submitAjax";
  var $submit = jQueryFrame(s);
  var pc = "#page\\:mainForm\\:ajax\\:ajaxPanel\\:\\:collapse";
  var $panelCollapsed = jQueryFrame(pc);
  var i = "#page\\:mainForm\\:ajax\\:inAjax\\:\\:field";
  var $in = jQueryFrame(i);

  $show.click();

  waitForAjax(function () {
    $panelCollapsed = jQueryFrame(pc);
    $in = jQueryFrame(i);
    return step === 1
        && $panelCollapsed.val() === "false"
        && $in.length === 1;
  }, function () {
    $panelCollapsed = jQueryFrame(pc);
    $in = jQueryFrame(i);
    $submit = jQueryFrame(s);

    assert.equal($panelCollapsed.val(), "false");
    assert.equal($in.length, 1);
    $in.val("some text");
    $submit.click();

    step++;
    done();
  });

  jQuery("#page\\:testframe").on("load", function () {
    if (step === 2) {
      $messages = jQueryFrame(m);
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);
      $submit = jQueryFrame(s);

      assert.equal($messages.length, 0);
      assert.equal($panelCollapsed.val(), "false");
      assert.equal($in.length, 1);
      $in.val("");
      $submit.click();

      step++;
      done();
    } else if (step === 3) {
      $messages = jQueryFrame(m);
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);
      $hide = jQueryFrame(h);

      assert.equal($messages.length, 1);
      assert.equal($panelCollapsed.val(), "false");
      assert.equal($in.length, 1);
      $hide.click();

      step++;
      done();

      waitForAjax(function () {
        $panelCollapsed = jQueryFrame(pc);
        $in = jQueryFrame(i);
        return step === 4
            && $panelCollapsed.val() === "true"
            && $in.length === 0;
      }, function () {
        $panelCollapsed = jQueryFrame(pc);
        $in = jQueryFrame(i);
        $submit = jQueryFrame(s);

        assert.equal($panelCollapsed.val(), "true");
        assert.equal($in.length, 0);
        $submit.click();

        step++;
        done();
      });
    } else if (step === 5) {
      $messages = jQueryFrame(m);
      $panelCollapsed = jQueryFrame(pc);
      $in = jQueryFrame(i);

      assert.equal($messages.length, 0);
      assert.equal($panelCollapsed.val(), "true");
      assert.equal($in.length, 0);

      done();
    }
  });
});
