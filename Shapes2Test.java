//import java.io.*;
//import java.text.DecimalFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//
//class IrregularCanvasException extends Throwable {
//    String message;
//    IrregularCanvasException(){
//        message = "Canvas dimensions invalid!";
//    }
//    IrregularCanvasException(String id, double maxArea){
//        message = "Canvas " + id + " has a shape with area larger than "+ maxArea;
//    }
//    void message(){
//        System.out.println(message);
//    }
//}
//class Canvas implements Comparable{
//    private final String ID;
//    private final int total_shapes, total_circles, total_squares;
//    private final double area, min_area, max_area, avg_area;
//
//    Canvas(String ID, int total_shapes, int total_circles, int total_squares, double area, double min_area, double max_area, double avg_area){
//        this.ID = ID;
//        this.total_shapes = total_shapes;
//        this.total_circles = total_circles;
//        this.total_squares = total_squares;
//        this.area = area;
//        this.min_area = min_area;
//        this.max_area = max_area;
//        this.avg_area = avg_area;
//    }
//    @Override
//    public String toString(){
//        DecimalFormat df = new DecimalFormat("0.00");
//        return ID + " " + df.format(total_shapes) + " " + df.format(total_circles) + " " + df.format(total_squares) + " " + df.format(min_area) + " " + df.format(max_area) + " " + df.format(avg_area) + "\n";
//    }
//
//    @Override
//    public int compareTo(Object o) {
//        if (!(o instanceof Canvas)) return 0;
//        Canvas c = (Canvas) o;
//        return Double.compare(area, c.area);
//    }
//}
//class ShapesApplication{
//    private final double maxArea;
//    private final ArrayList<Canvas> canvases;
//    ShapesApplication(double maxArea){
//        this.maxArea = maxArea;
//        canvases = new ArrayList<>();
//    }
//    void readCanvases(InputStream inputStream){
//        try{
//            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//            String line;
//            while((line = br.readLine()) != null){
//                if(line.isBlank()) break;
//                try {
//                    String[] arr = line.split(" ");
//                    String ID = arr[0];
//                    String type = "";
//                    int totalCircles = 0, totalSquares = 0;
//                    double area = 0, smallest = Double.MAX_VALUE, biggest = Double.MIN_VALUE, curr_area = 0;
//                    for (int i = 1; i < arr.length; i++) {
//                        if (i % 2 == 1) {
//                            type = arr[i];
//                        } else {
//                            double size = Double.parseDouble(arr[i]);
//                            if (type.equals("C")) {
//                                totalCircles++;
//                                curr_area = Math.PI * size * size;
//                            } else {
//                                totalSquares++;
//                                curr_area = size * size;
//                            }
//                            if (curr_area > maxArea) throw new IrregularCanvasException(ID, maxArea);
//                            if (curr_area < smallest) {
//                                smallest = curr_area;
//                            }
//                            if (curr_area > biggest) {
//                                biggest = curr_area;
//                            }
//                            area += curr_area;
//                        }
//                    }
//                    canvases.add(new Canvas(ID, totalCircles + totalSquares, totalCircles, totalSquares, area, smallest, biggest, area / (totalCircles + totalSquares)));
//                }catch(IrregularCanvasException e){
//                    e.message();
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("Invalid Input!");;
//        }
//    }
//    void printCanvases(OutputStream os){
//        try{
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
//            for(int i = 0; i<canvases.size(); i++){
//                for(int j = 0; j<canvases.size()-1; j++){
//                    if(canvases.get(j).compareTo(canvases.get(j+1)) < 0) {
//                        Canvas tmp = canvases.get(j);
//                        canvases.set(j, canvases.get(j+1));
//                        canvases.set(j+1, tmp);
//                    }
//                }
//            }
//            for (Canvas c : canvases){
//                bw.write(c.toString());
//                bw.flush();
//            }
//        }catch(IOException e){
//            System.out.println("Invalid Output Destination!");
//        }
//    }
//}
//public class Shapes2Test {
//
//    public static void main(String[] args) {
//
//        ShapesApplication shapesApplication = new ShapesApplication(10000);
//
//        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
//        shapesApplication.readCanvases(System.in);
//
//        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
//        shapesApplication.printCanvases(System.out);
//
//
//    }
//}