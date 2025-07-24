package com.common.models.items.buffs;

import com.common.models.App;
import dev.morphia.annotations.Embedded;

import java.time.LocalDateTime;

@Embedded
public class ActiveBuff {
    private FoodBuff foodBuff;
    private LocalDateTime expirationTime;

    public ActiveBuff() {
    }

    public ActiveBuff(FoodBuff foodBuff) {
        LocalDateTime timeNow = App.getLoggedInUser().getCurrentGame().getDate();
        this.foodBuff = foodBuff;
        this.expirationTime = handleFormattingOfTime(timeNow, foodBuff.getDuration());
    }

    public ActiveBuff(FoodBuff foodBuff, LocalDateTime expirationTime) {
        this.foodBuff = foodBuff;
        this.expirationTime = expirationTime;
    }

    private LocalDateTime handleFormattingOfTime(LocalDateTime dateTime, int durationInHours) {
        LocalDateTime output = dateTime.plusHours(durationInHours);
        if (output.getDayOfMonth() == 29) {
            output = output.plusMonths(1);
            output = output.minusDays(28);
        }
        return output;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public FoodBuff getFoodBuff() {
        return foodBuff;
    }

    @Override
    public String toString() {
        return foodBuff.toString() + " active until: " + expirationTime.getHour() + ":" + (expirationTime.getMinute() < 10 ? "0" + expirationTime.getMinute() : expirationTime.getMinute());
    }
}
