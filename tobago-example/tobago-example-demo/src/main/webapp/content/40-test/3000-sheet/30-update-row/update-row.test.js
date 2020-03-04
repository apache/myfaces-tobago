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

QUnit.test("Update timestamp for row 'Mercury' and 'Mars'", function (assert) {
  var mercuryTimestamp = jQueryFrameFn("#page\\:mainForm\\:sheet\\:1\\:timestamp");
  var mercuryRefresh = jQueryFrameFn("#page\\:mainForm\\:sheet\\:1\\:refresh");
  var marsTimestamp = jQueryFrameFn("#page\\:mainForm\\:sheet\\:4\\:timestamp");
  var marsRefresh = jQueryFrameFn("#page\\:mainForm\\:sheet\\:4\\:refresh");

  var oldMercuryTimestamp = Number(mercuryTimestamp().text().trim());
  var oldMarsTimestamp = Number(marsTimestamp().text().trim());

  var TTT = new TobagoTestTool(assert);
  TTT.action(function () {
    mercuryRefresh().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    var newMercuryTimestamp = Number(mercuryTimestamp().text().trim());
    var newMarsTimestamp = Number(marsTimestamp().text().trim());

    assert.ok(newMercuryTimestamp > oldMercuryTimestamp);
    assert.equal(newMarsTimestamp, oldMarsTimestamp);

    oldMercuryTimestamp = newMercuryTimestamp;
    oldMarsTimestamp = newMarsTimestamp;
  });
  TTT.action(function () {
    marsRefresh().click();
  });
  TTT.waitForResponse();
  TTT.asserts(2, function () {
    var newMercuryTimestamp = Number(mercuryTimestamp().text().trim());
    var newMarsTimestamp = Number(marsTimestamp().text().trim());

    assert.equal(newMercuryTimestamp, oldMercuryTimestamp);
    assert.ok(newMarsTimestamp > oldMarsTimestamp);
  });
  TTT.startTest();
});
