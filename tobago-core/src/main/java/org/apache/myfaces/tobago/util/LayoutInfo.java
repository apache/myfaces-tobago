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

package org.apache.myfaces.tobago.util;

import org.apache.myfaces.tobago.layout.HideLayoutToken;
import org.apache.myfaces.tobago.layout.LayoutToken;
import org.apache.myfaces.tobago.layout.LayoutTokens;
import org.apache.myfaces.tobago.layout.PercentLayoutToken;
import org.apache.myfaces.tobago.layout.PixelLayoutToken;
import org.apache.myfaces.tobago.layout.RelativeLayoutToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @deprecated xxx
 */
@Deprecated
public class LayoutInfo {

  private static final Logger LOG = LoggerFactory.getLogger(LayoutInfo.class);

  private static final int FREE = -1;
  public static final int HIDE = -2;

  private int cellsLeft;
  private int spaceLeft;
  private int[] spaces;
  private LayoutTokens layoutTokens;
  private String clientIdForLogging;

  public LayoutInfo(
      final int cellCount, final int space, final LayoutTokens layoutTokens, final String clientIdForLogging) {
    this(cellCount, space, layoutTokens, clientIdForLogging, false);
  }

  public LayoutInfo(final int cellCount, final int space, final LayoutTokens layoutTokens,
                    final String clientIdForLogging, final boolean ignoreMismatch) {

    this.cellsLeft = cellCount;
    this.spaceLeft = space;
    this.layoutTokens = layoutTokens;
    this.clientIdForLogging = clientIdForLogging;
    /*if (layoutTokens.length == cellCount) {
      this.layoutTokens = layoutTokens;
    } else */
    if (layoutTokens.getSize() > cellCount) {
      if (!ignoreMismatch) {
        LOG.warn("More tokens (" + layoutTokens.getSize()
            + ") for layout than cells (" + cellCount + ") found! Ignoring"
            + " redundant tokens. Token string was: "
            + layoutTokens
            + " clientId='" + clientIdForLogging + "'");
      }

      layoutTokens.shrinkSizeTo(cellCount);
    } else {
      if (!ignoreMismatch && LOG.isWarnEnabled() && cellCount - layoutTokens.getSize() != 0) {
        LOG.warn("More cells (" + cellCount + ") than tokens (" + layoutTokens.getSize()
            + ") for layout found! Setting missing tokens to '1*'."
            + " Token string was: " + layoutTokens
            + " clientId='" + clientIdForLogging + "'");
      }
      layoutTokens.ensureSize(cellCount, new RelativeLayoutToken(1));
      //this.layoutTokens = new String[cellCount];
      //for (int i = 0; i < cellCount; i++) {
      //  if (i < layoutTokens.length) {
      //    this.layoutTokens[i] = layoutTokens[i];
      //  } else {
      //    this.layoutTokens[i] = "1*";
      //  }
      //}
    }
    createAndInitSpaces(cellCount, FREE);
  }

  private void createAndInitSpaces(final int columns, final int initValue) {
    spaces = new int[columns];
    for (int j = 0; j < spaces.length; j++) {
      spaces[j] = initValue;
    }
  }

  public void update(final int space, final int index) {
    update(space, index, false);
  }

  public void update(final int spaceParameter, final int index, final boolean force) {
    int space = spaceParameter;
    if (space > spaceLeft) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("More space (" + space + ") needed than available (" + spaceLeft + ")!"
            + " clientId='" + clientIdForLogging + "'");
      }
      if (!force) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Cutting to fit. " + " clientId='" + clientIdForLogging + "'");
        }
        if (spaceLeft < 0) {
          space = 0;
        } else {
          space = spaceLeft;
        }
      }
    }

    spaceLeft -= space;
    cellsLeft--;
    if (index < spaces.length) {
      spaces[index] = space;
      if (spaceLeft < 1 && columnsLeft()) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("There are columns left but no more space! cellsLeft="
              + cellsLeft + ", tokens=" + layoutTokens
              + " clientId='" + clientIdForLogging + "'");
          LOG.warn("calculated spaces = " + tokensToString(spaces)
              + " clientId='" + clientIdForLogging + "'");
        }
      }
    } else {
      LOG.warn("More space to assign (" + space + "px) but no more columns!"
          + " More layout tokens than column tags?" + " clientId='" + clientIdForLogging + "'");
    }
  }

  public boolean columnsLeft() {
    return cellsLeft > 0;
  }


  public void handleIllegalTokens() {
    for (int i = 0; i < spaces.length; i++) {
      if (isFree(i)) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Illegal layout token pattern \"" + layoutTokens.get(i)
              + "\" ignored, set to 0px !" + " clientId='" + clientIdForLogging + "'");
        }
        spaces[i] = 0;
      }
    }
  }


  public static String listToTokenString(final List list) {
    final String[] tokens = new String[list.size()];
    for (int i = 0; i < list.size(); i++) {
      tokens[i] = list.get(i).toString();
    }
    return tokensToString(tokens);
  }

  public static String tokensToString(final int[] tokens) {
    final String[] strings = new String[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
      strings[i] = Integer.toString(tokens[i]);
    }
    return tokensToString(strings);
  }

  public static String tokensToString(final String[] tokens) {
    final StringBuilder sb = new StringBuilder();
    for (final String token : tokens) {
      if (sb.length() != 0) {
        sb.append(";");
      }
      sb.append(token);
    }
    sb.insert(0, "\"");
    sb.append("\"");
    return sb.toString();
  }

  public boolean isFree(final int column) {
    return spaces[column] == FREE;
  }

  public int getSpaceLeft() {
    return spaceLeft;
  }

  public LayoutTokens getLayoutTokens() {
    return layoutTokens;
  }

  public boolean hasLayoutTokens() {
    return !layoutTokens.isEmpty();
  }

  public List<Integer> getSpaceList() {
    final List<Integer> list = new ArrayList<Integer>(spaces.length);
    for (final int space : spaces) {
      list.add(space);
    }
    return list;
  }

  public void handleSpaceLeft() {
    if (spaceLeft > 0) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("spread spaceLeft (" + spaceLeft + "px) to columns" + " clientId='" + clientIdForLogging + "'");
        LOG.debug("spaces before spread :" + arrayAsString(spaces) + " clientId='" + clientIdForLogging + "'");
      }

      for (int i = 0; i < layoutTokens.getSize(); i++) {
        if (layoutTokens.get(i) instanceof RelativeLayoutToken) {
          addSpace(spaceLeft, i);
          break;
        }
      }
      boolean found = false;
      while (spaceLeft > 0) {
//        for (int i = 0; i < layoutTokens.length; i++) {
        for (int i = layoutTokens.getSize() - 1; i > -1; i--) {
          //String layoutToken = layoutTokens[i];
          if (spaceLeft > 0 && layoutTokens.get(i) instanceof RelativeLayoutToken) {
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
      LOG.warn("Space left after spreading : " + spaceLeft + "px!" + " clientId='" + clientIdForLogging + "'");
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("spaces after spread  :" + arrayAsString(spaces) + " clientId='" + clientIdForLogging + "'");
    }
  }

  //TODO replace with Arrays.asList ..
  private String arrayAsString(final int[] currentSpaces) {
    final StringBuilder sb = new StringBuilder("[");
    for (final int currentSpace : currentSpaces) {
      sb.append(currentSpace);
      sb.append(", ");
    }
    sb.replace(sb.lastIndexOf(", "), sb.length(), "]");
    return sb.toString();
  }

  private void addSpace(final int space, final int i) {
    if (spaces[i] > HIDE) {
      if (spaces[i] == FREE) {
        spaces[i] = space;
      } else {
        spaces[i] += space;
      }
      spaceLeft -= space;
    }
  }


  private void parsePortions(final int portions) {
    if (columnsLeft()) {

      //  2. calc and set portion
      if (portions > 0) {
        final int widthForPortions = getSpaceLeft();
        for (int i = 0; i < layoutTokens.getSize(); i++) {
          final LayoutToken token = layoutTokens.get(i);
          if (isFree(i) && token instanceof RelativeLayoutToken) {
            final int portion = ((RelativeLayoutToken) token).getFactor();
            final float w = (float) widthForPortions / portions * portion;
            if (w < 0) {
              update(0, i);
              if (LOG.isDebugEnabled()) {
                LOG.debug("set column " + i + " from " + token
                    + " to with " + w + " == 0px" + " clientId='" + clientIdForLogging + "'");
              }
            } else {
              update(Math.round(w), i);
              if (LOG.isDebugEnabled()) {
                LOG.debug("set column " + i + " from " + token
                    + " to with " + w + " == " + Math.round(w) + "px" + " clientId='" + clientIdForLogging + "'");
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

  public void parseColumnLayout(final double space) {
    parseColumnLayout(space, 0);
  }

  public void parseColumnLayout(final double space, final int padding) {

    if (hasLayoutTokens()) {
      int portions = 0;
      for (int i = 0; i < layoutTokens.getSize(); i++) {
        final LayoutToken token = layoutTokens.get(i);
        if (token instanceof HideLayoutToken) {
          update(0, i);
          spaces[i] = HIDE;
          if (LOG.isDebugEnabled()) {
            LOG.debug("set column " + i + " from " + layoutTokens.get(i)
                + " to hide " + " clientId='" + clientIdForLogging + "'");
          }
        } else if (token instanceof PixelLayoutToken) {
          final int w = ((PixelLayoutToken) token).getPixel();
          update(w, i, true);
          if (LOG.isDebugEnabled()) {
            LOG.debug("set column " + i + " from " + token
                + " to with " + w + " clientId='" + clientIdForLogging + "'");
          }
        } else if (token instanceof RelativeLayoutToken) {
          portions += ((RelativeLayoutToken) token).getFactor();
        } else if (token instanceof PercentLayoutToken) {
          final int percent = ((PercentLayoutToken) token).getPercent();
          final int w = (int) (space / 100 * percent);
          update(w, i);
          if (LOG.isDebugEnabled()) {
            LOG.debug("set column " + i + " from " + token
                + " to with " + w + " clientId='" + clientIdForLogging + "'");
          }
        }
      }
      parsePortions(portions);
//      parseAsterisks();
      handleSpaceLeft();
    }

    if (columnsLeft() && LOG.isWarnEnabled()) {
      handleIllegalTokens();
    }
  }

  @Override
  public String toString() {
    return "LayoutInfo{"
        + "cellsLeft=" + cellsLeft
        + ", spaceLeft=" + spaceLeft
        + ", spaces=" + Arrays.toString(spaces)
        + ", layoutTokens=" + layoutTokens
        + '}';
  }
}
