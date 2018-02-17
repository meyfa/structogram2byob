package structogram2byob.gui.panels;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import nsdlib.elements.NSDElement;
import nsdlib.elements.NSDRoot;
import structogram2byob.gui.GuiController;


/**
 * Panel GUI element for displaying a project unit, including a diagram display
 * and controls.
 */
public class UnitPanel extends JPanel
{
    private static final long serialVersionUID = -1587434284244337436L;

    private final JPanel controls;
    private final StructogramPanel display;

    /**
     * @param controller The controller that operates all of the GUI.
     * @param nsd The structogram this unit is displaying.
     */
    public UnitPanel(GuiController controller, NSDRoot nsd)
    {
        super(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.gridx = gbc.gridy = 0;

        // create dependencies
        controls = new JPanel(new FlowLayout(FlowLayout.LEADING));
        display = new StructogramPanel(nsd);

        // fill controls
        {
            JButton removeBtn = new JButton("Remove");
            removeBtn.addActionListener(e -> controller.remove(nsd));
            controls.add(removeBtn);

            JButton saveImageBtn = new JButton("Save image");
            saveImageBtn.addActionListener(
                    e -> controller.openImageSaveDialog(display.render()));
            controls.add(saveImageBtn);
        }
        // add controls
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(controls, gbc);

        // add display
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        add(display, gbc);

        // add filler
        gbc.gridy++;
        gbc.weighty = 100;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JPanel(), gbc);

        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
    }

    /**
     * Results in the given element being rendered with a red background in
     * future paint calls.
     *
     * @param element The element.
     */
    public void markError(NSDElement element)
    {
        display.markError(element);
    }
}
