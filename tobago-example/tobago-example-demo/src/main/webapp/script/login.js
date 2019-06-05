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

/**
 * Copies the values from the data-login attribute to the username/password fields.
 */
Demo.prepareQuickLinks = function () {
  jQuery("button[data-login]").click(function () {
    var link = jQuery(this);
    var login = link.data("login");
    jQuery(DomUtils.escapeClientId("page:mainForm:username::field")).val(login.username);
    jQuery(DomUtils.escapeClientId("page:mainForm:password::field")).val(login.password);
    return false;
  });
};

Listener.register(Demo.prepareQuickLinks, Phase.DOCUMENT_READY);

/**
 * This code is needed to "repair" the submit parameter names and url to use
 * the names that a required for servlet authentication.
 */
Demo.prepareLoginForm = function() {
  jQuery(DomUtils.escapeClientId("page:mainForm:username::field")).attr("name", "j_username");
  jQuery(DomUtils.escapeClientId("page:mainForm:password::field")).attr("name", "j_password");
  var contextPath = jQuery(DomUtils.escapeClientId("page:mainForm:login")).data("context-path");
  jQuery(DomUtils.escapeClientId("page::form")).attr("action", contextPath + "/j_security_check");
};

// XXX turned off in the moment Tobago5.Listener.register(Demo.prepareLoginForm, Tobago5.Phase.DOCUMENT_READY);
