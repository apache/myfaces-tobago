Tobago.SplitLayout = {
  init: function(elements) {
    Tobago.Utils.selectWidthJQuery(elements, "[data-tobago-split-layout]").each(Tobago.SplitLayout.initLayout);
  },

  initLayout: function() {
    var handle = jQuery(this);
    var options = {
      containment: "parent",
      distance: 10,
      stop: Tobago.SplitLayout.stopDragging
    };
    if (handle.data("tobago-split-layout") == "horizontal") {
      options.axis = "x"
    } else {
      options.axis = "y"
    }
    handle.draggable(options)
  },

  stopDragging: function(event, ui) {
    var hidden = ui.helper.children("input");
    if (ui.helper.data("tobago-split-layout") == "horizontal") {
      hidden.val(ui.position.left)
    } else {
      hidden.val(ui.position.top)
    }
    var panel = Tobago.SplitLayout.findParentElementWithId(ui.helper);
    var clientId = panel.id;
    Tobago.reloadComponent(panel, clientId, ui.helper.get(0).id);
  },

  findParentElementWithId: function(element) {
    var parent = element.parent();
    while (parent.get(0).id === undefined || parent.get(0).id.length == 0) {
      parent = parent.parent();
    }
    return parent.get(0);
  }
};

Tobago.registerListener(Tobago.SplitLayout.init, Tobago.Phase.AFTER_UPDATE);
Tobago.registerListener(Tobago.SplitLayout.init, Tobago.Phase.DOCUMENT_READY);