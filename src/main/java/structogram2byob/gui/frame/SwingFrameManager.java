package structogram2byob.gui.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import structogram2byob.gui.menu.IMenuBuilder;
import structogram2byob.gui.menu.SwingMenuBuilder;
import structogram2byob.gui.units.IUnitsManager;
import structogram2byob.gui.units.SwingUnitsManager;


/**
 * Implementation of a frame manager for the Swing GUI.
 */
public class SwingFrameManager implements IFrameManager
{
    private final SwingUnitsManager units;
    private final SwingMenuBuilder menu;

    private JFrame frame;

    /**
     * Constructor.
     */
    public SwingFrameManager()
    {
        this.units = new SwingUnitsManager();
        this.menu = new SwingMenuBuilder();

        frame = new JFrame("structogram2byob");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(480, 360));
        frame.setPreferredSize(new Dimension(1280, 720));

        frame.setLayout(new BorderLayout());

        frame.add(menu.getPanel(), BorderLayout.NORTH);

        JScrollPane contentScroller = new JScrollPane(units.getPanel());
        contentScroller.getHorizontalScrollBar().setUnitIncrement(16);
        contentScroller.getVerticalScrollBar().setUnitIncrement(16);
        contentScroller.setBorder(null);
        frame.add(contentScroller, BorderLayout.CENTER);
    }

    @Override
    public void show()
    {
        frame.pack();
        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

    @Override
    public IMenuBuilder getMenu()
    {
        return menu;
    }

    @Override
    public IUnitsManager getUnits()
    {
        return units;
    }
}
