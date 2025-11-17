package com.example.courtnowproject;

import java.util.List;

public class CourtDoneEvent {
    private List<Court> courtList;

    public CourtDoneEvent(List<Court> courtList) {
        this.courtList = courtList;
    }

    public List<Court> getCourtList() {
        return courtList;
    }

    public void setCourtList(List<Court> courtList) {
        this.courtList = courtList;
    }
}
