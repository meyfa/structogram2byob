package structogram2byob.gui.frame;

import structogram2byob.gui.menu.IMenuBuilder;
import structogram2byob.gui.units.IUnitsManager;


public interface IFrameManager
{
    void show();

    IMenuBuilder getMenu();

    IUnitsManager getUnits();
}
