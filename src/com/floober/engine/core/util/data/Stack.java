package com.floober.engine.core.util.data;

/**
 * A simple Stack implementation.
 * @param <T> the type of data to store
 */
public class Stack<T> {

	class Node {
		T data;
		Node next;
	}

	private Node first;
	private int size;
	private int capacity = 100;

	public Stack() {
		this.size = 0;
		this.first = null;
	}

	public Stack(int capacity) {
		this.capacity = capacity;
		this.size = 0;
		this.first = null;
	}

	public void push(T element) {
		if (size == capacity) throw new IndexOutOfBoundsException("This stack is full!");
		Node newFirst = new Node();
		newFirst.data = element;
		newFirst.next = first;
		first = newFirst;
	}

	public T peek() {
		return first.data;
	}

	public T poll() {
		T result = peek();
		remove();
		return result;
	}

	public void remove() {
		first = first.next;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public T[] getElements() {

		Object[] result = new Object[size];
		Stack<T> other = new Stack<>(size);

		int i = 0;
		while (!isEmpty()) {
			T element = poll();
			result[i++] = element;
			other.push(element);
		}

		// now put everything back into this stack
		while (!other.isEmpty()) {
			push(other.poll());
		}

		return (T[]) result;
	}

}