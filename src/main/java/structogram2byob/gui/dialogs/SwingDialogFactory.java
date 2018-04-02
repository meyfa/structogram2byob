package structogram2byob.gui.dialogs;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;


/**
 * Implementation of a dialog factory for use in a Swing GUI.
 */
public class SwingDialogFactory implements IDialogFactory
{
    @Override
    public IDialog<File> createSaveDialog(FileFilter type)
    {
        JFileChooser chooser = new JFileChooser();

        chooser.setFileFilter(type);
        chooser.setAcceptAllFileFilterUsed(true);

        return () -> chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION
                ? chooser.getSelectedFile()
                : null;
    }

    @Override
    public IDialog<File> createOpenDialog(FileFilter type)
    {
        JFileChooser chooser = new JFileChooser();

        chooser.setFileFilter(type);
        chooser.setAcceptAllFileFilterUsed(true);

        return () -> chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION
                ? chooser.getSelectedFile()
                : null;
    }

    @Override
    public IDialog<Boolean> createConfirmationDialog(String message)
    {
        return () -> JOptionPane.showConfirmDialog(null, message,
                "Confirm action", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }
}
