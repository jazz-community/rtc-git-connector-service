package org.jazzcommunity.GitConnectorService.dcc.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class TimeOutArrayList<T> {
  // TODO: This should be configurable somewhere
  private static final long LIMIT = 30;

  private final ArrayList<T> list = new ArrayList<>();
  private final Date creation = new Date();

  public int size() {
    return list.size();
  }

  public Collection<T> subList(int start, int end) {
    try {
      return list.subList(start, end);
    } catch (RuntimeException e) {
      return Collections.EMPTY_LIST;
    }
  }

  public void addAll(Collection<T> commits) {
    list.addAll(commits);
  }

  public boolean isEmpty() {
    return list.isEmpty();
  }

  public boolean dump(Date date) {
    long milliseconds = Math.abs(this.creation.getTime() - date.getTime());
    long difference = TimeUnit.MINUTES.convert(milliseconds, TimeUnit.MILLISECONDS);
    System.out.println(String.format("Needs to be removed: %s", difference > LIMIT));
    return difference > LIMIT;
  }

  public ArrayList<T> getList() {
    return list;
  }
}
