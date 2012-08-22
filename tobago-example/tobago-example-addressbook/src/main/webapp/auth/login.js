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

Addressbook = {};

/**
 * Copies the values from the data-login attribute to the username/password fields.
 */
Addressbook.prepareQuickLinks = function() {
  jQuery("a[data-login]").click(function() {
    var link = jQuery(this);
    var login = link.data("login");
    jQuery(Tobago.Utils.escapeClientId("page:j_username")).val(login.username);
    jQuery(Tobago.Utils.escapeClientId("page:j_password")).val(login.password).focus();
    return false;
  });
};

/**
 * This code is needed to "repair" the submit parameter names and url to use
 * the names that a required for servlet authentication.
 */
Addressbook.prepareLoginForm = function() {
  jQuery(Tobago.Utils.escapeClientId("page:j_username")).attr("name", "j_username");
  jQuery(Tobago.Utils.escapeClientId("page:j_password")).attr("name", "j_password");
  jQuery(Tobago.Utils.escapeClientId("page::form")).attr("action", Tobago.contextPath.value + "/j_security_check");
};

Tobago.registerListener(Addressbook.prepareQuickLinks, Tobago.Phase.DOCUMENT_READY);

Tobago.registerListener(Addressbook.prepareLoginForm, Tobago.Phase.BEFORE_SUBMIT);
