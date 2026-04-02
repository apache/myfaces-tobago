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

import {DropdownMenuStatic} from "./DropdownMenuStatic";
import {DropdownMenuAlignment} from "../tobago-dropdown-menu";

test("getHorizontalPositionAndSize", () => {
  //reference element placed in the default content area of the page
  expect(DropdownMenuStatic.getHorizontalPositionAndSize(DropdownMenuAlignment.start, false,
      365.453, 460.390,
      16, 1198
  )).toEqual({left: 349.453, right: null, maxWidth: 832.547});
  expect(DropdownMenuStatic.getHorizontalPositionAndSize(DropdownMenuAlignment.start, true,
      365.453, 460.390,
      16, 1198
  )).toEqual({left: null, right: 0, maxWidth: 1182});
  expect(DropdownMenuStatic.getHorizontalPositionAndSize(DropdownMenuAlignment.centerFullWidth, false,
      402, 975,
      16, 1198
  )).toEqual({left: 386, right: 223, maxWidth: null});
  expect(DropdownMenuStatic.getHorizontalPositionAndSize(DropdownMenuAlignment.end, false,
      936.921875, 975,
      16, 1198
  )).toEqual({left: null, right: 223, maxWidth: 959});
  expect(DropdownMenuStatic.getHorizontalPositionAndSize(DropdownMenuAlignment.end, true,
      936.921875, 975,
      16, 1198
  )).toEqual({left: 0, right: null, maxWidth: 1182});

  //reference element placed: display=fixed, left=10px
  expect(DropdownMenuStatic.getHorizontalPositionAndSize(DropdownMenuAlignment.start, false,
      10, 122,
      16, 1198
  )).toEqual({left: -6, right: null, maxWidth: 1188});
  expect(DropdownMenuStatic.getHorizontalPositionAndSize(DropdownMenuAlignment.start, true,
      10, 122,
      16, 1198
  )).toEqual({left: null, right: 0, maxWidth: 1188});
  expect(DropdownMenuStatic.getHorizontalPositionAndSize(DropdownMenuAlignment.centerFullWidth, false,
      10, 122,
      16, 1198
  )).toEqual({left: -6, right: 1076, maxWidth: null});
  expect(DropdownMenuStatic.getHorizontalPositionAndSize(DropdownMenuAlignment.end, false,
      10, 122,
      16, 1198
  )).toEqual({left: null, right: 1076, maxWidth: 112});
  expect(DropdownMenuStatic.getHorizontalPositionAndSize(DropdownMenuAlignment.end, true,
      10, 122,
      16, 1198
  )).toEqual({left: -6, right: null, maxWidth: 1188});

  //reference element placed: display=fixed, right=10px
  expect(DropdownMenuStatic.getHorizontalPositionAndSize(DropdownMenuAlignment.start, false,
      1072, 1202,
      16, 1198
  )).toEqual({left: 1056, right: null, maxWidth: 130});
  expect(DropdownMenuStatic.getHorizontalPositionAndSize(DropdownMenuAlignment.start, true,
      1072, 1202,
      16, 1198
  )).toEqual({left: null, right: -4, maxWidth: 1186});
  expect(DropdownMenuStatic.getHorizontalPositionAndSize(DropdownMenuAlignment.centerFullWidth, false,
      1072, 1202,
      16, 1198
  )).toEqual({left: 1056, right: -4, maxWidth: null});
  expect(DropdownMenuStatic.getHorizontalPositionAndSize(DropdownMenuAlignment.end, false,
      1072, 1202,
      16, 1198
  )).toEqual({left: null, right: -4, maxWidth: 1186});
  expect(DropdownMenuStatic.getHorizontalPositionAndSize(DropdownMenuAlignment.end, true,
      1072, 1202,
      16, 1198
  )).toEqual({left: 0, right: null, maxWidth: 1186});
});
