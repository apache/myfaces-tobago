package org.apache.myfaces.tobago.example.demo.jsp;

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

/*
 * Created on: 02.09.2002, 23:26:31
 * $Id: Converter.java,v 1.1.1.1 2004/04/15 18:41:00 idus Exp $
 */

public interface Converter {

  String convertMisc(String fragment);

  String convertMatch(String fragment);

  String convert(String fragment);

}
