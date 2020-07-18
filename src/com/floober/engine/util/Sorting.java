package com.floober.engine.util;

import java.util.Comparator;
import java.util.List;

public class Sorting {

	public static <T> void insertionSort(List<T> elements, Comparator<T> comparator) {

		int n = elements.size();

		for (int i = 1; i < n; ++i) {

			T key = elements.get(i);
			int j = i - 1;

			while (j >= 0 && comparator.compare(elements.get(j), key) > 0) {
				elements.set(j + 1, elements.get(j));
				j--;
			}
			elements.set(j + 1, key);

		}

	}

}