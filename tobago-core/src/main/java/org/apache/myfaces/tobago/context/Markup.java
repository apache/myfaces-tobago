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

import org.apache.myfaces.tobago.internal.util.ArrayUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * A markup signs a component to be rendered different from the normal.
 * E.g. <code>markup="emphasized"</code> might be rendered bold
 * or a <code>markup="deleted"</code> might be rendered with line-through style.
 * The concrete rendering depends from the theme.
 * </p>
 * <p>
 * The markup can also hold more than one value, e.g. <code>markup="emphasized, deleted"</code>.
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

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static final Markup NULL = new Markup((String) null);

  public static final Markup ASCENDING = valueOf("ascending");
  public static final Markup BADGE = valueOf("badge");
  public static final Markup BIG = valueOf("big");
  public static final Markup BOLD = valueOf("bold");
  public static final Markup BORDERED = valueOf("bordered");
  public static final Markup BOTTOM = valueOf("bottom");
  public static final Markup CLICKABLE = valueOf("clickable");
  public static final Markup DANGER = valueOf("danger");
  public static final Markup DARK = valueOf("dark");
  public static final Markup DEFAULT = valueOf("default");
  public static final Markup DELETED = valueOf("deleted");
  public static final Markup DESCENDING = valueOf("descending");
  public static final Markup DISABLED = valueOf("disabled");
  public static final Markup ERROR = valueOf("error");
  public static final Markup EVEN = valueOf("even");
  public static final Markup EXPANDED = valueOf("expanded");
  public static final Markup EXTRA_LARGE = valueOf("extraLarge");
  public static final Markup FATAL = valueOf("fatal");
  public static final Markup FOLDER = valueOf("folder");
  public static final Markup HIDE_CLOSE_BUTTON = valueOf("hideCloseButton");
  public static final Markup HIDE_TOGGLE_ICON = valueOf("hideToggleIcon");
  public static final Markup HOVER = valueOf("hover");
  public static final Markup INFO = valueOf("info");
  public static final Markup INLINE = valueOf("inline");
  public static final Markup ITALIC = valueOf("italic");
  public static final Markup JUSTIFY = valueOf("justify");
  public static final Markup LARGE = valueOf("large");
  public static final Markup LIGHT = valueOf("light");
  public static final Markup MARKED = valueOf("marked");
  public static final Markup MEDIUM = valueOf("medium");
  public static final Markup MIDDLE = valueOf("middle");
  public static final Markup MODAL = valueOf("modal");
  public static final Markup NONE = valueOf("none");
  public static final Markup NUMBER = valueOf("number");
  public static final Markup ODD = valueOf("odd");
  public static final Markup OUTLINE_DANGER = valueOf("outlineDanger");
  public static final Markup OUTLINE_DARK = valueOf("outlineDark");
  public static final Markup OUTLINE_INFO = valueOf("outlineInfo");
  public static final Markup OUTLINE_LIGHT = valueOf("outlineLight");
  public static final Markup OUTLINE_PRIMARY = valueOf("outlinePrimary");
  public static final Markup OUTLINE_SECONDARY = valueOf("outlineSecondary");
  public static final Markup OUTLINE_SUCCESS = valueOf("outlineSuccess");
  public static final Markup OUTLINE_WARNING = valueOf("outlineWarning");
  public static final Markup PILL = valueOf("pill");
  public static final Markup PRIMARY = valueOf("primary");
  public static final Markup READONLY = valueOf("readonly");
  public static final Markup REQUIRED = valueOf("required");
  public static final Markup RESIZABLE = valueOf("resizable");
  public static final Markup SECONDARY = valueOf("secondary");
  public static final Markup SECONDS = valueOf("seconds");
  public static final Markup SELECTED = valueOf("selected");
  public static final Markup SMALL = valueOf("small");
  public static final Markup SORTABLE = valueOf("sortable");
  public static final Markup SPREAD = valueOf("spread");
  public static final Markup STRIPED = valueOf("striped");
  public static final Markup STRONG = valueOf("strong");
  public static final Markup SUCCESS = valueOf("success");
  public static final Markup THIN = valueOf("thin");
  public static final Markup TOGGLER_LEFT = valueOf("togglerLeft");
  public static final Markup TOP = valueOf("top");
  public static final Markup VERTICALLY = valueOf("vertically");
  public static final Markup WARN = valueOf("warn");
  public static final Markup WARNING = valueOf("warning");

  public static final String STRING_ASCENDING = "ascending";
  public static final String STRING_BADGE = "badge";
  public static final String STRING_BIG = "big";
  public static final String STRING_BOLD = "bold";
  public static final String STRING_BORDERED = "bordered";
  public static final String STRING_BOTTOM = "bottom";
  public static final String STRING_CLICKABLE = "clickable";
  public static final String STRING_DANGER = "danger";
  public static final String STRING_DARK = "dark";
  public static final String STRING_DEFAULT = "default";
  public static final String STRING_DELETED = "deleted";
  public static final String STRING_DESCENDING = "descending";
  public static final String STRING_DISABLED = "disabled";
  public static final String STRING_ERROR = "error";
  public static final String STRING_EVEN = "even";
  public static final String STRING_EXPANDED = "expanded";
  public static final String STRING_EXTRA_LARGE = "extraLarge";
  public static final String STRING_FATAL = "fatal";
  public static final String STRING_FOLDER = "folder";
  public static final String STRING_HIDE_CLOSE_BUTTON = "hideCloseButton";
  public static final String STRING_HIDE_TOGGLE_ICON = "hideToggleIcon";
  public static final String STRING_HOVER = "hover";
  public static final String STRING_INFO = "info";
  public static final String STRING_INLINE = "inline";
  public static final String STRING_ITALIC = "italic";
  public static final String STRING_JUSTIFY = "justify";
  public static final String STRING_LARGE = "large";
  public static final String STRING_LIGHT = "light";
  public static final String STRING_MARKED = "marked";
  public static final String STRING_MEDIUM = "medium";
  public static final String STRING_MIDDLE = "middle";
  public static final String STRING_MODAL = "modal";
  public static final String STRING_NONE = "none";
  public static final String STRING_NUMBER = "number";
  public static final String STRING_ODD = "odd";
  public static final String STRING_OUTLINE_DANGER = "outlineDanger";
  public static final String STRING_OUTLINE_DARK = "outlineDark";
  public static final String STRING_OUTLINE_INFO = "outlineInfo";
  public static final String STRING_OUTLINE_LIGHT = "outlineLight";
  public static final String STRING_OUTLINE_PRIMARY = "outlinePrimary";
  public static final String STRING_OUTLINE_SECONDARY = "outlineSecondary";
  public static final String STRING_OUTLINE_SUCCESS = "outlineSuccess";
  public static final String STRING_OUTLINE_WARNING = "outlineWarning";
  public static final String STRING_PILL = "pill";
  public static final String STRING_PRIMARY = "primary";
  public static final String STRING_READONLY = "readonly";
  public static final String STRING_REQUIRED = "required";
  public static final String STRING_RESIZABLE = "resizable";
  public static final String STRING_SECONDARY = "secondary";
  public static final String STRING_SECONDS = "seconds";
  public static final String STRING_SELECTED = "selected";
  public static final String STRING_SMALL = "small";
  public static final String STRING_SORTABLE = "sortable";
  public static final String STRING_SPREAD = "spread";
  public static final String STRING_STRIPED = "striped";
  public static final String STRING_STRONG = "strong";
  public static final String STRING_SUCCESS = "success";
  public static final String STRING_THIN = "thin";
  public static final String STRING_TOGGLER_LEFT = "togglerLeft";
  public static final String STRING_TOP = "top";
  public static final String STRING_VERTICALLY = "vertically";
  public static final String STRING_WARN = "warn";
  public static final String STRING_WARNING = "warning";

  /* Just one of "values" and "value" must be null */

  private final String[] values;
  private final String value;

  private Markup(final String[] values) {
    this.values = values != null ? filterSpecialChars(values) : null;
    this.value = null;
  }

  private Markup(final String value) {
    this.values = null;
    this.value = value != null ? filterSpecialChars(value) : null;
  }

  private String[] filterSpecialChars(final String[] strings) {
    for (int i = 0; i < strings.length; i++) {
      strings[i] = filterSpecialChars(strings[i]);
    }
    return strings;
  }

  private String filterSpecialChars(String string) {
    StringBuilder stringBuilder = new StringBuilder(string.length());
    boolean forbiddenCharFound = false;
    for (int i = 0; i < string.length(); i++) {
      final char c = string.charAt(i);
      if ('0' <= c && c <= '9' || 'A' <= c && c <= 'Z' || 'a' <= c && c <= 'z') {
        stringBuilder.append(c);
      } else {
        forbiddenCharFound = true;
      }
    }
    if (forbiddenCharFound) {
      final String newString = stringBuilder.toString();
      LOG.warn("Only numeric and alphabetic characters are allowed for markups: '{}' converted to '{}'.", string,
          newString);
      return newString;
    } else {
      return string;
    }
  }

  public static Markup valueOf(final String[] values) {
    if (values == null || values.length == 0) {
      return null;
    } else if (values.length == 1) {
      return valueOf(values[0]);
    } else {
      final String[] clonedValues = values.clone();
      for (int i = 0; i < clonedValues.length; i++) {
        clonedValues[i] = clonedValues[i].trim();
      }
      return new Markup(clonedValues);
    }
  }

  public static Markup valueOf(final String value) {
    if (StringUtils.isEmpty(value)) {
      return null;
    }
    if (value.contains(" ") || value.contains(",")) {
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
      final List<String> list = new ArrayList<>();
      for (final Object object : (Iterable) value) {
        list.add(object.toString());
      }
      return valueOf(list.toArray(new String[0]));
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

    if (!Objects.equals(value, markup.value)) {
      return false;
    }
    return Arrays.equals(values, markup.values);
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
      return Collections.singleton(value).iterator();
    }
    if (values != null) {
      return Arrays.asList(values).iterator();
    }
    return Collections.emptyIterator();
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
          final Markup combined = result.add(summand);
          if (combined != null) {
            result = combined;
          }
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
        final Markup removed = result.remove(summand);
        if (removed == null) {
          result = NULL;
        } else {
          result = removed;
        }
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
          if (summand.equals(values[i])) {
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
    for (final String v : values) {
      if (v.equals(markup)) {
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
        for (final String v : values) {
          if (v.equals(markup.value)) {
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

  /**
   * Check if there is no value set inside this markup.
   */
  public boolean isEmpty() {
    return !(value != null || values != null && values.length != 0);
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
