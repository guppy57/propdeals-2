package com.guppy57.propdeals.dto;

import java.util.UUID;

public record QueueMessage(
        UUID analysisId,
        ProcessType processType
) {}