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

package org.apache.myfaces.tobago.renderkit.css;

import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Overflow;
import org.apache.myfaces.tobago.layout.Position;
import org.apache.myfaces.tobago.layout.TextAlign;
import org.junit.Assert;
import org.junit.Test;

public class StyleUnitTest {

  @Test
  public void testEncodedEmpty() {
    Style style = new Style();
    Assert.assertEquals("Check encoder", "", style.encode());
    Assert.assertEquals("Check encoder JSON", "{}", style.encodeJson());
  }

  @Test
  public void testEncodedFull() {
    Style style = new Style();

    style.setWidth(Measure.valueOf(110));
    style.setHeight(Measure.valueOf(220));

    style.setMinWidth(Measure.valueOf(100));
    style.setMinHeight(Measure.valueOf(200));
    style.setMaxWidth(Measure.valueOf(111));
    style.setMaxHeight(Measure.valueOf(222));

    style.setLeft(Measure.valueOf(44));
    style.setRight(Measure.valueOf(55));
    style.setTop(Measure.valueOf(66));
    style.setBottom(Measure.valueOf(77));

    style.setPaddingLeft(Measure.valueOf(333));
    style.setPaddingRight(Measure.valueOf(444));
    style.setPaddingTop(Measure.valueOf(555));
    style.setPaddingBottom(Measure.valueOf(666));

    style.setMarginLeft(Measure.valueOf(3333));
    style.setMarginRight(Measure.valueOf(4444));
    style.setMarginTop(Measure.valueOf(5555));
    style.setMarginBottom(Measure.valueOf(6666));

    style.setOverflowX(Overflow.auto);
    style.setOverflowX(Overflow.scroll);
    style.setDisplay(Display.block);
    style.setPosition(Position.absolute);

    style.setBackgroundImage("url('icon.pgn')");
    style.setBackgroundPosition("center");
    style.setZIndex(2);
    style.setTextAlign(TextAlign.justify);

    final String encoded = style.encode();
    final String json = style.encodeJson();

    final String manually = ("{\"" + encoded
        .replaceAll(":", "\":\"")
        .replaceAll(";", "\",\"")
        .replaceAll("-h", "H")
        .replaceAll("-w", "W")
        .replaceAll("-l", "L")
        .replaceAll("-r", "R")
        .replaceAll("-t", "T")
        .replaceAll("-b", "B")
        .replaceAll("-x", "X")
        .replaceAll("-y", "Y")
        .replaceAll("-i", "I")
        .replaceAll("-a", "A")
        .replaceAll("-p", "P")
        .replaceAll("\"2\"", "2")
        + "\"}")
        .replaceAll(",\"\"}", "}");

    Assert.assertEquals("Check different encoders", manually, json);
  }
}
