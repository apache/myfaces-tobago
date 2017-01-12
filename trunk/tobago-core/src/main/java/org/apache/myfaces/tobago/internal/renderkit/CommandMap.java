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

package org.apache.myfaces.tobago.internal.renderkit;

import org.apache.myfaces.tobago.component.ClientBehaviors;

import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Map of commands to be send to the user agent.
 * It contains the command which shall be executed by click or other events.
 *
 * @since 2.0.0
 */
public class CommandMap {

  private static final String KEY = CommandMap.class.getName() + ".KEY";

  private Command click;
  private Map<ClientBehaviors, Command> other;

  /**
   * Creates an empty command map, which may hold different command triggered by different keys.
   */
  public CommandMap() {
  }

  /**
   * Creates a command map, which hold the given command triggered by "click".
   */
  public CommandMap(final Command click) {
    this.click = click;
  }

  public void setClick(final Command click) {
    this.click = click;
  }

  public Command getClick() {
    return click;
  }

  public void addCommand(final ClientBehaviors name, final Command command) {
    if (name == ClientBehaviors.click) {
      setClick(command);
    } else {
      if (other == null) {
        other = new HashMap<ClientBehaviors, Command>();
      }

      other.put(name, command);
    }
  }

  public Map<ClientBehaviors, Command> getOther() {
    if (other != null) {
      return Collections.unmodifiableMap(other);
    } else {
      return null;
    }
  }

  /**
   * Merges these two maps.
   * If one is null, the other one will be return. It may also return null.
   * If both are not null, m1 will be filled with the data of m2.
   * @param m1 map 1
   * @param m2 map 2
   * @return m1 or m2
   */
  public static CommandMap merge(final CommandMap m1, final CommandMap m2) {
    if (m1 == null) {
      return m2;
    } else {
      if (m2 == null) {
        return m1;
      } else {
        final Command c2 = m2.getClick();
        if (c2 != null) {
          final Command c1 = m1.getClick();
          if (c1 == null) {
            m1.setClick(c2);
          } else {
            c1.merge(c2);
          }
        } else {
          for (Map.Entry<ClientBehaviors, Command> entry : m2.getOther().entrySet()) {
            ClientBehaviors key = entry.getKey();
            Command value = entry.getValue();
            if (m1.other.containsKey(key)) {
              final Command command = m1.other.get(key);
              command.merge(value);
            } else {
              m1.addCommand(key, value);
            }
          }
        }
        return m1;
      }
    }
  }

  public static CommandMap restoreCommandMap(final FacesContext facesContext) {
    return (CommandMap) facesContext.getAttributes().get(KEY);
  }

  public static void storeCommandMap(final FacesContext facesContext, final CommandMap map) {
    facesContext.getAttributes().put(KEY, map);
  }

  public boolean isEmpty() {
    return click == null && other == null;
  }
}
