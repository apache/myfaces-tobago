/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 16.03.2004 12:53:20.
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.taglib.decl.HasVar;

import javax.faces.context.FacesContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Load a resource bundle localized for the Locale of the current view
 * from the tobago resource path, and expose it (as a Map) in the request
 * attributes of the current request.
 */
@Tag(name = "loadBundle", bodyContent = BodyContent.EMPTY)
public class LoadBundleTag extends TagSupport implements HasVar {

  private String basename;

  private String var;

  public int doStartTag() throws JspException {
    Map toStore = new BundleMapWrapper(basename);
    FacesContext.getCurrentInstance().getExternalContext()
        .getSessionMap().put(var, toStore);
//        .getRequestMap().put(var, toStore);

    return EVAL_BODY_INCLUDE;
  }

  public void release() {
    basename = null;
    var = null;
  }

  public String getBasename() {
    return basename;
  }

  /**
   * Base name of the resource bundle to be loaded.
   */
  @TagAttribute(required = true)
  @UIComponentTagAttribute()
  public void setBasename(String basename) {
    this.basename = basename;
  }

  public String getVar() {
    return var;
  }

  public void setVar(String var) {
    this.var = var;
  }

  private static class BundleMapWrapper implements Map {

    private String basename;

    public BundleMapWrapper(String basename) {
      this.basename = basename;
    }

    public void clear() {
      throw new UnsupportedOperationException();
    }

    public boolean containsKey(Object key) {
      if (null == key) {
        return false;
      }
      String value = ResourceManagerUtil.getPropertyNotNull(
          FacesContext.getCurrentInstance(), basename, key.toString());
      return value != null;
    }

    public boolean containsValue(Object value) {
      throw new UnsupportedOperationException();
    }

    public Set entrySet() {
      throw new UnsupportedOperationException();
    }

    public Object get(Object key) {
      if (null == key) {
        return null;
      }
      String value = ResourceManagerUtil.getPropertyNotNull(
          FacesContext.getCurrentInstance(), basename, key.toString());
      return value;
    }

    public int hashCode() {
      return basename.hashCode();
    }

    public boolean isEmpty() {
      throw new UnsupportedOperationException();
    }

    public Set keySet() {
      throw new UnsupportedOperationException();
    }

    public Object put(Object k, Object v) {
      throw new UnsupportedOperationException();
    }

    public void putAll(Map t) {
      throw new UnsupportedOperationException();
    }

    public Object remove(Object k) {
      throw new UnsupportedOperationException();
    }

    public int size() {
      throw new UnsupportedOperationException();
    }

    public Collection values() {
      throw new UnsupportedOperationException();
    }
  }
}

