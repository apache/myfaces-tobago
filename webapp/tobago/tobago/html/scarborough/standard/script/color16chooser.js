var color16values = [, "", "000000", "C0C0C0", "808080", "FFFFFF",
                           "800000", "FF0000", "800080", "FF00FF",
                           "008000", "00FF00", "808000", "FFFF00",
                           "000080", "0000FF", "008080", "00FFFF"];
var colorsSet = 0;
function setColor(id, color) {
  var selector = document.getElementById(id + "_selector");
  if (selector) {
    selector.style.backgroundColor='#' + color;
  }
  document.getElementById(id + "_preview").style.backgroundColor='#' + color;
}
function setColorFromTextBox(id) {
  var selector = document.getElementById(id + "_selector");
  var value = document.getElementById(id + "_textbox" ).value.toUpperCase();
  var selected = 0;
  if (selector) {
    if (value.length == 0) {
      selected = 1;
      value = "ffffff";
    }
    else {
      for (i = 2 ; i < 18 ; i++ ) {
        if (value == color16values[i]) {
          selected = i;
        }
      }
      if (selected == 0) {
        color16values[0] = value;
      }
    }
    selector.options.selectedIndex=selected;
  }
  setColor(id, value);
}

function setColorFromSelector(id) {
  var color =   document.getElementById(id + "_selector" ).options.selectedIndex;
//  if (color==0){
    //setColor(document.getElementById(id + ".textbox" ).value);
//    setColorFromTextBox(id);
//  }
//  else {
    setColor(id, color16values[color]);
    document.getElementById(id + "_textbox").value=
        color16values[color];
//  }
}
function setSelectorColors(id){
  if (colorsSet == 0){
    colorsSet = 1;
    for (var i=2;i<color16values.length; i++){
      document.getElementById(id).options[i].style.backgroundColor='#' + color16values[i];
    }
  }
}
