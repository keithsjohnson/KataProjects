package uk.co.keithsjohnson.designpatterns.factory;

class Bus implements Vehicle {
	@Override
	public void drive() {
		System.out.println("Driving a Bus...");
	}

	@Override
	public void clean() {
		System.out.println("Cleaning a Bus...");
	}
}