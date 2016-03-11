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

/* global initialzing script for CSS */
CKEDITOR.on("instanceLoaded", function(event) {
  var textarea = jQuery(Tobago.Utils.escapeClientId(event.editor.name));
  var editor = textarea.next();
  editor.css("height", textarea.css("height"));
  editor.css("width", textarea.css("width"));
  editor.css("left", textarea.css("left"));
  editor.css("top", textarea.css("top"));
  editor.css("position", textarea.css("position"));
  var child = editor.children(".cke_inner");
  child.css("height", "100%");
  var top = child.children(".cke_top");
  var bottom = child.children(".cke_bottom");
  var content = child.children(".cke_contents");
  content.css("height", child.innerHeight() - top.outerHeight() - bottom.outerHeight());
});

(function ($) {

  $.widget("demo.htmlEditor", {

    _create: function () {

      var id = this.element.attr("id");
      CKEDITOR.replace(id);
    },

    _destroy: function () {
      // tbd: instance.destory()
    }

  });

}(jQuery));

Tobago.registerListener(function() {
  jQuery("[data-html-editor]").htmlEditor();
}, Tobago.Phase.DOCUMENT_READY);
