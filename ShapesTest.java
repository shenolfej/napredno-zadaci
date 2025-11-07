import java.lang.reflect.Array;
import java.util.*;

enum Color {
    RED, GREEN, BLUE
}
public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

interface Scalable{
    void scale(float scaleFactor);
}
interface Stackable{
    float weight();
}


abstract class Form implements Stackable, Scalable{
    String id;
    Color color;
    Form(String id, Color color){
        this.id = id;
        this.color = color;
    }
    public void scale(float scaleFactor){}
    public float weight(){
        return 0;
    };
}

class Square extends Form{
    float width, height;
    Square(String id, Color color, float width, float height){
        super(id, color);
        this.width = width;
        this.height = height;
    }
    public void scale(float scaleFactor){
        this.width = this.width * scaleFactor;
        this.height = this.height *scaleFactor;
    }
    public float weight(){
        return width*height;
    }
    @Override
    public String toString(){
        return String.format("R: %-5s%-10s%10.2f", id, color, weight());
    }
}
class Circle extends Form{
    float radius;
    Circle(String id, Color color, float radius){
        super(id, color);
        this.radius = radius;
    }
    public void scale(float scaleFactor){
        this.radius = this.radius * scaleFactor;
    }
    public float weight(){
        return (float) (Math.PI * this.radius * this.radius);
    }
    @Override
    public String toString(){
        return String.format("C: %-5s%-10s%10.2f", id, color, weight());
    }
}



class Canvas{
    ArrayList<Form> forms;

    Canvas(){
        forms = new ArrayList<>();
    }
    void add(Form e){
        if(forms.isEmpty()){
            forms.add(e);
            return;
        }
        if(e.weight() > forms.get(0).weight()) {
            forms.add(0,e);
            return;
        }
        if(e.weight() < forms.get(forms.size()-1).weight()){
            forms.add(e);
            return;
        }

        for(int i = 0; i < forms.size()-1; i++){
            if((e.weight() <= forms.get(i).weight()) && (e.weight() > forms.get(i+1).weight())){
                forms.add(i+1, e);
                return;
            }
        }

    }
    void add(String id, Color color, float radius){
        add(new Circle(id, color, radius));
    }
    void add(String id, Color color, float width, float height){
        add(new Square(id, color, width, height));
    }
    void scale(String id, float scaleFactor){
        Form tmp = null;
        for(Form e : forms){
            if(e.id.equals(id)){
                tmp = e;
                break;
            }
        }
        if(tmp == null) return;
        forms.remove(tmp);
        tmp.scale(scaleFactor);
        add(tmp);

    }

    @Override
    public String toString(){
        String s = "";
        for(Form e : forms){
            s += e.toString() + "\n";
        }
        return s;
    }

}