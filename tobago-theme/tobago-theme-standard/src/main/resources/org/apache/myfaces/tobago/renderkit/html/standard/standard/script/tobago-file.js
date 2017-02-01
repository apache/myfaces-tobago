// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Tobago.File = {};

Tobago.File.init = function(elements) {
  var files = Tobago.Utils.selectWithJQuery(elements, ".tobago-file-real");
  files.change(function () {
    var file = jQuery(this);
    var pretty = file.prev();
    var text;
    if (file.prop("multiple")) {
      var format = file.data("tobago-file-multi-format");
      text = format.replace("{}", file.prop("files").length);
    } else {
      text = file.val();
      // remove path, if any. Some old browsers set the path, others like webkit uses the prefix "C:\facepath\".
      var pos = Math.max(text.lastIndexOf('/'), text.lastIndexOf('\\'));
      if (pos >= 0) {
        text = text.substr(pos + 1);
      }
    }
    pretty.val(text);
  });
  if (files.length > 0) {
    jQuery("form").attr('enctype', 'multipart/form-data')
  }
};

Tobago.registerListener(Tobago.File.init, Tobago.Phase.DOCUMENT_READY);
Tobago.registerListener(Tobago.File.init, Tobago.Phase.AFTER_UPDATE);


// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

(function (jQuery) {

  var dragenterCount = 0;

  function initFileDropAreas(elements) {
    console.info("initFileDropAreas " + (elements ? elements.length : "body")); // @DEV_ONLY

    if (elements === undefined) {
      console.info("initialize Body"); // @DEV_ONLY
      var body = jQuery("body");
      //noinspection SpellCheckingInspection
      body.on("dragenter", dragenter);
      //noinspection SpellCheckingInspection
      body.on("dragleave", dragleave);
      //noinspection SpellCheckingInspection
      jQuery(window).on("dragover", dragoverOnDocument);
      jQuery(window).on("drop", dropOnDocument);
    }


    Tobago.Utils.selectWithJQuery(elements, "[data-tobago-file-drop]").each(initFileDropArea);
  }

  function initFileDropArea() {
    var area = jQuery(this);
    console.info("initFileDropArea " + area.attr("id")); // @DEV_ONLY
    area.filedroparea();
  }


  function dragenter(event) {
    // console.info("dragEnter : " + dragenterCount + " : " + event.target.id); // @DEV_ONLY
    event.stopPropagation();
    event.stopImmediatePropagation();
    event.preventDefault();
    if (dragenterCount == 0) {
      jQuery("[data-tobago-file-drop]").each(showDropAreas);
    }
    dragenterCount++;
  }

  function showDropAreas() {
    // console.info("showDropAreas"); // @DEV_ONLY
    jQuery(this).filedroparea("show");
  }

  function dragleave(event) {
    // console.info("dragLeave : " + dragenterCount + " : " + event.target.id); // @DEV_ONLY
    event.stopPropagation();
    event.stopImmediatePropagation();
    event.preventDefault();
    dragenterCount--;
    if (dragenterCount == 0) {
      jQuery("[data-tobago-file-drop]").each(hideDropArea);
    }
  }

  function hideDropArea() {
    // console.info("hideDropArea"); // @DEV_ONLY
    jQuery(this).filedroparea("hide");
  }


  function dragoverOnDocument(event) {
    // console.info("dragoverOnDocument : " + event.target.id); // @DEV_ONLY
    event.stopPropagation();
    event.stopImmediatePropagation();
    event.preventDefault();
  }

  function dropOnDocument(event) {
    // console.info("dropOnDocument : " + event.target.id); // @DEV_ONLY
    event.stopPropagation();
    event.stopImmediatePropagation();
    event.preventDefault();
    jQuery("[data-tobago-file-drop]").each(removeDropListener);
    dragenterCount = 0;
  }


  function removeDropListener() {
    // console.info("removeDropListener"); // @DEV_ONLY
    jQuery(this).filedroparea("hide");
  }

  jQuery.widget("tobago.filedroparea", {

    options: {

    },

    fileDropArea: null,

    findDropElement: function (dropZoneId) {
      if (dropZoneId.charAt(0) == ":" && dropZoneId.charAt(1) != ":") {
        return jQuery(Tobago.Utils.escapeClientId(dropZoneId.substring(1)));
      } else  {
        // TODO
        return jQuery(Tobago.Utils.escapeClientId(dropZoneId));
      }
    },

    _create: function () {

      console.info("filedroparea.create"); // @DEV_ONLY
      var data = this.element.data("tobago-file-drop");

      var dropZoneId = data.dropZoneId;

      var dropElement = this.findDropElement(dropZoneId);

      // create the overlay
      this.fileDropArea = jQuery("<div>").addClass("tobago-fileDrop-dropZone");
      this.fileDropArea.css({display: 'none'});
      this.fileDropArea.outerWidth(dropElement.outerWidth());
      this.fileDropArea.outerHeight(dropElement.outerHeight());
      this.fileDropArea.offset(dropElement.offset());
      this.fileDropArea.data("widget-element", this.element);

      jQuery(".tobago-page-menuStore").append(this.fileDropArea);

      this.element.click(function (event) {
        if (event.target.tagName != "input" && event.target.type != "file") {
          // don't delegate click if already clicked on file input
          jQuery(this).find("input[type='file']").click();
        }
      })
    },

    show: function () {
      // console.info("show");  // @DEV_ONLY
      this.fileDropArea.css({display: ''});
      this.fileDropArea.on("drop", this.filesDropped);
    },

    hide: function () {
      // console.info("hide"); // @DEV_ONLY
      this.fileDropArea.off("drop");
      this.fileDropArea.css({display: 'none'});
    },

    filesDropped: function (event) {
      console.info("dropFile"); // @DEV_ONLY
      event.stopPropagation();
      event.stopImmediatePropagation();
      event.preventDefault();

      var dropThis = jQuery(this);
      var fileDrop = dropThis.data("widget-element");
      jQuery("[data-tobago-file-drop]").each(removeDropListener);
      dragenterCount = 0;

      //noinspection JSUnresolvedVariable
      var files = event.originalEvent.dataTransfer.files;
      console.info("files.length: " + files.length); // @DEV_ONLY
      for (var i = 0; i < files.length; i++) {
        var file = files[i];
        console.info("file: " + file.name); // @DEV_ONLY
      }

      if (files.length == 0) {
        console.warn("no files dropped, aborting upload!");
      }

      var fileElement = fileDrop.find("input[type='file']");
      var commands = fileElement.data("tobago-commands");
      if (!commands || !commands.change || !commands.change.partially) {
        console.error("No input type file with renderedPartially found, abbort upload!");
      }

      jQuery(Tobago.form).data("tobago-file-drag-and-drop-files", {name: fileElement.attr("name"), files: files});
      fileElement.change();

    },

    _destroy: function () {
      try {
        this.fileDropArea.remove();
      } catch (e) { /* ignore */ }
      this.fileDropArea = null;
    }

  });

  Tobago.registerListener(initFileDropAreas, Tobago.Phase.AFTER_UPDATE);
  Tobago.registerListener(initFileDropAreas, Tobago.Phase.DOCUMENT_READY);

}(jQuery));
