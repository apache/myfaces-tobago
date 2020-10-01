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
export var Selectable;
(function (Selectable) {
    Selectable[Selectable["none"] = 0] = "none";
    Selectable[Selectable["multi"] = 1] = "multi";
    Selectable[Selectable["single"] = 2] = "single";
    Selectable[Selectable["singleOrNone"] = 3] = "singleOrNone";
    Selectable[Selectable["multiLeafOnly"] = 4] = "multiLeafOnly";
    Selectable[Selectable["singleLeafOnly"] = 5] = "singleLeafOnly";
    Selectable[Selectable["sibling"] = 6] = "sibling";
    Selectable[Selectable["siblingLeafOnly"] = 7] = "siblingLeafOnly";
    Selectable[Selectable["multiCascade"] = 8] = "multiCascade"; // Multi selection possible. When (de)selecting an item, the subtree will also be (un)selected.
})(Selectable || (Selectable = {}));
//# sourceMappingURL=tobago-selectable.js.map