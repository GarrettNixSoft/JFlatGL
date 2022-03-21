package com.floober.engine.core.util.data;

import java.util.NoSuchElementException;

/**
 * A basic Queue implementation. Supports limiting
 * capacity.
 * @param <T> the type of data to store
 */
public class Queue<T> {

	class Node {
		T data;
		Node previous;
		Node next;
	}

	private Node first;
	private Node last;
	private int size;
	private int capacity = 100;

	/**
	 * Create an empty queue. The default capacity is 100.
	 */
	public Queue() {
		first = null;
		last = null;
		size = 0;
	}

	/**
	 * Create an empty queue with a specified capacity.
	 * @param capacity the capacity of the queue
	 * @throws IllegalArgumentException if the specified queue is negative
	 */
	public Queue(int capacity) throws IllegalArgumentException {
		if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative");
		first = null;
		last = null;
		size = 0;
		this.capacity = capacity;
	}

	/**
	 * Get the element at the head of the queue.
	 * @return the next element
	 */
	public T peek() {
		if (first != null) return first.data;
		else return null;
	}

	/**
	 * Get the element at the head of the queue,
	 * and remove it from the queue.
	 * @return the next element
	 */
	public T poll() {
		if (first != null) {
			T result = peek();
			remove();
			return result;
		}
		else throw new NoSuchElementException("There are no elements in this queue!");
	}

	/**
	 * Remove the element at the head of this queue.
	 */
	public void remove() {
		if (first != null) {
			first = first.next;
			size--;
		}
		else throw new NoSuchElementException("There are no elements in the queue!");
	}

	/**
	 * Add an element to the end of this queue.
	 * @param element the element to add
	 * @return {@code true} if there is room in the queue and the element is added, or {@code false} if the queue is full.
	 */
	public boolean push(T element) {
		if (size < capacity) {
			Node newNode = new Node();
			newNode.data = element;
			newNode.previous = last;
			if (first == null) first = newNode;
			if (last != null) last.next = newNode;
			last = newNode;
			size++;
			return true;
		}
		else return false;
	}

	/**
	 * Get the number of elements currently in the queue.
	 * @return the size of the queue
	 */
	public int size() {
		return size;
	}

	/**
	 * Check if the queue is empty.
	 * @return {@code true} if there are zero elements in this queue
	 */
	public boolean isEmpty() {
		return size == 0;
	}

}