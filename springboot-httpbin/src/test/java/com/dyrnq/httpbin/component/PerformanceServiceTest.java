package com.dyrnq.httpbin.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PerformanceServiceTest {

  private PerformanceService performanceService = new PerformanceService();
  
  @Test
  void shouldDetectStartOfInterval() {
    assertEquals(100, performanceService.getStartOfInterval(100, 10));
    assertEquals(100, performanceService.getStartOfInterval(101, 10));
    assertEquals(100, performanceService.getStartOfInterval(109, 10));
    assertEquals(110, performanceService.getStartOfInterval(111, 10));
  }

  @Test
  void shouldGetSleepMillisToEndOfInterval() {
    assertEquals(2000, performanceService.getSleepMillisToEndOfInterval(100, 10, 2));
    assertEquals(1000, performanceService.getSleepMillisToEndOfInterval(101, 10, 2));
    assertEquals(0, performanceService.getSleepMillisToEndOfInterval(102, 10, 2));
    assertEquals(0, performanceService.getSleepMillisToEndOfInterval(109, 10, 2));
    assertEquals(2000, performanceService.getSleepMillisToEndOfInterval(110, 10, 2));
  }

  @Test
  void shouldSimulateInvocationWithGCPause() {
    assertTrue(performanceService.simulateInvocationWithGCPause(0, 1, 1));
    assertFalse(performanceService.simulateInvocationWithGCPause(0, Long.MAX_VALUE, 1));
  }

  @Test
  void shouldSimulateInvocationWithInvalidGCPauseInterval() {
    assertFalse(performanceService.simulateInvocationWithGCPause(0, -1, 1));
    assertFalse(performanceService.simulateInvocationWithGCPause(0, 1, -1));
  }
}
