package org.apache.myfaces.tobago.example.facelets;

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

import java.util.Date;

public class User {
  private String name;
  private String surname;
  private Date date;
  private Double value;

  public User(String name, String surname, Date date, double value) {
    this.name = name;
    this.surname = surname;
    this.date = date;
    this.value = Double.valueOf(value);
  }

  public String getName() {
    return name;
  }

  public String getSurname() {
    return surname;
  }


  public Date getDate() {
    return date;
  }

  public Double getDouble() {
    return value;
  }
}
