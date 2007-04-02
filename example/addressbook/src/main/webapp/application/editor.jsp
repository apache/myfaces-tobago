<%--
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
--%>
<%@ taglib uri="http://myfaces.apache.org/tobago/component" prefix="tc" %>
<%@ taglib uri="http://myfaces.apache.org/tobago/extension" prefix="tx" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<f:view locale="#{controller.language}">
  <tc:loadBundle basename="resource" var="bundle" />

  <tc:page label="#{bundle.editorTitle}" state="#{layout}" width="#{layout.width}" height="#{layout.height}">

    <tc:panel>
      <f:facet name="layout">
        <tc:gridLayout margin="10px"/>
      </f:facet>

      <tc:box label="#{bundle.editorBoxTitle}">
        <f:facet name="layout">
          <tc:gridLayout rows="fixed;1*;fixed" />
        </f:facet>

        <tc:messages />

        <tc:tabGroup>
        <tc:tab label="#{bundle.editorTabPersonal}">
          <tc:panel>
            <f:facet name="layout">
              <tc:gridLayout rows="fixed;fixed"/>
            </f:facet>
            <tc:panel>
              <f:facet name="layout">
                <tc:gridLayout columns="*;120px"/>
              </f:facet>
              <tc:panel>
                <f:facet name="layout">
                  <tc:gridLayout rows="fixed;fixed;fixed;fixed;fixed;*"/>
                </f:facet>
                <tx:in value="#{controller.currentAddress.firstName}"
                       label="#{bundle.editorFirstName}" required="true">
                  <f:validateLength minimum="2" maximum="20"/>
                </tx:in>

                <tx:label value="#{bundle.editorLastName}">
                  <tc:in value="#{controller.currentAddress.lastName}"
                         required="true"/>
                </tx:label>

                <tc:panel>
                  <f:facet name="layout">
                    <tc:gridLayout columns="6*;1*"/>
                  </f:facet>
                  <tx:in value="#{controller.currentAddress.street}"
                         label="#{bundle.editorStreet}"/>
                  <tc:in value="#{controller.currentAddress.houseNumber}"/>
                </tc:panel>

                <tc:panel>
                  <f:facet name="layout">
                    <tc:gridLayout columns="1*;1*"/>
                  </f:facet>
                  <tx:in value="#{controller.currentAddress.zipCode}"
                         label="#{bundle.editorCity}"/>
                  <tc:in value="#{controller.currentAddress.city}"/>
                </tc:panel>

                <tx:selectOneChoice
                    value="#{controller.currentAddress.country}"
                    label="#{bundle.editorCountry}">
                  <f:selectItems value="#{countries}"/>
                </tx:selectOneChoice>
                <tc:cell></tc:cell>
              </tc:panel>
              <tc:panel>
                <f:facet name="layout">
                  <tc:gridLayout rows="160px" columns="120px"/>
                </f:facet>
                <tc:form>
                  <tc:button
                      image="#{controller.currentAddress.picture != null?'${pageContext.request.contextPath}/faces/picture?id=XXXX':'image/empty_portrait.png'}"
                      action="#{controller.popupFileUpload}">
                    <f:facet name="popup">
                      <tc:popup width="300px" height="170px" left="200px"
                                top="200px" rendered="#{controller.renderFileUploadPopup}"
                                id="popup-fileUpload">
                        <tc:box label="FileUpload">
                          <f:facet name="layout">
                            <tc:gridLayout rows="fixed;1*;fixed" margin="10" />
                          </f:facet>
                          <tc:file value="#{controller.uploadedFile}" required="true">
                            <tc:validateFileItem contentType="image/*" />
                          </tc:file>
                          <tc:messages/>
                          <tc:panel>
                            <f:facet name="layout">
                               <tc:gridLayout columns="1*;100px;100px" />
                            </f:facet>
                            <tc:cell/>
                            <tc:button action="#{controller.okFileUpload}" label="OK" />
                            <tc:button action="#{controller.cancelFileUpload}" label="Cancel" immediate="true"/>
                          </tc:panel>
                        </tc:box>
                      </tc:popup>
                    </f:facet>
                  </tc:button>
                </tc:form>
              </tc:panel>
            </tc:panel>
              <tc:panel>
                <f:facet name="layout">
                  <tc:gridLayout rows="fixed;fixed;fixed;fixed;fixed;fixed;fixed;1*"/>
                </f:facet>
                <tx:in value="#{controller.currentAddress.phone}"
                    label="#{bundle.editorPhone}"
                    validator="#{controller.validatePhoneNumber}"/>

                <tx:in value="#{controller.currentAddress.mobile}"
                    label="#{bundle.editorMobile}"
                    validator="#{controller.validatePhoneNumber}"/>

                <tx:in value="#{controller.currentAddress.fax}"
                    label="#{bundle.editorFax}"
                    validator="#{controller.validatePhoneNumber}"/>

                <tx:in value="#{controller.currentAddress.email}"
                       label="#{bundle.editorEmail}">
                  <f:validator validatorId="EmailAddressValidator"/>
                </tx:in>

                <tx:in value="#{controller.currentAddress.icq}"
                       label="#{bundle.editorIcq}">
                  <f:validateLongRange minimum="0"/>
                </tx:in>
                <tx:in value="#{controller.currentAddress.homePage}"
                       label="#{bundle.editorHomepage}"/>

                <tx:date id="dayOfBirth" value="#{controller.currentAddress.dayOfBirth}"
                         label="#{bundle.editorBirthday}">
                  <f:convertDateTime pattern="#{bundle.editorDatePattern}"/>
                </tx:date>

                <tc:cell/>
              </tc:panel>
            </tc:panel>
          </tc:tab>

          <tc:tab label="#{bundle.editorTabBusiness}" rendered="#{!controller.simple}">
            <tc:panel>
              <f:facet name="layout">
                <tc:gridLayout rows="fixed;fixed;fixed;fixed;fixed;1*" />
              </f:facet>
              <tx:in value="#{controller.currentAddress.company}"
                  label="#{bundle.editorJobCompany}" />

              <tx:in value="#{controller.currentAddress.jobTitle}"
                  label="#{bundle.editorJobTitle}" />

              <tx:in value="#{controller.currentAddress.jobPhone}"
                  label="#{bundle.editorPhone}"
                  validator="#{controller.validatePhoneNumber}" />

              <tx:in value="#{controller.currentAddress.jobEmail}"
                  label="#{bundle.editorEmail}">
                <f:validator validatorId="EmailAddressValidator"/>
              </tx:in>

              <tx:in value="#{controller.currentAddress.jobHomePage}"
                  label="#{bundle.editorHomepage}" />

              <tc:cell/>

            </tc:panel>
          </tc:tab>

          <tc:tab label="#{bundle.editorTabMisc}" rendered="#{!controller.simple}">
            <tc:panel>
              <f:facet name="layout">
                <tc:gridLayout rows="1*" />
              </f:facet>

              <tx:textarea value="#{controller.currentAddress.note}"
                  label="#{bundle.editorNote}" />
            </tc:panel>

          </tc:tab>
        </tc:tabGroup>

        <tc:panel>
          <f:facet name="layout">
            <tc:gridLayout columns="3*;1*;1*" />
          </f:facet>

          <tc:cell />
          <tc:button action="#{controller.store}"
              label="#{bundle.editorStore}" defaultCommand="true" />
          <tc:button action="#{controller.cancel}" immediate="true"
              label="#{bundle.editorCancel}" />
        </tc:panel>

      </tc:box>

    </tc:panel>
  </tc:page>
</f:view>
