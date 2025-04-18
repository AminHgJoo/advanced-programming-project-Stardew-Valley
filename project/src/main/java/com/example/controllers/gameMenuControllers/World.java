package com.example.controllers.gameMenuControllers;

import com.example.controllers.Controller;
import com.example.models.App;
import com.example.models.IO.Request;
import com.example.models.IO.Response;
import com.example.models.enums.Weather;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class World extends Controller {
    public static Response handleTimeQuery(Request request) {
        Response response = new Response();
        response.setSuccess(true);
        response.setMessage(App.getLoggedInUser().getCurrentGame().getDate().toLocalTime().toString());
        return response;
    }

    public static Response handleDateQuery(Request request) {
        Response response = new Response();
        response.setSuccess(true);
        response.setMessage(App.getLoggedInUser().getCurrentGame().getDate().toLocalDate().toString());
        return response;
    }

    public static Response handleDatetimeQuery(Request request) {
        Response response = new Response();
        response.setSuccess(true);
        response.setMessage(App.getLoggedInUser().getCurrentGame().toString());
        return response;
    }

    public static Response handleDayOfWeekQuery(Request request) {
        Response response = new Response();
        response.setSuccess(true);
        LocalDateTime currentDateTime = App.getLoggedInUser().getCurrentGame().getDate();
        int currentDay = currentDateTime.getDayOfMonth();
        int dayOfWeek = currentDay % 7;
        response.setMessage(DayOfWeek.values()[dayOfWeek].toString());
        return response;
    }

    public static Response handleCheatAdvanceTime(Request request) {
        int amountOfHours = Integer.parseInt(request.body.get("X"));
        LocalDateTime currentDateTime = App.getLoggedInUser().getCurrentGame().getDate();
        LocalDateTime nextDateTime;
        int howManyDays = amountOfHours / 24;
        int howManyHours = amountOfHours % 24;
        int howManyMonths = howManyDays / 28;
        howManyDays %= 28;
        int currentHour = currentDateTime.getHour();
        int currentDay = currentDateTime.getDayOfMonth();
        if (howManyHours + currentHour > 22) {
            howManyHours = 22 - currentHour;
        }
        if (howManyDays + currentDay > 28) {
            howManyMonths++;
            howManyDays -= 28;
        }
        nextDateTime = currentDateTime.plusDays(howManyDays);
        nextDateTime = nextDateTime.plusHours(howManyHours);
        nextDateTime = nextDateTime.plusMonths(howManyMonths);
        App.getLoggedInUser().getCurrentGame().setDate(nextDateTime);
        App.getLoggedInUser().getCurrentGame().checkSeasonChange();
        return new Response(true, "Date and time set successfully.");
    }

    public static Response handleCheatAdvanceDate(Request request) {
        int amountOfDays = Integer.parseInt(request.body.get("X"));
        LocalDateTime currentDateTime = App.getLoggedInUser().getCurrentGame().getDate();
        LocalDateTime nextDateTime;
        int howManyDays = amountOfDays % 28;
        int howManyMonths = amountOfDays / 28;
        int currentDay = currentDateTime.getDayOfMonth();
        if (howManyDays + currentDay > 28) {
            howManyMonths++;
            howManyDays -= 28;
        }
        nextDateTime = currentDateTime.plusDays(howManyDays);
        nextDateTime = nextDateTime.plusMonths(howManyMonths);
        App.getLoggedInUser().getCurrentGame().setDate(nextDateTime);
        App.getLoggedInUser().getCurrentGame().checkSeasonChange();
        return new Response(true, "Date set successfully.");
    }

    public static Response handleSeasonQuery(Request request) {
        return new Response(true, App.getLoggedInUser().getCurrentGame().getSeason().toString());
    }

    public static Response handleCheatThor(Request request) {
        return null;
        //TODO: lightning doesn't do anything right now. implement later on.
    }

    public static Response handleWeatherQuery(Request request) {
        return new Response(true, App.getLoggedInUser().getCurrentGame().getWeatherToday().toString());
    }

    public static Response handleWeatherForecastQuery(Request request) {
        return new Response(true, "Tomorrow's weather forecast is: "
                + App.getLoggedInUser().getCurrentGame().getWeatherTomorrow().toString());
    }

    public static Response handleSetWeatherCheat(Request request) {
        String type = request.body.get("Type");
        Weather weather = Weather.getWeatherByName(type);
        if (weather == null) {
            return new Response(false, "Weather type is invalid.");
        } else {
            App.getLoggedInUser().getCurrentGame().setWeatherTomorrow(weather);
        }
        return new Response(true, "Tomorrow's weather set successfully.");
    }

    public static Response handleGreenhouseBuilding(Request request) {
        return null;

    }

    public static Response handleToolUse(Request request) {
        return null;

    }

    public static Response handleBuildBuilding(Request request) {
        return null;

    }
}
