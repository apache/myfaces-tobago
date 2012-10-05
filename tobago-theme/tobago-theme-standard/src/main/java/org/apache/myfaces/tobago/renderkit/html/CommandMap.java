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

package org.apache.myfaces.tobago.renderkit.html;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Map of commands to be send to the user agent.
 * It contains the command which shall be executed by click or other events.
 *
 * @since 1.6.0
 */
public class CommandMap {

  private Command click;
  private Map<String, Command> other;

  public CommandMap() {
  }

  public void setClick(Command click) {
    this.click = click;
  }

  public Command getClick() {
    return click;
  }

  public void addCommand(String name, Command command) {
    if (name.equals("click")) {
      setClick(command);
    } else {
      if (other == null) {
        other = new HashMap<String, Command>();
      }
      other.put(name, command);
    }
  }

  public Map<String, Command> getOther() {
    if (other != null) {
      return Collections.unmodifiableMap(other);
    } else {
      return null;
    }
  }
}
