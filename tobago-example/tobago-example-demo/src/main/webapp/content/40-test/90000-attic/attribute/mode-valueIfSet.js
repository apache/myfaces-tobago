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

jQuery(document).ready(function () {

  // only to display
  jQuery("#page\\:id-box").find("input").each(function () {
    jQuery(this).val(jQuery(this).attr('id'));
  });

  // assertions
  TobagoAssert.assertValue("page:direct", "direct literal");
  TobagoAssert.assertValue("page:v1", "from var");
  TobagoAssert.assertValue("page:v2", "from var");
  TobagoAssert.assertValue("page:v3", "from var ++");
  TobagoAssert.assertValue("page:v4", "value from model");
  TobagoAssert.assertValue("page:vu", "");

  var i1 = jQuery(Tobago.Utils.escapeClientId("page:id-box")).find("input:eq(0)");
  TobagoAssert.assertAttribute(i1, "id", "page:my_number_1");
//        TobagoAssert.assertAttribute(..., "id", "");
  var i3 = jQuery(Tobago.Utils.escapeClientId("page:id-box")).find("input:eq(2)");
  TobagoAssert.assertAttribute(i3, "id", "page:my_number_3");
//        TobagoAssert.assertAttribute(..., "id", "");
});
