public class Truck extends LandTransportation{
    public Truck(int capacity, int wheels){
        super("Truck", capacity, wheels);
    }

    public void calculateSpeed(){
        speed = 60.0;
    }

}