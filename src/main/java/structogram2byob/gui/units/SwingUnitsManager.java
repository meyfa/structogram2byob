package structogram2byob.gui.units;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import nsdlib.elements.NSDElement;
import nsdlib.elements.NSDRoot;
import structogram2byob.gui.GuiController;


/**
 * Implementation of a units manager for the Swing GUI.
 */
public class SwingUnitsManager implements IUnitsManager
{
    private final JPanel comp;

    /**
     * Constructor.
     */
    public SwingUnitsManager()
    {
        comp = new JPanel(new GridBagLayout());
    }

    @Override
    public JPanel getPanel()
    {
        return comp;
    }

    @Override
    public void addUnit(GuiController controller, NSDRoot unit)
    {
        if (comp.getComponentCount() > 0) {
            comp.remove(comp.getComponentCount() - 1);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        comp.add(new UnitPanel(controller, unit), gbc);

        gbc.weightx = 1000;
        gbc.fill = GridBagConstraints.BOTH;
        comp.add(new JPanel(), gbc);

        comp.revalidate();
        comp.repaint();
    }

    @Override
    public void removeUnit(int index)
    {
        comp.remove(index);

        comp.revalidate();
        comp.repaint();
    }

    @Override
    public void removeAllUnits()
    {
        comp.removeAll();

        comp.revalidate();
        comp.repaint();
    }

    @Override
    public void markError(int index, NSDElement element)
    {
        UnitPanel p = (UnitPanel) comp.getComponent(index);
        p.markError(element);
    }
}
