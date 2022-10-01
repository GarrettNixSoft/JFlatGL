package com.gnix.jflatgl.gui.component;

import com.gnix.jflatgl.core.renderEngine.elements.RenderElement;
import com.gnix.jflatgl.core.renderEngine.elements.TextureElement;
import com.gnix.jflatgl.core.renderEngine.elements.geometry.CircleElement;
import com.gnix.jflatgl.core.renderEngine.elements.geometry.OutlineElement;
import com.gnix.jflatgl.core.renderEngine.elements.geometry.RectElement;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.gnix.jflatgl.core.renderEngine.textures.TextureComponent;
import com.gnix.jflatgl.core.util.Logger;
import com.gnix.jflatgl.gui.GUI;
import org.joml.Vector4f;

public class Checkbox extends GUIComponent {

	// constants
	public static final int SQUARE = 0;
	public static final int CIRCLE = 1;
	public static final int IMAGE = 2;

	// visual components
	private RenderElement baseElement;
	private RenderElement outlineElement;
	private RenderElement checkElement;
	private float baseOpacity;
	private float checkScale;

	private final GUIText label;
	private float fontSize;

	public Checkbox(String componentID, GUI parent) {
		super(componentID, parent);
		label = new GUIText();
	}

	// ******************************** LABEL CONFIG ********************************
	public void setLabelFont(FontType font) {
		label.setFont(font);
	}

	public void setLabelFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public void setLabelText(String text) {
		label.replaceText(text);
	}

	public void setLabelLineWidth(float lineWidth) {
		label.setLineMaxSize(lineWidth);
	}

	public void setLabelWidth(float width) {
		label.setWidth(width);
	}

	public void setLabelEdge(float edge) {
		label.setEdge(edge);
	}

	public void setLabelBorderWidth(float borderWidth) {
		label.setBorderWidth(borderWidth);
	}

	public void setLabelBorderEdge(float borderEdge) {
		label.setBorderEdge(borderEdge);
	}

	public void setLabelColor(Vector4f color) {
		label.setColor(color);
	}

	public void setLabelBorderColor(Vector4f color) {
		label.setOutlineColor(color);
	}

	// ******************************** SHAPE CONFIG ********************************
	public void setShape(int shape) {
		switch (shape) {
			case SQUARE -> {
				baseElement = new RectElement();
				outlineElement = new OutlineElement();
				// rects and outlines aren't centered by default
				baseElement.setCentered(true);
				outlineElement.setCentered(true);
			}
			case CIRCLE -> {
				baseElement = new CircleElement();
				outlineElement = new CircleElement();
			}
			default -> throw new IllegalArgumentException();
		}
	}

	public void setBaseOpacity(float baseOpacity) {
		this.baseOpacity = baseOpacity;
	}

	public void setRounding(int mode, float round) {
		try {
			RectElement base = (RectElement) baseElement;
			OutlineElement outline = (OutlineElement) outlineElement;
			base.setRoundingMode(mode);
			outline.setRoundingMode(mode);
			base.setRoundRadius(round);
			outline.setRoundRadius(round);
		}
		catch (ClassCastException cce) {
			Logger.logError(Logger.MEDIUM, "Attempted to set rounding mode for a checkbox with shape type " + baseElement.getClass().getName());
		}
	}

	public void setOutlineWidth(float width) {
		switch (outlineElement) {
			case OutlineElement o -> {
				o.setLineWidth(width);
			}
			case CircleElement c -> {
				float ratio = (c.getWidth() - width) / c.getWidth();
				c.setInnerRadius(ratio);
			}
			default -> Logger.logError(Logger.MEDIUM, "Tried to set the outline width of a checkbox with outline type " + outlineElement.getClass().getName());
		}
	}

	// ******************************** CHECK CONFIG ********************************
	public void setCheckType(int checkType) {
		switch (checkType) {
			case SQUARE -> checkElement = new OutlineElement();
			case CIRCLE -> checkElement = new CircleElement();
			case IMAGE -> checkElement = new TextureElement();
			default -> throw new IllegalArgumentException();
		}
		checkElement.setCentered(true);
	}

	public void setCheckScale(float checkScale) {
		this.checkScale = checkScale;
	}

	public void setCheckRounding(int mode, float round) {
		try {
			RectElement r = (RectElement) checkElement;
			r.setRoundingMode(mode);
			r.setRoundRadius(round);
		}
		catch (ClassCastException cce) {
			Logger.logError(Logger.MEDIUM, "Attempted to set rounding mode for a check element of type " + checkElement.getClass().getName());
		}
	}

	public void setCheckImage(TextureComponent texture) {
		try {
			((TextureElement) checkElement).setTexture(texture);
		}
		catch (ClassCastException cce) {
			Logger.logError(Logger.MEDIUM, "Attempted to assign a texture to a check element of type " + checkElement.getClass().getName());
		}
	}

	// ******************************** BEHAVIOR ********************************
	@Override
	public void update() {
		// update colors
		baseElement.setColor(getSecondaryColor());
		outlineElement.setColor(getPrimaryColor());
		// update opacity
		baseElement.setAlpha(getOpacity() * baseOpacity);
		outlineElement.setAlpha(getOpacity());
		checkElement.setAlpha(getOpacity());
		label.setAlpha(getOpacity());
	}

	@Override
	public void doTransform() {
		// update size
		baseElement.setSize(getScaledSize());
		outlineElement.setSize(getScaledSize());
		checkElement.setSize(getScaledSize().mul(checkScale));
		label.setFontSize(fontSize * getScale());
		// update position
		// TODO
	}

	@Override
	public void render() {
		// TODO
	}

	@Override
	public void remove() {
		// TODO
	}

	@Override
	public void restore() {
		// TODO

	}
}
