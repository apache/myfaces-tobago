/*
 * Utilities to make client side tests easier.
 */

function checkLeft(id, left) {
  var offsetLeft = document.getElementById(id).offsetLeft;
  if (offsetLeft != left) {
    LOG.error("The element with id=" + id + " has wrong left: expected=" + left
        + " actual=" + offsetLeft);
  }
}

function checkTop(id, top) {
  var offsetTop = document.getElementById(id).offsetTop;
  if (offsetTop != top) {
    LOG.error("The element with id=" + id + " has wrong top: expected=" + top
        + " actual=" + offsetTop);
  }
}

function checkWidth(id, width) {
  var offsetWidth = document.getElementById(id).offsetWidth;
  if (offsetWidth != width) {
    LOG.error("The element with id=" + id + " has wrong width: expected=" + width
        + " actual=" + offsetWidth);
  }
}

function checkHeight(id, height) {
  var offsetHeight = document.getElementById(id).offsetHeight;
  if (offsetHeight != height) {
    LOG.error("The element with id=" + id + " has wrong height: expected=" + height
        + " actual=" + offsetHeight);
  }
}

function checkLayout(id, left, top, width, height) {
  checkLeft(id, left);
  checkTop(id, top);
  checkWidth(id, width);
  checkHeight(id, height);
}
