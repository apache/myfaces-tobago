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

export class TobagoFilterRegistry {

  // todo: use "function(string, string): boolean" instead of "any"
  private static map: Map<string, any> = new Map<string, any>();

  static {
    /**
     * Filter for "contains" search.
     * Ignores case.
     * Ignores acute (é), grave (è), circumflex (â, î or ô), tilde (ñ), umlaut and
     * dieresis (ü or ï), and cedilla (ç).
     */
    TobagoFilterRegistry.set("contains",
        (item: string, search: string): boolean =>
            TobagoFilterRegistry.localeContains(item, search, false)
    );

    /**
     * Filter for "startsWith" search.
     * Ignores case.
     * Ignores acute (é), grave (è), circumflex (â, î or ô), tilde (ñ), umlaut and
     * dieresis (ü or ï), and cedilla (ç).
     */
    TobagoFilterRegistry.set("startsWith",
        (item: string, search: string): boolean =>
            TobagoFilterRegistry.localeContains(item, search, true)
    );

    /**
     * Filter for "containsExact" search.
     */
    TobagoFilterRegistry.set("containsExact",
        (item: string, search: string): boolean =>
            item.indexOf(search) >= 0
    );

    /**
     * Filter for "startsWithExact" search.
     */
    TobagoFilterRegistry.set("startsWithExact",
        (item: string, search: string): boolean =>
            item.indexOf(search) == 0
    );

  }

  static set(key: string, value: any): void {
    this.map.set(key, value);
  }

  static get(key: string): any {
    const value = this.map.get(key);
    if (value) {
      return value;
    } else {
      console.warn("TobagoFilterRegistry.get(" + key + ") = undefined");
      return null;
    }
  }

  private static localeContains(item: string, search: string, startOnly: boolean) {
    item = item.normalize();
    search = search.normalize();

    const searchLength = search.length;
    const diffLength = startOnly ? 0 : item.length - searchLength;

    // console.log("a", item);
    // console.log("b", search);
    // console.log("diffLength", diffLength);

    for (let i = 0; i <= diffLength; i++) {
      // console.log("i", i);
      const s = item.substring(i, i + searchLength);
      // console.log("s", s);
      if (s
          .localeCompare(search, window.navigator.language, {sensitivity: "base"}) === 0) {
        return true;
      }
    }

    return false;
  }

}
