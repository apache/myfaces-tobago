Tobago.loadPngFix= function() {

    var images = document.getElementsByTagName("img");
    var supported = /MSIE (5\.5)|[6789]/.test(navigator.userAgent) && navigator.platform == "Win32";

    if (! supported)  {
      return;
    }

    for (i = 0; i < images.length; i++) {

      var image = images[i];

      fixImage(image);
      Tobago.addEventListener(image, 'propertyChanged', propertyChanged);
    }
  }

  function propertyChanged() {

     var pName = event.propertyName;
     if (pName != "src") return;
     // if not set to blank
     if ( ! new RegExp(Tobago.pngFixBlankImage).test(src))  {
        fixImage(this);
     }
  }

  function fixImage(element) {
     // get src
     var src = element.src;
     // check for real change

     if (src == element.realSrc) {
        element.src = Tobago.pngFixBlankImage;
        return;
     }

     if ( ! new RegExp(Tobago.pngFixBlankImage).test(src)) {
        // backup old src
        element.realSrc = src;
     }

     // test for png
     if (element.realSrc != null &&
         /\.png$/.test( element.realSrc.toLowerCase() ) ) {
       // get width and height of old src
       var origWidth = element.clientWidth;
       var origHeight = element.clientHeight;

        // set blank image
        element.src = Tobago.pngFixBlankImage;
        // set filter

        element.runtimeStyle.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" +
                                       src + "',sizingMethod='scale')";
        element.style.width = origWidth + 'px';
        element.style.height = origHeight + 'px';

     } else {
        // remove filter
        element.runtimeStyle.filter = "";
     }
  }