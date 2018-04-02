package structogram2byob.gui.units;

import javax.swing.JPanel;

import nsdlib.elements.NSDElement;
import nsdlib.elements.NSDRoot;
import structogram2byob.gui.GuiController;


/**
 * An interface for managing the program units shown in the GUI.
 */
public interface IUnitsManager
{
    /**
     * @return The managed panel, or {@code null} if not using Swing.
     */
    JPanel getPanel();

    /**
     * Adds a program unit to be displayed.
     *
     * @param controller The GUI controller for this unit.
     * @param unit The unit to add.
     */
    void addUnit(GuiController controller, NSDRoot unit);

    /**
     * Removes the unit with the given index from display.
     *
     * @param index The unit's index (starting at 0 for the first unit added).
     */
    void removeUnit(int index);

    /**
     * Removes all units from display.
     */
    void removeAllUnits();

    /**
     * Marks the given element as containing an error.
     *
     * @param index The index of the unit containing the given element.
     * @param element The element that is faulty.
     */
    void markError(int index, NSDElement element);
}
