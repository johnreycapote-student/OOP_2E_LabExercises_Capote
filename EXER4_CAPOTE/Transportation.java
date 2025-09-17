public class Transportation {
    private String name;
    private int capacity;
    protected double speed;
 
    public Transportation(String name, int capacity, double speed){
        this.name = name;
        this.capacity = capacity;
        this.speed = speed;
    }

    public void calculateSpeed(){

    }
     
   
    public void displayInfo(){    
        System.out.println("Name: " + name + ", capacity: " + capacity + ", speed: " + speed);
    }

    public String getName(){
        return name;
    }

    public int getCapacity(){
        return capacity;
    }

    public double getSpeed(){
        return speed;
    }

 }
 