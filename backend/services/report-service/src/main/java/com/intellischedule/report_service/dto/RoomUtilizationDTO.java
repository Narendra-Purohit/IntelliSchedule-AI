package com.intellischedule.report_service.dto;

import java.io.Serializable;

public record RoomUtilizationDTO(
    Long roomId,
    String roomNumber,
    String roomType,
    Integer capacity,
    Integer totalSlotsOccupied,
    Double utilizationPercentage
) implements Serializable {}
