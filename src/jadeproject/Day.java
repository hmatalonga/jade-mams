package jadeproject;

import java.util.ArrayList;

/**
 * Created by user on 16-01-2016.
 */
public class Day {
    private int numberOfDay;
    private ArrayList<TimeSlot> timeSlots;

    public Day(int numberOfDay) {
        this.setNumberOfDay(numberOfDay);
        this.setTimeSlots(new ArrayList<>());
        initTimeSlots();
    }

    private void initTimeSlots() {
        for (int i = 0; i < Settings.numTimeSlots; i++) {
            this.getTimeSlots().add(new TimeSlot());
        }
    }

    public int getNumberOfDay() {
        return numberOfDay;
    }

    public void setNumberOfDay(int numberOfDay) {
        this.numberOfDay = numberOfDay;
    }

    public ArrayList<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(ArrayList<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    @Override
    public String toString() {
        String s = numberOfDay + ": {  ";
        for (TimeSlot t: timeSlots)
            s += t + "  ";
        s += "}";
        return s;
    }
}
