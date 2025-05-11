package com.example.models;

import com.example.models.items.Misc;
import dev.morphia.annotations.Embedded;

@Embedded
public class MarriageRequest {
    private String from;
    private Misc ring;

    public MarriageRequest() {

    }

    public MarriageRequest(String from, Misc ring) {
        this.from = from;
        this.ring = ring;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Misc getRing() {
        return ring;
    }

    public void setRing(Misc ring) {
        this.ring = ring;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarriageRequest req = (MarriageRequest) o;
        return req.from.equals(from);
    }

}
