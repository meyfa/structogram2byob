package structogram2byob.gui.menu;

import javax.swing.JPanel;

public interface IMenuBuilder
{
    void addControl(String label, Runnable action);

    void addSeparator();

    JPanel getPanel();
}
