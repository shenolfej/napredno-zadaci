import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

class BetterDate{
    int year, month, day, hour, minute, second;

    BetterDate(Date date){
        String[] s = date.toInstant().toString().split("T");
        try {
            String[] dates = s[0].split("-");
            this.year = Integer.parseInt(dates[0]);
            this.month = Integer.parseInt(dates[1]);
            this.day = Integer.parseInt(dates[2]);

            String[] times = s[1].replace("Z", "").split(":");
            this.hour = Integer.parseInt(times[0]);
            this.minute = Integer.parseInt(times[1]);
            this.second = Integer.parseInt(times[2]);
        }catch(NumberFormatException e){
            System.out.println(Arrays.toString(s));
        }
    }

    public boolean isAfter(BetterDate date){
        if(year > date.year) return true;
        if((year == date.year) && (month > date.month)) return true;
        if((year == date.year) && (month == date.month) && (day > date.day)) return true;
        if(sameDay(date) && (hour > date.hour)) return true;
        if(sameDay(date) && (hour == date.hour) && (minute > date.minute)) return true;
        if(sameDay(date) && (hour == date.hour) && (minute == date.minute)) return second >= date.second;
        return false;
    }

    public boolean isBefore(BetterDate date){
        if(year < date.year) return true;
        if((year == date.year) && (month < date.month)) return true;
        if((year == date.year) && (month == date.month) && (day < date.day)) return true;
        if(sameDay(date) && (hour < date.hour)) return true;
        if(sameDay(date) && (hour == date.hour) && (minute < date.minute)) return true;
        if(sameDay(date) && (hour == date.hour) && (minute == date.minute)) return second <= date.second;
        return false;
    }

    public boolean sameDay(BetterDate d){
        return (year == d.year) && (month == d.month) && (day == d.day);
    }

}


class DayData{
    private float temperature, wind, humidity, visibility;
    private Date date;
    DayData(float temperature, float wind, float humidity, float visibility, Date date){
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.visibility = visibility;
        this.date = date;
    }

    @Override
    public String toString(){
        return temperature + " " + wind + " km/h "+humidity+"% "+visibility+" km " + date.toString();
    }

    public Date getDate(){
        return date;
    }
    public float getTemperature(){
        return temperature;
    }
}

class WeatherStation{
    private int days;
    private ArrayList<DayData> data;
    WeatherStation(int days){
        this.days = days;
        this.data = new ArrayList<>();
    }

    public void addMeasurment(float temperature, float wind, float humidity, float visibility, Date date){
        DayData d = new DayData(temperature, wind, humidity, visibility, date);
        if(data.isEmpty()){
            data.add(d);
            return;
        }

        BetterDate newDate = new BetterDate(date);
        BetterDate mostRecentDate = new BetterDate(data.get(data.size()-1).getDate());

        if(newDate.sameDay(mostRecentDate)){
            if(newDate.hour == mostRecentDate.hour){
                if(newDate.minute - mostRecentDate.minute < 2) return;
                else if(newDate.minute - mostRecentDate.minute == 2){
                    if(newDate.second - mostRecentDate.second <= 30) return;
                }

            }
        }

        LocalDate newLocalDate = date.toInstant().atZone(ZoneId.of("GMT")).toLocalDate();

        for(DayData dd : data){
            LocalDate ddLocalDate = dd.getDate().toInstant().atZone(ZoneId.of("GMT")).toLocalDate();
            if(ddLocalDate.until(newLocalDate).getDays() > 5){
                data.remove(dd);
            }
        }
        data.add(d);


    }

    public int total(){
        return data.size();
    }

    public void status(Date from, Date to) throws RuntimeException{
        float total_temp = 0;
        int total = 0;

        BetterDate betterFrom = new BetterDate(from);
        BetterDate betterTo = new BetterDate(to);
        for(DayData d : data){
            BetterDate betterD = new BetterDate(d.getDate());
            if(betterD.isAfter(betterFrom) && betterD.isBefore(betterTo)){
                total_temp += d.getTemperature();
                total++;
                System.out.println(d);
            }
        }
        if(total == 0) throw new RuntimeException();
        System.out.println("Average Temperature: " + total_temp/total);
    }
}



public class WeatherStationTest {
    public static void main(String[] args) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);
            line = scanner.nextLine();
            Date date = df.parse(line);
            ws.addMeasurment(temp, wind, hum, vis, date);
        }
        String line = scanner.nextLine();
        Date from = df.parse(line);
        line = scanner.nextLine();
        Date to = df.parse(line);
        scanner.close();
        System.out.println(ws.total());
        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

// vashiot kod ovde