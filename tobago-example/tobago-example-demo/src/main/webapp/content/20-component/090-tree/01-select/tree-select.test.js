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
  var mu = "#page\\:mainForm\\:categoriesTree\\:3\\:select";
  var $music = jQueryFrame(mu);
  var ma = "#page\\:mainForm\\:categoriesTree\\:9\\:select";
  var $mathematics = jQueryFrame(ma);
  var o = "#page\\:mainForm\\:selectedNodesOutput span";
  var $output = jQueryFrame(o);
  var sn = "#page\\:mainForm\\:selectable\\:\\:0";
  var $selectableNone = jQueryFrame(sn);
  var ss = "#page\\:mainForm\\:selectable\\:\\:1";
  var $selectableSingle = jQueryFrame(ss);

  $selectableNone.prop("checked", true).trigger("change");

  waitForAjax(function() {
    return jQueryFrame(".tobago-treeSelect input").length === 0;
  }, function() {
    assert.equal(jQueryFrame(".tobago-treeSelect input").length, 0);

    $selectableSingle = jQueryFrame(ss);
    $selectableSingle.prop("checked", true).trigger("change");

    waitForAjax(function() {
      return jQueryFrame(".tobago-treeSelect input").length > 0;
    }, function() {
      assert.notEqual(jQueryFrame(".tobago-treeSelect input").length, 0);

      $music = jQueryFrame(mu);
      $music.prop("checked", true).trigger("change");

      waitForAjax(function() {
        $output = jQueryFrame(o);
        return $output.text() === "Music"
      }, function() {
        $output = jQueryFrame(o);
        assert.equal($output.text(), "Music");

        $mathematics = jQueryFrame(ma);
        $mathematics.prop("checked", true).trigger("change");

        waitForAjax(function() {
          $output = jQueryFrame(o);
          return $output.text() === "Mathematics"
        }, function() {
          $output = jQueryFrame(o);
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
  var c = "#page\\:mainForm\\:categoriesTree\\:4\\:select";
  var $classic = jQueryFrame(c);
  var m = "#page\\:mainForm\\:categoriesTree\\:9\\:select";
  var $mathematics = jQueryFrame(m);
  var o = "#page\\:mainForm\\:selectedNodesOutput span";
  var $output = jQueryFrame(o);
  var sn = "#page\\:mainForm\\:selectable\\:\\:0";
  var $selectableNone = jQueryFrame(sn);
  var sslo = "#page\\:mainForm\\:selectable\\:\\:2";
  var $selectableSingleLeafOnly = jQueryFrame(sslo);

  $selectableNone.prop("checked", true).trigger("change");

  waitForAjax(function() {
    return jQueryFrame(".tobago-treeSelect input").length === 0;
  }, function() {
    assert.equal(jQueryFrame(".tobago-treeSelect input").length, 0);

    $selectableSingleLeafOnly = jQueryFrame(sslo);
    $selectableSingleLeafOnly.prop("checked", true).trigger("change");

    waitForAjax(function() {
      return jQueryFrame(".tobago-treeSelect input").length > 0;
    }, function() {
      assert.notEqual(jQueryFrame(".tobago-treeSelect input").length, 0);

      $classic = jQueryFrame(c);
      $classic.prop("checked", true).trigger("change");

      waitForAjax(function() {
        $output = jQueryFrame(o);
        return $output.text() === "Classic"
      }, function() {
        $output = jQueryFrame(o);
        assert.equal($output.text(), "Classic");

        $mathematics = jQueryFrame(m);
        $mathematics.prop("checked", true).trigger("change");

        waitForAjax(function() {
          $output = jQueryFrame(o);
          return $output.text() === "Mathematics"
        }, function() {
          $output = jQueryFrame(o);
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
  var mu = "#page\\:mainForm\\:categoriesTree\\:3\\:select";
  var $music = jQueryFrame(mu);
  var ma = "#page\\:mainForm\\:categoriesTree\\:9\\:select";
  var $mathematics = jQueryFrame(ma);
  var o = "#page\\:mainForm\\:selectedNodesOutput span";
  var $output = jQueryFrame(o);
  var sn = "#page\\:mainForm\\:selectable\\:\\:0";
  var $selectableNone = jQueryFrame(sn);
  var sm = "#page\\:mainForm\\:selectable\\:\\:3";
  var $selectableMulti = jQueryFrame(sm);

  $selectableNone.prop("checked", true).trigger("change");

  waitForAjax(function() {
    return jQueryFrame(".tobago-treeSelect input").length === 0;
  }, function() {
    assert.equal(jQueryFrame(".tobago-treeSelect input").length, 0);

    $selectableMulti = jQueryFrame(sm);
    $selectableMulti.prop("checked", true).trigger("change");

    waitForAjax(function() {
      return jQueryFrame(".tobago-treeSelect input").length > 0;
    }, function() {
      assert.notEqual(jQueryFrame(".tobago-treeSelect input").length, 0);

      $music = jQueryFrame(mu);
      $music.prop("checked", true).trigger("change");

      waitForAjax(function() {
        $output = jQueryFrame(o);
        return $output.text() === "Music"
      }, function() {
        $output = jQueryFrame(o);
        assert.equal($output.text(), "Music");

        $mathematics = jQueryFrame(ma);
        $mathematics.prop("checked", true).trigger("change");

        waitForAjax(function() {
          $output = jQueryFrame(o);
          return $output.text() === "Music, Mathematics"
        }, function() {
          $output = jQueryFrame(o);
          assert.equal($output.text(), "Music, Mathematics");

          $music = jQueryFrame(mu);
          $music.prop("checked", false).trigger("change");

          waitForAjax(function() {
            $output = jQueryFrame(o);
            return $output.text() === "Mathematics"
          }, function() {
            $output = jQueryFrame(o);
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
  var c = "#page\\:mainForm\\:categoriesTree\\:4\\:select";
  var $classic = jQueryFrame(c);
  var m = "#page\\:mainForm\\:categoriesTree\\:9\\:select";
  var $mathematics = jQueryFrame(m);
  var o = "#page\\:mainForm\\:selectedNodesOutput span";
  var $output = jQueryFrame(o);
  var sn = "#page\\:mainForm\\:selectable\\:\\:0";
  var $selectableNone = jQueryFrame(sn);
  var smlo = "#page\\:mainForm\\:selectable\\:\\:4";
  var $selectableMultiLeafOnly = jQueryFrame(smlo);

  $selectableNone.prop("checked", true).trigger("change");

  waitForAjax(function() {
    return jQueryFrame(".tobago-treeSelect input").length === 0;
  }, function() {
    assert.equal(jQueryFrame(".tobago-treeSelect input").length, 0);

    $selectableMultiLeafOnly = jQueryFrame(smlo);
    $selectableMultiLeafOnly.prop("checked", true).trigger("change");

    waitForAjax(function() {
      return jQueryFrame(".tobago-treeSelect input").length > 0;
    }, function() {
      assert.notEqual(jQueryFrame(".tobago-treeSelect input").length, 0);

      $classic = jQueryFrame(c);
      $classic.prop("checked", true).trigger("change");

      waitForAjax(function() {
        $output = jQueryFrame(o);
        return $output.text() === "Classic"
      }, function() {
        $output = jQueryFrame(o);
        assert.equal($output.text(), "Classic");

        $mathematics = jQueryFrame(m);
        $mathematics.prop("checked", true).trigger("change");

        waitForAjax(function() {
          $output = jQueryFrame(o);
          return $output.text() === "Classic, Mathematics"
        }, function() {
          $output = jQueryFrame(o);
          assert.equal($output.text(), "Classic, Mathematics");

          $classic = jQueryFrame(c);
          $classic.prop("checked", false).trigger("change");

          waitForAjax(function() {
            $output = jQueryFrame(o);
            return $output.text() === "Mathematics"
          }, function() {
            $output = jQueryFrame(o);
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
  var mu = "#page\\:mainForm\\:categoriesTree\\:3\\:select";
  var $music = jQueryFrame(mu);
  var c = "#page\\:mainForm\\:categoriesTree\\:4\\:select";
  var $classic = jQueryFrame(c);
  var p = "#page\\:mainForm\\:categoriesTree\\:5\\:select";
  var $pop = jQueryFrame(p);
  var w = "#page\\:mainForm\\:categoriesTree\\:6\\:select";
  var $world = jQueryFrame(w);
  var ma = "#page\\:mainForm\\:categoriesTree\\:9\\:select";
  var $mathematics = jQueryFrame(ma);
  var o = "#page\\:mainForm\\:selectedNodesOutput span";
  var $output = jQueryFrame(o);
  var sn = "#page\\:mainForm\\:selectable\\:\\:0";
  var $selectableNone = jQueryFrame(sn);
  var smc = "#page\\:mainForm\\:selectable\\:\\:5";
  var $selectableMultiCascade = jQueryFrame(smc);

  $selectableNone.prop("checked", true).trigger("change");

  waitForAjax(function() {
    return jQueryFrame(".tobago-treeSelect input").length === 0;
  }, function() {
    assert.equal(jQueryFrame(".tobago-treeSelect input").length, 0);

    $selectableMultiCascade = jQueryFrame(smc);
    $selectableMultiCascade.prop("checked", true).trigger("change");

    waitForAjax(function() {
      return jQueryFrame(".tobago-treeSelect input").length > 0;
    }, function() {
      assert.notEqual(jQueryFrame(".tobago-treeSelect input").length, 0);

      $music = jQueryFrame(mu);
      $music.prop("checked", true).trigger("change");

      waitForAjax(function() {
        $output = jQueryFrame(o);
        return $output.text() === "Music, Classic, Pop, World"
      }, function() {
        $output = jQueryFrame(o);
        assert.equal($output.text(), "Music, Classic, Pop, World");

        $mathematics = jQueryFrame(ma);
        $mathematics.prop("checked", true).trigger("change");

        waitForAjax(function() {
          $output = jQueryFrame(o);
          return $output.text() === "Music, Classic, Pop, World, Mathematics"
        }, function() {
          $output = jQueryFrame(o);
          assert.equal($output.text(), "Music, Classic, Pop, World, Mathematics");

          $classic = jQueryFrame(c);
          $classic.prop("checked", false).trigger("change");

          waitForAjax(function() {
            $output = jQueryFrame(o);
            return $output.text() === "Music, Pop, World, Mathematics"
          }, function() {
            $output = jQueryFrame(o);
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
