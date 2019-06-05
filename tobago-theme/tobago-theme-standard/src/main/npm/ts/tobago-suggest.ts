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

import {Listener, Order, Phase} from "./tobago-listener";
import {DomUtils} from "./tobago-utils";
import {Tobago4Utils} from "./tobago-utils";

class Suggest {

  static loadFromServer = function (input) {

    var timeout;

    return function findMatches(query, syncResults, asyncResults) {

      var suggest = jQuery(DomUtils.escapeClientId(input.data("tobago-suggest-for")));

      if (suggest.val() !== query) {

        if (timeout) {
          clearTimeout(timeout);
        }

        const delay = suggest.data("tobago-suggest-delay");

        timeout = setTimeout(function () {
          suggest.val(query);
          suggest.data("tobago-suggest-callback", asyncResults);
          suggest.removeData("tobago-suggest-data"); // clear jQuery-data-cache
          const id = suggest.attr("id");
          console.info("query: '" + query + "'");

          jsf.ajax.request(
              id,
              null, // todo: event?
              {
                "javax.faces.behavior.event": "suggest",
                execute: id,
                render: id
              });
        }, delay);

      }
    };
  };

  static fromClient = function (data) {
    return function findMatches(query, syncResults) {
      var result = [];
      for (var i = 0; i < data.length; i++) {
        if (data[i].indexOf(query) >= 0) {
          result.push(data[i]);
        }
      }
      syncResults(result);
    };
  };

  static init = function (element: HTMLElement) {

    Tobago4Utils.selectWithJQuery($(element), ".tobago-suggest").each(function () {
      var $suggest = jQuery(this);
      var $input = jQuery(DomUtils.escapeClientId($suggest.data("tobago-suggest-for")));

      var minChars = $suggest.data("tobago-suggest-min-chars");
      var maxItems = $suggest.data("tobago-suggest-max-items");

      var update = typeof $suggest.data("tobago-suggest-update") != "undefined";
      var totalCount = $suggest.data("tobago-suggest-total-count"); // todo

      var localMenu = false;
      var dataTobagoMarkup = jQuery(DomUtils.escapeClientId($input.attr("name"))).attr("data-tobago-markup");
      if (dataTobagoMarkup !== undefined) {
        var markups = jQuery.parseJSON(jQuery(DomUtils.escapeClientId($input.attr("name"))).attr("data-tobago-markup"));
        markups.forEach(function (markup) {
          if (markup === "localMenu") {
            localMenu = true;
          }
        });
      }

      if (update && $input.hasClass("tt-input")) { // already initialized: so only update data
        var asyncResults = $suggest.data("tobago-suggest-callback"); // comes from "findMatches()"
        if (asyncResults) {
          var data1 = $suggest.data("tobago-suggest-data");
          asyncResults(data1);
        }
      } else { // new
        $input.data("tobago-suggest-for", $suggest.attr("id"));
        $input.attr("autocomplete", "off");

        var source;
        if (update) {
          source = Suggest.loadFromServer($input);
        } else {
          var data2 = $suggest.data("tobago-suggest-data");
          source = Suggest.fromClient(data2);
        }

        var $suggestPopup = getSuggestPopup($suggest);
        if ($suggestPopup.length > 0) {
          $suggestPopup.remove();
        }

        jQuery(".tobago-page-menuStore")
            .append("<div id='" + $suggest.attr('id') + "::popup" + "' class='tt-menu tt-empty'/>");
        $suggestPopup = getSuggestPopup($suggest);

        $input.typeahead({
          menu: localMenu ? null : $suggestPopup,
          minLength: minChars,
          hint: true,// todo
          highlight: true // todo
        }, {
          //name: 'test',// todo
          limit: maxItems,
          source: source
        }).on('typeahead:change', function (event) {
          $input.trigger('change');
        });

        $input.on('typeahead:open', function () {
          var $input = jQuery(this);
          var $suggest = $input.parent().siblings(".tobago-suggest");
          if ($suggest.length === 0) {
            $suggest = $input.parent().parent().parent().siblings(".tobago-suggest");
          }
          var $suggestPopup = jQuery(DomUtils.escapeClientId($suggest.attr('id') + "::popup"));
          $suggestPopup.css("top", $input.offset().top + $input.outerHeight() + "px");
          $suggestPopup.css("left", $input.offset().left + "px");
          $suggestPopup.css("min-width", $input.outerWidth() + "px");
        });
      }
    });

    function getSuggestPopup(suggest) {
      return jQuery(DomUtils.escapeClientId(suggest.attr('id') + "::popup"));
    }
  };
}

// using "EARLY", because it must be called before Tobago.Layout.init
// this is because the suggest puts a span around the input field and doesn't copy the style.
Listener.register(Suggest.init, Phase.DOCUMENT_READY, Order.EARLY);
Listener.register(Suggest.init, Phase.AFTER_UPDATE, Order.EARLY);
