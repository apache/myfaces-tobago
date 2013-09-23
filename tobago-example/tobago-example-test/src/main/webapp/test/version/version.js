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

TestVersion = {};

TestVersion.init = function() {

  var checked = jQuery("input[checked]");
  if (checked.size() != 2) {
    TobagoAssert.fail("2 field must be checked", null, 2, checked);
  }

  var version = checked.eq(0).next().html();
  var packageVersion = "Version " + jQuery("input[type=text]").eq(0).val();

  if (version != packageVersion) {
    TobagoAssert.fail("Version number", null, version, packageVersion);
  }

  var impl = checked.eq(1).next().html();
  var packageImpl = jQuery("input[type=text]").eq(1).val();

  if (impl != packageImpl) {
    TobagoAssert.fail("Implementation", null, version, packageVersion);
  }
};

Tobago.registerListener(TestVersion.init, Tobago.Phase.DOCUMENT_READY);
