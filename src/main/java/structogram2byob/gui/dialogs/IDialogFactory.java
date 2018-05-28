package structogram2byob.gui.dialogs;

import java.io.File;

import javax.swing.filechooser.FileFilter;


/**
 * A factory for different dialog types.
 */
public interface IDialogFactory
{
    /**
     * Creates a new dialog for selecting a file save path.
     *
     * @param type Filter for valid file selections.
     * @return A new dialog.
     */
    IDialog<File> createSaveDialog(FileFilter type);

    /**
     * Creates a new dialog for selecting a file open path.
     *
     * @param type Filter for valid file selections.
     * @return A new dialog.
     */
    IDialog<File> createOpenDialog(FileFilter type);

    /**
     * Creates a new dialog for requesting user confirmation using the given
     * message.
     *
     * @param message The message to show inside the dialog.
     * @return A new dialog.
     */
    IDialog<Boolean> createConfirmationDialog(String message);

    /**
     * Creates a new dialog that shows a simple message to the user.
     *
     * @param message The message to show.
     * @return A new dialog.
     */
    IDialog<Void> createMessageDialog(String message);
}
