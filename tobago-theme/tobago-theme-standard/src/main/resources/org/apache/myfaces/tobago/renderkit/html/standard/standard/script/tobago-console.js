LOG = {};

LOG.WRAPPER_ACTIVE = false;

/**
 * This a fill-in, if a browser doesn't support "console", or some of its methods.
 * This is NOT a wrapper, so stacktraces, and line numbers remain valid.
 *
 * You can use, surely the following methods:
 *
 * console.log()
 *
 * console.debug() // deprecated, use console.log() instead
 *
 * console.info()
 *
 * console.warn()
 *
 * console.error()
 *
 * console.assert()
 *
 * console.time()
 *
 * console.timeEnd()
 *
 * console.dir()
 *
 * console.exception()
 *
 * console.group() // not implemented
 *
 * console.groupCollapsed() // not implemented
 *
 * console.groupEnd() // not implemented
 *
 * console.profile() // not implemented
 *
 * console.profileEnd() // not implemented
 *
 * console.trace()
 *
 * Restriction: multi args: printf is not implemented for console.log()
 */
if (!window.console) {
  console = {};
}

//XXX if (!console.log) {
console.log = function (message, other) {
  var console = jQuery(".tobago-console");
  console.show();
  var parameters = Array.prototype.slice.call(arguments).join(", ");
  parameters.substr(0, parameters.length - 2);

  // todo: make encoding for all array elements...
  parameters = parameters.replace(/&/g, "&amp;");
  parameters = parameters.replace(/</g, "&lt;");
  parameters = parameters.replace(/>/g, "&gt;");
  parameters = parameters.replace(/"/g, "&quot;");
  jQuery("<p>").appendTo(console).html("<pre>" + parameters + "</pre>");
};

jQuery(document).ready(function () {
  var console = jQuery("<div>").appendTo("body");
  console.addClass("tobago-console");
  console.css({
    border: "5px solid red",
    padding: "10px",
    position: "absolute",
    right: "0",
    bottom: "0",
    backgroundColor: "#ffffff",
    filter: "alpha(opacity=70)",
    opacity: 0.7
  });
  console.hide();
  var header = jQuery("<div>").appendTo(console);
  header.css({
    border: "1px solid red",
    marginBottom: "5px"
  });
  header.css("background-color", "red");
  var title = jQuery("<span>simple console replacement</span>").appendTo(header);
  var close = jQuery("<button>").appendTo(header);
  close.attr("type", "button");
  close.append("Hide");
  close.click(function () {
    console.hide();
  });
  var clear = jQuery("<button>").appendTo(header);
  clear.attr("type", "button");
  clear.append("Clear");
  clear.click(function () {
    console.children("p").detach();
  });
});
//}

console.util_array_slice = function(args, drop) {
  var result = [];
  for (var i = drop; i < args.length; i++) {
    result[i - drop] = args[i];
  }
  return result;
};

console.util_stack_trace = function () {
  var result;
  var e = new Error();
  var skipped;
  var textarea = jQuery("textarea");
  textarea.val("Start Logging...");
  if (e.stack) { // Firefox && WebKit

    textarea.val(textarea.val() + "\n\n" + e.stack);

    result = e.stack.split('\n');

  } else {
    var currentFunction = arguments.callee.caller;
    result = [];
    var i = 0;
    while (currentFunction) {
      result[i++] = currentFunction.toString();
      currentFunction = currentFunction.caller;
    }
  }

  textarea.val(textarea.val() + "\n\n" + result);

  return result;
};

if (!console.debug) {
  console.debug = function (message, other) {
    console.log("DEBUG: " + message, console.util_array_slice(arguments, 1));
  };
}

if (!console.info) {
  console.info = function (message, other) {
    console.log("INFO: " + message, console.util_array_slice(arguments, 1));
  };
}

if (!console.warn) {
  console.warn = function (message, other) {
    console.log("WARN: " + message, console.util_array_slice(arguments, 1));
  };
}

if (!console.error) {
  console.error = function (message, other) {
    console.log("ERROR: " + message, console.util_array_slice(arguments, 1));
  };
}

//XXX if (!console.assert) {
console.assert = function (test, message, other) { // multiargs?
  if (test == false) {
    console.log("ASSERTION FAILED: " + message, console.util_array_slice(arguments, 2));
  }
};
//}  [0]

if (!console.time) {
  console.time = function (name) {
    if (!console.timerMap) {
      console.timerMap = {};
    }
    console.timerMap[name] = new Date().getTime();
  };
}

if (!console.timeEnd) {
  console.timeEnd = function (name) {
    if (!console.timerMap) {
      console.timerMap = {};
    }
    var start = console.timerMap[name];
    if (start) {
      console.info("Timer '" + name + "': " + (new Date().getTime() - start) + " ms");
      console.timerMap[name] = null;
    } else {
      console.warn("Timer '" + name + "' not found!");
    }
  };
}

if (!console.dir) {
  console.dir = function (name) {
    // todo
    console.debug("(dir() not implemented) " + name);
  };
}

if (!console.exception) {
  console.exception = function (other) {
    console.error(arguments);
  };
}

if (!console.group) {
  console.group = function (other) {
    // todo
    console.warn("(group() not implemented)");
  };
}

if (!console.groupCollapsed) {
  console.groupCollapsed = function (other) {
    // todo
    console.warn("(groupCollapsed() not implemented)");
  };
}

if (!console.groupEnd) {
  console.groupEnd = function (other) {
    // todo
    console.warn("(groupEnd() not implemented)");
  };
}

if (!console.profile) {
  console.profile = function (other) {
    // todo
    console.warn("(profile() not implemented)");
  };
}

if (!console.profileEnd) {
  console.profileEnd = function (other) {
    // todo
    console.warn("(profileEnd() not implemented)");
  };
}

if (!console.trace) {
  console.trace = function () {
    console.log("STACK TRACE: " + console.util_stack_trace());
  };
}

if (LOG.WRAPPER_ACTIVE) {
  (function () {
    if (console && console.log) {
      var old = console.log;
      console.log = function () {
        Array.prototype.unshift.call(arguments, LOG.now() + " " + LOG.getStackTrace() + "\n");
        old.apply(this, arguments)
      }
    }
  })();
}

