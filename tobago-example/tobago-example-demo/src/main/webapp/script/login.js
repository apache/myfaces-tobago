/**
 * Copies the values from the data-login attribute to the username/password fields.
 */
Demo.prepareQuickLinks = function () {
  jQuery("button[data-login]").click(function () {
    var link = jQuery(this);
    var login = link.data("login");
    jQuery(Tobago.Utils.escapeClientId("page:mainForm:username::field")).val(login.username);
    jQuery(Tobago.Utils.escapeClientId("page:mainForm:password::field")).val(login.password);
    return false;
  });
};

Tobago.registerListener(Demo.prepareQuickLinks, Tobago.Phase.DOCUMENT_READY);

/**
 * This code is needed to "repair" the submit parameter names and url to use
 * the names that a required for servlet authentication.
 */
Demo.prepareLoginForm = function() {
  jQuery(Tobago.Utils.escapeClientId("page:mainForm:username::field")).attr("name", "j_username");
  jQuery(Tobago.Utils.escapeClientId("page:mainForm:password::field")).attr("name", "j_password");
  var contextPath = jQuery(Tobago.Utils.escapeClientId("page:mainForm:login")).data("context-path");
  jQuery(Tobago.Utils.escapeClientId("page::form")).attr("action", contextPath + "/j_security_check");
};

// Tobago.registerListener(Demo.prepareLoginForm, Tobago.Phase.DOCUMENT_READY);
