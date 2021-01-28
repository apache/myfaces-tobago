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

package org.apache.myfaces.tobago.internal.util;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * Helps to get a random string.
 */
public class RandomUtils {

  private static final SecureRandom RANDOM = new SecureRandom();

  private static final int SECRET_LENGTH = 16;

  private static String encodeBase64(final byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
  }

  public static String nextString() {
    final byte[] bytes = new byte[SECRET_LENGTH];
    RANDOM.nextBytes(bytes);
    return encodeBase64(bytes);
  }

}
