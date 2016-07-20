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

QUnit.test("Simple Collapsible Box: show -> hide transition", function (assert) {
  assert.expect(2);
  var done = assert.async(2);
  var step = 1;

  $show = jQueryFrame("#page\\:mainForm\\:controller\\:show");
  $hide = jQueryFrame("#page\\:mainForm\\:controller\\:hide");
  $content = jQueryFrame("#page\\:mainForm\\:controller\\:content");

  $show.click();

  jQuery("#page\\:testframe").load(function () {
    if (step == 1) {
      $hide = jQueryFrame($hide.selector);
      $content = jQueryFrame($content.selector);

      assert.equal($content.length, 1);
      $hide.click();

      done();
    } else if (step == 2) {
      $content = jQueryFrame($content.selector);

      assert.equal($content.length, 0);

      done();
    }
    step++;
  });
});

QUnit.test("Simple Collapsible Box: hide -> show transition", function (assert) {
  assert.expect(2);
  var done = assert.async(2);
  var step = 1;

  $show = jQueryFrame("#page\\:mainForm\\:controller\\:show");
  $hide = jQueryFrame("#page\\:mainForm\\:controller\\:hide");
  $content = jQueryFrame("#page\\:mainForm\\:controller\\:content");

  $hide.click();

  jQuery("#page\\:testframe").load(function () {
    if (step == 1) {
      $show = jQueryFrame($show.selector);
      $content = jQueryFrame($content.selector);

      assert.equal($content.length, 0);
      $show.click();

      done();
    } else if (step == 2) {
      $content = jQueryFrame($content.selector);

      assert.equal($content.length, 1);

      done();
    }
    step++;
  });
});

QUnit.test("Full Server Request: open both boxes", function (assert) {
  assert.expect(4);
  var done = assert.async(2);
  var step = 1;

  $show1 = jQueryFrame("#page\\:mainForm\\:server\\:show1");
  $show2 = jQueryFrame("#page\\:mainForm\\:server\\:show2");
  $content1 = jQueryFrame("#page\\:mainForm\\:server\\:content1");
  $content2 = jQueryFrame("#page\\:mainForm\\:server\\:content2");
  content2Length = $content2.length;

  $show1.click();

  jQuery("#page\\:testframe").load(function () {
    if (step == 1) {
      $show2 = jQueryFrame($show2.selector);
      $content1 = jQueryFrame($content1.selector);
      $content2 = jQueryFrame($content2.selector);

      assert.equal($content1.length, 1);
      assert.equal($content2.length, content2Length);
      $show2.click();

      done();
    } else if (step == 2) {
      $content1 = jQueryFrame($content1.selector);
      $content2 = jQueryFrame($content2.selector);

      assert.equal($content1.length, 1);
      assert.equal($content2.length, 1);

      done();
    }
    step++;
  });
});

QUnit.test("Full Server Request: open box 1, close box 2", function (assert) {
  assert.expect(4);
  var done = assert.async(2);
  var step = 1;

  $show1 = jQueryFrame("#page\\:mainForm\\:server\\:show1");
  $hide2 = jQueryFrame("#page\\:mainForm\\:server\\:hide2");
  $content1 = jQueryFrame("#page\\:mainForm\\:server\\:content1");
  $content2 = jQueryFrame("#page\\:mainForm\\:server\\:content2");
  content2Length = $content2.length;

  $show1.click();

  jQuery("#page\\:testframe").load(function () {
    if (step == 1) {
      $hide2 = jQueryFrame($hide2.selector);
      $content1 = jQueryFrame($content1.selector);
      $content2 = jQueryFrame($content2.selector);

      assert.equal($content1.length, 1);
      assert.equal($content2.length, content2Length);
      $hide2.click();

      done();
    } else if (step == 2) {
      $content1 = jQueryFrame($content1.selector);
      $content2 = jQueryFrame($content2.selector);

      assert.equal($content1.length, 1);
      assert.equal($content2.length, 0);

      done();
    }
    step++;
  });
});

QUnit.test("Full Server Request: close box 1, open box 2", function (assert) {
  assert.expect(4);
  var done = assert.async(2);
  var step = 1;

  $hide1 = jQueryFrame("#page\\:mainForm\\:server\\:hide1");
  $show2 = jQueryFrame("#page\\:mainForm\\:server\\:show2");
  $content1 = jQueryFrame("#page\\:mainForm\\:server\\:content1");
  $content2 = jQueryFrame("#page\\:mainForm\\:server\\:content2");
  content2Length = $content2.length;

  $hide1.click();

  jQuery("#page\\:testframe").load(function () {
    if (step == 1) {
      $show2 = jQueryFrame($show2.selector);
      $content1 = jQueryFrame($content1.selector);
      $content2 = jQueryFrame($content2.selector);

      assert.equal($content1.length, 0);
      assert.equal($content2.length, content2Length);
      $show2.click();

      done();
    } else if (step == 2) {
      $content1 = jQueryFrame($content1.selector);
      $content2 = jQueryFrame($content2.selector);

      assert.equal($content1.length, 0);
      assert.equal($content2.length, 1);

      done();
    }
    step++;
  });
});

QUnit.test("Full Server Request: close both boxes", function (assert) {
  assert.expect(4);
  var done = assert.async(2);
  var step = 1;

  $hide1 = jQueryFrame("#page\\:mainForm\\:server\\:hide1");
  $hide2 = jQueryFrame("#page\\:mainForm\\:server\\:hide2");
  $content1 = jQueryFrame("#page\\:mainForm\\:server\\:content1");
  $content2 = jQueryFrame("#page\\:mainForm\\:server\\:content2");
  content2Length = $content2.length;

  $hide1.click();

  jQuery("#page\\:testframe").load(function () {
    if (step == 1) {
      $hide2 = jQueryFrame($hide2.selector);
      $content1 = jQueryFrame($content1.selector);
      $content2 = jQueryFrame($content2.selector);

      assert.equal($content1.length, 0);
      assert.equal($content2.length, content2Length);
      $hide2.click();

      done();
    } else if (step == 2) {
      $content1 = jQueryFrame($content1.selector);
      $content2 = jQueryFrame($content2.selector);

      assert.equal($content1.length, 0);
      assert.equal($content2.length, 0);

      done();
    }
    step++;
  });
});

QUnit.test("Client Sided: show -> hide transition", function (assert) {
  assert.expect(2);

  $show = jQueryFrame("#page\\:mainForm\\:client\\:show");
  $hide = jQueryFrame("#page\\:mainForm\\:client\\:hide");
  $box = jQueryFrame("#page\\:mainForm\\:client\\:noRequestBox");

  $show.click();
  assert.equal($box.hasClass("tobago-collapsed"), false);

  $hide.click();
  assert.equal($box.hasClass("tobago-collapsed"), true);
});

QUnit.test("Client Sided: hide -> show transition", function (assert) {
  assert.expect(2);

  $show = jQueryFrame("#page\\:mainForm\\:client\\:show");
  $hide = jQueryFrame("#page\\:mainForm\\:client\\:hide");
  $box = jQueryFrame("#page\\:mainForm\\:client\\:noRequestBox");

  $hide.click();
  assert.equal($box.hasClass("tobago-collapsed"), true);

  $show.click();
  assert.equal($box.hasClass("tobago-collapsed"), false);
});

QUnit.test("Client Sided: hide content and submit empty string", function (assert) {
  assert.expect(2);
  var done = assert.async();

  $messages = jQueryFrame("#page\\:messages .tobago-messages");
  $show = jQueryFrame("#page\\:mainForm\\:client\\:show");
  $hide = jQueryFrame("#page\\:mainForm\\:client\\:hide");
  $box = jQueryFrame("#page\\:mainForm\\:client\\:noRequestBox");
  $in = jQueryFrame("#page\\:mainForm\\:client\\:in\\:\\:field");
  $submit = jQueryFrame("#page\\:mainForm\\:client\\:submit");

  $hide.click();
  assert.equal($box.hasClass("tobago-collapsed"), true);

  $in.val("");
  $submit.click();

  jQuery("#page\\:testframe").load(function () {
    $messages = jQueryFrame($messages.selector);
    assert.equal($messages.length, 1);
    done();
  });
});

QUnit.test("Ajax: show -> hide transition", function (assert) {
  assert.expect(2);
  var done = assert.async(2);

  $show = jQueryFrame("#page\\:mainForm\\:ajax\\:show");
  $hide = jQueryFrame("#page\\:mainForm\\:ajax\\:hide");
  $in = jQueryFrame("#page\\:mainForm\\:ajax\\:in\\:\\:field");

  $show.click();

  $.ajax({
    type: 'GET',
    url: 'content/30-concept/53-collapsible/00-collapsible-box/collapsible-box.xhtml'
  }).done(function () {
    $hide = jQueryFrame($hide.selector);
    $in = jQueryFrame($in.selector);

    assert.equal($in.length, 1);
    $hide.click();

    $.ajax({
      type: 'GET',
      url: 'content/30-concept/53-collapsible/00-collapsible-box/collapsible-box.xhtml'
    }).done(function () {
      $in = jQueryFrame($in.selector);
      assert.equal($in.length, 0);
      done();
    });

    done();
  });
});

QUnit.test("Ajax: hide -> show transition", function (assert) {
  assert.expect(2);
  var done = assert.async(2);

  $show = jQueryFrame("#page\\:mainForm\\:ajax\\:show");
  $hide = jQueryFrame("#page\\:mainForm\\:ajax\\:hide");
  $in = jQueryFrame("#page\\:mainForm\\:ajax\\:in\\:\\:field");

  $hide.click();

  $.ajax({
    type: 'GET',
    url: 'content/30-concept/53-collapsible/00-collapsible-box/collapsible-box.xhtml'
  }).done(function () {
    $show = jQueryFrame($show.selector);
    $in = jQueryFrame($in.selector);

    assert.equal($in.length, 0);
    $show.click();

    $.ajax({
      type: 'GET',
      url: 'content/30-concept/53-collapsible/00-collapsible-box/collapsible-box.xhtml'
    }).done(function () {
      $in = jQueryFrame($in.selector);
      assert.equal($in.length, 1);
      done();
    });

    done();
  });
});

QUnit.test("Client Sided: hide content and submit empty string", function (assert) {
  assert.expect(3);
  var done = assert.async(3);

  $messages = jQueryFrame("#page\\:messages .tobago-messages");
  $show = jQueryFrame("#page\\:mainForm\\:ajax\\:show");
  $hide = jQueryFrame("#page\\:mainForm\\:ajax\\:hide");
  $in = jQueryFrame("#page\\:mainForm\\:ajax\\:in\\:\\:field");
  $submit = jQueryFrame("#page\\:mainForm\\:ajax\\:submit");

  $show.click();

  $.ajax({
    type: 'GET',
    url: 'content/30-concept/53-collapsible/00-collapsible-box/collapsible-box.xhtml'
  }).done(function () {
    $hide = jQueryFrame($hide.selector);
    $in = jQueryFrame($in.selector);

    assert.equal($in.length, 1);
    $in.val("");
    $hide.click();

    $.ajax({
      type: 'GET',
      url: 'content/30-concept/53-collapsible/00-collapsible-box/collapsible-box.xhtml'
    }).done(function () {
      $in = jQueryFrame($in.selector);
      $submit = jQueryFrame($submit.selector);

      assert.equal($in.length, 0);
      $submit.click();
      done();
    });

    done();
  });

  jQuery("#page\\:testframe").load(function () {
    $messages = jQueryFrame($messages.selector);
    assert.equal($messages.length, 0);
    done();
  });
});
