package uk.co.keithsjohnson.jasperreports;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TestCollectorTest {

	private volatile Object syncObject = new Object();

	@Test
	public void shouldCollect() {
		String[] listArray2 = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "a1", "b1", "c1", "d1", "e1",
				"f1", "g1", "h1", "i1", "j1", "k1", "a2", "b2", "c2", "d2", "e2",
				"f2", "g2", "h2", "i2", "j2", "k2", "a3", "b3", "c3", "d3", "e3",
				"f3", "g3", "h3", "i3", "j3", "k4", "a4", "b4", "c4", "d4", "e4",
				"f4", "g4", "h4", "i4", "j4", "k4" };
		List<String> stringlist = Arrays.asList(listArray2);
		System.out.println(stringlist.size());
		int batchSize = 10;
		final List<String> batch = new ArrayList<>();
		stringlist
				.stream()
				.parallel()
				.forEach(s -> {
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					synchronized (syncObject) {
						if (batch.size() >= batchSize) {
							System.out.println("process batch: " + batch);
							batch.clear();
							batch.add(s);
						} else {
							batch.add(s);
						}
					}
				});
		System.out.println("process batch: " + batch);
		System.out.println("Done");
	}
}
