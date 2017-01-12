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

QUnit.test("single: select Music, select Mathematics", function(assert) {
  assert.expect(4);
  var done = assert.async(4);
  var $music = jQueryFrame("#page\\:mainForm\\:categoriesTree\\:3\\:select");
  var $mathematics = jQueryFrame("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  var $output = jQueryFrame("#page\\:mainForm\\:selectedNodesOutput span");
  var $selectableNone = jQueryFrame("#page\\:mainForm\\:selectable\\:\\:0");
  var $selectableSingle = jQueryFrame("#page\\:mainForm\\:selectable\\:\\:1");

  $selectableNone.prop("checked", true).trigger("change");

  waitForAjax(function() {
    return jQueryFrame(".tobago-treeSelect input").length == 0;
  }, function() {
    assert.equal(jQueryFrame(".tobago-treeSelect input").length, 0);

    $selectableSingle = jQueryFrame($selectableSingle.selector);
    $selectableSingle.prop("checked", true).trigger("change");

    waitForAjax(function() {
      return jQueryFrame(".tobago-treeSelect input").length > 0;
    }, function() {
      assert.notEqual(jQueryFrame(".tobago-treeSelect input").length, 0);

      $music = jQueryFrame($music.selector);
      $music.prop("checked", true).trigger("change");

      waitForAjax(function() {
        $output = jQueryFrame($output.selector);
        return $output.text() == "Music"
      }, function() {
        $output = jQueryFrame($output.selector);
        assert.equal($output.text(), "Music");

        $mathematics = jQueryFrame($mathematics.selector);
        $mathematics.prop("checked", true).trigger("change");

        waitForAjax(function() {
          $output = jQueryFrame($output.selector);
          return $output.text() == "Mathematics"
        }, function() {
          $output = jQueryFrame($output.selector);
          assert.equal($output.text(), "Mathematics");
          done();
        });
        done();
      });
      done();
    });
    done();
  });
});

QUnit.test("singleLeafOnly: select Classic, select Mathematics", function(assert) {
  assert.expect(4);
  var done = assert.async(4);
  var $classic = jQueryFrame("#page\\:mainForm\\:categoriesTree\\:4\\:select");
  var $mathematics = jQueryFrame("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  var $output = jQueryFrame("#page\\:mainForm\\:selectedNodesOutput span");
  var $selectableNone = jQueryFrame("#page\\:mainForm\\:selectable\\:\\:0");
  var $selectableSingleLeafOnly = jQueryFrame("#page\\:mainForm\\:selectable\\:\\:2");

  $selectableNone.prop("checked", true).trigger("change");

  waitForAjax(function() {
    return jQueryFrame(".tobago-treeSelect input").length == 0;
  }, function() {
    assert.equal(jQueryFrame(".tobago-treeSelect input").length, 0);

    $selectableSingleLeafOnly = jQueryFrame($selectableSingleLeafOnly.selector);
    $selectableSingleLeafOnly.prop("checked", true).trigger("change");

    waitForAjax(function() {
      return jQueryFrame(".tobago-treeSelect input").length > 0;
    }, function() {
      assert.notEqual(jQueryFrame(".tobago-treeSelect input").length, 0);

      $classic = jQueryFrame($classic.selector);
      $classic.prop("checked", true).trigger("change");

      waitForAjax(function() {
        $output = jQueryFrame($output.selector);
        return $output.text() == "Classic"
      }, function() {
        $output = jQueryFrame($output.selector);
        assert.equal($output.text(), "Classic");

        $mathematics = jQueryFrame($mathematics.selector);
        $mathematics.prop("checked", true).trigger("change");

        waitForAjax(function() {
          $output = jQueryFrame($output.selector);
          return $output.text() == "Mathematics"
        }, function() {
          $output = jQueryFrame($output.selector);
          assert.equal($output.text(), "Mathematics");
          done();
        });
        done();
      });
      done();
    });
    done();
  });
});

QUnit.test("multi: select Music, select Mathematics, deselect Music", function(assert) {
  assert.expect(4);
  var done = assert.async(4);
  var $music = jQueryFrame("#page\\:mainForm\\:categoriesTree\\:3\\:select");
  var $mathematics = jQueryFrame("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  var $output = jQueryFrame("#page\\:mainForm\\:selectedNodesOutput span");
  var $selectableNone = jQueryFrame("#page\\:mainForm\\:selectable\\:\\:0");
  var $selectableMulti = jQueryFrame("#page\\:mainForm\\:selectable\\:\\:3");

  $selectableNone.prop("checked", true).trigger("change");

  waitForAjax(function() {
    return jQueryFrame(".tobago-treeSelect input").length == 0;
  }, function() {
    assert.equal(jQueryFrame(".tobago-treeSelect input").length, 0);

    $selectableMulti = jQueryFrame($selectableMulti.selector);
    $selectableMulti.prop("checked", true).trigger("change");

    waitForAjax(function() {
      return jQueryFrame(".tobago-treeSelect input").length > 0;
    }, function() {
      assert.notEqual(jQueryFrame(".tobago-treeSelect input").length, 0);

      $music = jQueryFrame($music.selector);
      $music.prop("checked", true).trigger("change");

      waitForAjax(function() {
        $output = jQueryFrame($output.selector);
        return $output.text() == "Music"
      }, function() {
        $output = jQueryFrame($output.selector);
        assert.equal($output.text(), "Music");

        $mathematics = jQueryFrame($mathematics.selector);
        $mathematics.prop("checked", true).trigger("change");

        waitForAjax(function() {
          $output = jQueryFrame($output.selector);
          return $output.text() == "Music, Mathematics"
        }, function() {
          $output = jQueryFrame($output.selector);
          assert.equal($output.text(), "Music, Mathematics");

          $music = jQueryFrame($music.selector);
          $music.prop("checked", false).trigger("change");

          waitForAjax(function() {
            $output = jQueryFrame($output.selector);
            return $output.text() == "Mathematics"
          }, function() {
            $output = jQueryFrame($output.selector);
            assert.equal($output.text(), "Mathematics");
            done();
          });
          done();
        });
        done();
      });
      done();
    });
    done();
  });
});

QUnit.test("multiLeafOnly: select Classic, select Mathematics, deselect Classic", function(assert) {
  assert.expect(4);
  var done = assert.async(4);
  var $classic = jQueryFrame("#page\\:mainForm\\:categoriesTree\\:4\\:select");
  var $mathematics = jQueryFrame("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  var $output = jQueryFrame("#page\\:mainForm\\:selectedNodesOutput span");
  var $selectableNone = jQueryFrame("#page\\:mainForm\\:selectable\\:\\:0");
  var $selectableMultiLeafOnly = jQueryFrame("#page\\:mainForm\\:selectable\\:\\:4");

  $selectableNone.prop("checked", true).trigger("change");

  waitForAjax(function() {
    return jQueryFrame(".tobago-treeSelect input").length == 0;
  }, function() {
    assert.equal(jQueryFrame(".tobago-treeSelect input").length, 0);

    $selectableMultiLeafOnly = jQueryFrame($selectableMultiLeafOnly.selector);
    $selectableMultiLeafOnly.prop("checked", true).trigger("change");

    waitForAjax(function() {
      return jQueryFrame(".tobago-treeSelect input").length > 0;
    }, function() {
      assert.notEqual(jQueryFrame(".tobago-treeSelect input").length, 0);

      $classic = jQueryFrame($classic.selector);
      $classic.prop("checked", true).trigger("change");

      waitForAjax(function() {
        $output = jQueryFrame($output.selector);
        return $output.text() == "Classic"
      }, function() {
        $output = jQueryFrame($output.selector);
        assert.equal($output.text(), "Classic");

        $mathematics = jQueryFrame($mathematics.selector);
        $mathematics.prop("checked", true).trigger("change");

        waitForAjax(function() {
          $output = jQueryFrame($output.selector);
          return $output.text() == "Classic, Mathematics"
        }, function() {
          $output = jQueryFrame($output.selector);
          assert.equal($output.text(), "Classic, Mathematics");

          $classic = jQueryFrame($classic.selector);
          $classic.prop("checked", false).trigger("change");

          waitForAjax(function() {
            $output = jQueryFrame($output.selector);
            return $output.text() == "Mathematics"
          }, function() {
            $output = jQueryFrame($output.selector);
            assert.equal($output.text(), "Mathematics");
            done();
          });
          done();
        });
        done();
      });
      done();
    });
    done();
  });
});

QUnit.test("multiCascade: select Music, select Mathematics, deselect Classic", function(assert) {
  assert.expect(4);
  var done = assert.async(4);
  var $music = jQueryFrame("#page\\:mainForm\\:categoriesTree\\:3\\:select");
  var $classic = jQueryFrame("#page\\:mainForm\\:categoriesTree\\:4\\:select");
  var $pop = jQueryFrame("#page\\:mainForm\\:categoriesTree\\:5\\:select");
  var $world = jQueryFrame("#page\\:mainForm\\:categoriesTree\\:6\\:select");
  var $mathematics = jQueryFrame("#page\\:mainForm\\:categoriesTree\\:9\\:select");
  var $output = jQueryFrame("#page\\:mainForm\\:selectedNodesOutput span");
  var $selectableNone = jQueryFrame("#page\\:mainForm\\:selectable\\:\\:0");
  var $selectableMultiCascade = jQueryFrame("#page\\:mainForm\\:selectable\\:\\:5");

  $selectableNone.prop("checked", true).trigger("change");

  waitForAjax(function() {
    return jQueryFrame(".tobago-treeSelect input").length == 0;
  }, function() {
    assert.equal(jQueryFrame(".tobago-treeSelect input").length, 0);

    $selectableMultiCascade = jQueryFrame($selectableMultiCascade.selector);
    $selectableMultiCascade.prop("checked", true).trigger("change");

    waitForAjax(function() {
      return jQueryFrame(".tobago-treeSelect input").length > 0;
    }, function() {
      assert.notEqual(jQueryFrame(".tobago-treeSelect input").length, 0);

      $music = jQueryFrame($music.selector);
      $music.prop("checked", true).trigger("change");

      waitForAjax(function() {
        $output = jQueryFrame($output.selector);
        return $output.text() == "Music, Classic, Pop, World"
      }, function() {
        $output = jQueryFrame($output.selector);
        assert.equal($output.text(), "Music, Classic, Pop, World");

        $mathematics = jQueryFrame($mathematics.selector);
        $mathematics.prop("checked", true).trigger("change");

        waitForAjax(function() {
          $output = jQueryFrame($output.selector);
          return $output.text() == "Music, Classic, Pop, World, Mathematics"
        }, function() {
          $output = jQueryFrame($output.selector);
          assert.equal($output.text(), "Music, Classic, Pop, World, Mathematics");

          $classic = jQueryFrame($classic.selector);
          $classic.prop("checked", false).trigger("change");

          waitForAjax(function() {
            $output = jQueryFrame($output.selector);
            return $output.text() == "Music, Pop, World, Mathematics"
          }, function() {
            $output = jQueryFrame($output.selector);
            assert.equal($output.text(), "Music, Pop, World, Mathematics");
            done();
          });
          done();
        });
        done();
      });
      done();
    });
    done();
  });
});
