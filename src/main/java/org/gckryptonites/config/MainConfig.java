package org.gckryptonites.config;

import org.gckryptonites.core.PauseDetectorConfig;

import java.util.ArrayList;

public record MainConfig(
    PauseDetectorConfig detectorConfig,
    StateHolderConfig[] holderConfigs,
    BrutalAllocatorConfig[] brutalConfigs
) {
}
