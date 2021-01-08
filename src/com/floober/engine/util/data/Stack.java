package com.floober.engine.util.data;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple Stack implementation.
 * @param <T> the type of data to store
 */
@SuppressWarnings("unchecked")
public class Stack<T> {

	private final List<T> elements;

	public Stack() {
		elements = new ArrayList<>();
	}

	public void push(T element) {
		elements.add(element);
	}

	public T peek() {
		return elements.get(elements.size() - 1);
	}

	public T poll() {
		T result = peek();
		elements.remove(elements.size() - 1);
		return result;
	}

	public void remove() {
		elements.remove(elements.size() - 1);
	}

	public int size() {
		return elements.size();
	}

	public boolean isEmpty() {
		return elements.isEmpty();
	}

	public List<T> getElements() {
		return elements;
	}

}