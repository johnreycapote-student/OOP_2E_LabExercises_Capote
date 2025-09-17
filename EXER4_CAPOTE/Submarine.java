public class Submarine extends WaterTransportation{
    public Submarine(int capacity, double displacementTons){
        super("Submarine", capacity, displacementTons);

    }

    public void calculateSpeed(){
        speed = 56.0;
    }
    
}
