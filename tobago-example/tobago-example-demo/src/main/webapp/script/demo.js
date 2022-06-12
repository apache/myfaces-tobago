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

(function ($) {
  $.widget("demo.alert", {
    _create: function () {
      this._on({
        click: function (event) {
          var text = this.element.data("alert-text");
          alert(text);
        }
      });
    }
  });
}(jQuery));

var initAlert = function () {
  jQuery("[data-alert-text]").alert();
};

Tobago.registerListener(initAlert, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(initAlert, Tobago.Phase.AFTER_UPDATE);

jQuery.fn.select2.amd.define("CustomTokenizerAdapter", [
      "select2/utils",
      "select2/data/tokenizer"
    ],
    function(Utils, Tokenizer) {
      var emptyFunc = function (params) {

      };
      emptyFunc.tokenizer = function (params) {
        var result = Tokenizer.prototype.tokenizer.call(this, params);
        console.info("tokenizer result: " + result);
        return result;
      };
     return function(params) {emptyFunc.tokenizer(params)};
    });

jQuery.fn.select2.amd.define("HideDropdown", [
      "select2/utils",
      "select2/dropdown",
      "select2/dropdown/attachBody",
      "select2/dropdown/attachContainer",
      "select2/dropdown/search",
      "select2/dropdown/minimumResultsForSearch"
    ],
    function(Utils, Dropdown, AttachBody, AttachContainer, Search, MinimumResultsForSearch) {

      // Decorate the dropdown+search with necessary containers
      var adapter = Utils.Decorate(Dropdown, AttachBody);
      adapter.prototype.render = function() {
        Dropdown.prototype.render.call(this);
        return jQuery("<span/>");
      };
      return adapter;
    });


jQuery.fn.select2.amd.define("CustomDropdownAdapter", [
      "select2/utils",
      "select2/dropdown",
      "select2/dropdown/attachBody",
      "select2/dropdown/attachContainer",
      "select2/dropdown/search",
      "select2/dropdown/minimumResultsForSearch"
    ],
    function(Utils, Dropdown, AttachBody, AttachContainer, Search, MinimumResultsForSearch) {

      // Decorate Dropdown with Search functionalities
      var dropdownWithSearch = Utils.Decorate(Dropdown, Search);
      dropdownWithSearch.prototype.render = function() {
        // Copy and modify default search render method
        var $rendered = Dropdown.prototype.render.call(this);
        // Add ability for a placeholder in the search box
        var placeholder = this.options.get("placeholderForSearch") || "";
        var $search = $(
            '<span class="select2-search select2-search--dropdown">' +
            '<input class="select2-search__field" placeholder="' + placeholder + '" type="search"' +
            ' tabindex="-1" autocomplete="off" autocorrect="off" autocapitalize="off"' +
            ' spellcheck="false" role="textbox" />' +
            '</span>'
        );

        this.$searchContainer = $search;
        this.$search = $search.find('input');

        // $rendered.prepend($search);
        return $rendered;
      };

      // Decorate the dropdown+search with necessary containers
      var adapter = Utils.Decorate(dropdownWithSearch, AttachContainer);
      adapter = Utils.Decorate(adapter, AttachBody);

      return adapter;
    });


var TBG_DEMO = {
  Select2: {
    Tokenizer: function (params) {
      console.info("params: " + params);
      var tokenizer = jQuery.fn.select2.amd.require("Tokenizer");
      var results = tokenizer.call(this, params);
      console.info("results: " + results);
      return results;
    },

    doOnSelect: function (event) {
      var element = jQuery(this);
      var data = element.select2("data");
      var newData = event.params.data;
      console.info("doOnSelect id     : " +  element.attr("id"));
      console.info("doOnSelect tagName: " +  element.prop("tagName"));
      console.info("doOnSelect newData: " +  newData);
      console.info("doOnSelect data   : " +  data);
      // console.info("doOnSelect : " +  element);
    }
  }
};
