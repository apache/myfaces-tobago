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

package org.apache.myfaces.tobago.facelets;

import jakarta.el.ELContext;
import jakarta.el.ELException;
import jakarta.el.MethodExpression;
import jakarta.el.MethodInfo;
import jakarta.el.MethodNotFoundException;
import jakarta.el.PropertyNotFoundException;
import jakarta.faces.FacesWrapper;
import jakarta.faces.view.facelets.TagAttribute;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serial;

/*
 * Was copied from MyFaces-Impl.
 */

/**
 * Jacob Hookom
 */
public final class TagMethodExpression extends MethodExpression
    implements Externalizable, FacesWrapper<MethodExpression> {

  @Serial
  private static final long serialVersionUID = 1L;

  private String attr;
  private MethodExpression orig;

  public TagMethodExpression() {
    super();
  }

  public TagMethodExpression(final TagAttribute attr, final MethodExpression orig) {
    this.attr = attr.toString();
    this.orig = orig;
  }

  @Override
  public MethodInfo getMethodInfo(final ELContext context) {
    try {
      return this.orig.getMethodInfo(context);
    } catch (final PropertyNotFoundException pnfe) {
      throw new PropertyNotFoundException(this.attr + ": " + pnfe.getMessage(), pnfe.getCause());
    } catch (final MethodNotFoundException mnfe) {
      throw new MethodNotFoundException(this.attr + ": " + mnfe.getMessage(), mnfe.getCause());
    } catch (final ELException e) {
      throw new ELException(this.attr + ": " + e.getMessage(), e.getCause());
    }
  }

  @Override
  public Object invoke(final ELContext context, final Object[] params) {
    try {
      return this.orig.invoke(context, params);
    } catch (final PropertyNotFoundException pnfe) {
      throw new PropertyNotFoundException(this.attr + ": " + pnfe.getMessage(), pnfe.getCause());
    } catch (final MethodNotFoundException mnfe) {
      throw new MethodNotFoundException(this.attr + ": " + mnfe.getMessage(), mnfe.getCause());
    } catch (final ELException e) {
      throw new ELException(this.attr + ": " + e.getMessage(), e.getCause());
    }
  }

  @Override
  public String getExpressionString() {
    return this.orig.getExpressionString();
  }

  public boolean equals(final Object obj) {
    return this.orig.equals(obj);
  }

  public int hashCode() {
    return this.orig.hashCode();
  }

  @Override
  public boolean isLiteralText() {
    return this.orig.isLiteralText();
  }

  @Override
  public void writeExternal(final ObjectOutput out) throws IOException {
    out.writeObject(this.orig);
    out.writeUTF(this.attr);
  }

  @Override
  public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
    this.orig = (MethodExpression) in.readObject();
    this.attr = in.readUTF();
  }

  public String toString() {
    return this.attr + ": " + this.orig;
  }

  @Override
  public MethodExpression getWrapped() {
    return this.orig;
  }
}
