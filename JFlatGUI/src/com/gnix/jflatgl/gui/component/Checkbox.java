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
import com.gnix.jflatgl.gui.GUIAction;
import com.gnix.jflatgl.gui.ValueAction;
import org.joml.Vector4f;

public class Checkbox extends GUIComponent {

	// value changes and behavior
	private boolean checked;

	private ValueAction onValueChanged;

	// constants
	public static final int SQUARE = 0;
	public static final int CIRCLE = 1;
	public static final int IMAGE = 2;

	// visual components
	private RenderElement baseElement;
	private RenderElement outlineElement;
	private RenderElement checkElement;
	private float baseOpacity;
	private float checkScale = 0.9f;

	private final GUIText label;
	private float fontSize;
	private float labelPadding;

	public Checkbox(String componentID, GUI parent) {
		super(componentID, parent);
		label = new GUIText();
	}

	// ******************************** VALUE CHANGES ********************************
	public Checkbox onValueChanged(ValueAction action) {
		this.onValueChanged = action;
		return this;
	}

	/**
	 * Configure the left click action for this Checkbox.
	 * The checkbox will always toggle its value first before
	 * executing the provided code.
	 * @param action the action to take after toggling the value
	 * @return this
	 */
	@Override
	public Checkbox onLeftClick(GUIAction action) {
		super.onLeftClick(() -> {
			toggleChecked();
			action.onTrigger();
		});
		return this;
	}

	private void toggleChecked() {
		setChecked(!checked, true);
	}

	/**
	 * Manually set the value of this Checkbox.
	 * @param checked whether this Checkbox should be checked
	 * @param trigger whether this call should trigger the onValueChanged action for this Checkbox
	 */
	public void setChecked(boolean checked, boolean trigger) {
		this.checked = checked;
		if (trigger) this.onValueChanged.onTrigger(this.checked);
	}

	public boolean isChecked() {
		return checked;
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

	public void setLabelAlignment(GUIText.Alignment alignment) {
		label.setTextAlignment(alignment);
	}

	public void setLabelAnchorPoint(GUIText.AnchorPoint anchorPoint) {
		label.setAnchorPoint(anchorPoint);
	}

	public void setLabelPadding(float labelPadding) {
		this.labelPadding = labelPadding;
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
				float ratio = (getWidth() - width) / getWidth();
				c.setInnerRadius(ratio * 0.97f);
				Logger.log("Width was " + getWidth());
				Logger.log("Set inner radius to " + ratio * 0.97f);
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
		outlineElement.setColor(getPrimaryColor());
		checkElement.setColor(getSecondaryColor());
		baseElement.setColor(getTertiaryColor());
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
		baseElement.setPosition(getX(), getY(), getLayer());
		outlineElement.setPosition(getX(), getY(), getLayer());
		checkElement.setPosition(getX(), getY(), getLayer());
		// transform components
		baseElement.transform();
		outlineElement.transform();
		checkElement.transform();
		// position the label based on its anchor point
		switch (label.getAnchorPoint()) {
			case TOP_LEFT -> {
				float labelX = getRight() + labelPadding;
				float labelY = getTop();
				label.setPixelPosition(labelX, labelY, getLayer());
			}
			case TOP_RIGHT -> {
				float labelX = getLeft() - labelPadding - label.getPixelWidth();
				float labelY = getTop();
				label.setPixelPosition(labelX, labelY, getLayer());
			}
			case TOP_CENTER -> {
				float labelX = getX();
				float labelY = getBottom() + labelPadding;
				label.setPixelPosition(labelX, labelY, getLayer());
			}
			case CENTER_LEFT -> {
				float labelX = getRight() + labelPadding;
				float labelY = getY();
				label.setPixelPosition(labelX, labelY, getLayer());
			}
			case CENTER_RIGHT -> {
				float labelX = getLeft() - labelPadding - label.getPixelWidth();
				float labelY = getY();
				label.setPixelPosition(labelX, labelY, getLayer());
			}
			case CENTER -> {
				label.setPixelPosition(getX(), getY(), getLayer());
			}
			case BOTTOM_LEFT -> {
				float labelX = getRight() + labelPadding;
				float labelY = getBottom() - label.getPixelHeight();
				label.setPixelPosition(labelX, labelY, getLayer());
			}
			case BOTTOM_RIGHT -> {
				float labelX = getLeft() - labelPadding - label.getPixelWidth();
				float labelY = getBottom() - label.getPixelHeight();
				label.setPixelPosition(labelX, labelY, getLayer());
			}
			case BOTTOM_CENTER -> {
				float labelX = getX();
				float labelY = getTop() - labelPadding - label.getPixelHeight();
				label.setPixelPosition(labelX, labelY, getLayer());
			}
		}
	}

	@Override
	public void render() {
		if (label.isHidden()) label.show();
		baseElement.render();
		outlineElement.render();
		if (checked) checkElement.render();
	}

	@Override
	public void remove() {
		label.remove();
	}

	@Override
	public void restore() {
		label.show();
	}
}
