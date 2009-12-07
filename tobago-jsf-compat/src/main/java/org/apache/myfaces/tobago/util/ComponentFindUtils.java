/*
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: Dec 4, 2009
 * Time: 6:16:49 PM
 */
package org.apache.myfaces.tobago.util;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIComponent;

public class ComponentFindUtils {
   public static UIComponent findComponent(UIComponent from, String relativeId) {
    int idLength = relativeId.length();
    // Figure out how many colons
    int colonCount = 0;
    while (colonCount < idLength) {
      if (relativeId.charAt(colonCount) != NamingContainer.SEPARATOR_CHAR) {
        break;
      }
      colonCount++;
    }

    // colonCount == 0: fully relative
    // colonCount == 1: absolute (still normal findComponent syntax)
    // colonCount > 1: for each extra colon after 1, go up a naming container
    // (to the view root, if naming containers run out)
    if (colonCount > 1) {
      relativeId = relativeId.substring(colonCount);
      for (int j = 1; j < colonCount; j++) {
        while (from.getParent() != null) {
          from = from.getParent();
          if (from instanceof NamingContainer) {
            break;
          }
        }
      }
    }
    return from.findComponent(relativeId);
  }
}