public class TransportationTester {
    public static void main (String[] args){
        Truck truck = new Truck(3,10);
        Motorcycle motorcycle = new Motorcycle(3, 2);
        SUV suv = new SUV(5, 4);
        Tricycle tricycle = new Tricycle(7, 3);
        Kariton kariton = new Kariton(3, 4);
        Ship ship = new Ship(2, 100);
        Submarine submarine = new Submarine(200, 7900);
        Airplane airplane = new Airplane(800, 212);
        Helicopter helicopter = new Helicopter(2, 67);

        truck.calculateSpeed();
        truck.displayInfo();

        motorcycle.calculateSpeed();
        motorcycle.displayInfo();

        suv.calculateSpeed();
        suv.displayInfo();

        tricycle.calculateSpeed();
        tricycle.displayInfo();

        kariton.calculateSpeed();
        kariton.displayInfo();

        ship.calculateSpeed();
        ship.displayInfo();

        submarine.calculateSpeed();
        submarine.displayInfo();

        airplane.calculateSpeed();
        airplane.displayInfo();

        helicopter.calculateSpeed();
        helicopter.displayInfo();

    }
    
}
