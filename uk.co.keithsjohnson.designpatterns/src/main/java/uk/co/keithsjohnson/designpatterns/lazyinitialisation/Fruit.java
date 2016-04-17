package uk.co.keithsjohnson.designpatterns.lazyinitialisation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

public class Fruit {

	private static ConcurrentMap<FruitType, Fruit> types = new ConcurrentHashMap<>();

	/**
	 * Using a private constructor to force the use of the factory method.
	 * 
	 * @param type
	 */
	private Fruit(FruitType type) {
	}

	/**
	 * Lazy Factory method, gets the Fruit instance associated with a certain
	 * type. Instantiates new ones as needed.
	 * 
	 * @param type
	 *            Any allowed fruit type, e.g. APPLE
	 * @return The Fruit instance associated with that type.
	 */
	public static Fruit getFruitByTypeName(FruitType type) {
		if (types.containsKey(type)) {
			System.out.println("containsKey: " + type);
		} else {
			System.out.println("not containsKey: " + type);
		}
		return types.computeIfAbsent(type, newFruit());
	}

	/**
	 * Lazy Factory method, gets the Fruit instance associated with a certain
	 * type. Instantiates new ones as needed. Uses double-checked locking
	 * pattern for using in highly concurrent environments.
	 * 
	 * @param type
	 *            Any allowed fruit type, e.g. APPLE
	 * @return The Fruit instance associated with that type.
	 */
	public static Fruit getFruitByTypeNameHighConcurrentVersion(FruitType type) {
		if (types.containsKey(type)) {
			System.out.println("containsKey: " + type);
		} else {
			System.out.println("not containsKey: " + type);
		}
		return types.computeIfAbsent(type, newFruit());
	}

	protected static Function<FruitType, Fruit> newFruit() {
		System.out.println("new");
		return Fruit::new;
	}

	/**
	 * Displays all entered fruits.
	 */
	public static void showAll() {
		if (types.size() > 0) {

			System.out.println("Number of instances made = " + types.size());

			types
					.entrySet()
					.stream()
					.forEach(entry -> {
						String fruit = entry.getKey().toString();
						fruit = Character.toUpperCase(fruit.charAt(0)) + fruit.substring(1);
						System.out.println(fruit);
					});
			System.out.println();

		}
	}

}