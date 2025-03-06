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

package org.apache.myfaces.tobago.example.demo;

public enum Release {

  v1_0_7("12310824"),
  v1_0_8("12310900"),
  v1_0_9("12312042"),
  v1_0_10("12312204"),
  v1_0_11("12312376"),
  v1_0_12("12312498"),
  v1_0_13("12312766"),
  v1_0_14("12312878"),
  v1_0_15("12312929"),
  v1_0_16("12312966"),
  v1_0_17("12313084"),
  v1_0_18("12313150"),
  v1_0_19("12313372"),
  v1_0_20("12313447"),
  v1_0_21("12313470"),
  v1_0_22("12314027"),
  v1_0_23("12314159"),
  v1_0_24("12314193"),
  v1_0_25("12314527"),
  v1_0_26("12314961"),
  v1_0_27("12315099"),
  v1_0_28("12315109"),
  v1_0_29("12315262"),
  v1_0_30("12315336"),
  v1_0_31("12315383"),
  v1_0_32("12315489"),
  v1_0_33("12315586"),
  v1_0_34("12316162"),
  v1_0_35("12316183"),
  v1_0_36("12316297"),
  v1_0_37("12316458"),
  v1_0_38("12317350"),
  v1_0_39("12319455"),
  v1_0_40("12319866"),
  v1_0_41("12324116"),
  v1_0_42("12326705"),
  v1_0_43("12333048", false, true),

  v1_5_0_alpha_2("12314340"),
  v1_5_0_beta_1("12316222"),
  v1_5_0_beta_2("12317052"),
  v1_5_0("12312205"),
  v1_5_1("12319154"),
  v1_5_2("12319248"),
  v1_5_3("12319499"),
  v1_5_4("12319864"),
  v1_5_5("12319865"),
  v1_5_6("12321251"),
  v1_5_7("12321444"),
  v1_5_8("12322450"),
  v1_5_9("12323506"),
  v1_5_10("12324008"),
  v1_5_11("12324453"),
  v1_5_12("12325597"),
  v1_5_13("12325858"),
  v1_5_14("12326706", false, true),

  v1_6_0_beta_1("12321691"),
  v1_6_0_beta_2("12321701"),

  v2_0_0_alpha_1("12321874"),
  v2_0_0_alpha_2("12324818"),
  v2_0_0_alpha_3("12325247"),
  v2_0_0_beta_1("12325856"),
  v2_0_0_beta_2("12326662"),
  v2_0_0_beta_3("12326693"),
  v2_0_0_beta_4("12326809"),
  v2_0_0("12321253"),
  v2_0_1("12327455"),
  v2_0_2("12327500"),
  v2_0_3("12328040"),
  v2_0_4("12328041"),
  v2_0_5("12329025"),
  v2_0_6("12329161"),
  v2_0_7("12329376"),
  v2_0_8("12329723"),
  v2_0_9("12332146"),
  v2_0_10("12334742"),
  v2_1_0("12338208"),
  v2_1_1("12341246"),
  v2_2_0("12342740"),
  v2_3_0("12343979"),
  v2_4_0("12344409"),
  v2_4_1("12344899"),
  v2_4_2("12345177"),
  v2_4_3("12346997"),
  v2_4_4("12349632"),
  v2_4_5("12349661"),
  v2_5_0("12345962"),
  v2_5_1("12353364"),
  v2_5_2("12354404", true),
  v2_5_3("12354827", false, true),

  v3_0_0_alpha_1("12325880"),
  v3_0_0_alpha_2("12333887"),
  v3_0_0_alpha_3("12334363"),
  v3_0_0_alpha_4("12337842"),
  v3_0_0_alpha_5("12338164"),
  v3_0_0_alpha_6("12338231"),
  v3_0_0_alpha_7("12338278"),
  v3_0_0_beta_1("12333888"),
  v3_0_0("12325880"),
  v3_0_1("12338730"),
  v3_0_2("12339171"),
  v3_0_3("12339442"),
  v3_0_4("12340258"),
  v3_0_5("12340325"),
  v3_0_6("12341574"),
  v3_1_0("12342388"),
  v3_1_1("12343478"),

  v4_0_0("12338728"),
  v4_1_0("12339443"),
  v4_2_0("12342739"),
  v4_2_1("12342849"),
  v4_3_0("12342850"),
  v4_3_1("12343951"),
  v4_3_2("12344394"),
  v4_4_0("12344541"),
  v4_4_1("12345061"),
  v4_5_0("12345562"),
  v4_5_1("12348200"),
  v4_5_2("12349344"),
  v4_5_3("12349662"),
  v4_5_4("12350057"),
  v4_5_5("12350236"),
  v4_6_0("12352084", true),

  v5_0_0_alpha_1("12350237"),
  v5_0_0_alpha_2("12350431"),
  v5_0_0("12338729"),
  v5_1_0("12344152"),
  v5_2_0("12344151"),
  v5_3_0("12350747"),
  v5_4_0("12352274"),
  v5_5_0("12352597"),
  v5_6_0("12352743"),
  v5_6_1("12353083"),
  v5_7_0("12352928"),
  v5_7_1("12353286"),
  v5_7_2("12353344"),
  v5_8_0("12353182"),
  v5_9_0("12353822"),
  v5_10_0("12353965"),
  v5_11_0("12354165"),
  v5_12_0("12354344"),
  v5_13_0("12354672"),
  v5_14_0("12355069"),
  v5_15_0("12355194"),
  v5_15_1("12355673", true),

  v6_0_0_beta_1("12353634"),
  v6_0_0("12350675"),
  v6_1_0("12353821"),
  v6_2_0("12353966"),
  v6_3_0("12354164"),
  v6_4_0("12354343"),
  v6_5_0("12354671"),
  v6_6_0("12355068"),
  v6_7_0("12355193"),
  v6_7_1("12355672", true);

  private final String jira;
  private final String version;
  private final boolean current;
  private final boolean unreleased;

  Release(final String jira) {
    this(jira, false, false);
  }

  Release(final String jira, final boolean current) {
    this(jira, current, false);
  }

  Release(final String jira, final boolean current, final boolean unreleased) {
    this.current = current;
    this.jira = jira;
    this.unreleased = unreleased;
    version = name()
        .substring(1)
        .replaceAll("_alpha_", "-alpha-")
        .replaceAll("_beta_", "-beta-")
        .replace('_', '.');
  }

  public String getVersion() {
    return version;
  }

  public boolean isCurrent() {
    return current;
  }

  public boolean isUnreleased() {
    return unreleased;
  }

  public String getJira() {
    return jira;
  }
}
