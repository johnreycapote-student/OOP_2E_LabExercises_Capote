public class AndroidPhone {
	private String brand;
	private String model;
	private String color;
	private String imei;
	private String chipset;
	private String cpu;
	private String ram;
	private String storage;
	private String battery;
	private String camera;
	private String display;
	private String os;

	public AndroidPhone() {
		this.brand = "No Brand";
		this.model = "No Model";
		this.color = "No Color";
		this.imei = "No IMEI";
		this.chipset = "No Chipset";
		this.cpu = "No CPU";
		this.ram = "No RAM";
		this.storage = "No Storage";
		this.battery = "No Battery";
		this.camera = "No Camera";
		this.display = "No Display";
		this.os = "No OS";
	}

	public AndroidPhone(String color, String imei) {
		this();
		this.color = color;
		this.imei = imei;
	}

	public AndroidPhone(String brand, String model, String color, String imei) {
		this();
		this.brand = brand;
		this.model = model;
		this.color = color;
		this.imei = imei;
	}

	public AndroidPhone(String brand, String model, String color, String imei, String chipset, String cpu, String ram, String storage, String battery, String camera, String display, String os) {
		this();
		this.brand = brand;
		this.model = model;
		this.color = color;
		this.imei = imei;
		this.chipset = chipset;
		this.cpu = cpu;
		this.ram = ram;
		this.storage = storage;
		this.battery = battery;
		this.camera = camera;
		this.display = display;
		this.os = os;
	}

	public void displayInfo() {
		String info = "";
		info += "Brand: " + this.brand;
		info += "\nModel: " + this.model;
		info += "\nColor: " + this.color;
		info += "\nIMEI: " + this.imei;
		info += "\nChipset: " + this.chipset;
		info += "\nCPU: " + this.cpu;
		info += "\nRAM: " + this.ram;
		info += "\nStorage: " + this.storage;
		info += "\nBattery: " + this.battery;
		info += "\nCamera: " + this.camera;
		info += "\nDisplay: " + this.display;
		info += "\nOS: " + this.os;
		System.out.println(info);
	}
}

