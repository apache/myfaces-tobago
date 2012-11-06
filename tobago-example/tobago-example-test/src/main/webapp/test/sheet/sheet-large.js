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

TestSheet = {};

TestSheet.init = function() {

  jQuery("[data-clickrow]").click(function() {
    var rowIndex = jQuery(this).data("clickrow");
    var row = jQuery(".tobago-sheet-body tr").eq(rowIndex);
    var status = jQuery(this).prev().find("input");
    status.val("processing ...");
    var start = new Date();
    row.click();
    var end = new Date();
    status.val(end.getTime() - start.getTime() + " ms");
  });
};

Tobago.registerListener(TestSheet.init, Tobago.Phase.DOCUMENT_READY);
