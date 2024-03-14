package dev.hds.colocviu2;

public class Zbor {
    String from, to;
    Time timpPlecare, timpSosire;
    int dist;

    public Zbor(String from, String to, String date, String timeLeave, String timeArrival, String weekDay) {
        this.from = from;
        this.to = to;
        this.timpPlecare = new Time(date, timeLeave, weekDay);
        this.timpSosire = new Time(date, timeArrival, weekDay);
    }

    @Override
    public String toString() {
        String curString = "{";

        curString += "\"from\": \"" + from + "\",";
        curString += "\"to\": \"" + to + "\",";
        curString += "\"leave\": \"" + timpPlecare + "\",";
        curString += "\"arrive\": \"" + timpSosire + "\"}";

        return curString;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
