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

Tobago.Layout = {};

Tobago.Layout.init = function (elements) {

  // fixing fixed header/footer: content should not scroll behind the footer
  // XXX Is there a CSS solution?
  // TODO: this might be reevaluated after a "resize"

  var header = Tobago.Utils.selectWithJQuery(elements, ".fixed-top");
  header.each(function () {
    var content = header.next();
    content.css({
      marginTop: (parseInt(content.css("margin-top").replace("px", "")) + header.outerHeight(true)) + "px"
    });
  });

  var footer = Tobago.Utils.selectWithJQuery(elements, ".fixed-bottom");
  footer.each(function () {
    var content = footer.prev();
    content.css({
      marginBottom: (parseInt(content.css("margin-bottom").replace("px", "")) + footer.outerHeight(true)) + "px"
    });
  });

};

Tobago.registerListener(Tobago.Layout.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.Layout.init, Tobago.Phase.AFTER_UPDATE);
