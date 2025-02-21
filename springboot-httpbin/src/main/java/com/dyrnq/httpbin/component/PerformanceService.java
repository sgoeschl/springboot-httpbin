package com.dyrnq.httpbin.component;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Component;

@Component
public class PerformanceService extends BaseService {

  /**
   * Invoke this method using a simulated stop-the-world GC pause.
   *
   * @param responseTimeMillis        the response time if not blocked by simulated GC
   * @param intervalRepetitionSeconds repeat the simulated GC pause after X seconds
   * @param intervalDurationSeconds   the duration of the simulated GC pause
   * @return true if method was blocked by simulated GC pause
   */
  public boolean simulateInvocationWithGCPause(long responseTimeMillis,
      long intervalRepetitionSeconds,
      long intervalDurationSeconds) {
    Validate.isTrue(responseTimeMillis >= 0, "response time must be greater than or equal to zero");
    if (!isValidInterval(intervalRepetitionSeconds, intervalDurationSeconds)) {
      sleep(responseTimeMillis);
      return false;
    }

    long currentTimeSeconds = System.currentTimeMillis() / 1000L;
    final long sleepMillisForInterval = getSleepMillisToEndOfInterval(currentTimeSeconds,
        intervalRepetitionSeconds,
        intervalDurationSeconds);
    sleep(Math.max(responseTimeMillis, sleepMillisForInterval));
    return sleepMillisForInterval > 0;
  }

  @VisibleForTesting
  long getSleepMillisToEndOfInterval(long currentTimeSeconds, long intervalRepetitionSeconds,
      long intervalDurationSeconds) {
    long startOfIntervalSeconds = getStartOfInterval(currentTimeSeconds, intervalRepetitionSeconds);
    long endOfIntervalSeconds = startOfIntervalSeconds + intervalDurationSeconds;
    boolean isInInterval = startOfIntervalSeconds <= currentTimeSeconds && currentTimeSeconds < endOfIntervalSeconds;
    return isInInterval ? (endOfIntervalSeconds - currentTimeSeconds) * 1_000L : 0;
  }

  @VisibleForTesting
  long getStartOfInterval(long currentTimeSeconds, long intervalRepetitionSeconds) {
    return (currentTimeSeconds / intervalRepetitionSeconds) * intervalRepetitionSeconds;
  }

  private boolean isValidInterval(long intervalRepetitionSeconds, long intervalDurationSeconds) {
    if (intervalRepetitionSeconds <= 0 || intervalDurationSeconds <= 0) {
      return false;
    } else {
      return true;
    }
  }

  private void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
