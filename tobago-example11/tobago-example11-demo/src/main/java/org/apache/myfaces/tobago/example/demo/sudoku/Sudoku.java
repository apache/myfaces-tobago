package org.apache.myfaces.tobago.example.demo.sudoku;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class Sudoku {

  private static final Random RANDOM = new Random(System.currentTimeMillis());

  private byte[] field;
  private Stack<Byte> undefiened;

  private int depth;
  private int maxDepth;
  public static long START;

  public Sudoku() {
    field = new byte[]{
        0, 1, 2, 3, 4, 5, 6, 7, 8,
        -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1,

        -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1,

        -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1,
    };
    RandomList randomList = new RandomList((byte) 81);
    randomList.removeSmallest(9);
    depth = 9;
    undefiened = randomList.asStack();

  }

  public Sudoku(byte[] field) {
    this.field = field;
  //XXX  undefiened = new RandomList((byte) 81).asStack();
  }

  public Result solve() {
    if (undefiened.isEmpty()) {
      System.out.println("--------------- result ");
      System.out.print(this);
      System.out.println("--------------- result ");
      return Result.UNIQUE;
    }
    byte position = undefiened.pop();
    RandomList list = new RandomList((byte) 9);
    boolean foundOne = false;
    while (!list.isEmpty()) {
      field[position] = list.next();
//      System.out.println(depth);
//      System.out.print(this);
      if (checkRules()) {
//        System.out.println("ok");
        Result result = solve2();
        switch (result) {
          case ERROR:
            break;
          case MULTIPLE:
            return Result.MULTIPLE;
          case UNIQUE:
            if (foundOne) {
              return Result.MULTIPLE;
            } else {
              foundOne = true;
            }
            break;
        }
      }
    }
    undefiened.push(position);
    field[position] = -1;

    return Result.ERROR;
  }

  private Result solve2() {
    depth++;
    if (depth > maxDepth) {
      maxDepth = depth;
      System.out.println("new max depth: " + maxDepth);
    }
    Result result = solve();
    depth--;
    return result;
  }


  private boolean checkRules() {
    return checkRowRules() && checkColumnRules() && checkSquareRules();
  }

  protected boolean checkRowRules() {
    for (int i = 0; i < 9; i++) {
      BitSet xxx = new BitSet();
      for (int j = 0; j < 9; j++) {
        byte value = field[i * 9 + j];
        if (value != -1) {
          if (xxx.get(value)) {
//            System.out.println("fail h " + i);
            return false;
          }
          xxx.set(value);
        }
      }
    }
    return true;
  }

  protected boolean checkColumnRules() {
    for (int i = 0; i < 9; i++) {
      BitSet xxx = new BitSet();
      for (int j = 0; j < 9; j++) {
        byte value = field[j * 9 + i];
        if (value != -1) {
          if (xxx.get(value)) {
//            System.out.println("fail v " + i);
            return false;
          }
          xxx.set(value);
        }
      }
    }
    return true;
  }

  protected boolean checkSquareRules() {
    for (int i = 0; i < 9; i++) {
      BitSet xxx = new BitSet();
      for (int j = 0; j < 9; j++) {
        final int i1 = (i % 3) * 3 + i / 3 * 27 + j % 3 + j / 3 * 9;
        byte value = field[i1];
        if (value != -1) {
          if (xxx.get(value)) {
//            System.out.println("fail 3 " + i);
            return false;
          }
          xxx.set(value);
        }
      }
    }
    return true;
  }

  public static void main(String[] args) {
    System.out.println(new RandomList((byte) 9).list);
    Sudoku sudoku = new Sudoku();
    System.out.println("---------------------------------------------------------------------------------------------");
START = System.currentTimeMillis();
    final Result result = sudoku.solve();
    System.out.println(result);
    System.out.println("---------------------------------------------------------------------------------------------");
    System.out.println(sudoku);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < field.length; i++) {
      if (field[i] == -1) {
        builder.append('-');
      } else {
        builder.append(field[i] + 1);
      }
      if (i % 9 != 8) {
        builder.append(' ');
      } else {
        builder.append('\n');
      }
    }
    return builder.toString();
  }

  /**
   * Creates a random list of the numbers 1, 2, 3, ..., n like 4, n-1, 3, ... n-3.
   */
  private static class RandomList {

    private byte n;
    private List<Byte> list;

    private RandomList(byte n) {
      this.n = n;
      list = new ArrayList<Byte>(n);
      shuffle();
    }

    public void shuffle() {
      List<Byte> temp = new ArrayList<Byte>(n);
      for (byte i = 0; i < n; i++) {
        temp.add(i, i);
      }
      for (byte i = n; i > 0; i--) {
        byte index = (byte) RANDOM.nextInt(i);
        list.add(temp.remove(index));
      }
    }

    public byte next() {
      return list.remove(0);
    }

    public boolean isEmpty() {
      return list.isEmpty();
    }

    public Stack<Byte> asStack() {
      final Stack<Byte> stack = new Stack<Byte>();
      for (Byte aByte : list) {
        stack.add(aByte);
      }
      return stack;
    }

    public void removeSmallest(int n) {
      for (byte i = 0; i< n;i++) {
        list.remove((Byte)i);
      }
    }
  }

  private static enum Result {
    UNIQUE,
    MULTIPLE,
    ERROR
  }
}
