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

package org.apache.myfaces.tobago.util;

import org.apache.myfaces.tobago.internal.config.AbstractTobagoTestBase;
import org.apache.myfaces.tobago.validator.FileItemValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.faces.application.FacesMessage;

import java.util.Locale;

public class MessageUtilsUnitTest extends AbstractTobagoTestBase {

  @Test
  public void testDummy() {
    final FacesMessage dummy = MessageUtils.getMessage(
        facesContext,
        FacesMessage.SEVERITY_INFO,
        "dummy");
    Assertions.assertEquals("dummy", dummy.getSummary());
    Assertions.assertEquals("dummy", dummy.getDetail());
  }

  @Test
  public void testDefault() {
    final FacesMessage locale = MessageUtils.getMessage(
        facesContext,
        FacesMessage.SEVERITY_INFO,
        FileItemValidator.CONTENT_TYPE_MESSAGE_ID, "application/pdf");
    Assertions.assertEquals("Content type error", locale.getSummary());
    Assertions.assertEquals("The given file is not content type of 'application/pdf'.", locale.getDetail());
  }

  @Test
  public void testGermany() {
    facesContext.getViewRoot().setLocale(Locale.GERMANY);

    final FacesMessage locale = MessageUtils.getMessage(
        facesContext,
        FacesMessage.SEVERITY_INFO,
        FileItemValidator.CONTENT_TYPE_MESSAGE_ID, "application/pdf");
    Assertions.assertEquals("Dateityp Fehler", locale.getSummary());
    Assertions.assertEquals("Die hochgeladene Datei ist nicht vom Typ 'application/pdf'.", locale.getDetail());
  }

  @Test
  public void testSpanish() {
    facesContext.getViewRoot().setLocale(Locale.forLanguageTag("es"));

    final FacesMessage locale = MessageUtils.getMessage(
        facesContext,
        FacesMessage.SEVERITY_INFO,
        FileItemValidator.CONTENT_TYPE_MESSAGE_ID, "application/pdf");
    Assertions.assertEquals("Error de tipo de contenido", locale.getSummary());
    Assertions.assertEquals("El archivo no es del tipo de contenido 'application/pdf'.", locale.getDetail());
  }

  @Test
  public void testFallback() {
    facesContext.getViewRoot().setLocale(Locale.forLanguageTag("ja"));

    final FacesMessage locale = MessageUtils.getMessage(
        facesContext,
        FacesMessage.SEVERITY_INFO,
        FileItemValidator.CONTENT_TYPE_MESSAGE_ID, "application/pdf");
    Assertions.assertEquals("Content type error", locale.getSummary());
    Assertions.assertEquals("The given file is not content type of 'application/pdf'.", locale.getDetail());
  }
}
