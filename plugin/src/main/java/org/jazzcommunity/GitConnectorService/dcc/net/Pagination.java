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

  @Override
  public String toString() {
    return "Pagination{" +
        "start=" + start +
        ", end=" + end +
        '}';
  }

  public static Pagination fromValues(String size, String pos) {
    if (size == null) {
      return new Pagination(0, 0);
    } else if (size != null && pos == null) {
      return new Pagination(0, Integer.parseInt(size));
    }
    int s = Integer.parseInt(size);
    int p = Integer.parseInt(pos);
    return new Pagination(p, p + s);
  }
}
