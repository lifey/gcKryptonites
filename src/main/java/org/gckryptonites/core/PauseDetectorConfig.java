package org.gckryptonites.core;

public record PauseDetectorConfig(long wakeupIntervalMs, long jitterThreshold, long awefulJitterThreshold) {
}
