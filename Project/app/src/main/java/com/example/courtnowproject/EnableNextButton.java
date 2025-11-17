package com.example.courtnowproject;

public class EnableNextButton {
    private int step;
    private Court court;
    private Complex complex;
    private int timeSlot;

    public EnableNextButton(int step, int timeSlot) {
        this.step = step;
        this.timeSlot = timeSlot;
    }

    public EnableNextButton(int step, Court court) {
        this.step = step;
        this.court = court;
    }

    public EnableNextButton(int step, Complex complex) {
        this.step = step;
        this.complex = complex;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Court getCourt() {
        return court;
    }

    public void setCourt(Court court) {
        this.court = court;
    }

    public Complex getComplex() {
        return complex;
    }

    public void setComplex(Complex complex) {
        this.complex = complex;
    }

    public int getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(int timeSlot) {
        this.timeSlot = timeSlot;
    }
}
