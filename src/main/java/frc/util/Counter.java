package frc.util;

import java.util.LinkedList;
import java.util.List;

public class Counter {
	private int currentCount;
	private static List<Counter> counterList = new LinkedList<Counter>();
	private String name;

	public Counter(String name) {
		this(0, name);
	}

	public Counter(int initialValue, String name) {
		this.name = name;
		currentCount = initialValue;
		//counterList.add(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void add() {
		currentCount++;
	}

	public void addToList(Counter counter) {
		counterList.add(counter);
	}

	public void subtract() {
		currentCount--;
	}

	public int get() {
		return currentCount;
	}

	public static List<Counter> getAll() {
		return counterList;
	}

}
