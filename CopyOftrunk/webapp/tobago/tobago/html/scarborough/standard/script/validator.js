/* Copyright (c) 2004 Atanion GmbH, Germany
   All rights reserved.
   $Id$
  */

function validateRequired(id){
   if (document.getElementById(id).value == "") {
     alert('Element needs a value');
     document.getElementById(id).focus();
     return false;
   }
   return true;
}

function validateLongRange(id){
   return true;
}
