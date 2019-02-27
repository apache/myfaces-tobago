package org.apache.myfaces.tobago.internal.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface Fruit extends Serializable {

  static List<Fruit> getFreshFruits() {
      final List<Fruit> list = new ArrayList<>();
      list.add(Apple.GOLDEN_DELICIOUS);
      list.add(Apple.SCHOENER_AUS_BOSKOOP);
      list.add(Pear.WILLIAMS_CHRIST);
      list.add(Pear.KOESTLICHE_AUS_CHARNEUX);
      return list;
  }

  String getName();
  void setName(String name);
}
