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

QUnit.test("Basics: Name", function(assert) {
  assert.expect(37);
  var done = assert.async(4);

  var $colName = jQueryFrame("#page\\:mainForm\\:s1\\:columnName_sorter");
  var $rows = jQueryFrame("#page\\:mainForm\\:s1 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  var $leftPaging = jQueryFrame("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-left input");

  if ($colName.find(".fa-angle-up").length != 1) {
    $colName.click();
  }

  waitForAjax(function() {
    $colName = jQueryFrame($colName.selector);
    return $colName.find(".fa-angle-up").length == 1;
  }, function() {
    $colName = jQueryFrame($colName.selector);
    assert.equal($colName.find(".fa-angle-up").length, 1);
    done();

    $leftPaging = jQueryFrame($leftPaging.selector);
    $leftPaging.val("22").trigger("blur");

    waitForAjax(function() {
      $rows = jQueryFrame($rows.selector);
      return ajaxWaitingBodyTableCheck($rows,
          "Earth", "365.26", "",
          "Elara", "259.65", "1905",
          "Enceladus", "1.37", "1789",
          "Epimetheus", "0.69", "1980");
    }, function() {
      $rows = jQueryFrame($rows.selector);
      ajaxExecuteBodyTableCheck(assert, $rows,
          "Earth", "365.26", "",
          "Elara", "259.65", "1905",
          "Enceladus", "1.37", "1789",
          "Epimetheus", "0.69", "1980");
      done();

      $colName.click();

      waitForAjax(function() {
        $rows = jQueryFrame($rows.selector);
        return ajaxWaitingBodyTableCheck($rows,
            "Proteus", "1.12", "1989",
            "Prospero", "-1962.95", "1999",
            "Prometheus", "0.61", "1980",
            "Praxidike", "625.3", "2000");
      }, function() {
        $rows = jQueryFrame($rows.selector);
        ajaxExecuteBodyTableCheck(assert, $rows,
            "Proteus", "1.12", "1989",
            "Prospero", "-1962.95", "1999",
            "Prometheus", "0.61", "1980",
            "Praxidike", "625.3", "2000");
        done();

        $colName.click();

        waitForAjax(function() {
          $rows = jQueryFrame($rows.selector);
          return ajaxWaitingBodyTableCheck($rows,
              "Earth", "365.26", "",
              "Elara", "259.65", "1905",
              "Enceladus", "1.37", "1789",
              "Epimetheus", "0.69", "1980");
        }, function() {
          $rows = jQueryFrame($rows.selector);
          ajaxExecuteBodyTableCheck(assert, $rows,
              "Earth", "365.26", "",
              "Elara", "259.65", "1905",
              "Enceladus", "1.37", "1789",
              "Epimetheus", "0.69", "1980");
          done();
        });
      });
    });
  });
});

QUnit.test("Basics: Period", function(assert) {
  assert.expect(37);
  var done = assert.async(4);

  var $colPeriod = jQueryFrame("#page\\:mainForm\\:s1\\:columnPeriod_sorter");
  var $rows = jQueryFrame("#page\\:mainForm\\:s1 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  var $leftPaging = jQueryFrame("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-left input");

  if ($colPeriod.find(".fa-angle-up").length != 1) {
    $colPeriod.click();
  }

  waitForAjax(function() {
    $colPeriod = jQueryFrame($colPeriod.selector);
    return $colPeriod.find(".fa-angle-up").length == 1;
  }, function() {
    $colPeriod = jQueryFrame($colPeriod.selector);
    assert.equal($colPeriod.find(".fa-angle-up").length, 1);
    done();

    $leftPaging = jQueryFrame($leftPaging.selector);
    $leftPaging.val("22").trigger("blur");

    waitForAjax(function() {
      $rows = jQueryFrame($rows.selector);
      return ajaxWaitingBodyTableCheck($rows,
          "Galatea", "0.43", "1989",
          "Cressida", "0.46", "1986",
          "Desdemona", "0.47", "1986",
          "Juliet", "0.49", "1986");
    }, function() {
      $rows = jQueryFrame($rows.selector);
      ajaxExecuteBodyTableCheck(assert, $rows,
          "Galatea", "0.43", "1989",
          "Cressida", "0.46", "1986",
          "Desdemona", "0.47", "1986",
          "Juliet", "0.49", "1986");
      done();

      $colPeriod.click();

      waitForAjax(function() {
        $rows = jQueryFrame($rows.selector);
        return ajaxWaitingBodyTableCheck($rows,
            "Leda", "238.72", "1974",
            "Venus", "224.7", "",
            "Themisto", "130.02", "2000",
            "Mercury", "87.97", "");
      }, function() {
        $rows = jQueryFrame($rows.selector);
        ajaxExecuteBodyTableCheck(assert, $rows,
            "Leda", "238.72", "1974",
            "Venus", "224.7", "",
            "Themisto", "130.02", "2000",
            "Mercury", "87.97", "");
        done();

        $colPeriod.click();

        waitForAjax(function() {
          $rows = jQueryFrame($rows.selector);
          return ajaxWaitingBodyTableCheck($rows,
              "Galatea", "0.43", "1989",
              "Cressida", "0.46", "1986",
              "Desdemona", "0.47", "1986",
              "Juliet", "0.49", "1986");
        }, function() {
          $rows = jQueryFrame($rows.selector);
          ajaxExecuteBodyTableCheck(assert, $rows,
              "Galatea", "0.43", "1989",
              "Cressida", "0.46", "1986",
              "Desdemona", "0.47", "1986",
              "Juliet", "0.49", "1986");
          done();
        });
      });
    });
  });
});

QUnit.test("Basics: Year", function(assert) {
  assert.expect(13);
  var done = assert.async(4);

  var $colYear = jQueryFrame("#page\\:mainForm\\:s1\\:columnDiscoverYear_sorter");
  var $rows = jQueryFrame("#page\\:mainForm\\:s1 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  var $leftPaging = jQueryFrame("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-left input");

  if ($colYear.find(".fa-angle-up").length != 1) {
    $colYear.click();
  }

  waitForAjax(function() {
    $colYear = jQueryFrame($colYear.selector);
    return $colYear.find(".fa-angle-up").length == 1;
  }, function() {
    $colYear = jQueryFrame($colYear.selector);
    assert.equal($colYear.find(".fa-angle-up").length, 1);
    done();

    $leftPaging = jQueryFrame($leftPaging.selector);
    $leftPaging.val("22").trigger("blur");

    waitForAjax(function() {
      $rows = jQueryFrame($rows.selector);
      return $rows.eq(0).find(".tobago-sheet-cell span").eq(2).text() == "1789"
          && $rows.eq(1).find(".tobago-sheet-cell span").eq(2).text() == "1846"
          && $rows.eq(2).find(".tobago-sheet-cell span").eq(2).text() == "1846"
          && $rows.eq(3).find(".tobago-sheet-cell span").eq(2).text() == "1848";
    }, function() {
      $rows = jQueryFrame($rows.selector);
      assert.equal($rows.eq(0).find(".tobago-sheet-cell span").eq(2).text(), "1789", "row0col2");
      assert.equal($rows.eq(1).find(".tobago-sheet-cell span").eq(2).text(), "1846", "row1col2");
      assert.equal($rows.eq(2).find(".tobago-sheet-cell span").eq(2).text(), "1846", "row2col2");
      assert.equal($rows.eq(3).find(".tobago-sheet-cell span").eq(2).text(), "1848", "row3col2");
      done();

      $colYear.click();

      waitForAjax(function() {
        $rows = jQueryFrame($rows.selector);
        return $rows.eq(0).find(".tobago-sheet-cell span").eq(2).text() == "1989"
            && $rows.eq(1).find(".tobago-sheet-cell span").eq(2).text() == "1989"
            && $rows.eq(2).find(".tobago-sheet-cell span").eq(2).text() == "1989"
            && $rows.eq(3).find(".tobago-sheet-cell span").eq(2).text() == "1986";
      }, function() {
        $rows = jQueryFrame($rows.selector);
        assert.equal($rows.eq(0).find(".tobago-sheet-cell span").eq(2).text(), "1989", "row0col2");
        assert.equal($rows.eq(1).find(".tobago-sheet-cell span").eq(2).text(), "1989", "row1col2");
        assert.equal($rows.eq(2).find(".tobago-sheet-cell span").eq(2).text(), "1989", "row2col2");
        assert.equal($rows.eq(3).find(".tobago-sheet-cell span").eq(2).text(), "1986", "row3col2");
        done();

        $colYear.click();

        waitForAjax(function() {
          $rows = jQueryFrame($rows.selector);
          return $rows.eq(0).find(".tobago-sheet-cell span").eq(2).text() == "1789"
              && $rows.eq(1).find(".tobago-sheet-cell span").eq(2).text() == "1846"
              && $rows.eq(2).find(".tobago-sheet-cell span").eq(2).text() == "1846"
              && $rows.eq(3).find(".tobago-sheet-cell span").eq(2).text() == "1848";
        }, function() {
          $rows = jQueryFrame($rows.selector);
          assert.equal($rows.eq(0).find(".tobago-sheet-cell span").eq(2).text(), "1789", "row0col2");
          assert.equal($rows.eq(1).find(".tobago-sheet-cell span").eq(2).text(), "1846", "row1col2");
          assert.equal($rows.eq(2).find(".tobago-sheet-cell span").eq(2).text(), "1846", "row2col2");
          assert.equal($rows.eq(3).find(".tobago-sheet-cell span").eq(2).text(), "1848", "row3col2");
          done();
        });
      });
    });
  });
});

/**
 * 1. goto line 8
 * 2. goto line 9
 */
QUnit.test("Basics: left paging", function(assert) {
  assert.expect(25);
  var done = assert.async(3);

  var $colName = jQueryFrame("#page\\:mainForm\\:s1\\:columnName_sorter");
  var $rows = jQueryFrame("#page\\:mainForm\\:s1 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  var $leftPaging = jQueryFrame("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-left input");

  if ($colName.find(".fa-angle-up").length != 1) {
    $colName.click();
  }

  waitForAjax(function() {
    $colName = jQueryFrame($colName.selector);
    return $colName.find(".fa-angle-up").length == 1;
  }, function() {
    $colName = jQueryFrame($colName.selector);
    assert.equal($colName.find(".fa-angle-up").length, 1);
    done();

    $leftPaging = jQueryFrame($leftPaging.selector);
    $leftPaging.val("8").trigger("blur");

    waitForAjax(function() {
      $rows = jQueryFrame($rows.selector);
      return ajaxWaitingBodyTableCheck($rows,
          "Bianca", "0.43", "1986",
          "Caliban", "-579.39", "1997",
          "Callirrhoe", "758.8", "2000",
          "Callisto", "16.69", "1610");
    }, function() {
      $rows = jQueryFrame($rows.selector);
      ajaxExecuteBodyTableCheck(assert, $rows,
          "Bianca", "0.43", "1986",
          "Caliban", "-579.39", "1997",
          "Callirrhoe", "758.8", "2000",
          "Callisto", "16.69", "1610");
      done();

      $leftPaging = jQueryFrame($leftPaging.selector);
      $leftPaging.val("9").trigger("blur");

      waitForAjax(function() {
        $rows = jQueryFrame($rows.selector);
        return ajaxWaitingBodyTableCheck($rows,
            "Caliban", "-579.39", "1997",
            "Callirrhoe", "758.8", "2000",
            "Callisto", "16.69", "1610",
            "Calypso", "1.89", "1980");
      }, function() {
        $rows = jQueryFrame($rows.selector);
        ajaxExecuteBodyTableCheck(assert, $rows,
            "Caliban", "-579.39", "1997",
            "Callirrhoe", "758.8", "2000",
            "Callisto", "16.69", "1610",
            "Calypso", "1.89", "1980");
        done();
      });
    });
  });
});

/**
 * 1. goto page 7
 * 2. goto page 16
 * 3. goto page 13
 */
QUnit.test("Basics: center paging", function(assert) {
  assert.expect(49);
  var done = assert.async(5);

  var $colName = jQueryFrame("#page\\:mainForm\\:s1\\:columnName_sorter");
  var $rows = jQueryFrame("#page\\:mainForm\\:s1 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  var $leftPaging = jQueryFrame("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-left input");
  var $centerPaging = jQueryFrame("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-center li .page-link");

  if ($colName.find(".fa-angle-up").length != 1) {
    $colName.click();
  }

  waitForAjax(function() {
    $colName = jQueryFrame($colName.selector);
    return $colName.find(".fa-angle-up").length == 1;
  }, function() {
    $colName = jQueryFrame($colName.selector);
    assert.equal($colName.find(".fa-angle-up").length, 1);
    done();

    $leftPaging = jQueryFrame($leftPaging.selector);
    $leftPaging.val("1").trigger("blur");

    waitForAjax(function() {
      $rows = jQueryFrame($rows.selector);
      return ajaxWaitingBodyTableCheck($rows,
          "1986U10", "0.64", "1999",
          "Adrastea", "0.3", "1979",
          "Amalthea", "0.5", "1892",
          "Ananke", "-629.77", "1951");
    }, function() {
      $rows = jQueryFrame($rows.selector);
      ajaxExecuteBodyTableCheck(assert, $rows,
          "1986U10", "0.64", "1999",
          "Adrastea", "0.3", "1979",
          "Amalthea", "0.5", "1892",
          "Ananke", "-629.77", "1951");
      done();

      $centerPaging = jQueryFrame($centerPaging.selector);
      $centerPaging.eq(6).click();

      waitForAjax(function() {
        $rows = jQueryFrame($rows.selector);
        return ajaxWaitingBodyTableCheck($rows,
            "Epimetheus", "0.69", "1980",
            "Erinome", "728.3", "2000",
            "Europa", "3.55", "1610",
            "Galatea", "0.43", "1989");
      }, function() {
        $rows = jQueryFrame($rows.selector);
        ajaxExecuteBodyTableCheck(assert, $rows,
            "Epimetheus", "0.69", "1980",
            "Erinome", "728.3", "2000",
            "Europa", "3.55", "1610",
            "Galatea", "0.43", "1989");
        done();

        $centerPaging = jQueryFrame($centerPaging.selector);
        $centerPaging.eq(10).click();

        waitForAjax(function() {
          $rows = jQueryFrame($rows.selector);
          return ajaxWaitingBodyTableCheck($rows,
              "Phoebe", "-550.48", "1898",
              "Pluto", "90800.0", "1930",
              "Portia", "0.51", "1986",
              "Praxidike", "625.3", "2000");
        }, function() {
          $rows = jQueryFrame($rows.selector);
          ajaxExecuteBodyTableCheck(assert, $rows,
              "Phoebe", "-550.48", "1898",
              "Pluto", "90800.0", "1930",
              "Portia", "0.51", "1986",
              "Praxidike", "625.3", "2000");
          done();

          $centerPaging = jQueryFrame($centerPaging.selector);
          $centerPaging.eq(3).click();

          waitForAjax(function() {
            $rows = jQueryFrame($rows.selector);
            return ajaxWaitingBodyTableCheck($rows,
                "Neptune", "60190.0", "1846",
                "Nereid", "360.13", "1949",
                "Oberon", "13.46", "1787",
                "Ophelia", "0.38", "1986");
          }, function() {
            $rows = jQueryFrame($rows.selector);
            ajaxExecuteBodyTableCheck(assert, $rows,
                "Neptune", "60190.0", "1846",
                "Nereid", "360.13", "1949",
                "Oberon", "13.46", "1787",
                "Ophelia", "0.38", "1986");
            done();
          });
        });
      });
    });
  });
});

/**
 * 1. goto first page
 * 2. goto page 2 by pressing arrow-right
 * 3. goto last page
 * 4. goto page 21 by pressing arrow-left
 * 5. goto page 14
 */
QUnit.test("Basics: right paging", function(assert) {
  assert.expect(61);
  var done = assert.async(6);

  var $colName = jQueryFrame("#page\\:mainForm\\:s1\\:columnName_sorter");
  var $rows = jQueryFrame("#page\\:mainForm\\:s1 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  var $leftPaging = jQueryFrame("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-left input");
  var $rightPaging = jQueryFrame("#page\\:mainForm\\:s1 .tobago-sheet-paging-markup-right .page-link");

  if ($colName.find(".fa-angle-up").length != 1) {
    $colName.click();
  }

  waitForAjax(function() {
    $colName = jQueryFrame($colName.selector);
    return $colName.find(".fa-angle-up").length == 1;
  }, function() {
    $colName = jQueryFrame($colName.selector);
    assert.equal($colName.find(".fa-angle-up").length, 1);
    done();

    $leftPaging = jQueryFrame($leftPaging.selector);
    $leftPaging.val("22").trigger("blur");

    waitForAjax(function() {
      $rows = jQueryFrame($rows.selector);
      return ajaxWaitingBodyTableCheck($rows,
          "Earth", "365.26", "",
          "Elara", "259.65", "1905",
          "Enceladus", "1.37", "1789",
          "Epimetheus", "0.69", "1980");
    }, function() {
      $rows = jQueryFrame($rows.selector);
      ajaxExecuteBodyTableCheck(assert, $rows,
          "Earth", "365.26", "",
          "Elara", "259.65", "1905",
          "Enceladus", "1.37", "1789",
          "Epimetheus", "0.69", "1980");
      done();

      var $rightPagingFirstPage = jQueryFrame($rightPaging.selector).eq(0);
      $rightPagingFirstPage.click();

      waitForAjax(function() {
        $rows = jQueryFrame($rows.selector);
        return ajaxWaitingBodyTableCheck($rows,
            "1986U10", "0.64", "1999",
            "Adrastea", "0.3", "1979",
            "Amalthea", "0.5", "1892",
            "Ananke", "-629.77", "1951");
      }, function() {
        $rows = jQueryFrame($rows.selector);
        ajaxExecuteBodyTableCheck(assert, $rows,
            "1986U10", "0.64", "1999",
            "Adrastea", "0.3", "1979",
            "Amalthea", "0.5", "1892",
            "Ananke", "-629.77", "1951");
        done();

        var $rightPagingRight = jQueryFrame($rightPaging.selector).eq(3);
        $rightPagingRight.click();

        waitForAjax(function() {
          $rows = jQueryFrame($rows.selector);
          return ajaxWaitingBodyTableCheck($rows,
              "Ariel", "2.52", "1851",
              "Atlas", "0.6", "1980",
              "Belinda", "0.62", "1986",
              "Bianca", "0.43", "1986");
        }, function() {
          $rows = jQueryFrame($rows.selector);
          ajaxExecuteBodyTableCheck(assert, $rows,
              "Ariel", "2.52", "1851",
              "Atlas", "0.6", "1980",
              "Belinda", "0.62", "1986",
              "Bianca", "0.43", "1986");
          done();

          var $rightPagingLastPage = jQueryFrame($rightPaging.selector).eq(4);
          $rightPagingLastPage.click();

          waitForAjax(function() {
            $rows = jQueryFrame($rows.selector);
            return ajaxWaitingBodyTableCheck($rows,
                "Triton", "-5.88", "1846",
                "Umbriel", "4.14", "1851",
                "Uranus", "30685.0", "1781",
                "Venus", "224.7", "");
          }, function() {
            $rows = jQueryFrame($rows.selector);
            ajaxExecuteBodyTableCheck(assert, $rows,
                "Triton", "-5.88", "1846",
                "Umbriel", "4.14", "1851",
                "Uranus", "30685.0", "1781",
                "Venus", "224.7", "");
            done();

            var $rightPagingLeft = jQueryFrame($rightPaging.selector).eq(1);
            $rightPagingLeft.click();

            waitForAjax(function() {
              $rows = jQueryFrame($rows.selector);
              return ajaxWaitingBodyTableCheck($rows,
                  "Thebe", "0.67", "1979",
                  "Themisto", "130.02", "2000",
                  "Titan", "15.95", "1655",
                  "Titania", "8.71", "1787");
            }, function() {
              $rows = jQueryFrame($rows.selector);
              ajaxExecuteBodyTableCheck(assert, $rows,
                  "Thebe", "0.67", "1979",
                  "Themisto", "130.02", "2000",
                  "Titan", "15.95", "1655",
                  "Titania", "8.71", "1787");
              done();

              var $rightPagingJumpToPage = jQueryFrame($rightPaging.selector).find("input");
              $rightPagingJumpToPage.val("14").trigger("blur");

              waitForAjax(function() {
                $rows = jQueryFrame($rows.selector);
                return ajaxWaitingBodyTableCheck($rows,
                    "Neptune", "60190.0", "1846",
                    "Nereid", "360.13", "1949",
                    "Oberon", "13.46", "1787",
                    "Ophelia", "0.38", "1986");
              }, function() {
                $rows = jQueryFrame($rows.selector);
                ajaxExecuteBodyTableCheck(assert, $rows,
                    "Neptune", "60190.0", "1846",
                    "Nereid", "360.13", "1949",
                    "Oberon", "13.46", "1787",
                    "Ophelia", "0.38", "1986");
                done();
              });
            });
          });
        });
      });
    });
  });
});

QUnit.test("Custom Sorting: Name", function(assert) {
  assert.expect(37);
  var done = assert.async(4);

  var $colName = jQueryFrame("#page\\:mainForm\\:s2\\:customColumnName_sorter");
  var $rows = jQueryFrame("#page\\:mainForm\\:s2 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  var $leftPaging = jQueryFrame("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-left input");

  if ($colName.find(".fa-angle-up").length != 1) {
    $colName.click();
  }

  waitForAjax(function() {
    $colName = jQueryFrame($colName.selector);
    return $colName.find(".fa-angle-up").length == 1;
  }, function() {
    $colName = jQueryFrame($colName.selector);
    assert.equal($colName.find(".fa-angle-up").length, 1);
    done();

    $leftPaging = jQueryFrame($leftPaging.selector);
    $leftPaging.val("22").trigger("blur");

    waitForAjax(function() {
      $rows = jQueryFrame($rows.selector);
      return ajaxWaitingBodyTableCheck($rows,
          "Earth", "365.26", "",
          "Elara", "259.65", "1905",
          "Enceladus", "1.37", "1789",
          "Epimetheus", "0.69", "1980");
    }, function() {
      $rows = jQueryFrame($rows.selector);
      ajaxExecuteBodyTableCheck(assert, $rows,
          "Earth", "365.26", "",
          "Elara", "259.65", "1905",
          "Enceladus", "1.37", "1789",
          "Epimetheus", "0.69", "1980");
      done();

      $colName.click();

      waitForAjax(function() {
        $rows = jQueryFrame($rows.selector);
        return ajaxWaitingBodyTableCheck($rows,
            "Proteus", "1.12", "1989",
            "Prospero", "-1962.95", "1999",
            "Prometheus", "0.61", "1980",
            "Praxidike", "625.3", "2000");
      }, function() {
        $rows = jQueryFrame($rows.selector);
        ajaxExecuteBodyTableCheck(assert, $rows,
            "Proteus", "1.12", "1989",
            "Prospero", "-1962.95", "1999",
            "Prometheus", "0.61", "1980",
            "Praxidike", "625.3", "2000");
        done();

        $colName.click();

        waitForAjax(function() {
          $rows = jQueryFrame($rows.selector);
          return ajaxWaitingBodyTableCheck($rows,
              "Earth", "365.26", "",
              "Elara", "259.65", "1905",
              "Enceladus", "1.37", "1789",
              "Epimetheus", "0.69", "1980");
        }, function() {
          $rows = jQueryFrame($rows.selector);
          ajaxExecuteBodyTableCheck(assert, $rows,
              "Earth", "365.26", "",
              "Elara", "259.65", "1905",
              "Enceladus", "1.37", "1789",
              "Epimetheus", "0.69", "1980");
          done();
        });
      });
    });
  });
});

QUnit.test("Custom Sorting: Period", function(assert) {
  assert.expect(37);
  var done = assert.async(4);

  var $colPeriod = jQueryFrame("#page\\:mainForm\\:s2\\:customColumnPeriod_sorter");
  var $rows = jQueryFrame("#page\\:mainForm\\:s2 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  var $leftPaging = jQueryFrame("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-left input");

  if ($colPeriod.find(".fa-angle-up").length != 1) {
    $colPeriod.click();
  }

  waitForAjax(function() {
    $colPeriod = jQueryFrame($colPeriod.selector);
    return $colPeriod.find(".fa-angle-up").length == 1;
  }, function() {
    $colPeriod = jQueryFrame($colPeriod.selector);
    assert.equal($colPeriod.find(".fa-angle-up").length, 1);
    done();

    $leftPaging = jQueryFrame($leftPaging.selector);
    $leftPaging.val("22").trigger("blur");

    waitForAjax(function() {
      $rows = jQueryFrame($rows.selector);
      return ajaxWaitingBodyTableCheck($rows,
          "Belinda", "0.62", "1986",
          "Pandora", "0.63", "1980",
          "1986U10", "0.64", "1999",
          "Thebe", "0.67", "1979");
    }, function() {
      $rows = jQueryFrame($rows.selector);
      ajaxExecuteBodyTableCheck(assert, $rows,
          "Belinda", "0.62", "1986",
          "Pandora", "0.63", "1980",
          "1986U10", "0.64", "1999",
          "Thebe", "0.67", "1979");
      done();

      $colPeriod.click();

      waitForAjax(function() {
        $rows = jQueryFrame($rows.selector);
        return ajaxWaitingBodyTableCheck($rows,
            "Ananke", "-629.77", "1951",
            "Praxidike", "625.3", "2000",
            "Harpalyke", "623.3", "2000",
            "Caliban", "-579.39", "1997");
      }, function() {
        $rows = jQueryFrame($rows.selector);
        ajaxExecuteBodyTableCheck(assert, $rows,
            "Ananke", "-629.77", "1951",
            "Praxidike", "625.3", "2000",
            "Harpalyke", "623.3", "2000",
            "Caliban", "-579.39", "1997");
        done();

        $colPeriod.click();

        waitForAjax(function() {
          $rows = jQueryFrame($rows.selector);
          return ajaxWaitingBodyTableCheck($rows,
              "Belinda", "0.62", "1986",
              "Pandora", "0.63", "1980",
              "1986U10", "0.64", "1999",
              "Thebe", "0.67", "1979");
        }, function() {
          $rows = jQueryFrame($rows.selector);
          ajaxExecuteBodyTableCheck(assert, $rows,
              "Belinda", "0.62", "1986",
              "Pandora", "0.63", "1980",
              "1986U10", "0.64", "1999",
              "Thebe", "0.67", "1979");
          done();
        });
      });
    });
  });
});

QUnit.test("Custom Sorting: Year", function(assert) {
  assert.expect(13);
  var done = assert.async(4);

  var $colYear = jQueryFrame("#page\\:mainForm\\:s2\\:customColumnYear_sorter");
  var $rows = jQueryFrame("#page\\:mainForm\\:s2 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  var $leftPaging = jQueryFrame("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-left input");

  if ($colYear.find(".fa-angle-up").length != 1) {
    $colYear.click();
  }

  waitForAjax(function() {
    $colYear = jQueryFrame($colYear.selector);
    return $colYear.find(".fa-angle-up").length == 1;
  }, function() {
    $colYear = jQueryFrame($colYear.selector);
    assert.equal($colYear.find(".fa-angle-up").length, 1);
    done();

    $leftPaging = jQueryFrame($leftPaging.selector);
    $leftPaging.val("22").trigger("blur");

    waitForAjax(function() {
      $rows = jQueryFrame($rows.selector);
      return $rows.eq(0).find(".tobago-sheet-cell span").eq(2).text() == "1789"
          && $rows.eq(1).find(".tobago-sheet-cell span").eq(2).text() == "1846"
          && $rows.eq(2).find(".tobago-sheet-cell span").eq(2).text() == "1846"
          && $rows.eq(3).find(".tobago-sheet-cell span").eq(2).text() == "1848";
    }, function() {
      $rows = jQueryFrame($rows.selector);
      assert.equal($rows.eq(0).find(".tobago-sheet-cell span").eq(2).text(), "1789", "row0col2");
      assert.equal($rows.eq(1).find(".tobago-sheet-cell span").eq(2).text(), "1846", "row1col2");
      assert.equal($rows.eq(2).find(".tobago-sheet-cell span").eq(2).text(), "1846", "row2col2");
      assert.equal($rows.eq(3).find(".tobago-sheet-cell span").eq(2).text(), "1848", "row3col2");
      done();

      $colYear.click();

      waitForAjax(function() {
        $rows = jQueryFrame($rows.selector);
        return $rows.eq(0).find(".tobago-sheet-cell span").eq(2).text() == "1989"
            && $rows.eq(1).find(".tobago-sheet-cell span").eq(2).text() == "1989"
            && $rows.eq(2).find(".tobago-sheet-cell span").eq(2).text() == "1989"
            && $rows.eq(3).find(".tobago-sheet-cell span").eq(2).text() == "1986";
      }, function() {
        $rows = jQueryFrame($rows.selector);
        assert.equal($rows.eq(0).find(".tobago-sheet-cell span").eq(2).text(), "1989", "row0col2");
        assert.equal($rows.eq(1).find(".tobago-sheet-cell span").eq(2).text(), "1989", "row1col2");
        assert.equal($rows.eq(2).find(".tobago-sheet-cell span").eq(2).text(), "1989", "row2col2");
        assert.equal($rows.eq(3).find(".tobago-sheet-cell span").eq(2).text(), "1986", "row3col2");
        done();

        $colYear.click();

        waitForAjax(function() {
          $rows = jQueryFrame($rows.selector);
          return $rows.eq(0).find(".tobago-sheet-cell span").eq(2).text() == "1789"
              && $rows.eq(1).find(".tobago-sheet-cell span").eq(2).text() == "1846"
              && $rows.eq(2).find(".tobago-sheet-cell span").eq(2).text() == "1846"
              && $rows.eq(3).find(".tobago-sheet-cell span").eq(2).text() == "1848";
        }, function() {
          $rows = jQueryFrame($rows.selector);
          assert.equal($rows.eq(0).find(".tobago-sheet-cell span").eq(2).text(), "1789", "row0col2");
          assert.equal($rows.eq(1).find(".tobago-sheet-cell span").eq(2).text(), "1846", "row1col2");
          assert.equal($rows.eq(2).find(".tobago-sheet-cell span").eq(2).text(), "1846", "row2col2");
          assert.equal($rows.eq(3).find(".tobago-sheet-cell span").eq(2).text(), "1848", "row3col2");
          done();
        });
      });
    });
  });
});

/**
 * 1. goto line 8
 * 2. goto line 9
 */
QUnit.test("Custom Sorting: left paging", function(assert) {
  assert.expect(25);
  var done = assert.async(3);

  var $colName = jQueryFrame("#page\\:mainForm\\:s2\\:customColumnName_sorter");
  var $rows = jQueryFrame("#page\\:mainForm\\:s2 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  var $leftPaging = jQueryFrame("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-left input");

  if ($colName.find(".fa-angle-up").length != 1) {
    $colName.click();
  }

  waitForAjax(function() {
    $colName = jQueryFrame($colName.selector);
    return $colName.find(".fa-angle-up").length == 1;
  }, function() {
    $colName = jQueryFrame($colName.selector);
    assert.equal($colName.find(".fa-angle-up").length, 1);
    done();

    $leftPaging = jQueryFrame($leftPaging.selector);
    $leftPaging.val("8").trigger("blur");

    waitForAjax(function() {
      $rows = jQueryFrame($rows.selector);
      return ajaxWaitingBodyTableCheck($rows,
          "Bianca", "0.43", "1986",
          "Caliban", "-579.39", "1997",
          "Callirrhoe", "758.8", "2000",
          "Callisto", "16.69", "1610");
    }, function() {
      $rows = jQueryFrame($rows.selector);
      ajaxExecuteBodyTableCheck(assert, $rows,
          "Bianca", "0.43", "1986",
          "Caliban", "-579.39", "1997",
          "Callirrhoe", "758.8", "2000",
          "Callisto", "16.69", "1610");
      done();

      $leftPaging = jQueryFrame($leftPaging.selector);
      $leftPaging.val("9").trigger("blur");

      waitForAjax(function() {
        $rows = jQueryFrame($rows.selector);
        return ajaxWaitingBodyTableCheck($rows,
            "Caliban", "-579.39", "1997",
            "Callirrhoe", "758.8", "2000",
            "Callisto", "16.69", "1610",
            "Calypso", "1.89", "1980");
      }, function() {
        $rows = jQueryFrame($rows.selector);
        ajaxExecuteBodyTableCheck(assert, $rows,
            "Caliban", "-579.39", "1997",
            "Callirrhoe", "758.8", "2000",
            "Callisto", "16.69", "1610",
            "Calypso", "1.89", "1980");
        done();
      });
    });
  });
});

/**
 * 1. goto page 7
 * 2. goto page 16
 * 3. goto page 13
 */
QUnit.test("Custom Sorting: center paging", function(assert) {
  assert.expect(49);
  var done = assert.async(5);

  var $colName = jQueryFrame("#page\\:mainForm\\:s2\\:customColumnName_sorter");
  var $rows = jQueryFrame("#page\\:mainForm\\:s2 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  var $leftPaging = jQueryFrame("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-left input");
  var $centerPaging = jQueryFrame("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-center li .page-link");

  if ($colName.find(".fa-angle-up").length != 1) {
    $colName.click();
  }

  waitForAjax(function() {
    $colName = jQueryFrame($colName.selector);
    return $colName.find(".fa-angle-up").length == 1;
  }, function() {
    $colName = jQueryFrame($colName.selector);
    assert.equal($colName.find(".fa-angle-up").length, 1);
    done();

    $leftPaging = jQueryFrame($leftPaging.selector);
    $leftPaging.val("1").trigger("blur");

    waitForAjax(function() {
      $rows = jQueryFrame($rows.selector);
      return ajaxWaitingBodyTableCheck($rows,
          "1986U10", "0.64", "1999",
          "Adrastea", "0.3", "1979",
          "Amalthea", "0.5", "1892",
          "Ananke", "-629.77", "1951");
    }, function() {
      $rows = jQueryFrame($rows.selector);
      ajaxExecuteBodyTableCheck(assert, $rows,
          "1986U10", "0.64", "1999",
          "Adrastea", "0.3", "1979",
          "Amalthea", "0.5", "1892",
          "Ananke", "-629.77", "1951");
      done();

      $centerPaging = jQueryFrame($centerPaging.selector);
      $centerPaging.eq(6).click();

      waitForAjax(function() {
        $rows = jQueryFrame($rows.selector);
        return ajaxWaitingBodyTableCheck($rows,
            "Epimetheus", "0.69", "1980",
            "Erinome", "728.3", "2000",
            "Europa", "3.55", "1610",
            "Galatea", "0.43", "1989");
      }, function() {
        $rows = jQueryFrame($rows.selector);
        ajaxExecuteBodyTableCheck(assert, $rows,
            "Epimetheus", "0.69", "1980",
            "Erinome", "728.3", "2000",
            "Europa", "3.55", "1610",
            "Galatea", "0.43", "1989");
        done();

        $centerPaging = jQueryFrame($centerPaging.selector);
        $centerPaging.eq(10).click();

        waitForAjax(function() {
          $rows = jQueryFrame($rows.selector);
          return ajaxWaitingBodyTableCheck($rows,
              "Phoebe", "-550.48", "1898",
              "Pluto", "90800.0", "1930",
              "Portia", "0.51", "1986",
              "Praxidike", "625.3", "2000");
        }, function() {
          $rows = jQueryFrame($rows.selector);
          ajaxExecuteBodyTableCheck(assert, $rows,
              "Phoebe", "-550.48", "1898",
              "Pluto", "90800.0", "1930",
              "Portia", "0.51", "1986",
              "Praxidike", "625.3", "2000");
          done();

          $centerPaging = jQueryFrame($centerPaging.selector);
          $centerPaging.eq(3).click();

          waitForAjax(function() {
            $rows = jQueryFrame($rows.selector);
            return ajaxWaitingBodyTableCheck($rows,
                "Neptune", "60190.0", "1846",
                "Nereid", "360.13", "1949",
                "Oberon", "13.46", "1787",
                "Ophelia", "0.38", "1986");
          }, function() {
            $rows = jQueryFrame($rows.selector);
            ajaxExecuteBodyTableCheck(assert, $rows,
                "Neptune", "60190.0", "1846",
                "Nereid", "360.13", "1949",
                "Oberon", "13.46", "1787",
                "Ophelia", "0.38", "1986");
            done();
          });
        });
      });
    });
  });
});

/**
 * 1. goto first page
 * 2. goto page 2 by pressing arrow-right
 * 3. goto last page
 * 4. goto page 21 by pressing arrow-left
 * 5. goto page 14
 */
QUnit.test("Custom Sorting: right paging", function(assert) {
  assert.expect(61);
  var done = assert.async(6);

  var $colName = jQueryFrame("#page\\:mainForm\\:s2\\:customColumnName_sorter");
  var $rows = jQueryFrame("#page\\:mainForm\\:s2 .tobago-sheet-bodyTable tbody .tobago-sheet-row");
  var $leftPaging = jQueryFrame("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-left input");
  var $rightPaging = jQueryFrame("#page\\:mainForm\\:s2 .tobago-sheet-paging-markup-right .page-link");

  if ($colName.find(".fa-angle-up").length != 1) {
    $colName.click();
  }

  waitForAjax(function() {
    $colName = jQueryFrame($colName.selector);
    return $colName.find(".fa-angle-up").length == 1;
  }, function() {
    $colName = jQueryFrame($colName.selector);
    assert.equal($colName.find(".fa-angle-up").length, 1);
    done();

    $leftPaging = jQueryFrame($leftPaging.selector);
    $leftPaging.val("22").trigger("blur");

    waitForAjax(function() {
      $rows = jQueryFrame($rows.selector);
      return ajaxWaitingBodyTableCheck($rows,
          "Earth", "365.26", "",
          "Elara", "259.65", "1905",
          "Enceladus", "1.37", "1789",
          "Epimetheus", "0.69", "1980");
    }, function() {
      $rows = jQueryFrame($rows.selector);
      ajaxExecuteBodyTableCheck(assert, $rows,
          "Earth", "365.26", "",
          "Elara", "259.65", "1905",
          "Enceladus", "1.37", "1789",
          "Epimetheus", "0.69", "1980");
      done();

      var $rightPagingFirstPage = jQueryFrame($rightPaging.selector).eq(0);
      $rightPagingFirstPage.click();

      waitForAjax(function() {
        $rows = jQueryFrame($rows.selector);
        return ajaxWaitingBodyTableCheck($rows,
            "1986U10", "0.64", "1999",
            "Adrastea", "0.3", "1979",
            "Amalthea", "0.5", "1892",
            "Ananke", "-629.77", "1951");
      }, function() {
        $rows = jQueryFrame($rows.selector);
        ajaxExecuteBodyTableCheck(assert, $rows,
            "1986U10", "0.64", "1999",
            "Adrastea", "0.3", "1979",
            "Amalthea", "0.5", "1892",
            "Ananke", "-629.77", "1951");
        done();

        var $rightPagingRight = jQueryFrame($rightPaging.selector).eq(3);
        $rightPagingRight.click();

        waitForAjax(function() {
          $rows = jQueryFrame($rows.selector);
          return ajaxWaitingBodyTableCheck($rows,
              "Ariel", "2.52", "1851",
              "Atlas", "0.6", "1980",
              "Belinda", "0.62", "1986",
              "Bianca", "0.43", "1986");
        }, function() {
          $rows = jQueryFrame($rows.selector);
          ajaxExecuteBodyTableCheck(assert, $rows,
              "Ariel", "2.52", "1851",
              "Atlas", "0.6", "1980",
              "Belinda", "0.62", "1986",
              "Bianca", "0.43", "1986");
          done();

          var $rightPagingLastPage = jQueryFrame($rightPaging.selector).eq(4);
          $rightPagingLastPage.click();

          waitForAjax(function() {
            $rows = jQueryFrame($rows.selector);
            return ajaxWaitingBodyTableCheck($rows,
                "Triton", "-5.88", "1846",
                "Umbriel", "4.14", "1851",
                "Uranus", "30685.0", "1781",
                "Venus", "224.7", "");
          }, function() {
            $rows = jQueryFrame($rows.selector);
            ajaxExecuteBodyTableCheck(assert, $rows,
                "Triton", "-5.88", "1846",
                "Umbriel", "4.14", "1851",
                "Uranus", "30685.0", "1781",
                "Venus", "224.7", "");
            done();

            var $rightPagingLeft = jQueryFrame($rightPaging.selector).eq(1);
            $rightPagingLeft.click();

            waitForAjax(function() {
              $rows = jQueryFrame($rows.selector);
              return ajaxWaitingBodyTableCheck($rows,
                  "Thebe", "0.67", "1979",
                  "Themisto", "130.02", "2000",
                  "Titan", "15.95", "1655",
                  "Titania", "8.71", "1787");
            }, function() {
              $rows = jQueryFrame($rows.selector);
              ajaxExecuteBodyTableCheck(assert, $rows,
                  "Thebe", "0.67", "1979",
                  "Themisto", "130.02", "2000",
                  "Titan", "15.95", "1655",
                  "Titania", "8.71", "1787");
              done();

              var $rightPagingJumpToPage = jQueryFrame($rightPaging.selector).find("input");
              $rightPagingJumpToPage.val("14").trigger("blur");

              waitForAjax(function() {
                $rows = jQueryFrame($rows.selector);
                return ajaxWaitingBodyTableCheck($rows,
                    "Neptune", "60190.0", "1846",
                    "Nereid", "360.13", "1949",
                    "Oberon", "13.46", "1787",
                    "Ophelia", "0.38", "1986");
              }, function() {
                $rows = jQueryFrame($rows.selector);
                ajaxExecuteBodyTableCheck(assert, $rows,
                    "Neptune", "60190.0", "1846",
                    "Nereid", "360.13", "1949",
                    "Oberon", "13.46", "1787",
                    "Ophelia", "0.38", "1986");
                done();
              });
            });
          });
        });
      });
    });
  });
});

function ajaxWaitingBodyTableCheck($rows, row0col0, row0col1, row0col2, row1col0, row1col1, row1col2,
                                   row2col0, row2col1, row2col2, row3col0, row3col1, row3col2) {
  var $row0Col0 = $rows.eq(0).find(".tobago-sheet-cell span").eq(0).text();
  var $row0Col1 = $rows.eq(0).find(".tobago-sheet-cell span").eq(1).text();
  var $row0Col2 = $rows.eq(0).find(".tobago-sheet-cell span").eq(2).text();
  var $row1Col0 = $rows.eq(1).find(".tobago-sheet-cell span").eq(0).text();
  var $row1Col1 = $rows.eq(1).find(".tobago-sheet-cell span").eq(1).text();
  var $row1Col2 = $rows.eq(1).find(".tobago-sheet-cell span").eq(2).text();
  var $row2Col0 = $rows.eq(2).find(".tobago-sheet-cell span").eq(0).text();
  var $row2Col1 = $rows.eq(2).find(".tobago-sheet-cell span").eq(1).text();
  var $row2Col2 = $rows.eq(2).find(".tobago-sheet-cell span").eq(2).text();
  var $row3Col0 = $rows.eq(3).find(".tobago-sheet-cell span").eq(0).text();
  var $row3Col1 = $rows.eq(3).find(".tobago-sheet-cell span").eq(1).text();
  var $row3Col2 = $rows.eq(3).find(".tobago-sheet-cell span").eq(2).text();

  return $row0Col0 == row0col0 && $row0Col1 == row0col1 && $row0Col2 == row0col2
      && $row1Col0 == row1col0 && $row1Col1 == row1col1 && $row1Col2 == row1col2
      && $row2Col0 == row2col0 && $row2Col1 == row2col1 && $row2Col2 == row2col2
      && $row3Col0 == row3col0 && $row3Col1 == row3col1 && $row3Col2 == row3col2;
}

function ajaxExecuteBodyTableCheck(assert, $rows, row0col0, row0col1, row0col2, row1col0, row1col1, row1col2,
                                   row2col0, row2col1, row2col2, row3col0, row3col1, row3col2) {
  var $row0Col0 = $rows.eq(0).find(".tobago-sheet-cell span").eq(0).text();
  var $row0Col1 = $rows.eq(0).find(".tobago-sheet-cell span").eq(1).text();
  var $row0Col2 = $rows.eq(0).find(".tobago-sheet-cell span").eq(2).text();
  var $row1Col0 = $rows.eq(1).find(".tobago-sheet-cell span").eq(0).text();
  var $row1Col1 = $rows.eq(1).find(".tobago-sheet-cell span").eq(1).text();
  var $row1Col2 = $rows.eq(1).find(".tobago-sheet-cell span").eq(2).text();
  var $row2Col0 = $rows.eq(2).find(".tobago-sheet-cell span").eq(0).text();
  var $row2Col1 = $rows.eq(2).find(".tobago-sheet-cell span").eq(1).text();
  var $row2Col2 = $rows.eq(2).find(".tobago-sheet-cell span").eq(2).text();
  var $row3Col0 = $rows.eq(3).find(".tobago-sheet-cell span").eq(0).text();
  var $row3Col1 = $rows.eq(3).find(".tobago-sheet-cell span").eq(1).text();
  var $row3Col2 = $rows.eq(3).find(".tobago-sheet-cell span").eq(2).text();

  assert.equal($row0Col0, row0col0, "row0col0");
  assert.equal($row0Col1, row0col1, "row0col1");
  assert.equal($row0Col2, row0col2, "row0col2");
  assert.equal($row1Col0, row1col0, "row1col0");
  assert.equal($row1Col1, row1col1, "row1col1");
  assert.equal($row1Col2, row1col2, "row1col2");
  assert.equal($row2Col0, row2col0, "row2col0");
  assert.equal($row2Col1, row2col1, "row2col1");
  assert.equal($row2Col2, row2col2, "row2col2");
  assert.equal($row3Col0, row3col0, "row3col0");
  assert.equal($row3Col1, row3col1, "row3col1");
  assert.equal($row3Col2, row3col2, "row3col2");
}
