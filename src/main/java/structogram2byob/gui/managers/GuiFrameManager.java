package structogram2byob.gui.managers;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import structogram2byob.gui.GuiController;
import structogram2byob.gui.panels.UnitsPanel;


/**
 * Manages the actual GUI frame component.
 */
public class GuiFrameManager
{
    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private final UnitsPanel unitsPanel;
    private final GuiMenuManager menu;

    private JFrame frame;

    /**
     * @param controller The controller that operates all of the GUI.
     */
    public GuiFrameManager(GuiController controller)
    {
        this.unitsPanel = new UnitsPanel(controller);
        this.menu = new GuiMenuManager(controller);

        createUI();
    }

    private void createUI()
    {
        frame = new JFrame("structogram2byob");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(480, 360));
        frame.setPreferredSize(new Dimension(1280, 720));

        frame.setLayout(new BorderLayout());

        frame.add(menu.getComponent(), BorderLayout.NORTH);

        JScrollPane contentScroller = new JScrollPane(unitsPanel);
        contentScroller.getHorizontalScrollBar().setUnitIncrement(16);
        contentScroller.getVerticalScrollBar().setUnitIncrement(16);
        contentScroller.setBorder(null);
        frame.add(contentScroller, BorderLayout.CENTER);
    }

    /**
     * @return The managed frame.
     */
    public JFrame getFrame()
    {
        return frame;
    }

    /**
     * @return The panel displaying the project units.
     */
    public UnitsPanel getUnitsPanel()
    {
        return unitsPanel;
    }

    /**
     * Makes the managed frame visible.
     */
    public void show()
    {
        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }
}
