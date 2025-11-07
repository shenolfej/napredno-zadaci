import java.io.*;
import java.util.ArrayList;

class ShapesApplication{
    ArrayList<String> inputs;
    ShapesApplication(){
        inputs = new ArrayList<>();
    }
    int readCanvases(InputStream inputStream) {
        int count = 0;
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bf.readLine()) != null) {
                if(line.isBlank()) break;
                inputs.add(line);
                String[] arr = line.split(" ");
                count += arr.length - 1;
            }
        }catch(IOException e){
            System.out.println("Invalid Input!");
        }
        return count;
    }
    void printLargestCanvasTo(OutputStream outputStream){
        int max = -1;
        int count = 0;
        String largest = "";
        try{
            for(String s : inputs){
                String[] arr = s.split(" ");
                int perimeter = 0;
                for(int i = 1; i<arr.length; i++){
                    perimeter += Integer.parseInt(arr[i]) * 4;
                }
                if(perimeter > max){
                    largest = arr[0];
                    count = arr.length-1;
                    max = perimeter;
                }
            }

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(largest + " " + count + " " + max);
            bw.flush();

        }catch(IOException e){
            System.out.println("Invalid Output Location!");
        }
    }
}
public class Shapes1Test {

    public static void main(String[] args) {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}