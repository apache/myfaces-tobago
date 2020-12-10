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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UIDate;
import org.apache.myfaces.tobago.convert.DateTimeConverter;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateRendererUnitTest extends RendererTestBase {

  @Test
  public void date() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    DateTimeConverter c = new DateTimeConverter();
    c.setPattern("dd.MM.YYYY");
    d.setConverter(c);

    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/date.html"), formattedResult());
  }

  @Test
  public void dateLabel() throws IOException, ParseException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setLabel("Label");
    DateTimeConverter c = new DateTimeConverter();
    c.setPattern("dd.MM.yyyy");
    d.setConverter(c);
    final SimpleDateFormat sdf = new SimpleDateFormat(c.getPattern());
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    d.setValue(sdf.parse("10.12.2020"));

    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/date-label.html"), formattedResult());
  }

  @Test
  public void dateTodayButton() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setLabel("Label");
    d.setTodayButton(true);
    DateTimeConverter c = new DateTimeConverter();
    c.setPattern("dd.MM.yyyy");
    d.setConverter(c);

    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/date-today-button.html"), formattedResult());
  }

}
