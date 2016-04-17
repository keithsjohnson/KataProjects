package uk.co.keithsjohnson.designpatterns.factory;

class Car implements Vehicle {
	@Override
	public void drive() {
		System.out.println("Driving a car...");
	}

	@Override
	public void clean() {
		System.out.println("Cleaning a car...");
	}
}