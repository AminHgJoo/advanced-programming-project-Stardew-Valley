package com.example.models.items;

import dev.morphia.annotations.Embedded;

import java.time.LocalDateTime;

@Embedded
public class ActiveBuff {
    private FoodBuff foodBuff;
    private LocalDateTime expirationTime;

    public ActiveBuff() {
    }

    public ActiveBuff(FoodBuff foodBuff, LocalDateTime timeNow, int durationInHours) {
        this.foodBuff = foodBuff;
        this.expirationTime = handleFormattingOfTime(timeNow, durationInHours);
    }

    private LocalDateTime handleFormattingOfTime(LocalDateTime dateTime, int durationInHours) {
        LocalDateTime output = dateTime.plusHours(durationInHours);
        if (output.getDayOfMonth() == 29) {
            output = output.plusMonths(1);
            output = output.minusDays(28);
        }
        return output;
    }

    public FoodBuff getFoodBuff() {
        return foodBuff;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }
}
