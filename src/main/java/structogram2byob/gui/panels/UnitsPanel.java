package structogram2byob.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import nsdlib.elements.NSDElement;
import nsdlib.elements.NSDRoot;
import structogram2byob.gui.GuiController;


/**
 * Panel GUI element that hosts all project unit panels.
 */
public class UnitsPanel extends JPanel
{
    private static final long serialVersionUID = -3479015906765171109L;

    private final GuiController controller;

    /**
     * @param controller The controller that operates all of the GUI.
     */
    public UnitsPanel(GuiController controller)
    {
        super(new GridBagLayout());

        this.controller = controller;
    }

    /**
     * Adds a unit panel for the given diagram to this container.
     *
     * @param nsd The diagram to add a panel for.
     */
    public void doAdd(NSDRoot nsd)
    {
        if (getComponentCount() > 0) {
            remove(getComponentCount() - 1);
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        UnitPanel p = new UnitPanel(controller, nsd);
        add(p, gbc);

        gbc.weightx = 1000;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JPanel(), gbc);

        revalidate();
        repaint();
    }

    /**
     * Removes the unit panel at the given index from this container.
     *
     * @param index The index of the panel to remove.
     */
    public void doRemove(int index)
    {
        remove(index);

        revalidate();
        repaint();
    }

    /**
     * Removes all units from this container.
     */
    public void doClear()
    {
        removeAll();

        revalidate();
        repaint();
    }

    /**
     * Marks an error on the diagram with the given index for the given element.
     *
     * @param diagram The diagram/unit index.
     * @param element The element to mark on that diagram.
     */
    public void markError(int diagram, NSDElement element)
    {
        UnitPanel p = (UnitPanel) getComponent(diagram);
        p.markError(element);
    }
}
