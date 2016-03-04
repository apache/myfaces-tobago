package org.apache.myfaces.tobago.renderkit.util;

import javax.faces.FacesException;
import javax.faces.FacesWrapper;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

public class HttpPartWrapper implements Part, FacesWrapper<Part>, StateHolder {
  private Part delegate;

  public HttpPartWrapper() {
  }

  public HttpPartWrapper(Part delegate) {
    this.delegate = delegate;
  }

  public void delete() throws IOException {
    getWrapped().delete();
  }

  public String getContentType() {
    return getWrapped().getContentType();
  }

  public String getHeader(String headerName) {
    return getWrapped().getHeader(headerName);
  }

  public Collection<String> getHeaderNames() {
    return getWrapped().getHeaderNames();
  }

  public Collection<String> getHeaders(String headerName) {
    return getWrapped().getHeaders(headerName);
  }

  public InputStream getInputStream() throws IOException {
    return getWrapped().getInputStream();
  }

  public String getName() {
    return getWrapped().getName();
  }

  public long getSize() {
    return getWrapped().getSize();
  }

  public void write(String fileName) throws IOException {
    getWrapped().write(fileName);
  }

  public String getSubmittedFileName() {
    Part wrapped = getWrapped();
    try {
      Method m = wrapped.getClass().getMethod("getSubmittedFileName");
      return (String) m.invoke(wrapped);
    } catch (NoSuchMethodException ex) {
      throw new FacesException(ex);
    } catch (SecurityException ex) {
      throw new FacesException(ex);
    } catch (IllegalAccessException ex) {
      throw new FacesException(ex);
    } catch (IllegalArgumentException ex) {
      throw new FacesException(ex);
    } catch (InvocationTargetException ex) {
      throw new FacesException(ex);
    }
  }

  public Object saveState(FacesContext context) {
    return null;
  }

  public void restoreState(FacesContext context, Object state) {
  }

  public boolean isTransient() {
    return true;
  }

  public void setTransient(boolean newTransientValue) {
  }

  public Part getWrapped() {
    return delegate;
  }

}
