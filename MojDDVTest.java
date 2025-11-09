import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;


class AmountNotAllowedException extends Exception{
    String message;
    AmountNotAllowedException(String m){
        message = "Receipt with amount " + m +" is not allowed to be scanned";
    }
    void printMessage(){
        System.out.println(message);
    }
}
class Receipt{
    private String ID;
    private int sum;
    private double returns;

    Receipt(String ID, int sum, double returns){
        this.ID = ID;
        this.sum = sum;
        this.returns = returns;
    }
    @Override
    public String toString(){
        DecimalFormat df = new DecimalFormat("0.00");
        return ID + " " + sum + " " + df.format(returns) + "\n";
    }
}

class MojDDV{
    ArrayList<Receipt> receipts;
    MojDDV(){
        receipts = new ArrayList<>();
    }
    void readRecords(InputStream inputStream){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = br.readLine()) != null){
                String ID;
                ArrayList<Integer> sums = new ArrayList<>();
                double returns = 0;
                String[] arr = line.split(" ");
                ID = arr[0];
                for(int i = 1; i<arr.length; i++){
                    if(i % 2 == 1){
                        sums.add(Integer.parseInt(arr[i]));
                    }else{
                        double tax = 0;
                        if(arr[i].equals("A")) tax = 0.18;
                        if(arr[i].equals("B")) tax = 0.05;
                        returns += sums.get(sums.size()-1) * tax * 0.15;
                    }
                }
                try {
                    int total = 0;
                    for (Integer s : sums) total += s;
                    if (total > 30000) throw new AmountNotAllowedException(String.valueOf(total));
                    receipts.add(new Receipt(ID, total, returns));
                }catch(AmountNotAllowedException e){
                    e.printMessage();
                }
            }
        }catch(IOException e){
            System.out.println("Invalid Input!");
        }
    }

    public void printTaxReturns(OutputStream outputStream){
        try{
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            for(Receipt r : receipts){
                bw.write(r.toString());
                bw.flush();
            }
        }catch(IOException e){
            System.out.println("Invalid Output Location");
        }
    }
}

public class MojDDVTest {
    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}