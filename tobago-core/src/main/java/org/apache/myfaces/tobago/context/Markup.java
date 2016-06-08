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

package org.apache.myfaces.tobago.context;

import org.apache.commons.collections.iterators.EmptyIterator;
import org.apache.commons.collections.iterators.ObjectArrayIterator;
import org.apache.commons.collections.iterators.SingletonIterator;
import org.apache.myfaces.tobago.internal.util.ArrayUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * A markup signs a component to be rendered different from the normal.
 * E. g. <code>markup="emphasized"</code> might be rendered bold
 * or a <code>markup="deleted"</code> might be rendered with line-through style.
 * The concrete rendering depends from the theme.
 * </p>
 * <p>
 * The markup can also hold more than one value, e. g. <code>markup="emphasized, deleted"</code>.
 * </p>
 * <p>
 * The value of the markup is unmodifiable.
 * </p>
 * <p>
 * A markup must be registered for a component, before it can be used.
 * </p>
 * <p>
 * A markup should only contain ASCII characters and digits.
 * </p>
 * <p>
 * In JSPs the class {@link org.apache.myfaces.tobago.context.MarkupEditor} will convert the string literals.
 * </p>
 */
public final class Markup implements Serializable, Iterable<String> {

  public static final Markup NULL = new Markup((String) null);

  public static final Markup ASCENDING = valueOf("ascending");
  public static final Markup BIG = valueOf("big");
  public static final Markup BORDERED = valueOf("bordered");
  public static final Markup CENTER = valueOf("center");
  public static final Markup CLICKABLE = valueOf("clickable");
  public static final Markup DEFAULT = valueOf("default");
  public static final Markup DELETED = valueOf("deleted");
  public static final Markup DESCENDING = valueOf("descending");
  public static final Markup DISABLED = valueOf("disabled");
  public static final Markup ERROR = valueOf("error");
  public static final Markup EVEN = valueOf("even");
  public static final Markup EXPANDED = valueOf("expanded");
  public static final Markup FILLER = valueOf("filler");
  public static final Markup FATAL = valueOf("fatal");
  /**
   * @deprecated Can be selected via CSS3.
   */
  @Deprecated
  public static final Markup FIRST = valueOf("first");
  public static final Markup FOLDER = valueOf("folder");
  public static final Markup HOVER = valueOf("hover");
  public static final Markup INFO = valueOf("info");
  public static final Markup INLINE = valueOf("inline");
  public static final Markup JUSTIFY = valueOf("justify");
  public static final Markup INVERSE = valueOf("inverse");
  public static final Markup LEFT = valueOf("left");
  public static final Markup MARKED = valueOf("marked");
  public static final Markup MODAL = valueOf("modal");
  public static final Markup NUMBER = valueOf("number");
  public static final Markup ODD = valueOf("odd");
  public static final Markup PORTLET = valueOf("portlet");
  public static final Markup PURE = valueOf("pure");
  public static final Markup READONLY = valueOf("readonly");
  public static final Markup REQUIRED = valueOf("required");
  public static final Markup RESIZABLE = valueOf("resizable");
  public static final Markup RIGHT = valueOf("right");
  public static final Markup SECONDS = valueOf("seconds");
  public static final Markup SELECTED = valueOf("selected");
  public static final Markup SHEET_SELECT_ALL = valueOf("sheetSelectAll");
  public static final Markup SHEET_DESELECT_ALL = valueOf("sheetDeselectAll");
  public static final Markup SHEET_TOGGLE_ALL = valueOf("sheetToggleAll");
  public static final Markup SMALL = valueOf("small");
  public static final Markup SORTABLE = valueOf("sortable");
  public static final Markup STRIPED = valueOf("striped");
  public static final Markup STRONG = valueOf("strong");
  public static final Markup TOP = valueOf("top");
  public static final Markup VERTICALLY = valueOf("vertically");
  public static final Markup WARN = valueOf("warn");

  /* Just one of "values" and "value" must be null */

  private final String[] values;
  private final String value;

  private Markup(final String[] values) {
    this.values = values;
    this.value = null;
  }

  private Markup(final String value) {
    this.values = null;
    this.value = value;
  }

  public static Markup valueOf(final String[] values) {
    if (values == null || values.length == 0) {
      return null;
    } else if (values.length == 1) {
      return valueOf(values[0]);
    } else {
      final Markup markup = new Markup(values.clone());
      for (int i = 0; i < markup.values.length; i++) {
        markup.values[i] = markup.values[i].trim();
      }
      return markup;
    }
  }

  public static Markup valueOf(final String value) {
    if (StringUtils.isEmpty(value)) {
      return null;
    }
    if (value.contains(",")) {
      final String[] strings = StringUtils.split(value, ", \t\n");
      return new Markup(strings);
    } else {
      return new Markup(value.trim());
    }
  }

  public static Markup valueOf(final Object value) {
    if (value == null) {
      return null;
    }
    if (value instanceof Markup) {
      return (Markup) value;
    }
    if (value instanceof String) {
      return valueOf((String) value);
    }
    if (value instanceof String[]) {
      return valueOf((String[]) value);
    }
    if (value instanceof Iterable) {
      final List<String> list = new ArrayList<String>();
      for (final Object object : (Iterable) value) {
        list.add(object.toString());
      }
      return valueOf(list.toArray(new String[list.size()]));
    }
    return valueOf(value.toString());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final Markup markup = (Markup) o;

    if (value != null ? !value.equals(markup.value) : markup.value != null) {
      return false;
    }
    if (!Arrays.equals(values, markup.values)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = values != null ? Arrays.hashCode(values) : 0;
    result = 31 * result + (value != null ? value.hashCode() : 0);
    return result;
  }

  @Override
  public Iterator<String> iterator() {
    if (value != null) {
      return new SingletonIterator(value);
    }
    if (values != null) {
      return new ObjectArrayIterator(values);
    }
    return EmptyIterator.INSTANCE;
  }

  /**
   * Adds one markup to an other.
   * Attention: The markup itself is not modified, you need to use the result of this operation.
   */
  public Markup add(final Markup markup) {
    if (markup == null) {
      return this;
    }
    if (markup == NULL) {
      return this;
    }
    if (markup.value != null) {
      return add(markup.value);
    } else {
      // this part is not optimized, but it will be used rarely, in the moment...
      Markup result = this;
      if (markup.values != null) {
        for (final String summand : markup.values) {
          result = result.add(summand);
        }
      }
      return result;
    }
  }

  private Markup add(final String summand) {
    if (summand == null) {
      return this;
    }
    if (values == null) {
      if (value == null) {
        return valueOf(summand);
      } else {
        if (summand.equals(value)) {
          return this;
        } else {
          return valueOf(new String[]{value, summand});
        }
      }
    } else {
      if (ArrayUtils.contains(values, summand)) {
        return this;
      } else {
        final String[] strings = new String[values.length + 1];
        System.arraycopy(values, 0, strings, 0, values.length);
        strings[values.length] = summand;
        return valueOf(strings);
      }
    }
  }

  public Markup remove(final Markup markup) {
    if (markup.value != null) {
      return remove(markup.value);
    } else {
      // this part is not optimized, but it will be used rarely, in the moment...
      Markup result = this;
      for (final String summand : markup.values) {
        result = result.remove(summand);
      }
      return result;
    }
  }

  private Markup remove(final String summand) {
    if (summand == null) {
      return this;
    }
    if (values == null) {
      if (value == null) {
        return this;
      } else {
        if (summand.equals(value)) {
          return NULL;
        } else {
          return this;
        }
      }
    } else {
      if (ArrayUtils.contains(values, summand)) {
        final String[] strings = new String[values.length - 1];
        int found = 0;
        for (int i = 0; i < strings.length; i++) {
          if (values[i].equals(summand)) {
            found++;
          }
          strings[i] = values[i + found];
        }
        return valueOf(strings);
      } else {
        return this;
      }
    }
  }

  public boolean contains(final String markup) {
    if (markup == null) {
      return true;
    }
    if (this == NULL) {
      return this == Markup.valueOf(markup);
    }
    if (value != null) {
      return value.equals(markup);
    }
    for (final String value : values) {
        if (value.equals(markup)) {
          return true;
        }
      }
      return false;
  }

  public boolean contains(final Markup markup) {
    if (markup == null || markup == NULL) {
      return true;
    }
    if (this == NULL) {
      return this == markup;
    }
    if (markup.value != null) {
      if (value != null) {
        return value.equals(markup.value);
      } else {
        for (final String value : values) {
          if (value.equals(markup.value)) {
            return true;
          }
        }
        return false;
      }
    } else {
      if (value != null) {
        return false;
      } else {
        for (final String markupString : markup.values) {
          if (!contains(markupString)) {
            return false;
          }
        }
        return true;
      }
    }
  }

  @Override
  public String toString() {
    if (value != null) {
      return value;
    }
    if (values == null) {
      return "null";
    }
    return Arrays.toString(values);
  }

}
