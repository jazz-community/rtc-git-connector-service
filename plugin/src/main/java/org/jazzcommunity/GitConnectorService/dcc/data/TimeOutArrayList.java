package org.jazzcommunity.GitConnectorService.dcc.data;

import java.util.ArrayList;
import java.util.Collection;

public class TimeOutArrayList<T> {
  private final ArrayList<T> list = new ArrayList<T>();

  public int size() {
    return list.size();
  }

  public Collection<T> subList(int start, int end) {
    return list.subList(start, end);
  }

  public void addAll(Collection<T> commits) {
    list.addAll(commits);
  }
}
