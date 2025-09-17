public class Ship extends WaterTransportation{
    public Ship(int capacity, double displacementTons){
        super("Ship", capacity, displacementTons);
    }

    public void calculateSpeed(){
        speed = 20.0;
    }
}
