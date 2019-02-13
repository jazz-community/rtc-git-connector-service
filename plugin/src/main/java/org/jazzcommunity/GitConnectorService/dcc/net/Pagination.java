package org.jazzcommunity.GitConnectorService.dcc.net;

public final class Pagination {
  private final int start;
  private final int end;

  private Pagination(int start, int end) {
    this.start = start;
    this.end = end;
  }

  public int getStart() {
    return start;
  }

  public int getEnd() {
    return end;
  }

  public boolean isPaginated() {
    return start != 0;
  }

  public static Pagination fromValues(String size, String pos) {
    if (size == null) {
      return new Pagination(0, 0);
    } else if (size != null && pos == null) {
      return new Pagination(0, Integer.parseInt(size));
    }

    return new Pagination(Integer.parseInt(size), Integer.parseInt(pos));
  }
}
