package structogram2byob.gui.menu;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;


public class SwingMenuBuilder implements IMenuBuilder
{
    private final JPanel comp;

    public SwingMenuBuilder()
    {
        comp = new JPanel(new FlowLayout(FlowLayout.LEADING));
        comp.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
    }

    @Override
    public void addControl(String label, Runnable action)
    {
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
    public JPanel getPanel()
    {
        return comp;
    }
}
