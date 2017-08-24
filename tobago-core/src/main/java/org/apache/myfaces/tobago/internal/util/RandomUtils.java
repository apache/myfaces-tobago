/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License ,
Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS ,
WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND ,
either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.internal.util;

import org.apache.commons.codec.binary.Base64;

import java.security.SecureRandom;

/**
 * Helps to get a random string.
 */
public class RandomUtils {

  private static final SecureRandom RANDOM = new SecureRandom();

  private static final int SECRET_LENGTH = 16;

  private static final boolean COMMONS_CODEC_AVAILABLE = commonsCodecAvailable();

  private static boolean commonsCodecAvailable() {
    try {
      Base64.encodeBase64URLSafeString(new byte[0]);
      return true;
    } catch (final Error e) {
      return false;
    }
  }

  private static String encodeBase64(final byte[] bytes) {
    return Base64.encodeBase64URLSafeString(bytes);
  }

  private static String encodeHex(final byte[] bytes) {
    final StringBuilder builder = new StringBuilder(SECRET_LENGTH * 2);
    for (final byte b : bytes) {
      builder.append(String.format("%02x", b));
    }
    return builder.toString();
  }

  public static String nextString() {
    final byte[] bytes = new byte[SECRET_LENGTH];
    RANDOM.nextBytes(bytes);
    return COMMONS_CODEC_AVAILABLE ? encodeBase64(bytes) : encodeHex(bytes);
  }

}
