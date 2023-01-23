package org.gckryptonites.config;

import org.gckryptonites.core.PauseDetectorConfig;

public record MainConfig(
    PauseDetectorConfig detectorConfig,
    StateHolderConfig[] holderConfigs,
    BrutalAllocatorConfig[] brutalConfigs
) {
}
