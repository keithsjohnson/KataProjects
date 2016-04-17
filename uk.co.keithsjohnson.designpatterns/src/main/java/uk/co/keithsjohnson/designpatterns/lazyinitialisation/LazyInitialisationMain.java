package uk.co.keithsjohnson.designpatterns.lazyinitialisation;

public class LazyInitialisationMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Fruit.getFruitByTypeName(FruitType.banana);
		Fruit.showAll();
		Fruit.getFruitByTypeName(FruitType.apple);
		Fruit.showAll();
		Fruit.getFruitByTypeName(FruitType.banana);
		Fruit.showAll();
	}
}
