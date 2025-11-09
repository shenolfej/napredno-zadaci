import java.util.ArrayList;
import java.util.Scanner;

class Component{
    private String color;
    private final int weight;
    private ArrayList<Component> components;

    Component(String color, int weight){
        this.color = color;
        this.weight = weight;
        this.components = new ArrayList<>();
    }
    void addComponent(Component component){
        components.add(component);
        sort();
    }
    void sort(){
        for(int i = 0; i<components.size(); i++){
            for (int j = 0; j < components.size()-1; j++) {
                if(components.get(j).weight > components.get(j+1).weight){
                    Component tmp = components.get(j);
                    components.set(j, components.get(j+1));
                    components.set(j+1, tmp);
                }else if(components.get(j).weight == components.get(j+1).weight){
                    if(components.get(j).color.compareToIgnoreCase(components.get(j+1).color) < 0){
                        Component tmp = components.get(j);
                        components.set(j, components.get(j+1));
                        components.set(j+1, tmp);
                    }
                }
            }
        }
    }

    public int getWeight() {
        return weight;
    }
    public String getColor(){
        return color;
    }
    public void setColor(String color){
        this.color = color;
    }
}

class Window{
    private final String name;
    ArrayList<Component> components;

    Window(String name){
        this.name = name;
        components = new ArrayList<>();
    }

    void addComponent(int position, Component component) throws InvalidPositionException{
        if(position-1 > components.size()) throw new InvalidPositionException(String.valueOf(position));
        components.add(component);
    }

    void changeColor(int weight, String color){
        for(Component c : components){
            if(c.getWeight() < weight){
                c.setColor(color);
            }
        }
    }
    void switchComponents(int pos1, int pos2){
        Component tmp = components.get(pos1-1);
        components.set(pos1-1, components.get(pos2-1));
        components.set(pos2-1, tmp);
    }

    @Override
    public String toString(){
        String out = "";
        for(int i = 0; i<components.size(); i++){
            Component c = components.get(i);
            out += (i+1) + ":"+c.getWeight()+":"+c.getColor()+"\n";
        }
        return out;
    }
}
class InvalidPositionException extends Exception{
    private final String message;
    InvalidPositionException(String pos){
        this.message = "Invalid position " + pos +", already taken!";
    }

    public String getMessage(){
        return message;
    }
}


public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.switchComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде