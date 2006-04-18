package org.apache.myfaces.tobago.facelets;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.sun.facelets.tag.AbstractTagLibrary;

/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 18.04.2006
 * Time: 14:22:52
 * To change this template use File | Settings | File Templates.
 */
public class AbstractTobagoTagLibrary  extends AbstractTagLibrary {

  public AbstractTobagoTagLibrary(String namespace) {
    super(namespace);
    addTagHandler("attribute", AttributeHandler.class);
  }

}
