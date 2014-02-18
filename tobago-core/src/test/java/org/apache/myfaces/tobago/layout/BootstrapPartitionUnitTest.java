package org.apache.myfaces.tobago.layout;

import org.junit.Assert;
import org.junit.Test;

public class BootstrapPartitionUnitTest {

  @Test
  public void testArray() {
    Assert.assertArrayEquals(
        new Integer[]{12}, new BootstrapPartition().getParts()); // will be fixed
    Assert.assertArrayEquals(
        new Integer[]{12}, new BootstrapPartition(12).getParts());
    Assert.assertArrayEquals(
        new Integer[]{1,11}, new BootstrapPartition(1,11).getParts());
    Assert.assertArrayEquals(
        new Integer[]{1,11}, new BootstrapPartition(1,12).getParts()); // will be fixed
    Assert.assertArrayEquals(
        new Integer[]{1,2,3,4,2}, new BootstrapPartition(1,2,3,4,5).getParts()); // will be fixed
    Assert.assertArrayEquals(
        new Integer[]{1,1,1,1,1,7}, new BootstrapPartition(-1,-2,-3,-4,-5).getParts()); // will be fixed
  }

  @Test
  public void testString() {
    Assert.assertArrayEquals(
        new Integer[]{12}, BootstrapPartition.valueOf("").getParts()); // will be fixed
    Assert.assertArrayEquals(
        new Integer[]{12}, BootstrapPartition.valueOf("12").getParts());
    Assert.assertArrayEquals(
        new Integer[]{1,11}, BootstrapPartition.valueOf("1;11").getParts());
    Assert.assertArrayEquals(
        new Integer[]{1,11}, BootstrapPartition.valueOf("1;12").getParts()); // will be fixed
    Assert.assertArrayEquals(
        new Integer[]{1,2,3,4,2}, BootstrapPartition.valueOf("1;2;3;4;5").getParts()); // will be fixed
    Assert.assertArrayEquals(
        new Integer[]{1,1,1,1,1,7}, BootstrapPartition.valueOf("-1;-2;-3;-4;-5").getParts()); // will be fixed
  }

}
