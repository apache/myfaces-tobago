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

Tobago.Suggest = {};

Tobago.Suggest.substring = function(strings) {
  return function findMatches(query, callback) {
    // an array that will be populated with substring matches
    var matches = [];

    // regex used to determine if a string contains the substring `query`
    var substringRegex = new RegExp(query, 'i');

    // iterate through the pool of strings and for any string that
    // contains the substring `query`, add it to the `matches` array
    $.each(strings, function(i, string) {
      if (substringRegex.test(string)) {
        matches.push(string);
      }
    });

    callback(matches);
  };
};

Tobago.Suggest.init = function (elements) {

  var suggests = Tobago.Utils.selectWithJQuery(elements, "[data-tobago-suggest-data]");
  suggests.each(function () {
    var suggest = jQuery(this);

    var minChars = suggest.data("tobago-suggest-min-chars");

    var delay = suggest.data("tobago-suggest-delay"); // todo
    var maxItems = suggest.data("tobago-suggest-max-items"); // todo
    var update = suggest.data("tobago-suggest-update"); // todo
    var totalCount = suggest.data("tobago-suggest-total-count"); // todo

    var list = suggest.data("tobago-suggest-data");

    suggest.attr("autocomplete", "off");

    suggest.typeahead({
      minLength: minChars,
      hint: true,// todo
      highlight: true // todo
    }, {
      //name: 'test',// todo
      source: Tobago.Suggest.substring(list)
    });
  });
};

Tobago.registerListener(Tobago.Suggest.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Suggest.init, Tobago.Phase.AFTER_UPDATE);
