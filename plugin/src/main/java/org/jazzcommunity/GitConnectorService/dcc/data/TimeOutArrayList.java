package org.jazzcommunity.GitConnectorService.dcc.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class TimeOutArrayList<T> {
  // TODO: This should be configurable somewhere
  private static final long LIMIT = 30;

  private final ArrayList<T> list = new ArrayList<T>();
  private final Date creation = new Date();

  public int size() {
    return list.size();
  }

  public Collection<T> subList(int start, int end) {
    return list.subList(start, end);
  }

  public void addAll(Collection<T> commits) {
    list.addAll(commits);
  }

  public boolean dump(Date date) {
    long milliseconds = Math.abs(this.creation.getTime() - date.getTime());
    long difference = TimeUnit.MINUTES.convert(milliseconds, TimeUnit.MILLISECONDS);
    System.out.println(String.format("Needs to be removed: %s", difference > LIMIT));
    return difference > LIMIT;
  }
}
