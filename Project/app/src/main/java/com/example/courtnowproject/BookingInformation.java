package com.example.courtnowproject;

import com.google.firebase.Timestamp;

public class BookingInformation {
    private String username, userId, email, time, courtId, courtName, complexId, complexName, complexAddress, date, area, bookingId;
    private Long slot;
    private Timestamp timestamp;
    private boolean done;

    public BookingInformation() {
    }




    public BookingInformation(String area, String bookingId, String username, String userId, String email, String time, String courtId, String courtName, String complexId, String complexName, String complexAddress, Long slot, String date) {
        this.bookingId = bookingId;
        this.area = area;
        this.username = username;
        this.userId = userId;
        this.email = email;
        this.time = time;
        this.courtId = courtId;
        this.courtName = courtName;
        this.complexId = complexId;
        this.complexName = complexName;
        this.complexAddress = complexAddress;
        this.slot = slot;
        this.date = date;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCourtId() {
        return courtId;
    }

    public void setCourtId(String courtId) {
        this.courtId = courtId;
    }

    public String getCourtName() {
        return courtName;
    }

    public void setCourtName(String courtName) {
        this.courtName = courtName;
    }

    public String getComplexId() {
        return complexId;
    }

    public void setComplexId(String complexId) {
        this.complexId = complexId;
    }

    public String getComplexName() {
        return complexName;
    }

    public void setComplexName(String complexName) {
        this.complexName = complexName;
    }

    public String getComplexAddress() {
        return complexAddress;
    }

    public void setComplexAddress(String complexAddress) {
        this.complexAddress = complexAddress;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
