package dev.hds.colocviu2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Ruta implements Comparable<Ruta> {
    protected int nrZboruri = 0, distTot;
    protected ArrayList<Zbor> zboruri = new ArrayList<Zbor>();

    public Ruta(String from, String to, String date, String time, String stops, String weekDay){
        this.nrZboruri = Integer.parseInt(stops);
        zboruri.add(new Zbor(from, to, date, time, "00:00", weekDay));
    }

    public void addFlightFromResult(ResultSet ret, int startIndex, Time desiredDate) throws SQLException {
        this.nrZboruri++;
        int encodedWeekday = Time.encodeWeekDay(ret.getString(7+startIndex));

        this.zboruri.add(new Zbor(
           ret.getString(2+startIndex),
           ret.getString(3+startIndex),
           Time.nextWeekdayDate(desiredDate, encodedWeekday),
           ret.getString(5+startIndex),
           ret.getString(6+startIndex),
           Integer.toString(encodedWeekday)
        ));
    }

    public static Ruta makeRutaFromResult(ResultSet ret, int nrFlights, Time desiredDate) throws SQLException {
        Ruta curRuta = new Ruta("a", "b", "0-0-0", "0:0", "0", "0");
        curRuta.zboruri.remove(0);
        curRuta.nrZboruri = 0;

        for(int i = 0; i < nrFlights; i++){
            curRuta.addFlightFromResult(ret, i * 7, desiredDate);
        }

        return curRuta;
    }

    @Override
    public String toString() {

        String curString = "[";
        for(Zbor z: zboruri){
            curString += z + ",";
        }

        curString = curString.substring(0, curString.length()-1) + "]";

        return curString;
    }

    @Override
    public int compareTo(Ruta r) {
        Time x = this.zboruri.get(this.nrZboruri-1).timpSosire;
        Time y = r.zboruri.get(r.nrZboruri-1).timpSosire;
        Calendar a = new GregorianCalendar(x.an, x.luna, x.zi, x.ora, x.min);
        Calendar b = new GregorianCalendar(y.an, y.luna, y.zi, y.ora, y.min);

        return a.compareTo(b);
    }
}
