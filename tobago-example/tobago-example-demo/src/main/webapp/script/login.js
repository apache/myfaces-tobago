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

class LoginDemo {

  /**
   * Copies the values from the data-login attribute to the username/password fields.
   */
  static initQuickLinks() {
    document.querySelectorAll("button[data-login]").forEach((element) => element.addEventListener("click",
        function (event) {
          const link = event.currentTarget;
          const login = JSON.parse(link.dataset.login);
          document.getElementById("page:mainForm:username::field").value = login.username;
          document.getElementById("page:mainForm:password::field").value = login.password;
          event.preventDefault();
        }));
  };
}

document.addEventListener("DOMContentLoaded", LoginDemo.initQuickLinks);
// todo: ajax
// Listener.register(LoginDemo.initQuickLinks, Phase.DOCUMENT_READY);
// Listener.register(LoginDemo.initQuickLinks, Phase.AFTER_UPDATE);
