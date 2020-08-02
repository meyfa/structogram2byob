package structogram2byob.gui;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;


public class ExtensionFileFilterTest
{
    @Test
    public void returnsDescription()
    {
        ExtensionFileFilter obj = new ExtensionFileFilter("Extension", "ext");

        assertEquals("Extension (*.ext)", obj.getDescription());
    }

    @Test
    public void acceptsDirectories()
    {
        ExtensionFileFilter obj = new ExtensionFileFilter("Extension", "ext");

        File currentWorkingDir = new File(System.getProperty("user.dir"));
        assertTrue(obj.accept(currentWorkingDir));
    }

    @Test
    public void acceptsFilesWithExtension()
    {
        ExtensionFileFilter obj = new ExtensionFileFilter("Extension", "ext");

        File correct = new File("file.ext");
        assertTrue(obj.accept(correct));

        File correctCapital = new File("file.EXT");
        assertTrue(obj.accept(correctCapital));

        File incorrect = new File("file.oext");
        assertFalse(obj.accept(incorrect));
    }
}
