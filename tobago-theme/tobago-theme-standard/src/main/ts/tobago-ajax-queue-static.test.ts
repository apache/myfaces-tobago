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

import {AjaxQueueStatic} from "./tobago-ajax-queue-static";
import {QueueItem} from "./tobago-ajax-queue";

test("Press button 1, 2, 3, 2, 1; than the queue is processed", () => {
  const queue: QueueItem[] = [];

  document.querySelector("body").replaceChildren(); //fresh body
  document.querySelector("body").insertAdjacentHTML("afterbegin", `
<BUTTON id="b1"/>
<BUTTON id="b2"/>
<BUTTON id="b3"/>
`);

  const button1 = document.getElementById("b1");
  const button1Func = () => console.log("faces.ajax.request for button 1");
  const button2 = document.getElementById("b2");
  const button2Func = () => console.log("faces.ajax.request for button 2");
  const button3 = document.getElementById("b3");
  const button3Func = () => console.log("faces.ajax.request for button 3");

  AjaxQueueStatic.request(queue, button1, new Event("click"), null, button1Func);
  let expectQueue: QueueItem[] = [];
  expectQueue.push({
    element: button1,
    event: new Event("click"),
    executeValues: [],
    renderIds: new Set<string>(),
    func: button1Func,
    inProgress: true
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.request(queue, button2, new Event("click"), null, button2Func);
  expectQueue.push({
    element: button2,
    event: new Event("click"),
    executeValues: [],
    renderIds: new Set<string>(),
    func: button2Func,
    inProgress: false
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.request(queue, button3, new Event("click"), null, button3Func);
  expectQueue.push({
    element: button3,
    event: new Event("click"),
    executeValues: [],
    renderIds: new Set<string>(),
    func: button3Func,
    inProgress: false
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.request(queue, button2, new Event("click"), null, button2Func);
  expectQueue = [];
  expectQueue.push({
    element: button1,
    event: new Event("click"),
    executeValues: [],
    renderIds: new Set<string>(),
    func: button1Func,
    inProgress: true
  });
  expectQueue.push({
    element: button3,
    event: new Event("click"),
    executeValues: [],
    renderIds: new Set<string>(),
    func: button3Func,
    inProgress: false
  });
  expectQueue.push({
    element: button2,
    event: new Event("click"),
    executeValues: [],
    renderIds: new Set<string>(),
    func: button2Func,
    inProgress: false
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.request(queue, button1, new Event("click"), null, button1Func);
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, button1, "event", "begin");
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, button1, "event", "complete");
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, button1, "event", "success");
  expectQueue = [];
  expectQueue.push({
    element: button3,
    event: new Event("click"),
    executeValues: [],
    renderIds: new Set<string>(),
    func: button3Func,
    inProgress: true
  });
  expectQueue.push({
    element: button2,
    event: new Event("click"),
    executeValues: [],
    renderIds: new Set<string>(),
    func: button2Func,
    inProgress: false
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, button3, "event", "begin");
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, button3, "error", "httpError");
  expectQueue = [];
  expectQueue.push({
    element: button2,
    event: new Event("click"),
    executeValues: [],
    renderIds: new Set<string>(),
    func: button2Func,
    inProgress: true
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, button2, "event", "begin");
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, button2, "event", "complete");
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, button2, "event", "success");
  expectQueue = [];
  expect(queue).toEqual(expectQueue);
});

test("getExecuteValues()", () => {
  document.querySelector("body").replaceChildren(); //fresh body
  document.querySelector("body").insertAdjacentHTML("afterbegin", `
<tobago-in id="page:mainForm:tcIn" class="tobago-auto-spacing">
  <input type="text" name="page:mainForm:tcIn" id="page:mainForm:tcIn::field" value="tc:in" class="form-control">
</tobago-in>
<tobago-date id="page:mainForm:tcDate" class="tobago-label-container tobago-auto-spacing">
  <label for="page:mainForm:tcDate::field" class="col-form-label">Date</label>
  <div class="input-group">
    <input type="date" name="page:mainForm:tcDate" id="page:mainForm:tcDate::field" value="1969-07-20" max="9999-12-31" class="form-control">
  </div>
</tobago-date>
<tobago-range id="page:mainForm:tcRange" class="tobago-label-container tobago-auto-spacing">
  <label for="page:mainForm:tcRange::field" class="col-form-label">0 to 100</label>
  <span class="tobago-tooltip fade" style="top: 157px; left: 805.5px;">10</span>
  <input type="range" name="page:mainForm:tcRange" id="page:mainForm:tcRange::field" value="10" min="0" max="100" step="1" class="tobago-range form-range">
</tobago-range>
<tobago-textarea id="page:mainForm:tcTextarea" class="tobago-auto-spacing">
  <textarea name="page:mainForm:tcTextarea" id="page:mainForm:tcTextarea::field" class="form-control">tc:textarea</textarea>
</tobago-textarea>
<tobago-select-boolean-checkbox id="page:mainForm:checkboxUnchecked" class="tobago-auto-spacing">
  <div class="form-check col-form-label">
    <input class="form-check-input" type="checkbox" value="true" name="page:mainForm:checkboxUnchecked" id="page:mainForm:checkboxUnchecked::field">
    <label class="form-check-label" for="page:mainForm:checkboxUnchecked::field"></label>
  </div>
</tobago-select-boolean-checkbox>
<tobago-select-boolean-checkbox id="page:mainForm:checkboxChecked" class="tobago-auto-spacing">
  <div class="form-check col-form-label">
    <input class="form-check-input" type="checkbox" value="true" name="page:mainForm:checkboxChecked" id="page:mainForm:checkboxChecked::field" checked="checked">
    <label class="form-check-label" for="page:mainForm:checkboxChecked::field"></label>
  </div>
</tobago-select-boolean-checkbox>
<tobago-select-one-radio id="page:mainForm:radioButtons" class="tobago-auto-spacing">
  <div>
    <div class="form-check">
      <input class="form-check-input" type="radio" checked="checked" name="page:mainForm:radioButtons" id="page:mainForm:radioButtons::0" value="solis">
      <label class="form-check-label" for="page:mainForm:radioButtons::0">Sun</label>
    </div>
    <div class="form-check">
      <input class="form-check-input" type="radio" name="page:mainForm:radioButtons" id="page:mainForm:radioButtons::1" value="luna">
      <label class="form-check-label" for="page:mainForm:radioButtons::1">Moon</label>
    </div>
    <div class="form-check">
      <input class="form-check-input" type="radio" name="page:mainForm:radioButtons" id="page:mainForm:radioButtons::2" value="stella">
      <label class="form-check-label" for="page:mainForm:radioButtons::2">Stars</label>
    </div>
  </div>
</tobago-select-one-radio>
<tobago-select-one-listbox id="page:mainForm:oneListbox" class="tobago-auto-spacing">
  <select id="page:mainForm:oneListbox::field" name="page:mainForm:oneListbox" class="form-select" size="3">
    <option value="alpha" selected="selected">Alpha</option>
    <option value="beta">Beta</option>
    <option value="gamma">Gamma</option>
    <option value="delta">Delta</option>
  </select>
</tobago-select-one-listbox>
<tobago-select-many-listbox id="page:mainForm:manyListbox" class="tobago-label-container tobago-auto-spacing">
  <label for="page:mainForm:manyListbox::field" class="col-form-label">Deserts</label>
  <select id="page:mainForm:manyListbox::field" name="page:mainForm:manyListbox" class="form-select" multiple="multiple" size="4">
    <option value="Antarctic Desert">Antarctic Desert</option>
    <option value="Arctic">Arctic</option>
    <option value="Sahara" selected="selected">Sahara</option>
    <option value="Arabian Desert">Arabian Desert</option>
    <option value="Gobi Desert" selected="selected">Gobi Desert</option>
  </select>
</tobago-select-many-listbox>
<button type="button" id="page:mainForm:submit" name="page:mainForm:submit" class="tobago-button btn btn-secondary tobago-auto-spacing">
  <tobago-behavior event="click" client-id="page:mainForm:submit"></tobago-behavior>
  <span>Submit</span>
</button>
`);

  const element = document.getElementById("page:mainForm:submit");
  const execute = "page:mainForm:tcIn"
      + " page:mainForm:tcDate"
      + " page:mainForm:tcRange"
      + " page:mainForm:tcTextarea"
      + " page:mainForm:checkboxUnchecked"
      + " page:mainForm:checkboxChecked"
      + " page:mainForm:radioButtons"
      + " page:mainForm:oneListbox"
      + " page:mainForm:manyListbox"
      + " page:mainForm:submit";
  const result = AjaxQueueStatic.getExecuteValues(element, execute);
  const expected: string[] = [];
  expected.push("tc:in");
  expected.push("1969-07-20");
  expected.push("10");
  expected.push("tc:textarea");
  expected.push("false"); //checkbox unchecked
  expected.push("true"); //checkbox checked
  expected.push("true"); //radio button
  expected.push("false"); //radio button
  expected.push("false"); //radio button
  expected.push("true"); //tobago-select-one-listbox
  expected.push("false"); //tobago-select-one-listbox
  expected.push("false"); //tobago-select-one-listbox
  expected.push("false"); //tobago-select-one-listbox
  expected.push("false"); //tobago-select-many-listbox
  expected.push("false");  //tobago-select-many-listbox
  expected.push("true"); //tobago-select-many-listbox
  expected.push("false"); //tobago-select-many-listbox
  expected.push("true"); //tobago-select-many-listbox

  expect(result).toEqual(expected);
});

test("Input field: type 'h', 'i', delete 'i'", () => {
  const queue: QueueItem[] = [];
  let expectQueue: QueueItem[] = [];
  const dummyFunction = () => console.log("dummy function");

  document.querySelector("body").replaceChildren(); //fresh body
  document.querySelector("body").insertAdjacentHTML("afterbegin", `
<tobago-in id="page:mainForm:inputAjax" class="tobago-label-container tobago-auto-spacing">
  <label for="page:mainForm:inputAjax::field" class="col-form-label">Input</label>
  <div class="input-group">
    <span class="input-group-text">AJAX</span>
    <input type="text" name="page:mainForm:inputAjax" id="page:mainForm:inputAjax::field" class="form-control">
    <tobago-behavior event="input" client-id="page:mainForm:inputAjax" field-id="page:mainForm:inputAjax::field" execute="page:mainForm:inputAjax" render="page:mainForm:outputAjax"></tobago-behavior>
    <span class="input-group-text">on change</span>
  </div>
</tobago-in>`);
  const tobagoIn = document.getElementById("page:mainForm:inputAjax");
  const inputField: HTMLInputElement = tobagoIn.querySelector("input[id='page:mainForm:inputAjax::field']");

  inputField.value = "h";
  AjaxQueueStatic.request(queue, tobagoIn, new Event("input"), {
    execute: "page:mainForm:inputAjax",
    render: "page:mainForm:outputAjax"
  }, dummyFunction);
  const renderIdsSet = new Set<string>;
  renderIdsSet.add("page:mainForm:outputAjax");
  expectQueue.push({
    element: tobagoIn,
    event: new Event("input"),
    executeValues: ["h"],
    renderIds: renderIdsSet,
    func: dummyFunction,
    inProgress: true
  });
  expect(queue).toEqual(expectQueue);

  inputField.value = "hi";
  AjaxQueueStatic.request(queue, tobagoIn, new Event("input"), {
    execute: "page:mainForm:inputAjax",
    render: "page:mainForm:outputAjax"
  }, dummyFunction);
  expectQueue.push({
    element: tobagoIn,
    event: new Event("input"),
    executeValues: ["hi"],
    renderIds: renderIdsSet,
    func: dummyFunction,
    inProgress: false
  });
  expect(queue).toEqual(expectQueue);

  inputField.value = "h";
  AjaxQueueStatic.request(queue, tobagoIn, new Event("input"), {
    execute: "page:mainForm:inputAjax",
    render: "page:mainForm:outputAjax"
  }, dummyFunction);
  expectQueue = [];
  expectQueue.push({
    element: tobagoIn,
    event: new Event("input"),
    executeValues: ["h"],
    renderIds: renderIdsSet,
    func: dummyFunction,
    inProgress: true
  });
  expect(queue).toEqual(expectQueue);
});

test("SelectManyList server side filtering; click, search for 'hea', click 'Rhea'", () => {
  const queue: QueueItem[] = [];

  document.querySelector("body").replaceChildren(); //fresh body
  document.querySelector("body").insertAdjacentHTML("afterbegin", `
<tobago-select-many-list id="page:mainForm:selectManyList" class="tobago-label-container tobago-auto-spacing tobago-focus">
  <label for="page:mainForm:selectManyList::field" class="col-form-label">selectManyList</label>
  <select id="page:mainForm:selectManyList::field" name="page:mainForm:selectManyList" class="d-none" multiple="multiple">
  </select>
  <div class="dropdown">
    <div id="page:mainForm:selectManyList::selectField" name="page:mainForm:selectManyList" class="form-select tobago-select-field show" aria-expanded="true">
      <div class="tobago-badges">
        <span class="btn-group" role="group" data-tobago-value="Amalthea">
          <tobago-badge class="badge text-bg-primary btn">Amalthea</tobago-badge>
          <button type="button" class="tobago-button btn btn-secondary badge" aria-label="deselect Amalthea">
            <i class="bi-x-lg"></i>
          </button>
        </span>
        <span class="btn-group" role="group" data-tobago-value="Rhea">
          <tobago-badge class="badge text-bg-primary btn">Rhea</tobago-badge>
          <button type="button" class="tobago-button btn btn-secondary badge" aria-label="deselect Rhea">
            <i class="bi-x-lg"></i>
          </button>
        </span>
      </div>
      <input type="search" id="page:mainForm:selectManyList::filter" class="tobago-filter form-control" autocomplete="off">
    </div>
  </div>
  <input id="page:mainForm:selectItemsFilteredMany::query" type="hidden" name="page:mainForm:selectItemsFilteredMany::query" data-tobago-delay="200" data-tobago-min-chars="0">
  <tobago-behavior event="click" client-id="page:mainForm:selectManyList" field-id="page:mainForm:selectManyList::field" execute="page:mainForm:selectManyList" render="page:mainForm:statistics"></tobago-behavior>
  <tobago-behavior event="dblclick" client-id="page:mainForm:selectManyList" field-id="page:mainForm:selectManyList::field" execute="page:mainForm:selectManyList" render="page:mainForm:statistics"></tobago-behavior>
  <tobago-behavior event="change" client-id="page:mainForm:selectManyList" field-id="page:mainForm:selectManyList::field" execute="page:mainForm:selectManyList" render="page:mainForm:statistics page:mainForm:outputSelectManyList"></tobago-behavior>
  <tobago-behavior event="focus" client-id="page:mainForm:selectManyList" field-id="page:mainForm:selectManyList::field" execute="page:mainForm:selectManyList" render="page:mainForm:statistics"></tobago-behavior>
  <tobago-behavior event="blur" client-id="page:mainForm:selectManyList" field-id="page:mainForm:selectManyList::field" execute="page:mainForm:selectManyList" render="page:mainForm:statistics"></tobago-behavior>
  <div class="spinner"></div>
</tobago-select-many-list>`);

  const functionFilterEvent = () => console.log("serverSideFiltering filter event");
  const functionSelectEvent = () => console.log("serverSideFiltering select event");
  const functionClickEvent = () => console.log("click ajax event");
  const functionChangeEvent = () => console.log("change ajax event");
  const functionFocusEvent = () => console.log("focus ajax event");
  const tobagoSelectManyList = document.getElementById("page:mainForm:selectManyList");

  //focus + click
  AjaxQueueStatic.request(queue, tobagoSelectManyList, new Event("focus"), {render: "page:mainForm:statistics"}, functionFocusEvent);
  AjaxQueueStatic.request(queue, tobagoSelectManyList, new Event("click"), {render: "page:mainForm:statistics"}, functionClickEvent);
  let executeValues: string[] = [];
  executeValues.push(""); //page:mainForm:selectManyList::filter
  executeValues.push(""); //page:mainForm:selectItemsFilteredMany::query
  let renderIds: Set<string> = new Set<string>();
  renderIds.add("page:mainForm:statistics");
  let expectQueue: QueueItem[] = [];
  expectQueue.push({
    element: tobagoSelectManyList,
    event: new Event("focus"),
    executeValues: executeValues,
    renderIds: renderIds,
    func: functionFocusEvent,
    inProgress: true
  });
  expectQueue.push({
    element: tobagoSelectManyList,
    event: new Event("click"),
    executeValues: executeValues,
    renderIds: renderIds,
    func: functionClickEvent,
    inProgress: false
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "begin");
  expect(queue).toEqual(expectQueue);
  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "complete");
  expect(queue).toEqual(expectQueue);
  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "success");
  expectQueue = [];
  expectQueue.push({
    element: tobagoSelectManyList,
    event: new Event("click"),
    executeValues: executeValues,
    renderIds: renderIds,
    func: functionClickEvent,
    inProgress: true
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "begin");
  expect(queue).toEqual(expectQueue);
  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "complete");
  expect(queue).toEqual(expectQueue);
  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "success");
  expectQueue = [];
  expect(queue).toEqual(expectQueue);

  //server side filtering event "h", "he", "hea"
  const queryElement: HTMLInputElement = document.querySelector("input[id='page:mainForm:selectItemsFilteredMany::query']");
  queryElement.value = "h";
  AjaxQueueStatic.request(queue, tobagoSelectManyList, null, {
    execute: "page:mainForm:selectManyList",
    render: "page:mainForm:selectManyList"
  }, functionFilterEvent);
  queryElement.value = "he";
  AjaxQueueStatic.request(queue, tobagoSelectManyList, null, {
    execute: "page:mainForm:selectManyList",
    render: "page:mainForm:selectManyList"
  }, functionFilterEvent);
  queryElement.value = "hea";
  AjaxQueueStatic.request(queue, tobagoSelectManyList, null, {
    execute: "page:mainForm:selectManyList",
    render: "page:mainForm:selectManyList"
  }, functionFilterEvent);
  expectQueue = [];
  executeValues = [];
  executeValues.push(""); //page:mainForm:selectManyList::filter
  executeValues.push("h"); //page:mainForm:selectItemsFilteredMany::query
  renderIds = new Set<string>();
  renderIds.add("page:mainForm:selectManyList");
  expectQueue.push({
    element: tobagoSelectManyList,
    event: null,
    executeValues: executeValues,
    renderIds: renderIds,
    func: functionFilterEvent,
    inProgress: true
  });
  executeValues = [];
  executeValues.push(""); //page:mainForm:selectManyList::filter
  executeValues.push("hea"); //page:mainForm:selectItemsFilteredMany::query
  renderIds = new Set<string>();
  renderIds.add("page:mainForm:selectManyList");
  expectQueue.push({
    element: tobagoSelectManyList,
    event: null,
    executeValues: executeValues,
    renderIds: renderIds,
    func: functionFilterEvent,
    inProgress: false
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "begin");
  expect(queue).toEqual(expectQueue);
  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "complete");
  expect(queue).toEqual(expectQueue);
  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "success");
  expectQueue = [];
  expectQueue.push({
    element: tobagoSelectManyList,
    event: null,
    executeValues: executeValues,
    renderIds: renderIds,
    func: functionFilterEvent,
    inProgress: true
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "begin");
  expect(queue).toEqual(expectQueue);
  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "complete");
  expect(queue).toEqual(expectQueue);
  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "success");
  expectQueue = [];
  expect(queue).toEqual(expectQueue);

  const hiddenSelectField = document.querySelector("select[id='page:mainForm:selectManyList::field']");
  hiddenSelectField.insertAdjacentHTML("afterbegin", `
<option value="Amalthea">Amalthea</option>
<option value="Lysithea">Lysithea</option>
<option value="Rhea">Rhea</option>`);

  //click on "Rhea" trigger a select event (special event for server filtered lists), a change event and a click event
  const rheaOption: HTMLOptionElement = document.querySelector("select option[value='Rhea']");
  rheaOption.selected = true;
  AjaxQueueStatic.request(queue, tobagoSelectManyList, null, null, functionSelectEvent);
  AjaxQueueStatic.request(queue, tobagoSelectManyList, new Event("change"), {render: "page:mainForm:statistics page:mainForm:outputSelectManyList"}, functionChangeEvent);
  AjaxQueueStatic.request(queue, tobagoSelectManyList, new Event("click"), {render: "page:mainForm:statistics"}, functionClickEvent);
  expectQueue = [];
  executeValues = [];
  executeValues.push(""); //page:mainForm:selectManyList::filter
  executeValues.push("hea"); //page:mainForm:selectItemsFilteredMany::query
  executeValues.push("false"); //Amalthea
  executeValues.push("false"); //Lysithea
  executeValues.push("true"); //Rhea
  expectQueue.push({
    element: tobagoSelectManyList,
    event: null,
    executeValues: executeValues,
    renderIds: new Set<string>(),
    func: functionSelectEvent,
    inProgress: true
  });
  renderIds = new Set<string>();
  renderIds.add("page:mainForm:statistics");
  renderIds.add("page:mainForm:outputSelectManyList");
  expectQueue.push({
    element: tobagoSelectManyList,
    event: new Event("change"),
    executeValues: executeValues,
    renderIds: renderIds,
    func: functionChangeEvent,
    inProgress: false
  });
  renderIds = new Set<string>();
  renderIds.add("page:mainForm:statistics");
  expectQueue.push({
    element: tobagoSelectManyList,
    event: new Event("click"),
    executeValues: executeValues,
    renderIds: renderIds,
    func: functionClickEvent,
    inProgress: false
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "begin");
  expect(queue).toEqual(expectQueue);
  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "complete");
  expect(queue).toEqual(expectQueue);
  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "success");
  expectQueue = [];
  renderIds = new Set<string>();
  renderIds.add("page:mainForm:statistics");
  renderIds.add("page:mainForm:outputSelectManyList");
  expectQueue.push({
    element: tobagoSelectManyList,
    event: new Event("change"),
    executeValues: executeValues,
    renderIds: renderIds,
    func: functionChangeEvent,
    inProgress: true
  });
  renderIds = new Set<string>();
  renderIds.add("page:mainForm:statistics");
  expectQueue.push({
    element: tobagoSelectManyList,
    event: new Event("click"),
    executeValues: executeValues,
    renderIds: renderIds,
    func: functionClickEvent,
    inProgress: false
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "begin");
  expect(queue).toEqual(expectQueue);
  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "complete");
  expect(queue).toEqual(expectQueue);
  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "success");
  expectQueue = [];
  renderIds = new Set<string>();
  renderIds.add("page:mainForm:statistics");
  expectQueue.push({
    element: tobagoSelectManyList,
    event: new Event("click"),
    executeValues: executeValues,
    renderIds: renderIds,
    func: functionClickEvent,
    inProgress: true
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "begin");
  expect(queue).toEqual(expectQueue);
  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "complete");
  expect(queue).toEqual(expectQueue);
  AjaxQueueStatic.ajaxEventListener(queue, tobagoSelectManyList, "event", "success");
  expectQueue = [];
  expect(queue).toEqual(expectQueue);
});

test("Dblclick on a button with f:ajax for click and dblclick", () => {
  const queue: QueueItem[] = [];
  let expectQueue: QueueItem[] = [];

  document.querySelector("body").replaceChildren(); //fresh body
  document.querySelector("body").insertAdjacentHTML("afterbegin", `
<BUTTON id="button">
  <tobago-behavior event="click" client-id="button" execute="button"></tobago-behavior>
  <tobago-behavior event="dblclick" client-id="button" execute="button"></tobago-behavior>
</BUTTON>`);

  const button = document.getElementById("button");
  const clickFunction = () => console.log("faces.ajax.request: click event");
  const dblclickFunction = () => console.log("faces.ajax.request: dblclick event");

  AjaxQueueStatic.request(queue, button, new Event("click"), null, clickFunction);
  expectQueue.push({
    element: button,
    event: new Event("click"),
    executeValues: [],
    renderIds: new Set<string>(),
    func: clickFunction,
    inProgress: true
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.request(queue, button, new Event("click"), null, clickFunction);
  AjaxQueueStatic.request(queue, button, new Event("dblclick"), null, dblclickFunction);
  expectQueue.push({
    element: button,
    event: new Event("dblclick"),
    executeValues: [],
    renderIds: new Set<string>(),
    func: dblclickFunction,
    inProgress: false
  });
  expect(queue).toEqual(expectQueue);
});

test("Ignore and remove unconnected queue item", () => {
  const queue: QueueItem[] = [];

  document.querySelector("body").replaceChildren(); //fresh body
  const domWrapperElement = `
<tobago-panel id="wrapper">
  <button type="button" id="button">
    <tobago-behavior event="focus" client-id="button" execute="button" render="wrapper"></tobago-behavior>
    <tobago-behavior event="click" client-id="button" execute="button" render="wrapper"></tobago-behavior>
    <tobago-behavior event="dblclick" client-id="button" execute="button" render="wrapper"></tobago-behavior>
    <span>multiple ajax</span>
  </button>
</tobago-panel>
`;
  document.querySelector("body").insertAdjacentHTML("afterbegin", domWrapperElement);

  const button = document.getElementById("button");
  const focusFunction = () => console.log("faces.ajax.request focus event");
  const clickFunction = () => console.log("faces.ajax.request click event");
  const dblclickFunction = () => console.log("faces.ajax.request dblclick event");

  //click 4 times on the button (fast).
  AjaxQueueStatic.request(queue, button, new Event("focus"), {render: "wrapper"}, focusFunction);
  const renderIds = new Set<string>();
  renderIds.add("wrapper");
  let expectQueue: QueueItem[] = [];
  expectQueue.push({
    element: button,
    event: new Event("focus"),
    executeValues: [],
    renderIds: renderIds,
    func: focusFunction,
    inProgress: true
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.request(queue, button, new Event("click"), {render: "wrapper"}, clickFunction);
  expectQueue.push({
    element: button,
    event: new Event("click"),
    executeValues: [],
    renderIds: renderIds,
    func: clickFunction,
    inProgress: false
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.request(queue, button, new Event("dblclick"), {render: "wrapper"}, dblclickFunction);
  expectQueue.push({
    element: button,
    event: new Event("dblclick"),
    executeValues: [],
    renderIds: renderIds,
    func: dblclickFunction,
    inProgress: false
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.request(queue, button, new Event("click"), {render: "wrapper"}, clickFunction);
  expectQueue = [];
  expectQueue.push({
    element: button,
    event: new Event("focus"),
    executeValues: [],
    renderIds: renderIds,
    func: focusFunction,
    inProgress: true
  });
  expectQueue.push({
    element: button,
    event: new Event("dblclick"),
    executeValues: [],
    renderIds: renderIds,
    func: dblclickFunction,
    inProgress: false
  });
  expectQueue.push({
    element: button,
    event: new Event("click"),
    executeValues: [],
    renderIds: renderIds,
    func: clickFunction,
    inProgress: false
  });
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, button, "event", "begin");
  expect(queue).toEqual(expectQueue);

  AjaxQueueStatic.ajaxEventListener(queue, button, "event", "complete");
  expect(queue).toEqual(expectQueue);

  // After the focus Ajax request is done, the elements in the queue are no longer connected to the DOM.
  document.querySelector("#wrapper").remove();
  document.querySelector("body").insertAdjacentHTML("afterbegin", domWrapperElement);
  AjaxQueueStatic.ajaxEventListener(queue, button, "event", "success");

  expectQueue = [];
  expect(queue).toEqual(expectQueue);
});
