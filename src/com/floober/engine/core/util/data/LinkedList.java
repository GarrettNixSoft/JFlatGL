package com.floober.engine.core.util.data;

import java.util.ArrayList;
import java.util.List;

public class LinkedList<T> {

	public class Node {
		T data;
		Node previous;
		Node next;
	}

	private int capacity;
	private int size;
	private Node first;
	private Node last;

	/**
	 * Create a new empty Linked List. The capacity is initialized to
	 * {@code Integer.MAX_VALUE}.
	 */
	public LinkedList() {
		capacity = Integer.MAX_VALUE;
		size = 0;
		first = last = null;
	}

	/**
	 * Create a new empty linked list with the specified capacity.
	 * @param capacity the maximum capacity of the Linked List
	 */
	public LinkedList(int capacity) {
		this.capacity = capacity;
		size = 0;
		first = last = null;
	}

	/**
	 * Get the capacity of this Linked List.
	 * @return the capacity of the list
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * Get the current size of this Linked List.
	 * @return the number of elements in the list
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Get the first Node in this Linked List.
	 * @return the first Node in the list
	 */
	public Node getFirst() {
		return first;
	}

	/**
	 * Get the last Node in this Linked List.
	 * @return the last Node in the list
	 */
	public Node getLast() {
		return last;
	}

	/**
	 * Set the maximum capacity of this linked list.
	 * @param newCapacity the new capacity
	 * @throws RuntimeException if the new capacity is smaller than the current size
	 * @throws IllegalArgumentException if the new capacity is negative
	 */
	public void setCapacity(int newCapacity) {
		if (newCapacity < size)
			throw new RuntimeException("New capacity is smaller than current size of this Linked List");
		else if (newCapacity < 0)
			throw new IllegalArgumentException("Linked List capacity cannot be negative");
		else capacity = newCapacity;
	}

	/**
	 * Add an element to the beginning of this Linked List.
	 * @param element the element to add
	 */
	public void addFirst(T element) {
		Node newFirst = new Node();
		newFirst.data = element;
		newFirst.next = first;
		newFirst.previous = null;
		first = newFirst;
		size++;
	}

	/**
	 * Add an element to the end of this Linked List.
	 * @param element the element to add
	 */
	public void addLast(T element) {
		Node newLast = new Node();
		newLast.data = element;
		newLast.next = null;
		newLast.previous = last;
		last = newLast;
		size++;
	}

	/**
	 * Get the first element from this Linked List and remove it from the list.
	 * @return the first element from the Linked List
	 */
	public T pollFirst() {
		T data = first.data;
		popFirst();
		return data;
	}

	/**
	 * Get the last element from this Linked List and remove it from the list.
	 * @return the first element from the linked list
	 */
	public T pollLast() {
		T data = last.data;
		popLast();
		return data;
	}

	/**
	 * Remove the first element from this Linked List.
	 */
	public void popFirst() {
		first = first.next;
		size--;
	}

	/**
	 * Remove the last element from this Linked List.
	 */
	public void popLast() {
		last = last.previous;
		size--;
	}

	/**
	 * Get all of the elements from this Linked List in a standard Java List.
	 * This operation runs in O(n) (linear) time.
	 * @return every element from the Linked List in a List
	 */
	public List<T> getElements() {
		List<T> elements = new ArrayList<>();
		Node ptr = first;
		while (ptr.next != null) {
			elements.add(ptr.data);
			ptr = ptr.next;
		}
		return elements;
	}

}
