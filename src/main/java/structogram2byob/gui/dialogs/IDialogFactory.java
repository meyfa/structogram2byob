package structogram2byob.gui.dialogs;

import java.io.File;

import javax.swing.filechooser.FileFilter;


public interface IDialogFactory
{
    IDialog<File> createSaveDialog(FileFilter type);

    IDialog<File> createOpenDialog(FileFilter type);

    IDialog<Boolean> createConfirmationDialog(String message);
}
