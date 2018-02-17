package structogram2byob.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;


/**
 * Filter for {@link JFileChooser} instances that only shows directories and
 * files that have a specific extension.
 */
public class ExtensionFileFilter extends FileFilter
{
    private final String name, extension;

    /**
     * Constructs a new filter.
     *
     * @param name The name for this filter displayed in the chooser dropdown.
     * @param extension The extension files must have to be accepted.
     */
    public ExtensionFileFilter(String name, String extension)
    {
        this.name = name;
        this.extension = extension;
    }

    @Override
    public String getDescription()
    {
        return name + " (*." + extension + ")";
    }

    @Override
    public boolean accept(File f)
    {
        if (f.isDirectory()) {
            return true;
        }

        return f.getName().toLowerCase().endsWith("." + extension);
    }
}
