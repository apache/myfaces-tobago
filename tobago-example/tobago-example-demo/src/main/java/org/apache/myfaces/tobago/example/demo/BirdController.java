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

package org.apache.myfaces.tobago.example.demo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
// XXX This doesn't work with CODI in the JSP version
@SessionScoped
@Named(value = "birdController")
*/
public class BirdController implements Serializable {

  private List<Bird> birds = new ArrayList<Bird>(Arrays.asList(
      new Bird("Amsel", 25),
      new Bird("Drossel", 25),
      new Bird("Fink", 9),
      new Bird("Star", 19))
  );

  private String status;

  private String newBirdName;

  private int newBirdSize;

  public List<Bird> getBirds() {
    return birds;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(final String status) {
    this.status = status;
  }

  public String getNewBirdName() {
    return newBirdName;
  }

  public void setNewBirdName(final String newBirdName) {
    this.newBirdName = newBirdName;
  }

  public int getNewBirdSize() {
    return newBirdSize;
  }

  public void setNewBirdSize(final int newBirdSize) {
    this.newBirdSize = newBirdSize;
  }

  public String addNewBird() {
    birds.add(new Bird(newBirdName, newBirdSize));
    newBirdName = null;
    newBirdSize = 0;
    return null;
  }
}
