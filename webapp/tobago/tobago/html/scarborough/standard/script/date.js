  function calendarWindow(url) {

//    var scrX = window.event.screenX;
//    var scrY = window.event.screenY;
    var scrX = 300;
    var scrY = 300;

    var avaWidth = screen.availWidth;
    var avaHeight= screen.availHeight;

//    alert(scrX+" "+ scrY+"\n"+avaWidth+" "+avaHeight );

    var pickerWidth = 204;
    var pickerHeight = 280;

    var xPos;
    var yPos;

    if (scrX + pickerWidth + 4 > avaWidth) {
      xPos = avaWidth - (pickerWidth+4);
    } else {
      xPos = scrX;
    }
    if (scrY + pickerHeight + 24 > avaHeight) {
      yPos = avaHeight - (pickerHeight+24);
    } else {
      yPos = scrY;
    }

    var calendarWindow = window.open(url, "Calendar",
        "width=330,height=330,left="+xPos+",top="+yPos+",scrollbars=0,resizable=0,resizable=0,dependent=1");
    calendarWindow.focus();

  }
