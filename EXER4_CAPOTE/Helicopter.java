public class Helicopter extends AirTransportation{
    public Helicopter(int capacity, double wingspan){
        super("Helicopter", capacity, wingspan);
    }

    public void calculateSpeed(){
        speed = 150.0;
    }
    
}
