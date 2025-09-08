public class Car {
	private String color;
	private String plateNo;
	private String chassisNo;
	private String engine;
	private String transmission;
	private String battery;
	private String suspension;
	private String tires;
	private String brakes;
	private String radiator;
	private String headlights;
	private String wipers;
    private String brand;

	public Car(){
        this.brand = "No Brand";
		this.color = "No Color";
		this.plateNo = "No plateNo";
		this.chassisNo = "No chassisno";
		this.engine = "No engine";
		this.transmission = "No transmission";
		this.battery = "No Battery";
		this.suspension = "No suspension";
		this.tires = "No tires";
		this.brakes = "No brakes";
		this.radiator = "No radiator";
		this.headlights = "No headlights";
		this.wipers = "No wipers";
	}

    public Car(String color, String plateNo){
        this();
        this.color = color;
        this.plateNo = plateNo;
    }

    public Car(String brand, String color, String plateNo){
        this();
        this.brand = brand;
        this.color = color;
        this.plateNo = plateNo;
    }

	public Car(String brand, String color, String plateNo, String chassisNo, String engine, String transmission, String battery, String suspension, String tires, String brakes, String radiator, String headlights, String wipers ){
		this();
        this.brand = brand;
		this.color = color;
		this.plateNo = plateNo;
        this.chassisNo = chassisNo;
        this.engine = engine;
        this.transmission = transmission;
        this.battery = battery;
        this.suspension = suspension;
        this.tires = tires;
        this.brakes = brakes;
        this.radiator = radiator;
        this.headlights = headlights;
        this.wipers = wipers;
    
	}

	public Car(String color, String plateNo, String chassisNo, String engine, String transmission, String battery, String suspension, String tires, String brakes, String radiator, String headlights, String wipers){
		this.color = color;
		this.plateNo = plateNo;
		this.chassisNo = chassisNo;
		this.engine = engine;
		this.transmission = transmission;
		this.battery = battery;
		this.suspension = suspension;
		this.tires = tires;
		this.brakes = brakes;
		this.radiator = radiator;
		this.headlights = headlights;
		this.wipers = wipers;
	}

	public void displayInfo(){
		String info = "";
        info += "Brand: " + this.brand;
		info += "\nColor: " + this.color;
		info += "\nPlateNo: " + this.plateNo;
		info += "\nChassisNo: " + this.chassisNo;
		info += "\nEngine: " + this.engine;
		info += "\nTransmission: " + this.transmission;
		info += "\nBattery: " + this.battery;
		info += "\nSuspension: " + this.suspension;
		info += "\nTires: " + this.tires;
		info += "\nBrakes: " + this.brakes;
		info += "\nRadiator: " + this.radiator;
		info += "\nHeadlights: " + this.headlights;
		info += "\nWipers: " + this.wipers;
		System.out.println(info);
	}
}
