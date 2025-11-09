import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Scanner;

class ZeroDenominatorException extends Throwable{
    String message;
    ZeroDenominatorException(){
        message = "Denominator cannot be zero";
    }
    public String getMessage(){
        return message;
    }
}

class GenericFraction<T extends Number, U extends Number>{
    T numerator;
    U denominator;
    GenericFraction(T numerator, U denominator) throws ZeroDenominatorException{
        if(denominator.doubleValue() == 0) throw new ZeroDenominatorException();
        this.denominator = denominator;
        this.numerator = numerator;
    }
    public GenericFraction<Double, Double> add(GenericFraction<? extends Number, ? extends Number> gf) throws ZeroDenominatorException{
        Double num = numerator.doubleValue() * gf.denominator.doubleValue() + denominator.doubleValue() * gf.numerator.doubleValue();
        Double den = denominator.doubleValue() * gf.denominator.doubleValue();

        return new GenericFraction<>(num, den);
    }

    public double toDouble(){
        return numerator.doubleValue() / denominator.doubleValue();
    }
    @Override
    public String toString(){
        DecimalFormat df = new DecimalFormat("0.00");
        int num = numerator.intValue();
        int den = denominator.intValue();
        while(true){
            BigInteger one = BigInteger.valueOf(num);
            BigInteger two = BigInteger.valueOf(den);

            int gcd = one.gcd(two).intValue();
            if(gcd <= 1) break;
            num /= gcd;
            den /= gcd;
        }
        return df.format(num) + " / " + df.format(den);
    }
}

public class GenericFractionTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double n1 = scanner.nextDouble();
        double d1 = scanner.nextDouble();
        float n2 = scanner.nextFloat();
        float d2 = scanner.nextFloat();
        int n3 = scanner.nextInt();
        int d3 = scanner.nextInt();
        try {
            GenericFraction<Double, Double> gfDouble = new GenericFraction<Double, Double>(n1, d1);
            GenericFraction<Float, Float> gfFloat = new GenericFraction<Float, Float>(n2, d2);
            GenericFraction<Integer, Integer> gfInt = new GenericFraction<Integer, Integer>(n3, d3);
            System.out.printf("%.2f\n", gfDouble.toDouble());
            System.out.println(gfDouble.add(gfFloat));
            System.out.println(gfInt.add(gfFloat));
            System.out.println(gfDouble.add(gfInt));
            gfInt = new GenericFraction<Integer, Integer>(n3, 0);
        } catch(ZeroDenominatorException e) {
            System.out.println(e.getMessage());
        }

        scanner.close();
    }

}

// вашиот код овде
