package org.apache.myfaces.tobago.util;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class LayoutInfo {

  private static final int FREE = -1;
  private static final Log LOG = LogFactory.getLog(LayoutInfo.class);
  public static final int HIDE = -2;
  public static final String HIDE_CELL = "hide";

  private int cellsLeft;
  private int spaceLeft;
  private int[] spaces;
  private String[] layoutTokens;

  public LayoutInfo(int cellCount, int space, String layout) {
    this(cellCount, space, createLayoutTokens(layout, cellCount), false);
  }

  public LayoutInfo(int cellCount, int space, String[] layoutTokens, boolean ignoreMismatch) {

    this.cellsLeft = cellCount;
    this.spaceLeft = space;
    if (layoutTokens.length == cellCount) {
      this.layoutTokens = layoutTokens;
    } else if (layoutTokens.length > cellCount) {
      if (!ignoreMismatch && LOG.isWarnEnabled()) {
        LOG.warn("More tokens (" + layoutTokens.length
            + ") for layout than cells (" + cellCount + ") found! Ignoring"
            + " redundant tokens. Token string was: "
            + tokensToString(layoutTokens));
      }
      this.layoutTokens = new String[cellCount];
      System.arraycopy(layoutTokens, 0, this.layoutTokens, 0, cellCount);
    } else {
      if (!ignoreMismatch && LOG.isWarnEnabled()) {
        LOG.warn(Integer.toString(cellCount - layoutTokens.length)
            + "More cells (" + cellCount + ") than tokens (" + layoutTokens.length
            + ") for layout found! Setting missing tokens to '1*'."
            + " Token string was: " + tokensToString(layoutTokens));
      }
      this.layoutTokens = new String[cellCount];
      for (int i = 0; i < cellCount; i++) {
        if (i < layoutTokens.length) {
          this.layoutTokens[i] = layoutTokens[i];
        } else {
          this.layoutTokens[i] = "1*";
        }
      }
    }
    createAndInitSpaces(cellCount, FREE);
  }

  private void createAndInitSpaces(int columns, int initValue) {
    spaces = new int[columns];
    for (int j = 0; j < spaces.length; j++) {
      spaces[j] = initValue;
    }
  }

  public void update(int space, int index) {


    if (space > spaceLeft) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("More space (" + space + ") needed than available (" + spaceLeft
            + ")! Cutting to fit.");
      }
      space = spaceLeft;
    }

    spaceLeft -= space;
    cellsLeft--;
    if (index < spaces.length) {
      spaces[index] = space;
      if (spaceLeft < 1 && columnsLeft()) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("There are columns left but no more space! cellsLeft="
              + cellsLeft + ", tokens=" + tokensToString(layoutTokens));
          LOG.warn("calculated spaces = " + tokensToString(spaces));
        }
      }
    } else {
      LOG.warn("More space to assign (" + space + "px) but no more columns!"
          + " More layout tokens than column tags?");
    }
  }

  public boolean columnsLeft() {
    return cellsLeft > 0;
  }


  public void handleIllegalTokens() {
    for (int i = 0; i < spaces.length; i++) {
      if (isFree(i)) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Illegal layout token pattern \"" + layoutTokens[i]
              + "\" ignored, set to 0px !");
        }
        spaces[i] = 0;
      }
    }
  }


  public static String[] createLayoutTokens(String columnLayout, int count) {
    return createLayoutTokens(columnLayout, count, "1*");
  }

  public static String[] createLayoutTokens(String columnLayout, int count,
      String defaultToken) {
    String[] tokens;
    if (columnLayout != null) {
      List<String>  list = new ArrayList<String>();
      StringTokenizer tokenizer = new StringTokenizer(columnLayout, ";");
      while (tokenizer.hasMoreTokens()) {
        String token = tokenizer.nextToken().trim();
        if ("*".equals(token)) {
          token = "1*";
        }
        list.add(token);
      }
      tokens = list.toArray(new String[list.size()]);
    } else {
      defaultToken = "*".equals(defaultToken) ? "1*" : defaultToken;
      tokens = new String[count];
      for (int i = 0; i < tokens.length; i++) {
        tokens[i] = defaultToken;
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("created Tokens : " + tokensToString(tokens));
    }
    return tokens;
  }

  public static String listToTokenString(List list) {
    String[] tokens = new String[list.size()];
    for (int i = 0; i < list.size(); i++) {
      tokens[i] = list.get(i).toString();
    }
    return tokensToString(tokens);
  }

  public static String tokensToString(int[] tokens) {
    String[] strings = new String[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
      strings[i] = Integer.toString(tokens[i]);
    }
    return tokensToString(strings);
  }

  public static String tokensToString(String[] tokens) {
    StringBuffer sb = new StringBuffer();
    for (String token : tokens) {
      if (sb.length() != 0) {
        sb.append(";");
      }
      sb.append(token);
    }
    sb.insert(0, "\"");
    sb.append("\"");
    return sb.toString();
  }

  public boolean isFree(int column) {
    return spaces[column] == FREE;
  }

  public int getSpaceForColumn(int column) {
    return spaces[column];
  }

  public int getSpaceLeft() {
    return spaceLeft;
  }

  public String[] getLayoutTokens() {
    return layoutTokens;
  }

  public boolean hasLayoutTokens() {
    return layoutTokens.length > 0;
  }

  public List<Integer> getSpaceList() {
    List<Integer> list = new ArrayList<Integer>(spaces.length);
    for (int space : spaces) {
      list.add(space);
    }
    return list;
  }

  public void handleSpaceLeft() {
    if (spaceLeft > 0) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("spread spaceLeft (" + spaceLeft + "px) to columns");
        LOG.debug("spaces before spread :" + arrayAsString(spaces));
      }

      for (int i = 0; i < layoutTokens.length; i++) {
        if ("*".equals(layoutTokens[i])) {
          addSpace(spaceLeft, i);
          break;
        }
      }
      boolean found = false;
      while (spaceLeft > 0) {
//        for (int i = 0; i < layoutTokens.length; i++) {
        for (int i = layoutTokens.length - 1; i > -1; i--) {
          String layoutToken = layoutTokens[i];
          if (spaceLeft > 0 && layoutToken.matches("^\\d+\\*")) {
            found = true;
            addSpace(1, i);
          }
        }
        if (!found) {
          break;
        }
      }
    }
    if (spaceLeft > 0 && LOG.isWarnEnabled()) {
      LOG.warn("Space left after spreading : " + spaceLeft + "px!");
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("spaces after spread  :" + arrayAsString(spaces));
    }
  }
  //TODO replace with Arrays.asList ..
  private String arrayAsString(int[] currentSpaces) {
    StringBuffer sb = new StringBuffer("[");
    for (int currentSpace : currentSpaces) {
      sb.append(currentSpace);
      sb.append(", ");
    }
    sb.replace(sb.lastIndexOf(", "), sb.length(), "]");
    return sb.toString();
  }

  private void addSpace(int space, int i) {
    if (spaces[i] > HIDE) {
      if (spaces[i] == FREE) {
        spaces[i] = space;
      } else {
        spaces[i] += space;
      }
      spaceLeft -= space;
    }
  }


  public void parseHides(int padding) {
    String[] tokens = getLayoutTokens();
    for (int i = 0; i < tokens.length; i++) {
      if (tokens[i].equals(HIDE_CELL)) {
        update(0, i);
        spaces[i] = HIDE;
        if (i != 0) {
          spaceLeft += padding;
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("set column " + i + " from " + tokens[i]
              + " to hide ");
        }
      }
    }
  }

  public void parsePixels() {
    String[] tokens = getLayoutTokens();
    for (int i = 0; i < tokens.length; i++) {
      if (tokens[i].endsWith("px")) {
        String token = tokens[i].substring(0, tokens[i].length() - 2);
        try {
          int w = Integer.parseInt(token);
          update(w, i);
          if (LOG.isDebugEnabled()) {
            LOG.debug("set column " + i + " from " + tokens[i]
                + " to with " + w);
          }
        } catch (NumberFormatException e) {
          if (LOG.isWarnEnabled()) {
            LOG.warn("NumberFormatException parsing " + tokens[i]);
          }
        }
      }
    }
  }


  public void parsePercent(double innerWidth) {
    String[] tokens = getLayoutTokens();
    if (columnsLeft()) {
      for (int i = 0; i < tokens.length; i++) {
        if (isFree(i) && tokens[i].endsWith("%")) {
          String token = tokens[i].substring(0, tokens[i].length() - 1);
          try {
            int percent = Integer.parseInt(token);
            int w = (int) (innerWidth / 100 * percent);
            update(w, i);
            if (LOG.isDebugEnabled()) {
              LOG.debug("set column " + i + " from " + tokens[i]
                  + " to with " + w);
            }
          } catch (NumberFormatException e) {
            if (LOG.isWarnEnabled()) {
              LOG.warn("NumberFormatException parsing " + tokens[i]);
            }
          }
        }
      }
    }
  }

  public void parsePortions() {
    String[] tokens = getLayoutTokens();
    if (columnsLeft()) {
      //   1. count portions
      int portions = 0;
      for (int i = 0; i < tokens.length; i++) {
        if (isFree(i) && tokens[i].matches("^\\d+\\*")) {
          String token = tokens[i].substring(0, tokens[i].length() - 1);
          try {
            portions += Integer.parseInt(token);
          } catch (NumberFormatException e) {
            if (LOG.isWarnEnabled()) {
              LOG.warn("NumberFormatException parsing " + tokens[i]);
            }
          }
        }
      }
      //  2. calc and set portion
      if (portions > 0) {
        int widthForPortions = getSpaceLeft();
        for (int i = 0; i < tokens.length; i++) {
          if (isFree(i) && tokens[i].matches("^\\d+\\*")) {
            String token = tokens[i].substring(0, tokens[i].length() - 1);
            try {
              int portion = Integer.parseInt(token);
              float w = (float) widthForPortions / portions * portion;
              update(Math.round(w), i);
              if (LOG.isDebugEnabled()) {
                LOG.debug("set column " + i + " from " + tokens[i]
                    + " to with " + w + " == " + Math.round(w) + "px");
              }
            } catch (NumberFormatException e) {
              if (LOG.isWarnEnabled()) {
                LOG.warn("NumberFormatException parsing " + tokens[i]);
              }
            }
          }
        }
      }
    }
  }

/*
  public void parseAsterisks() {
    String[] tokens = getLayoutTokens();
    if (columnsLeft()) {
      //  1. count unset columns
      int portions = 0;
      for (int i = 0; i < tokens.length; i++) {
        if (isFree(i) && tokens[i].equals("*")) {
          portions++;
        }
      }
      //  2. calc and set portion
      int widthPerPortion;
      if (portions > 0) {
        widthPerPortion = getSpaceLeft() / portions;
        for (int i = 0; i < tokens.length; i++) {
          if (isFree(i) && tokens[i].equals("*")) {
            int w = widthPerPortion;
            update(w, i);
            if (LOG.isDebugEnabled()) {
              LOG.debug("set column " + i + " from " + tokens[i]
                  + " to with " + w);
            }
          }
        }
      }
    }
  }
*/

  public void parseColumnLayout(double space) {
    parseColumnLayout(space, 0);
  }

  public void parseColumnLayout(
      double space, int padding) {

    if (hasLayoutTokens()) {
      parseHides(padding);
      parsePixels();
      parsePercent(space);
      parsePortions();
//      parseAsterisks();
      handleSpaceLeft();
    }

    if (columnsLeft() && LOG.isWarnEnabled()) {
      handleIllegalTokens();
    }
  }
  public String toString() {
    return new ToStringBuilder(this).
        append("cellLeft", cellsLeft).
        append("spaceLeft", spaceLeft).
        append("spaces", spaces).
        append("layoutTokens", layoutTokens).
        toString();
  }
}

