package uk.co.keithsjohnson.designpatterns.factory;

public class FactoryMethodPatternMain {
	public static void main(String[] args) {

		handleVehicle(Car::new);
		handleVehicle(Bus::new);
	}

	static void handleVehicle(VehicleDriver vDriver) {
		System.out.println("Handling a new vehicle...");
		vDriver.driveVehicle();
		vDriver.cleanVehicle();
	}
}