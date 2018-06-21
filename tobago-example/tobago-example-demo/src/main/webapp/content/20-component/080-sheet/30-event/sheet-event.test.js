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

Selectors = {};

Selectors.oneClickAjax = "#page\\:mainForm\\:changeExample\\:\\:0";
Selectors.oneClickFullRequest = "#page\\:mainForm\\:changeExample\\:\\:1";
Selectors.doubleClickFullRequest = "#page\\:mainForm\\:changeExample\\:\\:2";
Selectors.venus0 = "#page\\:mainForm\\:s1\\:2\\:sample0";
Selectors.venus1 = "#page\\:mainForm\\:s1\\:2\\:sample1";
Selectors.venus2 = "#page\\:mainForm\\:s1\\:2\\:sample2";
Selectors.jupiter0 = "#page\\:mainForm\\:s1\\:5\\:sample0";
Selectors.jupiter1 = "#page\\:mainForm\\:s1\\:5\\:sample1";
Selectors.jupiter2 = "#page\\:mainForm\\:s1\\:5\\:sample2";
Selectors.saturn0 = "#page\\:mainForm\\:s1\\:6\\:sample0";
Selectors.saturn1 = "#page\\:mainForm\\:s1\\:6\\:sample1";
Selectors.saturn2 = "#page\\:mainForm\\:s1\\:6\\:sample2";
Selectors.namefield = "#page\\:mainForm\\:name\\:\\:field";

QUnit.test("On click with ajax", function(assert) {
  assert.expect(6);
  var done = assert.async(4);

  var $oneClickAjax = jQueryFrame(Selectors.oneClickAjax);
  var $venus = jQueryFrame(Selectors.venus0);
  var $jupiter = jQueryFrame(Selectors.jupiter0);
  var $saturn = jQueryFrame(Selectors.saturn0);
  var $namefield = jQueryFrame(Selectors.namefield);

  $oneClickAjax.click();

  waitForAjax(function() {
    $venus = jQueryFrame(Selectors.venus0);
    $jupiter = jQueryFrame(Selectors.jupiter0);
    $saturn = jQueryFrame(Selectors.saturn0);
    return $venus.length === 1 && $jupiter.length === 1 && $saturn.length === 1;
  }, function() {
    $venus = jQueryFrame(Selectors.venus0);
    $jupiter = jQueryFrame(Selectors.jupiter0);
    $saturn = jQueryFrame(Selectors.saturn0);
    assert.equal($venus.length, 1);
    assert.equal($jupiter.length, 1);
    assert.equal($saturn.length, 1);
    done();

    $venus.click();

    waitForAjax(function() {
      $namefield = jQueryFrame(Selectors.namefield);
      return $namefield.val() === "Venus";
    }, function() {
      $namefield = jQueryFrame(Selectors.namefield);
      assert.equal($namefield.val(), "Venus");
      done();

      $jupiter.click();

      waitForAjax(function() {
        $namefield = jQueryFrame(Selectors.namefield);
        return $namefield.val() === "Jupiter";
      }, function() {
        $namefield = jQueryFrame(Selectors.namefield);
        assert.equal($namefield.val(), "Jupiter");
        done();

        $saturn.click();

        waitForAjax(function() {
          $namefield = jQueryFrame(Selectors.namefield);
          return $namefield.val() === "Saturn";
        }, function() {
          $namefield = jQueryFrame(Selectors.namefield);
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

  var $oneClickFullRequest = jQueryFrame(Selectors.oneClickFullRequest);
  var $venus = jQueryFrame(Selectors.venus1);
  var $jupiter = jQueryFrame(Selectors.jupiter1);
  var $saturn = jQueryFrame(Selectors.saturn1);
  var $namefield = jQueryFrame(Selectors.namefield);

  $oneClickFullRequest.click();

  waitForAjax(function() {
    $venus = jQueryFrame(Selectors.venus1);
    $jupiter = jQueryFrame(Selectors.jupiter1);
    $saturn = jQueryFrame(Selectors.saturn1);
    return $venus.length === 1 && $jupiter.length === 1 && $saturn.length === 1;
  }, function() {
    if (step === 1) {
      $venus = jQueryFrame(Selectors.venus1);
      $jupiter = jQueryFrame(Selectors.jupiter1);
      $saturn = jQueryFrame(Selectors.saturn1);
      assert.equal($venus.length, 1);
      assert.equal($jupiter.length, 1);
      assert.equal($saturn.length, 1);

      $venus.click();
    }

    done();
    step++;
  });

  jQuery("#page\\:testframe").on("load", function() {
    if (step === 2) {
      $namefield = jQueryFrame(Selectors.namefield);
      assert.equal($namefield.val(), "Venus");

      $jupiter = jQueryFrame(Selectors.jupiter1);
      $jupiter.click();
    } else if (step === 3) {
      $namefield = jQueryFrame(Selectors.namefield);
      assert.equal($namefield.val(), "Jupiter");

      $saturn = jQueryFrame(Selectors.saturn1);
      $saturn.click();
    } else if (step === 4) {
      $namefield = jQueryFrame(Selectors.namefield);
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

  var $doubleClickFullRequest = jQueryFrame(Selectors.doubleClickFullRequest);
  var $venus = jQueryFrame(Selectors.venus2);
  var $jupiter = jQueryFrame(Selectors.jupiter2);
  var $saturn = jQueryFrame(Selectors.saturn2);
  var $namefield = jQueryFrame(Selectors.namefield);

  $doubleClickFullRequest.click();

  waitForAjax(function() {
    $venus = jQueryFrame(Selectors.venus2);
    $jupiter = jQueryFrame(Selectors.jupiter2);
    $saturn = jQueryFrame(Selectors.saturn2);
    return $venus.length === 1 && $jupiter.length === 1 && $saturn.length === 1;
  }, function() {
    if (step === 1) {
      $venus = jQueryFrame(Selectors.venus2);
      $jupiter = jQueryFrame(Selectors.jupiter2);
      $saturn = jQueryFrame(Selectors.saturn2);
      assert.equal($venus.length, 1);
      assert.equal($jupiter.length, 1);
      assert.equal($saturn.length, 1);

      $venus.dblclick();
    }

    done();
    step++;
  });

  jQuery("#page\\:testframe").on("load", function() {
    if (step === 2) {
      $namefield = jQueryFrame(Selectors.namefield);
      assert.equal($namefield.val(), "Venus");

      $jupiter = jQueryFrame(Selectors.jupiter2);
      $jupiter.dblclick();
    } else if (step === 3) {
      $namefield = jQueryFrame(Selectors.namefield);
      assert.equal($namefield.val(), "Jupiter");

      $saturn = jQueryFrame(Selectors.saturn2);
      $saturn.dblclick();
    } else if (step === 4) {
      $namefield = jQueryFrame(Selectors.namefield);
      assert.equal($namefield.val(), "Saturn");
    }

    step++;
    done();
  });
});
