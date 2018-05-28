package structogram2byob.gui.menu;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;


/**
 * Implementation of a menu manager for the Swing GUI.
 */
public class SwingMenuBuilder implements IMenuBuilder
{
    private final JPanel comp;

    /**
     * Constructor.
     */
    public SwingMenuBuilder()
    {
        comp = new JPanel();
        comp.setLayout(new BoxLayout(comp, BoxLayout.LINE_AXIS));
        comp.setBorder(BorderFactory.createCompoundBorder(//
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY), // outside
                BorderFactory.createEmptyBorder(4, 4, 4, 4) // inside
        ));
    }

    @Override
    public void addControl(String label, Runnable action)
    {
        if (comp.getComponentCount() > 0) {
            comp.add(Box.createHorizontalStrut(4));
        }

        JButton btn = new JButton(label);
        btn.addActionListener(e -> action.run());

        comp.add(btn);
    }

    @Override
    public void addSeparator()
    {
        comp.add(Box.createHorizontalStrut(16));
    }

    @Override
    public void addFiller()
    {
        comp.add(Box.createHorizontalGlue());
    }

    @Override
    public JPanel getPanel()
    {
        return comp;
    }
}
