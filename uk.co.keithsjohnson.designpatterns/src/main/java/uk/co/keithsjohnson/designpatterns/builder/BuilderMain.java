package uk.co.keithsjohnson.designpatterns.builder;

public class BuilderMain {

	public static void main(String[] args) {
		Person personObject = GenericBuilder
				.of(Person::new)
				.with(Person::setAge, 5)
				.with(Person::setName, "Keith")
				.build();

		System.out.println(personObject.toString());
	}

}
