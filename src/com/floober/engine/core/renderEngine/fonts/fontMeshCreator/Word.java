package com.floober.engine.core.renderEngine.fonts.fontMeshCreator;

import com.floober.engine.core.util.data.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * During the loading of a text this represents one word in the text.
 * @author Karl
 *
 */
public class Word {
	
	private final List<Character> characters = new ArrayList<>();
	private double width = 0;
	private final double fontSize;

	/**
	 * Create a new empty word.
	 * @param fontSize - the font size of the text which this word is in.
	 */
	protected Word(double fontSize){
		this.fontSize = fontSize;
	}

	/**
	 * Adds a character to the end of the current word and increases the screen-space width of the word.
	 * @param character - the character to be added.
	 */
	protected void addCharacter(Character character){
		characters.add(character);
		width += character.xAdvance() * fontSize;
	}
	
	/**
	 * @return The list of characters in the word.
	 */
	protected List<Character> getCharacters(){
		return characters;
	}
	
	/**
	 * @return The width of the word in terms of screen size.
	 */
	protected double getWordWidth(){
		return width;
	}

	/**
	 * Get a Pair of Words representing the longest subword of this Word
	 * that will fit on one line of the specified length as the first object
	 * and a Word representing the rest of this Word as the second.
	 * @param length The maximum line length.
	 * @return A pair containing the longest subword and the rest of this word.
	 */
	public Pair<Word, Word> getMaxLengthSubword(float length) {
		// get subword
		Word subword = new Word(fontSize);
		int index = 0;
		while (true) {
			subword.addCharacter(characters.get(index));
			index++;
			// break if next character will put it over the limit
			if (index < characters.size() - 1 && subword.getWordWidth() + characters.get(index + 1).sizeX() * fontSize > length) {
				break;
			}
			// else break if that's the last char
			else if (index >= characters.size()) break;
		}
		// get rest
		Word rest = new Word(fontSize);
		while (index < characters.size()) {
			rest.addCharacter(characters.get(index));
			index++;
		}
		// return pair
		return new Pair<>(subword, rest);
	}

}
