package uk.co.keithsjohnson.designpatterns.factory;

interface VehicleDriver {
	public Vehicle getVehicle();

	public default void driveVehicle() {
		getVehicle().drive();
	}

	public default void cleanVehicle() {
		getVehicle().clean();
	}
}