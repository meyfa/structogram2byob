package structogram2byob.gui.units;

import javax.swing.JPanel;

import nsdlib.elements.NSDElement;
import nsdlib.elements.NSDRoot;
import structogram2byob.gui.GuiController;


public interface IUnitsManager
{
    JPanel getPanel();

    void addUnit(GuiController controller, NSDRoot unit);

    void removeUnit(int index);

    void removeAllUnits();

    void markError(int index, NSDElement element);
}
