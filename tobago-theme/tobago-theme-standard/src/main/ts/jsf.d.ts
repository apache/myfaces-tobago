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

interface Options {
  execute?: string,
  render?: string,
  onevent?: Function,
  onerror?: Function,
  params?: Context,
  delay?: number,
  resetValues?: boolean,
  myfaces?: {
    upload: {
      onProgress: ((upload: XMLHttpRequestUpload, event: ProgressEvent) => any)
    }
  }
}

type Context = Record<string, any>;

declare interface EventData {
  type: string;
  status: string;
  source: any;
  responseCode: string;
  responseText: string;
  responseXML: Document;
}

declare interface ErrorData {
  type: string;
  source: string;
  errorName: string;
  errorMessage: string;
  responseText: string;
  responseXML: Document;
  status: string;
  serverErrorName: string;
  serverErrorMessage: string;
  responseCode: number;
}

declare const jsf: Faces;

interface Faces {
  ajax: Ajax;
}

interface Ajax {
  request(element: Element | string, event?: Event, options?: Options): void;

  addOnEvent(eventFunc: (data: EventData) => void): void;

  addOnError(errorFunc: (data: ErrorData) => void): void;
}
