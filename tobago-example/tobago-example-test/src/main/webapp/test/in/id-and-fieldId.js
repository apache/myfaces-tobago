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

jQuery(document).ready(function() {

  fillIdDisplay("0", ".tobago-in");
  fillIdDisplay("1", ".tobago-in");
  fillIdDisplay("2", ".tobago-in");
  fillIdDisplay("3", ".tobago-in");
  fillIdDisplay("4", ".tobago-in");
  fillIdDisplay("5", ".tobago-date");
  fillIdDisplay("6", ".tobago-time");
  fillIdDisplay("7", ".tobago-file");
  fillIdDisplay("8", ".tobago-textarea");
  fillIdDisplay("9", ".tobago-selectBooleanCheckbox");
  fillIdDisplay("10", ".tobago-selectManyCheckbox");
  fillIdDisplay("11", ".tobago-selectManyListbox");
  fillIdDisplay("12", ".tobago-selectOneChoice");
  fillIdDisplay("13", ".tobago-selectOneListbox");
  fillIdDisplay("14", ".tobago-selectOneRadio");

  test("page:panel2", "page:alpha");
  test("page:field3", "page:beta");
  test("page:panel4", "page:gamma");
  test("page:field4", "page:delta");
  test("page:panel5", "page:datePanel");
  test("page:field5", "page:dateField");
  test("page:panel6", "page:timePanel");
  test("page:field6", "page:timeField");
  test("page:panel7", "page:filePanel");
  test("page:field7", "page:fileField");
  test("page:panel8", "page:textareaPanel");
  test("page:field8", "page:textareaField");
  test("page:panel9", "page:selectBooleanCheckboxPanel");
  test("page:field9", "page:selectBooleanCheckboxField");
  test("page:panel10", "page:selectManyCheckboxPanel");
  test("page:field10", "page:selectManyCheckboxField");
  test("page:panel11", "page:selectManyListboxPanel");
  test("page:field11", "page:selectManyListboxField");
  test("page:panel12", "page:selectOneChoicePanel");
  test("page:field12", "page:selectOneChoiceField");
  test("page:panel13", "page:selectOneListboxPanel");
  test("page:field13", "page:selectOneListboxField");
  test("page:panel14", "page:selectOneRadioPanel");
  test("page:field14", "page:selectOneRadioField");

});

function fillIdDisplay(i, fieldClass) {
  var panelIn = jQuery(Tobago.Utils.escapeClientId("page:panel" + i));
  var labelIn = jQuery(Tobago.Utils.escapeClientId("page:label" + i));
  var fieldIn = jQuery(Tobago.Utils.escapeClientId("page:field" + i));

  var panel = panelIn.prev();
  var label = panel.children(".tobago-label");
  var field = panel.children(fieldClass);

  panelIn.val(panel.attr("id"));
  labelIn.val(label.attr("id"));
  fieldIn.val(field.attr("id"));
}

function test(testId, expected) {
  var element = jQuery(Tobago.Utils.escapeClientId(testId));
  if (element.val() != expected) {
    element.addClass("tobago-in-markup-error");
    element.attr("title", "expected: '" + expected + "'");
  }
}
