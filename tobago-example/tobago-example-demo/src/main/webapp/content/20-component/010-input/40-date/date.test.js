/**
 * Created by henning on 15.06.16.
 */

function jQueryFrame(expression) {
  return document.getElementById("page:testframe").contentWindow.jQuery(expression);
}

function getToday() {
  var now = new Date();
  var dd = now.getDate();
  var mm = now.getMonth() + 1;
  var yyyy = now.getFullYear();
  if (dd < 10) {
    dd = '0' + dd
  }
  if (mm < 10) {
    mm = '0' + mm
  }
  return dd + "." + mm + "." + yyyy;
}

QUnit.test("date with label", function (assert) {
  var $label = jQueryFrame("#page\\:dnormal > label");
  var $dateField = jQueryFrame("#page\\:dnormal\\:\\:field");
  var $dateButton = jQueryFrame("#page\\:dnormal button");
  var today = getToday();

  assert.equal($label.text(), "Date");
  assert.equal($dateField.val(), today);

  $dateField.val("32.05.2016");
  $dateButton.click();

  assert.equal($dateField.val(), today);
});

QUnit.test("submit", function (assert) {
  var $dateField = jQueryFrame("#page\\:fsbmt\\:sbmtinput\\:\\:field");
  var $dateButton = jQueryFrame("#page\\:fsbmt\\:sbmtinput button");
  var $outField = jQueryFrame("#page\\:fsbmt\\:sbmtoutput span");
  var $submitButton = jQueryFrame("#page\\:fsbmt\\:sbmtbutton");

  assert.equal($dateField.val(), "22.05.2016");
  assert.equal($outField.text(), "22.05.2016");

  $dateButton.click();
  assert.ok(jQueryFrame(".bootstrap-datetimepicker-widget").get(0));
  jQueryFrame(".bootstrap-datetimepicker-widget .day").get(37).click(); // Choose '01.06.2016'.

  assert.equal($dateField.val(), "01.06.2016");
  $submitButton.click();

  var done = assert.async();
  setTimeout(function () {
    $outField = jQueryFrame("#page\\:fsbmt\\:sbmtoutput span");
    assert.equal($outField.text(), "01.06.2016");
    done();
  }, 500);
});

QUnit.test("ajax", function (assert) {
  var $dateField = jQueryFrame("#page\\:ajaxinput\\:\\:field");
  var $dateButton = jQueryFrame("#page\\:ajaxinput button");
  var $outField = jQueryFrame("#page\\:outputfield span");
  var today = getToday();

  assert.equal($dateField.val(), "");
  assert.equal($outField.text(), "");

  $dateButton.click();

  assert.ok(jQueryFrame(".bootstrap-datetimepicker-widget").get(0));
  assert.equal($dateField.val(), today);

  var done = assert.async();
  setTimeout(function () {
    $outField = jQueryFrame("#page\\:outputfield span");
    assert.equal($outField.text(), today);
    done();
  }, 500);
});
