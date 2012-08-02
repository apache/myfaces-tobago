/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard;

import junit.framework.TestCase;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public abstract class AbstractJavaScriptTestBase extends TestCase {

  protected Context cx;
  protected Scriptable scope;

  protected void setUp() throws Exception {
    super.setUp();
    cx = Context.enter();
    scope = cx.initStandardObjects(null);
  }

  protected void tearDown() throws Exception {
    Context.exit();
  }

  public void testDummy() {

}

  protected Object eval(String script) throws JavaScriptException {
    return cx.evaluateString(scope, script, "test", 1, null);
  }

  protected int evalInt(String script) throws JavaScriptException {
    Object o = eval(script);
    if (o instanceof Number) {
      return ((Number) o).intValue();
    }
    throw new JavaScriptException(null, "invalid return type "
        + o.getClass().getName() + " with value " + o, 0);
  }

  protected long evalLong(String script) throws JavaScriptException {
    Object o = eval(script);
    if (o instanceof Number) {
      return ((Number) o).longValue();
    }
    throw new JavaScriptException(null, "invalid return type "
        + o.getClass().getName() + " with value " + o, 0);
  }

  protected boolean evalBoolean(String script) throws JavaScriptException {
    Object o = eval(script);
    if (o instanceof Boolean) {
      return ((Boolean) o).booleanValue();
    }
    throw new JavaScriptException(null, "invalid return type "
        + o.getClass().getName() + " with value " + o, 0);
  }

  // XXX directory handling +  Maven reactor current dir problem
  protected void loadScriptFile(String jsFile)
      throws IOException, JavaScriptException {
    String fileName
        = "src/main/resources/org/apache/myfaces/tobago/renderkit/html/scarborough/standard/script/" + jsFile;
    File file = new File(fileName);
    if (!file.exists()) {
      fileName = System.getProperty("basedir") + "/" + fileName;
      file = new File(fileName);
    }
    FileReader fileReader = new FileReader(file);
    cx.evaluateReader(scope, fileReader, jsFile, 0, null);
  }
}
