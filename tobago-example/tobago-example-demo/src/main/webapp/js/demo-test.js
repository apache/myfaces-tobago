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
// todo: this code is not tested
class DemoTest {
    static initTestLinks(element) {
        const runLink = document.getElementById("page:header:runtest");
        if (runLink && parent.document.getElementById("qunit")) {
            runLink.classList.add("d-none");
        }
    }
    static initTestFrame(element) {
        const testFrame = document.getElementById("page:testframe");
        if (testFrame) {
            alert("Might currently not working...");
            testFrame.addEventListener("onload", function () {
                // XXX is element an iframe?
                const iframe = element;
                iframe.style.height = "" + iframe.contentWindow.document.querySelector("body").scrollHeight + "px";
            });
        }
    }
}
document.addEventListener("DOMContentLoaded", function (event) {
    const element = document.documentElement; // XXX fixme
    // XXX init areas after Faces AJAX update not implemented yet!
    DemoTest.initTestLinks(element);
    DemoTest.initTestFrame(element);
});
//# sourceMappingURL=demo-test.js.map