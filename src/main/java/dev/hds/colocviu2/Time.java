package dev.hds.colocviu2;

import org.springframework.util.StringUtils;

import javax.swing.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Time {

    private static final String decoder[] = {"Lu", "Ma", "Mi", "Jo", "Vi", "Sa", "Du"};

    int ora, min, ziuaSapt, zi, luna, an;

    public Time(String date, String time, String weekDay){
        this.ziuaSapt = Integer.parseInt(weekDay);

        if(time.length() > 5) time = time.substring(0, 5);

        String[] oraMin = time.split(":");
        String[] data = date.split("-");

        this.an = Integer.parseInt(data[0]);
        this.luna = Integer.parseInt(data[1]);
        this.zi = Integer.parseInt(data[2]);

        this.ora = Integer.parseInt(oraMin[0]);
        this.min = Integer.parseInt(oraMin[1]);
    }

    @Override
    public String toString() {
        return (an + "-" + luna + "-" + zi + " " + ora + ":" + min);
    }

    static public int encodeWeekDay(String day){
        int code = -1;

        switch(day){
            case "Lu":
                code = 0;
                break;
            case "Ma":
                code = 1;
                break;
            case "Mi":
                code = 2;
                break;
            case "Jo":
                code = 3;
                break;
            case "Vi":
                code = 4;
                break;
            case "Sa":
                code = 5;
                break;
            case "Du":
                code = 6;
                break;
        }

        return code;
    }

    static public String decodeWeekday(int day){
        return decoder[day];
    }

    static public String stDateFormat(Calendar cal) {
        return (cal.get(Calendar.YEAR) +"-"+ (cal.get(Calendar.MONTH) + 1) +"-"+ cal.get(Calendar.DAY_OF_MONTH));
    }

    static public int WeekdayDif(int cwd, int twd){
        //if(cwd == twd) return 7;
        return (twd - cwd + 7) % 7;
    }

    static public String nextWeekdayDate(Time time, int flightWeekday){

//        Time time = new Time(date, "00:00", "0");
        Calendar cal = new GregorianCalendar(time.an, time.luna-1, time.zi);
        // if flight week day matches cur date and flight time > curTime ret 0
        // or assume we cannot return flights for the current day

        int curWeekday = (cal.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        int ziDif = WeekdayDif(curWeekday, flightWeekday);
        cal.add(Calendar.DAY_OF_WEEK, ziDif);

        System.out.println(curWeekday + " " + flightWeekday);
        System.out.println(stDateFormat(cal));
        return stDateFormat(cal);
    }
}
