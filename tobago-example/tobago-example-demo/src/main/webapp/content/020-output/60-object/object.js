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

class MapDemo {
  static init() {
    document.querySelectorAll("[data-maps-target]").forEach((element) => element.addEventListener("click",
        function (event) {
          const button = event.currentTarget;
          const targetId = button.dataset.mapsTarget;
          const position = JSON.parse(button.dataset.mapsPosition);
          const zoom = JSON.parse(button.dataset.mapsZoom);
          const url = 'https://www.openstreetmap.org/export/embed.html?bbox='
              + (position.x - zoom) + ','
              + (position.y - zoom) + ','
              + (position.x + zoom) + ','
              + (position.y + zoom);
          document.getElementById(targetId).setAttribute("src", url);
          event.preventDefault();
        }));
  }
}

document.addEventListener("DOMContentLoaded", MapDemo.init);
// todo: ajax
Listener.register(MapDemo.init, Phase.DOCUMENT_READY);
Listener.register(MapDemo.init, Phase.AFTER_UPDATE);
