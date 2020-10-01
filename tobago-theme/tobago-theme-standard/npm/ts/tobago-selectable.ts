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

export enum Selectable {
  none, // Not selectable.
  multi, // Multi selection possible. No other limitations.
  single, // Only one item is selectable.
  singleOrNone, // Only one of no item is selectable.
  multiLeafOnly, // Only leafs are selectable.
  singleLeafOnly, // Only one item is selectable and it must be a leaf.
  sibling, // Only siblings are selectable.
  siblingLeafOnly, // Only siblings are selectable and they have to be leafs.
  multiCascade // Multi selection possible. When (de)selecting an item, the subtree will also be (un)selected.
}
