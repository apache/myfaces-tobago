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

import jakarta.faces.application.FacesMessage;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.DateTimeConverter;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.Tags;
import org.apache.myfaces.tobago.component.UIDate;
import org.apache.myfaces.tobago.component.UIOut;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class DateRendererUnitTest extends RendererTestBase {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final LocalDateTime SPUTNIK_LOCAL_DATE_TIME = LocalDateTime.of(1957, 10, 5, 0, 28, 34, 123456789);
  private static final LocalDate SPUTNIK_LOCAL_DATE = SPUTNIK_LOCAL_DATE_TIME.toLocalDate();
  private static final LocalTime SPUTNIK_LOCAL_TIME = SPUTNIK_LOCAL_DATE_TIME.toLocalTime();
  private static final ZonedDateTime SPUTNIK_ZONED_DATE_TIME = SPUTNIK_LOCAL_DATE_TIME.atZone(ZoneId.of("+05:00"));
  private static final Date SPUTNIK_DATE = Date.from(SPUTNIK_ZONED_DATE_TIME.toInstant());

//  Naming scheme: value-type + converter-type

  @Test
  public void dateBoth() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setValue(SPUTNIK_DATE);

    DateTimeConverter c = new DateTimeConverter();
    c.setType("both");
    c.setPattern("yyyy-MM-dd'T'HH:mm:ss");
    d.setConverter(c);

    log(d);
    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/dateBoth.html"), formattedResult());
  }

  @Test
  public void dateDate() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setValue(SPUTNIK_DATE);

    DateTimeConverter c = new DateTimeConverter();
    c.setType("date");
    c.setPattern("yyyy-MM-dd");
    d.setConverter(c);

    log(d);
    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/dateDate.html"), formattedResult());
  }

  @Test
  public void dateFacetBefore() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setValue(SPUTNIK_DATE);

    final UIOut out = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "out");
    d.getFacets().put("before", out);
    out.setValue("before");

    log(d);
    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/dateFacetBefore.html"), formattedResult());
  }

  @Test
  public void dateFacetAfter() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setValue(SPUTNIK_DATE);

    final UIOut out = (UIOut) ComponentUtils.createComponent(
        facesContext, Tags.out.componentType(), RendererTypes.Out, "out");
    out.setValue("after");
    d.getFacets().put("after", out);

    log(d);
    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/dateFacetAfter.html"), formattedResult());
  }

  @Test
  public void dateAuto() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setValue(SPUTNIK_DATE);

    log(d);
    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/dateAuto.html"), formattedResult());
  }

  @Test
  public void dateTime() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setValue(SPUTNIK_DATE);

    DateTimeConverter c = new DateTimeConverter();
    c.setType("time");
    c.setPattern("HH:mm:ss");
    d.setConverter(c);

    log(d);
    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/dateTime.html"), formattedResult());
  }

  @Test
  public void dateTimeStep() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setValue(SPUTNIK_DATE);
    d.setStep(.001);

    DateTimeConverter c = new DateTimeConverter();
    c.setType("time");
    c.setPattern("HH:mm:ss.SSS");
    d.setConverter(c);

    log(d);
    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/dateTimeStep.html"), formattedResult());
  }

  @Test
  public void testLabel() throws IOException, ParseException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setLabel("Label");
    d.setValue(SPUTNIK_LOCAL_DATE);

    log(d);
    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/testLabel.html"), formattedResult());
  }

  @Test
  public void localDateAuto() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setValue(SPUTNIK_LOCAL_DATE);

    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/localDateAuto.html"), formattedResult());
  }

  @Test
  public void minMax() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setValue(SPUTNIK_LOCAL_DATE);
    d.setMin(SPUTNIK_LOCAL_DATE.minusDays(30));
    d.setMax(SPUTNIK_LOCAL_DATE.plusDays(30));

    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/minMax.html"), formattedResult());
  }

  @Test
  public void localDateTimeAuto() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setValue(SPUTNIK_LOCAL_DATE_TIME);

    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/localDateTimeAuto.html"), formattedResult());
  }

  // old

  @Test
  public void zonedDateTimeAuto() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setValue(SPUTNIK_ZONED_DATE_TIME);

    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/zonedDateTimeAuto.html"), formattedResult());
  }

  // todo: might be removed
  @Test
  public void testTodayButton() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setLabel("Label");
    d.setValue(SPUTNIK_LOCAL_DATE);
    d.setTodayButton(true);

    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/testTodayButton.html"), formattedResult());
  }

  @Test
  @Disabled
  public void text() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setValue(SPUTNIK_LOCAL_DATE_TIME);

    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/text.html"), formattedResult());
  }

  @Test
  public void localTimeAuto() throws IOException {

    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setValue(SPUTNIK_LOCAL_TIME);

    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/localTimeAuto.html"), formattedResult());
  }

  @Test
  public void errorMessage() throws IOException {
    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setValue(SPUTNIK_LOCAL_DATE);

    d.setValid(false);
    facesContext.addMessage("id",
        new FacesMessage(FacesMessage.SEVERITY_ERROR, "test", "a test"));
    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/error-message.html"), formattedResult());
  }

  @Test
  public void help() throws IOException {
    final UIDate d = (UIDate) ComponentUtils.createComponent(
        facesContext, Tags.date.componentType(), RendererTypes.Date, "id");
    d.setValue(SPUTNIK_LOCAL_DATE);

    d.setHelp("Help!");
    d.encodeAll(facesContext);

    Assertions.assertEquals(loadHtml("renderer/date/help.html"), formattedResult());
  }

  private void log(UIDate d) {
    Converter<?> converter = d.getConverter();
    String pattern = converter instanceof DateTimeConverter ? ((DateTimeConverter) converter).getPattern() : "-";
    String type = converter instanceof DateTimeConverter ? ((DateTimeConverter) converter).getType() : "-";
    LOG.info(
        "type-of-value='{}' with converter='{}', pattern='{}', type='{}'",
        d.getValue().getClass().getName(),
        converter != null ? converter.getClass().getName() : "-",
        pattern,
        type);
  }

}
