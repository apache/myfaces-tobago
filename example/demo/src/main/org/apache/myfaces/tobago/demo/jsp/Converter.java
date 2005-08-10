/*
 * Copyright (c) 2001 Atanion GmbH, Germany. All rights reserved.
 * Created on: 02.09.2002, 23:26:31
 * $Id: Converter.java,v 1.1.1.1 2004/04/15 18:41:00 idus Exp $
 */
package org.apache.myfaces.tobago.demo.jsp;

public interface Converter {

  public String convertMisc(String fragment);
  public String convertMatch(String fragment);

  public String convert(String fragment);

}
