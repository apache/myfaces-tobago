package org.apache.myfaces.tobago.layout.math;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SubsetOfReferencesToVariables {

  private static final Logger LOG = LoggerFactory.getLogger(SubsetOfReferencesToVariables.class);

  private final List<ReferenceToVariable> variables;

  public SubsetOfReferencesToVariables() {
    this.variables = new ArrayList<ReferenceToVariable>();
  }

  public List<ReferenceToVariable> getVariables() {
    return variables;
  }

  @Override
  public String toString() {
    return toString(0, 0);
  }

  public String toString(int depth, int indexOfVariable) {
    StringBuilder builder = new StringBuilder();
    repeat(builder, "  ", depth);
    builder.append("S(");
      builder.append("x_");
      builder.append(indexOfVariable);
    builder.append(")\n");

    LOG.info(builder);

    for (ReferenceToVariable variable : variables) {
      builder.append(variable.toString(depth + 1));
    }

    return builder.toString();
  }

  private void repeat(StringBuilder builder, String value, int count) {
    for (int i = 0; i < count; i++) {
      builder.append(value);
    }
  }

}
