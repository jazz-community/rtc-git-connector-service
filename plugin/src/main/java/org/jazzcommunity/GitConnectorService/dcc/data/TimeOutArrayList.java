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
    // TODO: Replace try with actual range check.
    // We don't need to pollute the stack with an exception
    try {
      return list.subList(start, end);
    } catch (RuntimeException e) {
      return Collections.emptyList();
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
    return difference > LIMIT;
  }

  // this should probably refresh the 'creation' date, which instead should be the time of
  // last access. That way, a long time between calls will keep the cache valid. However,
  // that probably requires more testing on how long many http requests can actually take
  // when working with production data.
  public ArrayList<T> getList() {
    return list;
  }
}
