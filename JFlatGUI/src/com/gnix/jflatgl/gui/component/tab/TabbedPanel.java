package com.gnix.jflatgl.gui.component.tab;

import com.gnix.jflatgl.core.renderEngine.display.DisplayManager;
import com.gnix.jflatgl.core.renderEngine.display.Window;
import com.gnix.jflatgl.core.Game;
import com.gnix.jflatgl.gui.GUI;
import com.gnix.jflatgl.gui.GUIAction;
import com.gnix.jflatgl.gui.component.GUIComponent;
import com.gnix.jflatgl.gui.component.GUIPanel;
import com.gnix.jflatgl.gui.event.BlockingDelayEvent;
import com.gnix.jflatgl.core.renderEngine.textures.TextureComponent;
import com.gnix.jflatgl.core.util.Logger;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class TabbedPanel extends GUIPanel {

	public enum ListPosition {
		TOP, BOTTOM, LEFT, RIGHT
	}

	// button layout settings
	private ListPosition listPosition;
	private Vector2f anchorPosition;
	private Vector2f buttonSize;
	private float buttonSpacing;
	private float borderPadding;

	// panel content
	private final List<TabButton> tabButtons;
	private final List<TabContentPanel> tabs;
	private TabContentPanel currentTab;

	// panel behavior gets passed to tab buttons
	private GUIAction onCloseAction;
	private GUIAction onOpenAction;
	private GUIAction onMouseOverAction;
	private GUIAction onMouseExitAction;
	private float closeTime;

	public TabbedPanel(String componentID, GUI parent) {
		super(componentID, parent);
		tabButtons = new ArrayList<>();
		tabs = new ArrayList<>();
		listPosition = ListPosition.TOP;
	}

	// CONSTRUCTION

	public GUIComponent onClose(GUIAction action) {
		super.onClose(action);
		onCloseAction = action;
		return this;
	}

	/**
	 * Set the time it takes for a tab to close. When a new
	 * tab is selected to open, a BlockingDelayEvent will
	 * be inserted to the current tab's event queue to ensure
	 * that all components are given this amount of time to
	 * perform their close operations before the new tab
	 * is opened.
	 * @param closeTime the time to delay
	 * @return this
	 */
	public TabbedPanel closeTime(float closeTime) {
		this.closeTime = closeTime;
		return this;
	}

	public TabbedPanel listPosition(ListPosition listPosition) {
		this.listPosition = listPosition;
		return this;
	}

	public TabbedPanel anchorPosition(Vector2f anchorPosition) {
		this.anchorPosition = anchorPosition;
		return this;
	}

	public TabbedPanel buttonSize(Vector2f buttonSize) {
		this.buttonSize = buttonSize;
		return this;
	}

	public TabbedPanel buttonSpacing(float buttonSpacing) {
		this.buttonSpacing = buttonSpacing;
		return this;
	}

	public TabbedPanel borderPadding(float borderPadding) {
		this.borderPadding = borderPadding;
		return this;
	}

	public TabbedPanel buttonOnOpen(GUIAction action) {
		onOpenAction = action;
		return this;
	}

	public TabbedPanel buttonOnClose(GUIAction action) {
		onCloseAction = action;
		return this;
	}

	public TabbedPanel buttonOnMouseOver(GUIAction action) {
		onMouseOverAction = action;
		return this;
	}

	public TabbedPanel buttonOnMouseExit(GUIAction action) {
		onMouseExitAction = action;
		return this;
	}

	public TabContentPanel generateTab(String tabLabel, TextureComponent tabIcon) {
		// generate the tab
		TabContentPanel tab = new TabContentPanel(tabLabel, getParent(), closeTime);
		// make it wait on close
		tab.onClose(() -> tab.queueEvent(new BlockingDelayEvent(closeTime)));
		// generate a button for it
		TabButton button = new TabButton(tab.getComponentID() + "_button", getParent(), tabIcon);
		button.label(tabLabel)
				.location(getPosition())
				.primaryColor(getPrimaryColor())
				.secondaryColor(getSecondaryColor())
				.tertiaryColor(getTertiaryColor())
				.onOpen(getActions()[ON_OPEN])
				.onClose(getActions()[ON_CLOSE])
				.onMouseOver(() -> Game.playSfx("hover2"))
				.onLeftClick(() -> {
					Game.playSfx("select");
					setCurrentTab(tab);
				});
		button.size(buttonSize);
		// add it to the list
		tabButtons.add(button);
		// check if this is the first tab added; if so, it's the one we start on
		if (currentTab == null) currentTab = tab;
		// add the tab to the list of tabs
		tabs.add(tab);
		addComponent(tab);
		// finally, return the result so the caller can add the content they want to the tab
		return tab;
	}

	/**
	 * Get the list of tab buttons. Use this to iterate over
	 * the list and assign actions to button interactions.
	 * @return the list of all TabButtons for the tabs in thiss panel
	 */
	public List<TabButton> getTabButtons() {
		return tabButtons;
	}

	/**
	 * Finalize the layout of the TabbedPanel. This should
	 * be called once the creator is finished adding tabs and
	 * has set the desired list position. The buttons for each
	 * tab will be automatically positioned based on the set
	 * list position, button size, spacing and padding values.
	 */
	public void finalizeLayout() {
		// set anchor position based on list position and button size
		Window gameWindow = DisplayManager.getPrimaryGameWindow();
		anchorPosition = switch (listPosition) {
			case TOP -> new Vector2f(gameWindow.centerX(), buttonSize.y() / 2 + borderPadding);
			case BOTTOM -> new Vector2f(gameWindow.centerX(), gameWindow.getHeight() - (buttonSize.y() / 2 + borderPadding));
			case LEFT -> new Vector2f(buttonSize.x() / 2 + borderPadding, gameWindow.centerY());
			case RIGHT -> new Vector2f(gameWindow.getHeight() - (buttonSize.x() / 2 + borderPadding), gameWindow.centerY());
		};
		// place all the buttons in their proper positions
		float listSize = listPosition == ListPosition.TOP || listPosition == ListPosition.BOTTOM ?
				(buttonSize.x() + buttonSpacing) * (tabButtons.size() - 1) :
				(buttonSize.y() + buttonSpacing) * (tabButtons.size() - 1) ;
		for (int i = 0; i < tabButtons.size(); i++) {
			Vector2f position = new Vector2f(anchorPosition);
			float offset = -listSize / 2;
			switch (listPosition) {
				case TOP, BOTTOM -> {
					offset += i * (buttonSize.x() + buttonSpacing);
					position.add(offset, 0);
				}
				case LEFT, RIGHT -> {
					offset += i * (buttonSize.y() + buttonSpacing);
					position.add(0, offset);
				}
			}
			tabButtons.get(i).location(position, (int) getPosition().z());
		}
	}

	// ACTIONS

	public void setCurrentTab(TabContentPanel tab) {
		if (currentTab != null) {
			if (currentTab.equals(tab)) return; // don't set to same tab
			Logger.log("Setting current tab to " + tab.getComponentID());
			// add a delay to the current tab's close sequence to give its components time to close
			Logger.log("Delaying close complete for " + closeTime + "s");
			// instruct the current tab to set this panel's current tab to the new tab when it is finished
			currentTab.onCloseComplete(() -> {
				this.currentTab = tab;
				Logger.log("tab close complete, current tab now set to " + this.currentTab.getComponentID());
				tab.open();
				tab.unlock();
			});
			currentTab.close();
			currentTab.lock();
		}
	}

	/**
	 * Resets the current tab's close action to not
	 * actually do anything. In the event that the
	 * GUI is closed entirely, this prevents a new
	 * tab being opened during the close sequence.
	 */
	public void resetCloseAction() {
		currentTab.onClose(GUIAction.NOP);
	}

	// RUNNING GUI

	@Override
	public void open() {
		super.open();
		for (TabButton button : tabButtons) {
			button.open();
		}
		currentTab.open();
		for (TabContentPanel tab : tabs) {
			if (tab != currentTab) tab.close();
		}
	}

	@Override
	public void close() {
		Logger.log("Close tab panel called");
		super.close();
		for (TabButton button : tabButtons) {
			button.onClose(onCloseAction);
			button.close();
		}
		currentTab.close();
	}

	@Override
	public void update() {
//		Logger.log("Updating tabbed panel " + getComponentID() + ", active: " + isActive());
		updateEvents();
		for (TabButton button : tabButtons) {
			button.updateEvents();
			if (!button.isActive() || button.isLocked()) continue;
//			Logger.log("Updating tab button " + button.getComponentID());
			button.checkInput();
			button.update();
		}
		for (TabContentPanel tab : tabs) {
			if (!tab.isLocked()) tab.update();
			tab.updateEvents();
		}
//		currentTab.update();
	}

	@Override
	public void doTransform() {
		for (TabButton button : tabButtons) {
			button.doTransform();
		}
		currentTab.doTransform();
	}

	@Override
	public void render() {
		for (TabButton button : tabButtons) {
			button.render();
		}
		currentTab.render();
	}


	// TEST
	@Override
	public void addComponent(GUIComponent component) {
		super.addComponent(component);
		Logger.log("ADDED COMPONENT TO TABBED PANEL " + getComponentID() + "; THERE ARE NOW " + getComponents().size());
	}
}
