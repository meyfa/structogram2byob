package structogram2byob.gui.frame;

import structogram2byob.gui.menu.IMenuBuilder;
import structogram2byob.gui.units.IUnitsManager;


/**
 * An interface for managing a GUI frame.
 */
public interface IFrameManager
{
    /**
     * Makes the frame visible.
     */
    void show();

    /**
     * @return The frame's menu to which items can be added.
     */
    IMenuBuilder getMenu();

    /**
     * @return The frame's program units manager.
     */
    IUnitsManager getUnits();
}
