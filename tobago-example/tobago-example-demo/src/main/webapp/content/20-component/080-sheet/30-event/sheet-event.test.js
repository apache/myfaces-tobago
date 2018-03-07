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

QUnit.test("On click with ajax", function (assert) {
  assert.expect(6);
  var done = assert.async(4);

  var $oneClickAjax = jQueryFrame("#page\\:mainForm\\:changeExample\\:\\:0");
  var $venus = jQueryFrame("#page\\:mainForm\\:s1\\:2\\:sample0");
  var $jupiter = jQueryFrame("#page\\:mainForm\\:s1\\:5\\:sample0");
  var $saturn = jQueryFrame("#page\\:mainForm\\:s1\\:6\\:sample0");
  var $namefield = jQueryFrame("#page\\:mainForm\\:name\\:\\:field");

  $oneClickAjax.click();

  waitForAjax(function () {
    $venus = jQueryFrame($venus.selector);
    $jupiter = jQueryFrame($jupiter.selector);
    $saturn = jQueryFrame($saturn.selector);
    return $venus.length === 1 && $jupiter.length === 1 && $saturn.length === 1;
  }, function () {
    $venus = jQueryFrame($venus.selector);
    $jupiter = jQueryFrame($jupiter.selector);
    $saturn = jQueryFrame($saturn.selector);
    assert.equal($venus.length, 1);
    assert.equal($jupiter.length, 1);
    assert.equal($saturn.length, 1);
    done();

    $venus.click();

    waitForAjax(function () {
      $namefield = jQueryFrame($namefield.selector);
      return $namefield.val() === "Venus";
    }, function () {
      $namefield = jQueryFrame($namefield.selector);
      assert.equal($namefield.val(), "Venus");
      done();

      $jupiter.click();

      waitForAjax(function () {
        $namefield = jQueryFrame($namefield.selector);
        return $namefield.val() === "Jupiter";
      }, function () {
        $namefield = jQueryFrame($namefield.selector);
        assert.equal($namefield.val(), "Jupiter");
        done();

        $saturn.click();

        waitForAjax(function () {
          $namefield = jQueryFrame($namefield.selector);
          return $namefield.val() === "Saturn";
        }, function () {
          $namefield = jQueryFrame($namefield.selector);
          assert.equal($namefield.val(), "Saturn");
          done();
        });
      });
    });
  });
});

QUnit.test("On click with full request", function (assert) {
  assert.expect(6);
  var done = assert.async(4);
  var step = 1;

  var $oneClickFullRequest = jQueryFrame("#page\\:mainForm\\:changeExample\\:\\:1");
  var $venus = jQueryFrame("#page\\:mainForm\\:s1\\:2\\:sample1");
  var $jupiter = jQueryFrame("#page\\:mainForm\\:s1\\:5\\:sample1");
  var $saturn = jQueryFrame("#page\\:mainForm\\:s1\\:6\\:sample1");
  var $namefield = jQueryFrame("#page\\:mainForm\\:name\\:\\:field");

  $oneClickFullRequest.click();

  waitForAjax(function () {
    $venus = jQueryFrame($venus.selector);
    $jupiter = jQueryFrame($jupiter.selector);
    $saturn = jQueryFrame($saturn.selector);
    return $venus.length === 1 && $jupiter.length === 1 && $saturn.length === 1;
  }, function () {
    if (step === 1) {
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

  jQuery("#page\\:testframe").load(function () {
    if (step === 2) {
      $namefield = jQueryFrame($namefield.selector);
      assert.equal($namefield.val(), "Venus");

      $jupiter = jQueryFrame($jupiter.selector);
      $jupiter.click();

      step++;
      done();
    } else if (step === 3) {
      $namefield = jQueryFrame($namefield.selector);
      assert.equal($namefield.val(), "Jupiter");

      $saturn = jQueryFrame($saturn.selector);
      $saturn.click();

      step++;
      done();
    } else if (step === 4) {
      $namefield = jQueryFrame($namefield.selector);
      assert.equal($namefield.val(), "Saturn");

      step++;
      done();
    }
  });
});

QUnit.test("On double click with full request", function (assert) {
  assert.expect(6);
  var done = assert.async(4);
  var step = 1;

  var $doubleClickFullRequest = jQueryFrame("#page\\:mainForm\\:changeExample\\:\\:2");
  var $venus = jQueryFrame("#page\\:mainForm\\:s1\\:2\\:sample2");
  var $jupiter = jQueryFrame("#page\\:mainForm\\:s1\\:5\\:sample2");
  var $saturn = jQueryFrame("#page\\:mainForm\\:s1\\:6\\:sample2");
  var $namefield = jQueryFrame("#page\\:mainForm\\:name\\:\\:field");

  $doubleClickFullRequest.click();

  waitForAjax(function () {
    $venus = jQueryFrame($venus.selector);
    $jupiter = jQueryFrame($jupiter.selector);
    $saturn = jQueryFrame($saturn.selector);
    return $venus.length === 1 && $jupiter.length === 1 && $saturn.length === 1;
  }, function () {
    if (step === 1) {
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

  jQuery("#page\\:testframe").load(function () {
    if (step === 2) {
      $namefield = jQueryFrame($namefield.selector);
      assert.equal($namefield.val(), "Venus");

      $jupiter = jQueryFrame($jupiter.selector);
      $jupiter.dblclick();

      step++;
      done();
    } else if (step === 3) {
      $namefield = jQueryFrame($namefield.selector);
      assert.equal($namefield.val(), "Jupiter");

      $saturn = jQueryFrame($saturn.selector);
      $saturn.dblclick();

      step++;
      done();
    } else if (step === 4) {
      $namefield = jQueryFrame($namefield.selector);
      assert.equal($namefield.val(), "Saturn");

      step++;
      done();
    }
  });
});

QUnit.test("Open popup on click with ajax", function (assert) {
  assert.expect(12);
  var done = assert.async(7);

  var $radioButton = jQueryFrame("#page\\:mainForm\\:changeExample\\:\\:3");
  var $venus = jQueryFrame("#page\\:mainForm\\:s1\\:2\\:sample3");
  var $jupiter = jQueryFrame("#page\\:mainForm\\:s1\\:5\\:sample3");
  var $saturn = jQueryFrame("#page\\:mainForm\\:s1\\:6\\:sample3");
  var $popup = jQueryFrame("#page\\:mainForm\\:popup");
  var $name = jQueryFrame("#page\\:mainForm\\:popup\\:popupName\\:\\:field");
  var $cancel = jQueryFrame("#page\\:mainForm\\:popup\\:cancel");

  $radioButton.click();

  waitForAjax(function () {
    $venus = jQueryFrame($venus.selector);
    $jupiter = jQueryFrame($jupiter.selector);
    $saturn = jQueryFrame($saturn.selector);
    return $venus.length === 1 && $jupiter.length === 1 && $saturn.length === 1;
  }, function () {
    $venus = jQueryFrame($venus.selector);
    $jupiter = jQueryFrame($jupiter.selector);
    $saturn = jQueryFrame($saturn.selector);
    assert.equal($venus.length, 1);
    assert.equal($jupiter.length, 1);
    assert.equal($saturn.length, 1);
    done();

    $venus.click();

    waitForAjax(function () {
      $popup = jQueryFrame($popup.selector);
      $name = jQueryFrame($name.selector);
      return $popup.hasClass("show") && $name.val() === "Venus";
    }, function () {
      $popup = jQueryFrame($popup.selector);
      $name = jQueryFrame($name.selector);
      assert.ok($popup.hasClass("show"));
      assert.equal($name.val(), "Venus");
      done();

      waitForAjax(function () {
        $cancel = jQueryFrame($cancel.selector);
        $popup = jQueryFrame($popup.selector);

        $cancel.click();
        return !$popup.hasClass("show");
      }, function () {
        $popup = jQueryFrame($popup.selector);
        assert.notOk($popup.hasClass("show"));
        done();

        $jupiter.click();

        waitForAjax(function () {
          $popup = jQueryFrame($popup.selector);
          $name = jQueryFrame($name.selector);
          return $popup.hasClass("show") && $name.val() === "Jupiter";
        }, function () {
          $popup = jQueryFrame($popup.selector);
          $name = jQueryFrame($name.selector);
          assert.ok($popup.hasClass("show"));
          assert.equal($name.val(), "Jupiter");
          done();

          waitForAjax(function () {
            $cancel = jQueryFrame($cancel.selector);
            $popup = jQueryFrame($popup.selector);

            $cancel.click();
            return !$popup.hasClass("show");
          }, function () {
            $popup = jQueryFrame($popup.selector);
            assert.notOk($popup.hasClass("show"));
            done();

            $saturn.click();

            waitForAjax(function () {
              $popup = jQueryFrame($popup.selector);
              $name = jQueryFrame($name.selector);
              return $popup.hasClass("show") && $name.val() === "Saturn";
            }, function () {
              $popup = jQueryFrame($popup.selector);
              $name = jQueryFrame($name.selector);
              assert.ok($popup.hasClass("show"));
              assert.equal($name.val(), "Saturn");
              done();


              waitForAjax(function () {
                $cancel = jQueryFrame($cancel.selector);
                $popup = jQueryFrame($popup.selector);

                $cancel.click();
                return !$popup.hasClass("show");
              }, function () {
                $popup = jQueryFrame($popup.selector);
                assert.notOk($popup.hasClass("show"));
                done();
              });
            });
          });
        });
      });
    });
  });
});
