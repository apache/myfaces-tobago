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

package org.apache.myfaces.tobago.taglib.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.taglib.decl.HasVar;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Replacement for the JSTL &lt;c:foreach> tag. <br />
 * This tags iterates over the body content without setting up an exported
 * scope variable, but replaces all occurrence's of <code>var</code> in
 * <code>TobagoTag's ValueBinding</code> attributes.<br />
 * All non TobagoTags are treated as they are, no
 * replacement is done, and so no ability to use the <code>var</code> in el.
 */
@Tag(name = "forEach")
@Deprecated()

public class ForEachTag extends BodyTagSupport implements HasVar {

  private static final Log LOG = LogFactory.getLog(ForEachTag.class);

  public static final String ALL = "-1";

  private String forEachItems;

  private String var;

  private String begin = "0";

  private String end = ALL;

  private String step = "1";

  private IterationHelper helper;

  public int doStartTag() throws JspException {
    super.doStartTag();

    final FacesContext facesContext = FacesContext.getCurrentInstance();

    if (helper == null) {
      helper = new IterationHelper();
    }


    String replacement = forEachItems.trim();
    if (replacement.startsWith("#{") && replacement.endsWith("}")) {
      replacement = replacement.substring(2, replacement.length() - 1);
    }

    int position = getIntValue(begin);
    int stop = getIntValue(end);
    Object[] keys = null;
    if (stop == IterationHelper.ALL) {
      if (UIComponentTag.isValueReference(forEachItems)) {
        final Object items
            = ComponentUtil.createValueBinding(this.forEachItems).getValue(facesContext);
        if (items instanceof List) {
          stop = ((List) items).size();
        } else if (items instanceof Object[]) {
          stop = ((Object[]) items).length;
        } else if (items instanceof Map) {
          List keyList = new ArrayList();
          for (Iterator i = ((Map) items).keySet().iterator(); i.hasNext();) {
            keyList.add(i.next());
          }
          keys = keyList.toArray(new Object[keyList.size()]);
          stop = keys.length;
        } else if (items == null) {
          if (LOG.isInfoEnabled()) {
            LOG.info("No Elements to render!");
          }
        } else {
          LOG.error("Illegal items object : " + items.getClass().getName());
        }
      } else {
        LOG.error("Not a ValueBinding : \"" + forEachItems + "\"");
      }
      if (stop == IterationHelper.ALL) {
        stop = 0;
      }
    }


    helper.init(replacement, var, position, stop, getIntValue(step), keys);

    return position < stop ? EVAL_BODY_INCLUDE : SKIP_BODY;
  }

  public int doAfterBody() throws JspException {
    return helper.next() ? EVAL_BODY_AGAIN : SKIP_BODY;
  }


  private int getIntValue(String value) {
    int result;
    if (UIComponentTag.isValueReference(value)) {
      ValueBinding valueBinding = FacesContext.getCurrentInstance()
          .getApplication().createValueBinding(value);
      result = ComponentUtil.getIntValue(valueBinding);
    } else {
      result = Integer.parseInt(value);
    }
    return result;
  }


  public void release() {
    super.release();
    forEachItems = null;
    var = null;
    begin = "0";
    end = ALL;
    if (helper != null) {
      helper.reset();
    }
  }

  /**
   * <strong>ValueBindingExpression</strong> pointing to a
   * <code>java.util.List</code>, <code>java.util.Map</code> or
   * <code>Object[]</code> of items to iterate over.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.util.List", "java.util.Map", "java.lang.Object[]"})
  public void setItems(String items) {
    this.forEachItems = items;
  }

  public void setVar(String var) {
    this.var = var;
  }


  /**
   * Index at which the iteration begins.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Integer"}, defaultValue = "0")
  public void setBegin(String begin) {
    this.begin = begin;
  }


  /**
   * Index at which the iteration stops.
   * Defaults to <code>items.length()</code>.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Integer"}, defaultValue = "items.lenght()")
  public void setEnd(String end) {
    this.end = end;
  }


  /**
   * Index increments every iteration by this value.
   */
  @TagAttribute
  @UIComponentTagAttribute(type = {"java.lang.Integer"}, defaultValue = "1")
  public void setStep(String step) {
    this.step = step;
  }

  public IterationHelper getIterationHelper() {
    return helper;
  }

  public static class IterationHelper {

    public static final int ALL = -1;

    private int helperPosition;
    private int helperStop;
    private int helperStep;
    private String helperReplacement;
    private Object[] helperKeys;

    private Pattern pattern;

    public IterationHelper() {
      reset();
    }

    public boolean next() {
      helperPosition += helperStep;
      return helperPosition < helperStop;
    }

    public String replace(String binding) {
      final String result = pattern.matcher(binding).replaceAll(
          "$1" + helperReplacement + "["
              + (helperKeys == null ? Integer.toString(helperPosition) : "'" + helperKeys[helperPosition] + "'")
              + "]$2");
      if (LOG.isDebugEnabled()) {
        LOG.debug("transform \"" + binding + "\" to \"" + result + "\"");
      }
      return result;
    }

    public void reset() {
      helperPosition = 0;
      helperStop = ALL;
      helperStep = 1;
      helperReplacement = null;
      helperKeys = null;
    }


    public void init(String replacement, String var, int position, int stop,
        int step, Object[] keys) {
      this.helperReplacement = replacement;
      this.helperPosition = position;
      this.helperStop = stop;
      this.helperStep = step;
      this.helperKeys = keys;
      pattern = Pattern.compile("(\\W *?[^\\.] *?)" + var + "( *?\\W)");
    }
  }
}
