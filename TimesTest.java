import java.io.*;
import java.util.ArrayList;

class Time{
    int hours;
    int minutes;

    Time(int hours, int minutes){
        this.hours = hours;
        this.minutes = minutes;
    }
}

class UnsupportedFormatException extends Throwable{
    String message;
    UnsupportedFormatException(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}
class InvalidTimeException extends Throwable{
    String message;
    InvalidTimeException(String message){
        this.message = message;
    }
    public String getMessage(){
        return message;
    }
}

class TimeTable{
    ArrayList<Time> times;
    TimeTable(){
        times = new ArrayList<>();
    }
    void readTimes(InputStream inputStream) throws UnsupportedFormatException, InvalidTimeException{
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = br.readLine()) != null){
                for(String s : line.split(" ")){
                    String[] arr = s.split(":");
                    if(arr.length != 2) arr = s.split("\\.");
                    if(arr.length != 2) throw new UnsupportedFormatException(s);

                    int firstVal = Integer.parseInt(arr[0]);
                    int secondVal = Integer.parseInt(arr[1]);

                    if(!((firstVal >= 0) && (firstVal <= 23))) throw new InvalidTimeException(s);
                    if(!((secondVal >= 0) && (secondVal <= 59))) throw new InvalidTimeException(s);

                    times.add(new Time(firstVal, secondVal));
                }
            }
        }catch(IOException e){
            System.out.println("Input Error!");
        }
    }
    void writeTimes(OutputStream outputStream, TimeFormat format){
        try{
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            sortTimes();
            for(Time t : times){
                String minutes = String.valueOf(t.minutes);
                if(t.minutes < 10) minutes = "0" + minutes;
                if(format == TimeFormat.FORMAT_24){
                    bw.write(String.format("%2d:%s\n", t.hours, minutes));
                }else{
                    int hours = t.hours;
                    String suffix = "AM";
                    if(hours == 0) hours = 12;
                    else if(hours > 12) {
                        hours -= 12;
                        suffix = "PM";
                    }
                    else if(hours == 12) suffix = "PM";
                    bw.write(String.format("%2d:%s %s\n", hours, minutes, suffix));
                }
                bw.flush();
            }
        }catch(IOException e){
            System.out.println("Invalid Output!");
        }
    }
    void sortTimes(){
        for(int i = 0; i<times.size(); i++){
            for(int j = 0; j<times.size()-1; j++){
                if(times.get(j).hours > times.get(j+1).hours){
                    Time tmp = times.get(j);
                    times.set(j, times.get(j+1));
                    times.set(j+1, tmp);
                }
                else if(times.get(j).hours == times.get(j+1).hours){
                    if(times.get(j).minutes > times.get(j+1).minutes){
                        Time tmp = times.get(j);
                        times.set(j, times.get(j+1));
                        times.set(j+1, tmp);
                    }
                }
            }
        }
    }

}

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}