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

/**
 * Utility links:
 * Copies the values from the data-login attribute to the username/password fields.
 */
class DemoLogin extends HTMLElement {

  constructor() {
    super();
  }

  connectedCallback(): void {
    this.addEventListener("click", this.fillFields.bind(this));
  }

  fillFields(event: MouseEvent): void {
    const rootNode = this.getRootNode() as ShadowRoot | Document;
    const username = rootNode.getElementById(this.usernameId) as HTMLInputElement;
    username.value = this.username;
    const password = rootNode.getElementById(this.passwordId) as HTMLInputElement;
    password.value = this.password;
    event.preventDefault();
  }

  get username(): string {
    return this.getAttribute("username");
  }

  get usernameId(): string {
    return this.getAttribute("username-id");
  }

  get password(): string {
    return this.getAttribute("password");
  }

  get passwordId(): string {
    return this.getAttribute("password-id");
  }
}

document.addEventListener("DOMContentLoaded", function (event: Event): void {
  if (window.customElements.get("demo-login") == null) {
    window.customElements.define("demo-login", DemoLogin);
  }
});
