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

QUnit.test("tc:button", function(assert) {
  var eventNames = ["click", "dblclick", "focus", "blur"];
  var $eventComponent = jQueryFrame("#page\\:mainForm\\:buttonevent");
  var $ajaxComponent = jQueryFrame("#page\\:mainForm\\:buttonajax");
  testEvent(assert, "button", eventNames, $eventComponent, $ajaxComponent, null);
});

QUnit.test("tc:in", function(assert) {
  var eventNames = ["change", "click", "dblclick", "focus", "blur"];
  var $eventComponent = jQueryFrame("#page\\:mainForm\\:inevent\\:\\:field");
  var $ajaxComponent = jQueryFrame("#page\\:mainForm\\:inajax\\:\\:field");
  var changeValue = function($component) {
    var oldValue = $component.val();
    var newValue = "hello";
    if (oldValue == "hello") {
      newValue = "hello there!"
    }
    return $component.val(newValue);
  };
  testEvent(assert, "in", eventNames, $eventComponent, $ajaxComponent, changeValue);
});

QUnit.test("tc:row", function(assert) {
  var eventNames = ["click", "dblclick"];
  var $eventComponent = jQueryFrame("#page\\:mainForm\\:sheetevent\\:0\\:selectPlanet");
  var $ajaxComponent = jQueryFrame("#page\\:mainForm\\:sheetajax\\:0\\:selectPlanet");
  testEvent(assert, "row", eventNames, $eventComponent, $ajaxComponent, null);
});

QUnit.test("tc:selectBooleanCheckbox", function(assert) {
  var eventNames = ["change", "click", "dblclick", "focus", "blur"];
  var $eventComponent = jQueryFrame("#page\\:mainForm\\:selectBooleanCheckboxevent\\:\\:field");
  var $ajaxComponent = jQueryFrame("#page\\:mainForm\\:selectBooleanCheckboxajax\\:\\:field");
  var changeValue = function($component) {
    var currentEvent = jQueryFrame("#page\\:mainForm\\:outEventName span").text();
    if (currentEvent != "click") {
      console.log("old -- currentEvent: " + currentEvent + " isChecked: " + $component.prop("checked"));
      return $component.prop("checked", !$component.prop("checked"));
      console.log("new -- currentEvent: " + currentEvent + " isChecked: " + $component.prop("checked"));
    } else {
      return $component;
    }
  };
  testEvent(assert, "selectBooleanCheckbox", eventNames, $eventComponent, $ajaxComponent, changeValue);
});

QUnit.test("tc:textarea", function(assert) {
  var eventNames = ["change", "click", "dblclick", "focus", "blur"];
  var $eventComponent = jQueryFrame("#page\\:mainForm\\:textareaevent\\:\\:field");
  var $ajaxComponent = jQueryFrame("#page\\:mainForm\\:textareaajax\\:\\:field");
  var changeValue = function($component) {
    var oldValue = $component.val();
    var newValue = "hello";
    if (oldValue == "hello") {
      newValue = "hello there!"
    }
    return $component.val(newValue);
  };
  testEvent(assert, "textarea", eventNames, $eventComponent, $ajaxComponent, changeValue);
});

function testEvent(assert, componentName, eventNames, $eventComponent, $ajaxComponent, changeValue) {
  assert.expect(10 * eventNames.length);
  var step = 0;
  var done = assert.async(3 * eventNames.length);

  var oldActionCount;
  var oldActionListenerCount;
  var oldAjaxListenerCount;
  var oldValueChangeListenerCount;
  var oldTimestamp;

  var eventName = eventNames[0];
  activateComponent(componentName, eventName);

  jQuery("#page\\:testframe").load(function() {
    if (step % 3 == 0) {
      oldActionCount = getActionCount();
      oldActionListenerCount = getActionListenerCount();
      oldAjaxListenerCount = getAjaxListenerCount();
      oldValueChangeListenerCount = getValueChangeListenerCount();
      oldTimestamp = getTimestamp();

      $eventComponent = jQueryFrame($eventComponent.selector);
      if (changeValue != null) {
        $eventComponent = changeValue($eventComponent);
      }
      $eventComponent.trigger(eventName);
    } else if (step % 3 == 1) {
      var newActionCount = getActionCount();
      var newActionListenerCount = getActionListenerCount();
      var newAjaxListenerCount = getAjaxListenerCount();
      var newValueChangeListenerCount = getValueChangeListenerCount();
      var newTimestamp = getTimestamp();

      assert.equal(newActionCount, oldActionCount + 1, eventName + " - tc:event - action");
      assert.equal(newActionListenerCount, oldActionListenerCount + 1, eventName + " - tc:event - actionListener");
      assert.equal(newAjaxListenerCount, oldAjaxListenerCount, eventName + " - tc:event - ajaxListener");
      if (changeValue != null) {
        assert.equal(newValueChangeListenerCount, oldValueChangeListenerCount + 1,
            eventName + " - tc:event - valueChangeListener");
      } else {
        assert.equal(newValueChangeListenerCount, oldValueChangeListenerCount,
            eventName + " - tc:event - valueChangeListener");
      }
      assert.ok(newTimestamp > oldTimestamp, eventName + " - tc:event - timestamp");


      oldActionCount = getActionCount();
      oldActionListenerCount = getActionListenerCount();
      oldAjaxListenerCount = getAjaxListenerCount();
      oldValueChangeListenerCount = getValueChangeListenerCount();
      oldTimestamp = getTimestamp();

      $ajaxComponent = jQueryFrame($ajaxComponent.selector);
      if (changeValue != null) {
        $ajaxComponent = changeValue($ajaxComponent);
      }
      $ajaxComponent.trigger(eventName);

      waitForAjax(function() {
        newActionCount = getActionCount();
        newActionListenerCount = getActionListenerCount();
        newAjaxListenerCount = getAjaxListenerCount();
        newValueChangeListenerCount = getValueChangeListenerCount();
        newTimestamp = getTimestamp();

        return step % 3 == 2
            && newAjaxListenerCount == oldAjaxListenerCount + 1
            && newTimestamp > oldTimestamp;
      }, function() {
        newActionCount = getActionCount();
        newActionListenerCount = getActionListenerCount();
        newAjaxListenerCount = getAjaxListenerCount();
        newValueChangeListenerCount = getValueChangeListenerCount();
        newTimestamp = getTimestamp();

        assert.equal(newActionCount, oldActionCount, eventName + " - f:ajax - action");
        assert.equal(newActionListenerCount, oldActionListenerCount, eventName + " - f:ajax - actionListener");
        assert.equal(newAjaxListenerCount, oldAjaxListenerCount + 1, eventName + " - f:ajax - ajaxListener");
        if (changeValue != null) {
          assert.equal(newValueChangeListenerCount, oldValueChangeListenerCount + 1,
              eventName + " - f:ajax - valueChangeListener");
        } else {
          assert.equal(newValueChangeListenerCount, oldValueChangeListenerCount,
              eventName + " - f:ajax - valueChangeListener");
        }
        assert.ok(newTimestamp > oldTimestamp, eventName + " - f:ajax - timestamp");

        step++;
        done();

        // activate next event
        eventName = eventNames[step / 3];
        activateComponent(componentName, eventName);
      });
    }
    step++;
    done();
  });
}

function activateComponent(componentName, eventName) {
  jQueryFrame("#page\\:mainForm\\:componentTable .tobago-sheet-row").each(function() {
    if (jQuery(this).find("td").eq(0).find("span").text() == componentName) {
      jQuery(this).find("button").each(function() {
        var id = jQuery(this).attr("id");
        if (id != undefined && id.indexOf(eventName + "Behavior") >= 0) {
          this.click();
        }
      });
    }
  });
}

function getActionCount() {
  return parseInt(jQueryFrame("#page\\:mainForm\\:inAction\\:\\:field").val());
}

function getActionListenerCount() {
  return parseInt(jQueryFrame("#page\\:mainForm\\:inActionListener\\:\\:field").val());
}

function getAjaxListenerCount() {
  return parseInt(jQueryFrame("#page\\:mainForm\\:inAjaxListener\\:\\:field").val());
}

function getValueChangeListenerCount() {
  return parseInt(jQueryFrame("#page\\:mainForm\\:inValueChangeListener\\:\\:field").val());
}

function getTimestamp() {
  return parseInt(jQueryFrame("#page\\:mainForm\\:inTimestamp\\:\\:field").val());
}
