package structogram2byob.gui.managers;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

import structogram2byob.gui.GuiController;


/**
 * Manages the GUI frame menu panel.
 */
public class GuiMenuManager
{
    private final JPanel comp;

    /**
     * @param controller The controller that operates all of the GUI.
     */
    public GuiMenuManager(GuiController controller)
    {
        comp = new JPanel(new FlowLayout(FlowLayout.LEADING));
        comp.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

        // section 1
        comp.add(createControl("Import Structogram",
                controller::openImportDialog));
        comp.add(createControl("Remove All", controller::removeAll));

        comp.add(createSeparator());

        // section 2
        comp.add(createControl("Export as BYOB Project",
                controller::openExportDialog));
    }

    private JButton createControl(String label, Runnable action)
    {
        JButton btn = new JButton(label);
        btn.addActionListener(e -> action.run());

        return btn;
    }

    private Component createSeparator()
    {
        return Box.createHorizontalStrut(16);
    }

    /**
     * @return The managed panel.
     */
    public JPanel getComponent()
    {
        return comp;
    }
}
