public class TransportationTester {
    public static void main(String[] args) {
        Transportation[] transports = {
            new Plane(),
            new Helicopter(),
            new Truck(),
            new SUV(),
            new Motorcycle(),
            new Tricycle(),
            new Kariton(),
            new Boat(),
            new Submarine()
        };

        for (Transportation transport : transports) {
            transport.move();
        }
    }
}