package structogram2byob.gui.menu;

import javax.swing.JPanel;

import structogram2byob.gui.frame.IFrameManager;


/**
 * An interface for managing a frame's menu items.
 *
 * @see IFrameManager#getMenu()
 */
public interface IMenuBuilder
{
    /**
     * Adds a button control to this menu.
     *
     * @param label The button's label.
     * @param action The action to run when the button is activated.
     */
    void addControl(String label, Runnable action);

    /**
     * Adds a visual separator after the last control that was added.
     */
    void addSeparator();

    /**
     * Adds a separator that takes up all remaining space, so that components
     * added after it are placed at the right edge of the menu.
     */
    void addFiller();

    /**
     * @return The managed panel, or {@code null} if not using Swing.
     */
    JPanel getPanel();
}
