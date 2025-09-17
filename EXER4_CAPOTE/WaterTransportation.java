public class WaterTransportation extends Transportation{
    private double displacementTons;

    public WaterTransportation(String name, int capacity, double displacementTons){
        super(name, capacity, 0.0);
        this.displacementTons = displacementTons;
    }

    public void displayDisplacementTons(){
        System.out.println("DisplacementTons: " + displacementTons);
    }

    public void displayInfo(){
        System.out.println("Name: " + getName() + ", Capacity: " + getCapacity() + ", Displacement Tons: " + displacementTons);
    }
}
