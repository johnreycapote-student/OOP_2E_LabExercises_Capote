class Transportation {
    private String name;

    public Transportation(String name) {
        this.name = name;
    }

    public void move() {
        System.out.println("This transport is moving.");
    }

    public String getName() {
        return name;
    }
}