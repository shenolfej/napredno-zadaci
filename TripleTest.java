import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

class Triple<T extends Number>{
    ArrayList<T> numbers;

    Triple(T one, T two, T three){
        numbers = new ArrayList<>();
        numbers.add(one);
        numbers.add(two);
        numbers.add(three);
    }

    public double max(){
        double m = Math.max(numbers.get(0).doubleValue(), numbers.get(1).doubleValue());
        return Math.max(m, numbers.get(2).doubleValue());
    }

    public double avarage(){
        return (numbers.get(0).doubleValue() + numbers.get(1).doubleValue() + numbers.get(2).doubleValue()) / 3;
    }
    public void sort(){
        for(int i = 0; i<numbers.size(); i++){
            for(int j = 0; j<numbers.size()-1; j++){
                if(numbers.get(j).doubleValue() > numbers.get(j+1).doubleValue()){
                    T tmp = numbers.get(j);
                    numbers.set(j, numbers.get(j+1));
                    numbers.set(j+1, tmp);
                }
            }
        }
    }
    @Override
    public String toString(){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(numbers.get(0).doubleValue()) + " " + df.format(numbers.get(1).doubleValue()) + " " + df.format(numbers.get(2).doubleValue());
    }

}

public class TripleTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        int c = scanner.nextInt();
        Triple<Integer> tInt = new Triple<Integer>(a, b, c);
        System.out.printf("%.2f\n", tInt.max());
        System.out.printf("%.2f\n", tInt.avarage());
        tInt.sort();
        System.out.println(tInt);
        float fa = scanner.nextFloat();
        float fb = scanner.nextFloat();
        float fc = scanner.nextFloat();
        Triple<Float> tFloat = new Triple<Float>(fa, fb, fc);
        System.out.printf("%.2f\n", tFloat.max());
        System.out.printf("%.2f\n", tFloat.avarage());
        tFloat.sort();
        System.out.println(tFloat);
        double da = scanner.nextDouble();
        double db = scanner.nextDouble();
        double dc = scanner.nextDouble();
        Triple<Double> tDouble = new Triple<Double>(da, db, dc);
        System.out.printf("%.2f\n", tDouble.max());
        System.out.printf("%.2f\n", tDouble.avarage());
        tDouble.sort();
        System.out.println(tDouble);
    }
}



