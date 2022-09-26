package frc.util;

import java.util.List;

public class Counter {
	private int currentCount;
	private static List<Counter> counterList;


	public Counter() {
		this(0);
	}

	public Counter(int initialValue) {
		currentCount = initialValue;
		counterList.add(this);
	}

	public void add() {
		currentCount++;
	}

	public void subtract() {
		currentCount--;
	}

	public int get() {
		return currentCount;
	}

	public List<Counter> getAll() {
		return counterList;
	}

}
