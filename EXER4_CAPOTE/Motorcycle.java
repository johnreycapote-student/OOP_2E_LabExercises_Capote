public class Motorcycle extends LandTransportation{
    public Motorcycle(int capacity, int wheels){
    super("Motorcycle", capacity, wheels);
    } 
    
    public void calculateSpeed(){
        speed = 80.0;
    }
    
}
