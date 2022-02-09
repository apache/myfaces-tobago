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

package org.apache.myfaces.tobago.example.addressbook;

import org.apache.commons.io.IOUtils;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

@Entity
public class Picture implements Serializable {
  private static final long serialVersionUID = -7637551581782102682L;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String contentType;
  @Lob
  @Basic(fetch = FetchType.EAGER)
  private byte[] content;


  public Picture() {
  }

  public Picture(final String contentType, final InputStream inputStream) throws IOException {
    this.contentType = contentType;
    this.content = IOUtils.toByteArray(inputStream);
  }

  public Integer getId() {
    return id;
  }

  public String getContentType() {
    return contentType;
  }

  public byte[] getContent() {
    return content;
  }

}
