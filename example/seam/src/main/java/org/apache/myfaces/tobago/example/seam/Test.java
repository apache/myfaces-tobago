package org.apache.myfaces.tobago.example.seam;

import java.util.Random;

public class Test {

  private int x;
  private int y;
  private int[][] data;


  public static void main(String[] args) {
    Test t = new Test(3,3);
    t.distribute();
    System.out.print("\n---------------------------------------------------------\n");
    System.out.print("\n---------------------------------------------------------\n");
    System.out.print(t);

  }

  public Test(int x, int y) {
    this.x = x;
    this.y = y;
    init();
  }

  private void init() {
    data = new int[y*y][x*x];
    for (int i = 0; i < y*y; i++) {
      for (int j = 0; j < x*x; j++) {
        data[i][j] = 0;
      }

    }
  }

  private void distribute() {

    Random random = new Random();

    for (int i = 0; i < y*y; i++) {
      for (int j = 0; j < x*x; j++) {

        boolean tryAgain = false;
        int test;
        boolean[] wasTested = new boolean[x*y];
        do {
          test = random.nextInt(x*y) + 1;
          wasTested[test - 1] = true;
          if (allTested(wasTested)) {
            tryAgain = true; // die ganze Zeile noch mal versuchen
            break;
          }
        } while (!isAllowedHere(test, i, j));

        if (tryAgain) {
          // Zeile löschen
          for (int k = 0; k < x*y; k++) {
            data[i][j] = 0;
          }
          i--;
          System.out.print("\nnoch mal versuchen\n");
          break;
        }

        data[i][j] = test;

        System.out.print("\n---------------------------------------------------------\n");
        System.out.print(this);

      }
    }
  }

  private boolean allTested(boolean[] wasTested) {
    for (boolean b : wasTested) {
      if (!b) {
        return false;
      }
    }
    return true;
  }

  private boolean isAllowedHere(int n, int i, int j) {

    for (int k = 0; k < x*y; k++) {
      if (data[i][k] == n) {
        return false;
      }
    }

    for (int k = 0; k < y*y; k++) {
      if (data[k][j] == n) {
        return false;
      }
    }

    for (int k = 0; k < y; k++) {
      for (int l = 0; l < x; l++) {
        if (data[i / y * y + k][j / x * x + l] == n) {
          return false;
        }
      }
    }


    return true;
  }

  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < x*x; i++) {
      if (i % y == 0) {
        buffer.append('\n');
      }
      for (int j = 0; j < y*y; j++) {
        if (j % x == 0) {
          buffer.append(' ');
        }
        if (x*y >= 10 && data[i][j] < 10) {
          buffer.append(' ');
        }
        buffer.append(data[i][j]);
        buffer.append(' ');
      }
      buffer.append('\n');
    }
    return buffer.toString();
  }
}
