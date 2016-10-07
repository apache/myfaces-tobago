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

QUnit.test("On click with ajax", function(assert) {
  assert.expect(6);
  var done = assert.async(4);

  $oneClickAjax = jQueryFrame("#page\\:mainForm\\:changeExample\\:\\:0");
  $venus = jQueryFrame("#page\\:mainForm\\:s1\\:2\\:columnEventAjax");
  $jupiter = jQueryFrame("#page\\:mainForm\\:s1\\:5\\:columnEventAjax");
  $saturn = jQueryFrame("#page\\:mainForm\\:s1\\:6\\:columnEventAjax");
  $namefield = jQueryFrame("#page\\:mainForm\\:name\\:\\:field");

  $oneClickAjax.click();

  waitForAjax(function() {
    $venus = jQueryFrame($venus.selector);
    $jupiter = jQueryFrame($jupiter.selector);
    $saturn = jQueryFrame($saturn.selector);
    return $venus.length == 1 && $jupiter.length == 1 && $saturn.length == 1;
  }, function() {
    $venus = jQueryFrame($venus.selector);
    $jupiter = jQueryFrame($jupiter.selector);
    $saturn = jQueryFrame($saturn.selector);
    assert.equal($venus.length, 1);
    assert.equal($jupiter.length, 1);
    assert.equal($saturn.length, 1);
    done();

    $venus.click();

    waitForAjax(function() {
      $namefield = jQueryFrame($namefield.selector);
      return $namefield.val() == "Venus";
    }, function() {
      $namefield = jQueryFrame($namefield.selector);
      assert.equal($namefield.val(), "Venus");
      done();

      $jupiter.click();

      waitForAjax(function() {
        $namefield = jQueryFrame($namefield.selector);
        return $namefield.val() == "Jupiter";
      }, function() {
        $namefield = jQueryFrame($namefield.selector);
        assert.equal($namefield.val(), "Jupiter");
        done();

        $saturn.click();

        waitForAjax(function() {
          $namefield = jQueryFrame($namefield.selector);
          return $namefield.val() == "Saturn";
        }, function() {
          $namefield = jQueryFrame($namefield.selector);
          assert.equal($namefield.val(), "Saturn");
          done();
        });
      });
    });
  });
});

QUnit.test("On click with full request", function(assert) {
  assert.expect(6);
  var done = assert.async(4);
  var step = 1;

  $oneClickFullRequest = jQueryFrame("#page\\:mainForm\\:changeExample\\:\\:1");
  $venus = jQueryFrame("#page\\:mainForm\\:s1\\:2\\:columnEventClick");
  $jupiter = jQueryFrame("#page\\:mainForm\\:s1\\:5\\:columnEventClick");
  $saturn = jQueryFrame("#page\\:mainForm\\:s1\\:6\\:columnEventClick");
  $namefield = jQueryFrame("#page\\:mainForm\\:name\\:\\:field");

  $oneClickFullRequest.click();

  waitForAjax(function() {
    $venus = jQueryFrame($venus.selector);
    $jupiter = jQueryFrame($jupiter.selector);
    $saturn = jQueryFrame($saturn.selector);
    return $venus.length == 1 && $jupiter.length == 1 && $saturn.length == 1;
  }, function() {
    if (step == 1) {
      $venus = jQueryFrame($venus.selector);
      $jupiter = jQueryFrame($jupiter.selector);
      $saturn = jQueryFrame($saturn.selector);
      assert.equal($venus.length, 1);
      assert.equal($jupiter.length, 1);
      assert.equal($saturn.length, 1);

      $venus.click();
    }

    done();
    step++;
  });

  jQuery("#page\\:testframe").load(function() {
    if (step == 2) {
      $namefield = jQueryFrame($namefield.selector);
      assert.equal($namefield.val(), "Venus");

      $jupiter = jQueryFrame($jupiter.selector);
      $jupiter.click();
    } else if (step == 3) {
      $namefield = jQueryFrame($namefield.selector);
      assert.equal($namefield.val(), "Jupiter");

      $saturn = jQueryFrame($saturn.selector);
      $saturn.click();
    } else if (step == 4) {
      $namefield = jQueryFrame($namefield.selector);
      assert.equal($namefield.val(), "Saturn");
    }

    step++;
    done();
  });
});

QUnit.test("On double click with full request", function(assert) {
  assert.expect(6);
  var done = assert.async(4);
  var step = 1;

  $doubleClickFullRequest = jQueryFrame("#page\\:mainForm\\:changeExample\\:\\:2");
  $venus = jQueryFrame("#page\\:mainForm\\:s1\\:2\\:columnEventDblClick");
  $jupiter = jQueryFrame("#page\\:mainForm\\:s1\\:5\\:columnEventDblClick");
  $saturn = jQueryFrame("#page\\:mainForm\\:s1\\:6\\:columnEventDblClick");
  $namefield = jQueryFrame("#page\\:mainForm\\:name\\:\\:field");

  $doubleClickFullRequest.click();

  waitForAjax(function() {
    $venus = jQueryFrame($venus.selector);
    $jupiter = jQueryFrame($jupiter.selector);
    $saturn = jQueryFrame($saturn.selector);
    return $venus.length == 1 && $jupiter.length == 1 && $saturn.length == 1;
  }, function() {
    if (step == 1) {
      $venus = jQueryFrame($venus.selector);
      $jupiter = jQueryFrame($jupiter.selector);
      $saturn = jQueryFrame($saturn.selector);
      assert.equal($venus.length, 1);
      assert.equal($jupiter.length, 1);
      assert.equal($saturn.length, 1);

      $venus.dblclick();
    }

    done();
    step++;
  });

  jQuery("#page\\:testframe").load(function() {
    if (step == 2) {
      $namefield = jQueryFrame($namefield.selector);
      assert.equal($namefield.val(), "Venus");

      $jupiter = jQueryFrame($jupiter.selector);
      $jupiter.dblclick();
    } else if (step == 3) {
      $namefield = jQueryFrame($namefield.selector);
      assert.equal($namefield.val(), "Jupiter");

      $saturn = jQueryFrame($saturn.selector);
      $saturn.dblclick();
    } else if (step == 4) {
      $namefield = jQueryFrame($namefield.selector);
      assert.equal($namefield.val(), "Saturn");
    }

    step++;
    done();
  });
});
