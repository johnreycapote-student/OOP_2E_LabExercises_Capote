public class Airplane extends AirTransportation{
    public Airplane(int capacity, double wingspan){
        super("Airplane", capacity, wingspan);

    }

    public void calculateSpeed(){
        speed = 550.0;
    }
    
}
