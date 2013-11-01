Tobago.SplitLayout = {
  init: function(elements) {
    Tobago.Utils.selectWidthJQuery(elements, "[data-tobago-split-layout]").each(Tobago.SplitLayout.initLayout);
  },

  initLayout: function() {
    var handle = jQuery(this);
    var offset = handle.parent().offset();
    var containment = handle.data("tobago-split-layout-containment");
    var options = {
      distance: 10,
      containment: [offset.left + containment[0], offset.top + containment[1],
          offset.left + containment[2], offset.top + containment[3]],
      axis: handle.data("tobago-split-layout") == "horizontal" ? "x" : "y",
      start: Tobago.SplitLayout.startDragging,
      stop: Tobago.SplitLayout.stopDragging
    };
    handle.draggable(options)
  },

  startDragging: function(event, ui) {
    ui.helper.parent().find("iframe").each(function() {
      var iframe = jQuery(this);
      iframe.data("tobago-split-layout-iframe-zindex", iframe.css("z-index"));
      iframe.css("z-index", "-1");
    });
  },


  stopDragging: function(event, ui) {
    ui.helper.parent().find("iframe").each(function() {
      var iframe = jQuery(this);
      iframe.css("z-index", iframe.data("tobago-split-layout-iframe-zindex"));
    });
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