class AirTransport extends Transportation {
    public AirTransport(String name) {
        super(name);
    }
}

class Plane extends AirTransport {
    public Plane() {
        super("Plane");
    }

    @Override
    public void move() {
        System.out.println("The plane flies in the sky.");
    }
}

class Helicopter extends AirTransport {
    public Helicopter() {
        super("Helicopter");
    }

    @Override
    public void move() {
        System.out.println("The helicopter hovers and flies in the air.");
    }
}