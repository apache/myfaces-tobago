console.log("mytest huhu");

const sourceDir = "node_modules/jsf.js_next_gen/dist/window/";
const targetDir = "tobago-theme-standard/src/main/js/";
const jsFile = "jsf.js";
const mapFile = "jsf.js.map";

const fs = require('fs')

// copy and patch jsf.js
fs.readFile(sourceDir + jsFile, "utf8", function (e, data) {
  if (e) {
    console.error(e);
  } else {

    // This replace is, because the last line refers to a sourceMappingURL which is not available in Tobago.
    // In Safari, with open Development Console, we got an 404.xhtml request otherwise (not shown in network section).

    const result = data.replace(/\n\/\/# sourceMappingURL=jsf\.js\.map\.jsf\?ln=scripts/g, "");

    fs.writeFile(targetDir + jsFile, result, 'utf8', function (e) {
      if (e) {
        console.error(e);
      }
    });
  }
});

// copy jsf.js.map
fs.copyFile(sourceDir + mapFile, targetDir + mapFile, function (e) {
  if (e) {
    console.error(e);
  }
});
