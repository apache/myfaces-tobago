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

  static loadFromServer = function (input: HTMLInputElement) {

    var timeout;

    return function findMatches(query, syncResults, asyncResults) {

      var suggest = document.getElementById(input.dataset["tobagoSuggestFor"]) as HTMLInputElement;

      if (suggest.value !== query) {

        if (timeout) {
          clearTimeout(timeout);
        }

        const delay = parseInt(suggest.dataset["tobagoSuggestDelay"]);

        timeout = setTimeout(function () {
          suggest.value = query;
          // suggest.dataset["tobagoSuggestCallback"] = asyncResults;
          jQuery(suggest).data("tobago-suggest-callback", asyncResults); // tbd: evtl. to fix!!!
          delete suggest.dataset["tobagoSuggestData"];
          console.info("query: '" + query + "'");

          jsf.ajax.request(
              suggest.id,
              null, // todo: event?
              {
                "javax.faces.behavior.event": "suggest",
                execute: suggest.id,
                render: suggest.id
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

    for (const suggest of DomUtils.selfOrQuerySelectorAll(element, ".tobago-suggest")) {
      var $suggest = jQuery(suggest);
      var input = document.getElementById(suggest.dataset["tobagoSuggestFor"]) as HTMLInputElement;
      var $input = jQuery(input);

      var minChars = parseInt(suggest.dataset["tobagoSuggestMinChars"]);
      var maxItems = parseInt(suggest.dataset["tobagoSuggestMaxItems"]);

      var update = suggest.dataset["tobagoSuggestUpdate"] !== null;
      var totalCount = parseInt(suggest.dataset["tobagoSuggestTotalCount"]); // todo

      // todo!
      // var localMenu = false;
      // var dataTobagoMarkup = jQuery(DomUtils.escapeClientId($input.attr("name"))).attr("data-tobago-markup");
      // if (dataTobagoMarkup !== undefined) {
      //   var markups = jQuery.parseJSON(jQuery(DomUtils.escapeClientId($input.attr("name"))).attr("data-tobago-markup"));
      //   markups.forEach(function (markup) {
      //     if (markup === "localMenu") {
      //       localMenu = true;
      //     }
      //   });
      // }

      if (update && input.classList.contains("tt-input")) { // already initialized: so only update data
        var asyncResults = $suggest.data("tobago-suggest-callback"); // comes from "findMatches()"
        if (asyncResults) {
          const data1 = JSON.parse(suggest.dataset["tobagoSuggestData"]);
          asyncResults(data1);
        }
      } else { // new
        input.dataset["tobagoSuggestFor"] = suggest.id;
        $input.attr("autocomplete", "off");

        var source;
        if (update) {
          source = Suggest.loadFromServer(input);
        } else {
          var data2 = JSON.parse(suggest.dataset["tobagoSuggestData"]);
          source = Suggest.fromClient(data2);
        }

        let $suggestPopup = jQuery(document.getElementById(suggest.id + "::popup"));
        if ($suggestPopup.length > 0) {
          $suggestPopup.remove();
        }

        jQuery(".tobago-page-menuStore")
            .append("<div id='" + suggest.id + "::popup" + "' class='tt-menu tt-empty'/>");
        $suggestPopup = jQuery(document.getElementById(suggest.id + "::popup"));

        $input.typeahead({
          menu: /* todo localMenu ? null :*/ $suggestPopup,
          minLength: minChars,
          hint: true,// todo
          highlight: true // todo
        }, {
          //name: 'test',// todo
          limit: maxItems,
          source: source
        });
        // old with jQuery:
        $input.on('typeahead:change', function (event: JQuery.Event) {
            const input = this;
            input.dispatchEvent(new Event("change"));
        });
        // new without jQuery:
        // input.addEventListener("typeahead:change", (event: Event) => {
        //   const input = event.currentTarget as HTMLInputElement;
        //   input.dispatchEvent(new Event("change"));
        // });

        // old with jQuery:
        $input.on('typeahead:open', function (event: Event) {
          const input = this;
          const suggestPopup = document.getElementById(input.dataset["tobagoSuggestFor"] + "::popup");
          suggestPopup.style.top = DomUtils.offset(input).top + input.offsetHeight + "px";
          suggestPopup.style.left = DomUtils.offset(input).left + "px";
          suggestPopup.style.minWidth = input.offsetWidth + "px";
        });

        // new without jQuery:
        // input.addEventListener("typeahead:open", (event: Event) => {
        //   const input = event.currentTarget as HTMLInputElement;
        //   const suggestPopup = document.getElementById(input.dataset["tobagoSuggestFor"] + "::popup");
        //   suggestPopup.style.top = DomUtils.offset(input).top + input.offsetHeight + "px";
        //   suggestPopup.style.left = DomUtils.offset(input).left + "px";
        //   suggestPopup.style.minWidth = input.offsetWidth + "px";
        // });

      }
    }
  };
}

// using "EARLY", because it must be called before Tobago.Layout.init
// this is because the suggest puts a span around the input field and doesn't copy the style.
Listener.register(Suggest.init, Phase.DOCUMENT_READY, Order.EARLY);
Listener.register(Suggest.init, Phase.AFTER_UPDATE, Order.EARLY);
