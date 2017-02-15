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

  $.widget("demo.htmlEditor", {

    _create: function () {

      if (typeof tinymce == "undefined") {
        console.error("No TinyMCE installation found!");
        return;
      }
      tinymce.init({
        selector: Tobago.Utils.escapeClientId(this.element.attr("id")),
        init_instance_callback: function (editor) {
          var textarea = jQuery(Tobago.Utils.escapeClientId(editor.id));
          var editorContainer = jQuery(editor.editorContainer);

          editorContainer.css("height", textarea.css("height"));
          editorContainer.css("width", textarea.css("width"));
          editorContainer.css("left", textarea.css("left"));
          editorContainer.css("top", textarea.css("top"));
          editorContainer.css("position", textarea.css("position"));

          var text = jQuery(editor.contentAreaContainer);

          // set estimated height
          text.outerHeight(text.outerHeight - 107);

          // compute the real height a bit later, (didn't found an event to listen, so using setTimeout)
          setTimeout(function () {
            var heightDelta = 2; // extra pixel...
            editorContainer.find(".mce-toolbar, .mce-statusbar").each(function () {
              heightDelta += jQuery(this).outerHeight();
            });
            text.outerHeight(text.outerHeight() - heightDelta);
          }, 50);
        },
        setup: function(ed) {
          ed.on('init', function(args) {
            // TinyMCE v4
            if (jQuery("#" + args.target.id.replace(/:/g, "\\:")).attr('readonly') == "readonly") {
              tinymce.get(args.target.id).setMode('readonly');
            }
          });
        }
      });
    },

    _destroy: function () {
      // tbd: instance.destroy()
    }

  });

}(jQuery));

Tobago.registerListener(function () {
  jQuery("[data-html-editor]").htmlEditor();
}, Tobago.Phase.DOCUMENT_READY);
