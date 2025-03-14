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

import {elementByIdFn, querySelectorAllFn, querySelectorFn} from "/script/tobago-test.js";
import {JasmineTestTool} from "/tobago/test/tobago-test-tool.js";

it("Basics: 'M'", function (done) {
  const inputFn = elementByIdFn("page:mainForm:inBasic::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length !== 10,
      () => inputFn().value = "", "input", inputFn);
  test.do(() => inputFn().value = "M");
  test.event("input", inputFn, () => resultListFn().length === 10);
  test.do(() => expect(resultListFn().length).toBe(10));
  test.do(() => expect(resultListFn()[0].textContent).toBe("Mercury"));
  test.do(() => expect(resultListFn()[1].textContent).toBe("Mars"));
  test.do(() => expect(resultListFn()[2].textContent).toBe("Moon"));
  test.do(() => expect(resultListFn()[3].textContent).toBe("Deimos"));
  test.do(() => expect(resultListFn()[4].textContent).toBe("Metis"));
  test.do(() => expect(resultListFn()[5].textContent).toBe("Amalthea"));
  test.do(() => expect(resultListFn()[6].textContent).toBe("Ganymede"));
  test.do(() => expect(resultListFn()[7].textContent).toBe("Themisto"));
  test.do(() => expect(resultListFn()[8].textContent).toBe("Himalia"));
  test.do(() => expect(resultListFn()[9].textContent).toBe("Carme"));
  test.event("blur", inputFn, () => resultListFn().length === 0);
  test.start();
});

it("Basics: 'Ma'", function (done) {
  const inputFn = elementByIdFn("page:mainForm:inBasic::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length !== 4,
      () => inputFn().value = "", "input", inputFn);
  test.do(() => inputFn().value = "Ma");
  test.event("input", inputFn, () => resultListFn().length === 4);
  test.do(() => expect(resultListFn().length).toBe(4));
  test.do(() => expect(resultListFn()[0].textContent).toBe("Mars"));
  test.do(() => expect(resultListFn()[1].textContent).toBe("Amalthea"));
  test.do(() => expect(resultListFn()[2].textContent).toBe("Himalia"));
  test.do(() => expect(resultListFn()[3].textContent).toBe("Mimas"));
  test.event("blur", inputFn, () => resultListFn().length === 0);
  test.start();
});

it("Basics: 'Mar'", function (done) {
  const inputFn = elementByIdFn("page:mainForm:inBasic::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length !== 1,
      () => inputFn().value = "", "input", inputFn);
  test.do(() => inputFn().value = "Mar");
  test.event("input", inputFn, () => resultListFn().length === 1);
  test.do(() => expect(resultListFn().length).toBe(1));
  test.do(() => expect(resultListFn()[0].textContent).toBe("Mars"));
  test.event("blur", inputFn, () => resultListFn().length === 0);
  test.start();
});

it("Basics: 'Mars'", function (done) {
  const inputFn = elementByIdFn("page:mainForm:inBasic::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length !== 1,
      () => inputFn().value = "", "input", inputFn);
  test.do(() => inputFn().value = "Mars");
  test.event("input", inputFn, () => resultListFn().length === 1);
  test.do(() => expect(resultListFn().length).toBe(1));
  test.do(() => expect(resultListFn()[0].textContent).toBe("Mars"));
  test.event("blur", inputFn, () => resultListFn().length === 0);
  test.start();
});

it("Basics: Add 'eus' and click first entry.", function (done) {
  const inputFn = elementByIdFn("page:mainForm:inBasic::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");
  const firstResult = querySelectorFn("#" + resultId + " [data-result-index='0'] > .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length !== 3,
      () => inputFn().value = "", "input", inputFn);
  test.do(() => inputFn().value = "eus");
  test.event("input", inputFn, () => resultListFn().length === 3);
  test.do(() => expect(resultListFn().length).toBe(3));
  test.do(() => expect(resultListFn()[0].textContent).toBe("Prometheus"));
  test.do(() => expect(resultListFn()[1].textContent).toBe("Epimetheus"));
  test.do(() => expect(resultListFn()[2].textContent).toBe("Proteus"));
  test.event("click", firstResult, () => inputFn().value === "Prometheus");
  test.do(() => expect(inputFn().value).toBe("Prometheus"));
  test.start();
});

it("Minimum Characters: 'C'", function (done) {
  const inputFn = elementByIdFn("page:mainForm:minimumCharacters::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length !== 0,
      () => inputFn().value = "Ear", "input", inputFn);
  test.do(() => inputFn().value = "C");
  test.event("input", inputFn, () => resultListFn().length === 0);
  test.do(() => expect(resultListFn().length).toBe(0));
  test.start();
});

it("Minimum Characters: 'Ca'", function (done) {
  const inputFn = elementByIdFn("page:mainForm:minimumCharacters::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length !== 7,
      () => inputFn().value = "Ear", "input", inputFn);
  test.do(() => inputFn().value = "Ca");
  test.event("input", inputFn, () => resultListFn().length === 7);
  test.do(() => expect(resultListFn().length).toBe(7));
  test.do(() => expect(resultListFn()[0].textContent).toBe("Callisto"));
  test.do(() => expect(resultListFn()[1].textContent).toBe("Carme"));
  test.do(() => expect(resultListFn()[2].textContent).toBe("Iocaste"));
  test.do(() => expect(resultListFn()[3].textContent).toBe("Callirrhoe"));
  test.do(() => expect(resultListFn()[4].textContent).toBe("Calypso"));
  test.do(() => expect(resultListFn()[5].textContent).toBe("Bianca"));
  test.do(() => expect(resultListFn()[6].textContent).toBe("Caliban"));
  test.event("blur", inputFn, () => resultListFn().length === 0);
  test.start();
});

it("Minimum Characters: Zero", function (done) {
  const inputFn = elementByIdFn("page:mainForm:minimumCharacters0::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length === 0, () => inputFn().value = "", "blur", inputFn);
  test.event("input", inputFn, () => resultListFn().length > 0);
  test.do(() => expect(resultListFn().length).toBeGreaterThan(0));
  test.event("blur", inputFn, () => resultListFn().length === 0);
  test.start();
});

it("Client side: 'Ju'", function (done) {
  const inputFn = elementByIdFn("page:mainForm:inClient::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length !== 2,
      () => inputFn().value = "", "input", inputFn);
  test.do(() => inputFn().value = "Ju");
  test.event("input", inputFn, () => resultListFn().length === 2);
  test.do(() => expect(resultListFn().length).toBe(2));
  test.do(() => expect(resultListFn()[0].textContent).toBe("Jupiter"));
  test.do(() => expect(resultListFn()[1].textContent).toBe("Juliet"));
  test.event("blur", inputFn, () => resultListFn().length === 0);
  test.start();
});

it("Client side - Filter All: 'me'", function (done) {
  const inputFn = elementByIdFn("page:mainForm:inClientFilterAll::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length !== 88,
      () => inputFn().value = "", "input", inputFn);
  test.do(() => inputFn().value = "me");
  test.event("input", inputFn, () => resultListFn().length === 88);
  test.do(() => expect(resultListFn().length).toBe(88));
  test.do(() => expect(resultListFn()[0].textContent).toBe("Sun"));
  test.do(() => expect(resultListFn()[1].textContent).toBe("Mercury"));
  test.do(() => expect(resultListFn()[2].textContent).toBe("Venus"));
  test.do(() => expect(resultListFn()[3].textContent).toBe("Earth"));
  test.do(() => expect(resultListFn()[4].textContent).toBe("Mars"));
  test.do(() => expect(resultListFn()[5].textContent).toBe("Jupiter"));
  test.do(() => expect(resultListFn()[6].textContent).toBe("Saturn"));
  test.do(() => expect(resultListFn()[7].textContent).toBe("Uranus"));
  test.do(() => expect(resultListFn()[8].textContent).toBe("Neptune"));
  test.do(() => expect(resultListFn()[9].textContent).toBe("Pluto"));
  test.do(() => expect(resultListFn()[10].textContent).toBe("Moon"));
  test.do(() => expect(resultListFn()[11].textContent).toBe("Phobos"));
  test.do(() => expect(resultListFn()[12].textContent).toBe("Deimos"));
  test.do(() => expect(resultListFn()[13].textContent).toBe("Metis"));
  test.do(() => expect(resultListFn()[14].textContent).toBe("Adrastea"));
  test.do(() => expect(resultListFn()[15].textContent).toBe("Amalthea"));
  test.do(() => expect(resultListFn()[16].textContent).toBe("Thebe"));
  test.do(() => expect(resultListFn()[17].textContent).toBe("Io"));
  test.do(() => expect(resultListFn()[18].textContent).toBe("Europa"));
  test.do(() => expect(resultListFn()[19].textContent).toBe("Ganymede"));
  test.do(() => expect(resultListFn()[20].textContent).toBe("Callisto"));
  test.do(() => expect(resultListFn()[21].textContent).toBe("Themisto"));
  test.do(() => expect(resultListFn()[22].textContent).toBe("Leda"));
  test.do(() => expect(resultListFn()[23].textContent).toBe("Himalia"));
  test.do(() => expect(resultListFn()[24].textContent).toBe("Lysithea"));
  test.do(() => expect(resultListFn()[25].textContent).toBe("Elara"));
  test.do(() => expect(resultListFn()[26].textContent).toBe("Ananke"));
  test.do(() => expect(resultListFn()[27].textContent).toBe("Carme"));
  test.do(() => expect(resultListFn()[28].textContent).toBe("Pasiphae"));
  test.do(() => expect(resultListFn()[29].textContent).toBe("Sinope"));
  test.do(() => expect(resultListFn()[30].textContent).toBe("Iocaste"));
  test.do(() => expect(resultListFn()[31].textContent).toBe("Harpalyke"));
  test.do(() => expect(resultListFn()[32].textContent).toBe("Praxidike"));
  test.do(() => expect(resultListFn()[33].textContent).toBe("Taygete"));
  test.do(() => expect(resultListFn()[34].textContent).toBe("Chaldene"));
  test.do(() => expect(resultListFn()[35].textContent).toBe("Kalyke"));
  test.do(() => expect(resultListFn()[36].textContent).toBe("Callirrhoe"));
  test.do(() => expect(resultListFn()[37].textContent).toBe("Megaclite"));
  test.do(() => expect(resultListFn()[38].textContent).toBe("Isonoe"));
  test.do(() => expect(resultListFn()[39].textContent).toBe("Erinome"));
  test.do(() => expect(resultListFn()[40].textContent).toBe("Pan"));
  test.do(() => expect(resultListFn()[41].textContent).toBe("Atlas"));
  test.do(() => expect(resultListFn()[42].textContent).toBe("Prometheus"));
  test.do(() => expect(resultListFn()[43].textContent).toBe("Pandora"));
  test.do(() => expect(resultListFn()[44].textContent).toBe("Epimetheus"));
  test.do(() => expect(resultListFn()[45].textContent).toBe("Janus"));
  test.do(() => expect(resultListFn()[46].textContent).toBe("Mimas"));
  test.do(() => expect(resultListFn()[47].textContent).toBe("Enceladus"));
  test.do(() => expect(resultListFn()[48].textContent).toBe("Tethys"));
  test.do(() => expect(resultListFn()[49].textContent).toBe("Telesto"));
  test.do(() => expect(resultListFn()[50].textContent).toBe("Calypso"));
  test.do(() => expect(resultListFn()[51].textContent).toBe("Dione"));
  test.do(() => expect(resultListFn()[52].textContent).toBe("Helene"));
  test.do(() => expect(resultListFn()[53].textContent).toBe("Rhea"));
  test.do(() => expect(resultListFn()[54].textContent).toBe("Titan"));
  test.do(() => expect(resultListFn()[55].textContent).toBe("Hyperion"));
  test.do(() => expect(resultListFn()[56].textContent).toBe("Iapetus"));
  test.do(() => expect(resultListFn()[57].textContent).toBe("Phoebe"));
  test.do(() => expect(resultListFn()[58].textContent).toBe("Cordelia"));
  test.do(() => expect(resultListFn()[59].textContent).toBe("Ophelia"));
  test.do(() => expect(resultListFn()[60].textContent).toBe("Bianca"));
  test.do(() => expect(resultListFn()[61].textContent).toBe("Cressida"));
  test.do(() => expect(resultListFn()[62].textContent).toBe("Desdemona"));
  test.do(() => expect(resultListFn()[63].textContent).toBe("Juliet"));
  test.do(() => expect(resultListFn()[64].textContent).toBe("Portia"));
  test.do(() => expect(resultListFn()[65].textContent).toBe("Rosalind"));
  test.do(() => expect(resultListFn()[66].textContent).toBe("Belinda"));
  test.do(() => expect(resultListFn()[67].textContent).toBe("1986U10"));
  test.do(() => expect(resultListFn()[68].textContent).toBe("Puck"));
  test.do(() => expect(resultListFn()[69].textContent).toBe("Miranda"));
  test.do(() => expect(resultListFn()[70].textContent).toBe("Ariel"));
  test.do(() => expect(resultListFn()[71].textContent).toBe("Umbriel"));
  test.do(() => expect(resultListFn()[72].textContent).toBe("Titania"));
  test.do(() => expect(resultListFn()[73].textContent).toBe("Oberon"));
  test.do(() => expect(resultListFn()[74].textContent).toBe("Caliban"));
  test.do(() => expect(resultListFn()[75].textContent).toBe("Stephano"));
  test.do(() => expect(resultListFn()[76].textContent).toBe("Sycorax"));
  test.do(() => expect(resultListFn()[77].textContent).toBe("Prospero"));
  test.do(() => expect(resultListFn()[78].textContent).toBe("Setebos"));
  test.do(() => expect(resultListFn()[79].textContent).toBe("Naiad"));
  test.do(() => expect(resultListFn()[80].textContent).toBe("Thalassa"));
  test.do(() => expect(resultListFn()[81].textContent).toBe("Despina"));
  test.do(() => expect(resultListFn()[82].textContent).toBe("Galatea"));
  test.do(() => expect(resultListFn()[83].textContent).toBe("Larissa"));
  test.do(() => expect(resultListFn()[84].textContent).toBe("Proteus"));
  test.do(() => expect(resultListFn()[85].textContent).toBe("Triton"));
  test.do(() => expect(resultListFn()[86].textContent).toBe("Nereid"));
  test.do(() => expect(resultListFn()[87].textContent).toBe("Charon"));
  test.event("blur", inputFn, () => resultListFn().length === 0);
  test.start();
});

it("Client side - Filter Prefix: 'me'", function (done) {
  const inputFn = elementByIdFn("page:mainForm:inClientFilterPrefix::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length !== 3,
      () => inputFn().value = "", "input", inputFn);
  test.do(() => inputFn().value = "me");
  test.event("input", inputFn, () => resultListFn().length === 3);
  test.do(() => expect(resultListFn().length).toBe(3));
  test.do(() => expect(resultListFn()[0].textContent).toBe("Mercury"));
  test.do(() => expect(resultListFn()[1].textContent).toBe("Metis"));
  test.do(() => expect(resultListFn()[2].textContent).toBe("Megaclite"));
  test.event("blur", inputFn, () => resultListFn().length === 0);
  test.start();
});

it("Client side - Filter Contains: 'me'", function (done) {
  const inputFn = elementByIdFn("page:mainForm:inClientFilterContains::field");
  const resultId = inputFn().getAttribute("aria-owns").replaceAll(":", "\\:");
  const resultListFn = querySelectorAllFn("#" + resultId + " .dropdown-item");

  const test = new JasmineTestTool(done);
  test.setup(() => resultListFn().length !== 8,
      () => inputFn().value = "", "input", inputFn);
  test.do(() => inputFn().value = "me");
  test.event("input", inputFn, () => resultListFn().length === 8);
  test.do(() => expect(resultListFn().length).toBe(8));
  test.do(() => expect(resultListFn()[0].textContent).toBe("Mercury"));
  test.do(() => expect(resultListFn()[1].textContent).toBe("Metis"));
  test.do(() => expect(resultListFn()[2].textContent).toBe("Ganymede"));
  test.do(() => expect(resultListFn()[3].textContent).toBe("Carme"));
  test.do(() => expect(resultListFn()[4].textContent).toBe("Megaclite"));
  test.do(() => expect(resultListFn()[5].textContent).toBe("Erinome"));
  test.do(() => expect(resultListFn()[6].textContent).toBe("Prometheus"));
  test.do(() => expect(resultListFn()[7].textContent).toBe("Epimetheus"));
  test.event("blur", inputFn, () => resultListFn().length === 0);
  test.start();
});
