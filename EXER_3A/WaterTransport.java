class WaterTransport extends Transportation {
    public WaterTransport(String name) {
        super(name);
    }
}

class Boat extends WaterTransport {
    public Boat() {
        super("Boat");
    }

    @Override
    public void move() {
        System.out.println("The boat sails on water.");
    }
}

class Submarine extends WaterTransport {
    public Submarine() {
        super("Submarine");
    }

    @Override
    public void move() {
        System.out.println("The submarine dives underwater.");
    }
}
