package jadeproject;

import java.util.ArrayList;

/**
 * Created by user on 16-01-2016.
 */
public class Calendar {
    private ArrayList<Day> days;

    public Calendar() {
        this.setDays(new ArrayList<>());
        this.initDays();
    }

    private void initDays() {
        for (int i = 1; i <= Settings.numDays; i++) {
            this.getDays().add(new Day(i));
        }
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public void setDays(ArrayList<Day> days) {
        this.days = days;
    }

    public Day getDaybyNum(int num) {
        for(Day d : days)
            if (d.getNumberOfDay() == num)
                return d;
        return null;
    }

    @Override
    public String toString() {
        String s = "";
        for (Day d: days)
            s += d.toString() + "\n";
        return s;
    }
}