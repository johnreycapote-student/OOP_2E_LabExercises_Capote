class LandTransport extends Transportation {
    public LandTransport(String name) {
        super(name);
    }
}

class Truck extends LandTransport {
    public Truck() {
        super("Truck");
    }

    @Override
    public void move() {
        System.out.println("The truck drives on roads.");
    }
}

class SUV extends LandTransport {
    public SUV() {
        super("SUV");
    }

    @Override
    public void move() {
        System.out.println("The SUV navigates through urban and off-road terrain.");
    }
}

class Motorcycle extends LandTransport {
    public Motorcycle() {
        super("Motorcycle");
    }

    @Override
    public void move() {
        System.out.println("The motorcycle weaves through traffic.");
    }
}

class Tricycle extends LandTransport {
    public Tricycle() {
        super("Tricycle");
    }

    @Override
    public void move() {
        System.out.println("The tricycle moves slowly on streets.");
    }
}

class Kariton extends LandTransport {
    public Kariton() {
        super("Kariton");
    }

    @Override
    public void move() {
        System.out.println("The kariton is pushed manually.");
    }
}