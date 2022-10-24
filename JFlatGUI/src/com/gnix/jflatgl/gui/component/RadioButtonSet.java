package com.gnix.jflatgl.gui.component;

import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.FontType;
import com.gnix.jflatgl.core.renderEngine.fonts.fontMeshCreator.GUIText;
import com.gnix.jflatgl.core.renderEngine.textures.TextureComponent;
import com.gnix.jflatgl.core.renderEngine.util.AnchorPoint;
import com.gnix.jflatgl.gui.GUI;
import com.gnix.jflatgl.gui.ValueAction;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class RadioButtonSet extends GUIComponent {

	// ******************************** OPTIONS ********************************
	private final List<RadioButton> radioButtons = new ArrayList<>();
	private int currentSelectedIndex = 0;

	// ******************************** ARRANGEMENT ********************************
	public static final int VERTICAL = 0;
	public static final int HORIZONTAL = 1;
	private int buttonArrangement;

	private AnchorPoint anchorPoint;
	private float buttonPadding;

	// ******************************** CONSTRUCTOR ********************************
	public RadioButtonSet(String componentID, GUI parent) {
		super(componentID, parent);
	}

	// ******************************** ADDING BUTTONS ********************************

	/**
	 * Add a new option to this RadioButtonSet. Generates another
	 * RadioButton, which are stored and will be displayed in the
	 * order that they are created.
	 * <br>
	 * All RadioButtons in the set should be added before calling any
	 * methods to configure their appearance.
	 *
	 * @param label the label for the button
	 * @param onClickAction what the radio button for this option should do when selected or deselected
	 */
	public void addOption(String label, ValueAction onClickAction) {
		// convert the label to a component name suffix
		String name = label.toLowerCase().replace(' ', '_');
		// generate a new button
		RadioButton newButton = new RadioButton(getComponentID() + '_' + name, getParent());
		// the button should be selected when clicked
		int index = radioButtons.size();
		newButton.onLeftClick(() -> {
			radioButtons.get(currentSelectedIndex).setChecked(false, true);
			currentSelectedIndex = index;
			newButton.setChecked(true, true);
		});
		// the caller decides what happens when this radio button is selected or deselected
		newButton.onValueChanged(onClickAction);
		// introduce the new button
		radioButtons.add(newButton);
	}

	// ******************************** CONFIGURING APPEARANCE ********************************

	// **************** ARRANGEMENT ****************
	public RadioButtonSet arrangement(int buttonArrangement) {
		setButtonArrangement(buttonArrangement);
		return this;
	}

	public RadioButtonSet padding(float buttonPadding) {
		setButtonPadding(buttonPadding);
		return this;
	}

	public void setButtonArrangement(int buttonArrangement) {
		this.buttonArrangement = buttonArrangement;
	}

	public void setButtonPadding(float buttonPadding) {
		this.buttonPadding = buttonPadding;
	}

	// **************** LABELS ****************
	public void setLabelFont(FontType font) {
		for (RadioButton rb : radioButtons) {
			rb.setLabelFont(font);
		}
	}

	public void setLabelFontSize(float fontSize) {
		for (RadioButton rb : radioButtons) {
			rb.setLabelFontSize(fontSize);
		}
	}

	public void setLabelText(String text) {
		for (RadioButton rb : radioButtons) {
			rb.setLabelText(text);
		}
	}

	public void setLabelLineWidth(float lineWidth) {
		for (RadioButton rb : radioButtons) {
			rb.setLabelLineWidth(lineWidth);
		}
	}

	public void setLabelWidth(float width) {
		for (RadioButton rb : radioButtons) {
			rb.setLabelWidth(width);
		}
	}

	public void setLabelEdge(float edge) {
		for (RadioButton rb : radioButtons) {
			rb.setLabelEdge(edge);
		}
	}

	public void setLabelBorderWidth(float borderWidth) {
		for (RadioButton rb : radioButtons) {
			rb.setLabelBorderWidth(borderWidth);
		}
	}

	public void setLabelBorderEdge(float borderEdge) {
		for (RadioButton rb : radioButtons) {
			rb.setLabelBorderEdge(borderEdge);
		}
	}

	public void setLabelColor(Vector4f color) {
		for (RadioButton rb : radioButtons) {
			rb.setLabelColor(color);
		}
	}

	public void setLabelBorderColor(Vector4f color) {
		for (RadioButton rb : radioButtons) {
			rb.setLabelBorderColor(color);
		}
	}

	public void setLabelAlignment(GUIText.Alignment alignment) {
		for (RadioButton rb : radioButtons) {
			rb.setLabelAlignment(alignment);
		}
	}

	public void setLabelAnchorPoint(AnchorPoint anchorPoint) {
		for (RadioButton rb : radioButtons) {
			rb.setLabelAnchorPoint(anchorPoint);
		}
	}

	public void setLabelPadding(float labelPadding) {
		for (RadioButton rb : radioButtons) {
			rb.setLabelPadding(labelPadding);
		}
	}

	// **************** SHAPES ****************
	public void setShape(int shape) {
		for (RadioButton rb : radioButtons) {
			rb.setShape(shape);
		}
	}

	public void setBaseOpacity(float baseOpacity) {
		for (RadioButton rb : radioButtons) {
			rb.setBaseOpacity(baseOpacity);
		}
	}

	public void setRounding(int mode, float round) {
		for (RadioButton rb : radioButtons) {
			rb.setRounding(mode, round);
		}
	}

	public void setOutlineWidth(float width) {
		for (RadioButton rb : radioButtons) {
			rb.setOutlineWidth(width);
		}
	}

	// **************** CHECK CONFIG ****************
	public void setCheckType(int checkType) {
		for (RadioButton rb : radioButtons) {
			rb.setCheckType(checkType);
		}
	}

	public void setCheckScale(float checkScale) {
		for (RadioButton rb : radioButtons) {
			rb.setCheckScale(checkScale);
		}
	}

	public void setCheckRounding(int mode, float round) {
		for (RadioButton rb : radioButtons) {
			rb.setCheckRounding(mode, round);
		}
	}

	public void setCheckImage(TextureComponent texture) {
		for (RadioButton rb : radioButtons) {
			rb.setCheckImage(texture);
		}
	}

	// ******************************** POSITIONING BUTTONS ********************************
	public void positionButtons() {
		// TODO
	}

	// ******************************** UPDATING/RENDERING BUTTONS ********************************
	@Override
	public void update() {
		for (RadioButton rb : radioButtons) {
			rb.update();
		}
	}

	@Override
	public void doTransform() {

		if (scaleChanged()) {
			positionButtons();
		}

		// transform all buttons
		for (RadioButton rb : radioButtons) {
			rb.doTransform();
		}
	}

	@Override
	public void render() {
		for (RadioButton rb : radioButtons) {
			rb.render();
		}
	}

	@Override
	public void remove() {
		for (RadioButton rb : radioButtons) {
			rb.remove();
		}
	}

	@Override
	public void restore() {
		for (RadioButton rb : radioButtons) {
			rb.restore();
		}
	}

	/**
	 * The RadioButton class models the individual buttons in
	 * a RadioButton set. Their behavior is sufficiently similar
	 * to a Checkbox when taken in isolation, so they are a subclass
	 * of the Checkbox.
	 */
	private static class RadioButton extends Checkbox {

		public RadioButton(String componentID, GUI parent) {
			super(componentID, parent);
		}

	}

}
