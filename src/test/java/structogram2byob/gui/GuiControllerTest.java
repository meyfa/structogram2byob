package structogram2byob.gui;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import nsdlib.elements.NSDElement;
import nsdlib.elements.NSDInstruction;
import nsdlib.elements.NSDRoot;
import org.junit.jupiter.api.Test;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.gui.dialogs.IDialog;
import structogram2byob.gui.dialogs.IDialogFactory;
import structogram2byob.gui.frame.IFrameManager;
import structogram2byob.gui.menu.IMenuBuilder;
import structogram2byob.gui.units.IUnitsManager;

import static org.junit.jupiter.api.Assertions.*;


public class GuiControllerTest
{
    private static class MockDialogFactory implements IDialogFactory
    {
        private boolean confirmationResult = false;
        private boolean confirmationCalled = false;

        private File openResult;
        private boolean openCalled;

        private File saveResult;
        private boolean saveCalled;

        private boolean messageCalled;
        private String messageText;

        @Override
        public IDialog<File> createSaveDialog(FileFilter type)
        {
            return () -> {
                saveCalled = true;
                return saveResult;
            };
        }

        @Override
        public IDialog<File> createOpenDialog(FileFilter type)
        {
            return () -> {
                openCalled = true;
                return openResult;
            };
        }

        @Override
        public IDialog<Boolean> createConfirmationDialog(String message)
        {
            return () -> {
                confirmationCalled = true;
                return confirmationResult;
            };
        }

        @Override
        public IDialog<Void> createMessageDialog(String message)
        {
            return () -> {
                messageCalled = true;
                messageText = message;
                return null;
            };
        }
    }

    private static class MockMenuBuilder implements IMenuBuilder
    {
        @Override
        public void addControl(String label, Runnable action)
        {
        }

        @Override
        public void addSeparator()
        {
        }

        @Override
        public void addFiller()
        {
        }

        @Override
        public JPanel getPanel()
        {
            return null;
        }
    }

    private static class MockUnitsManager implements IUnitsManager
    {
        private boolean addCalled = false;
        private GuiController addWithController;
        private NSDRoot addWithUnit;

        private boolean removeCalled = false;
        private int removeWithIndex;

        private boolean removeAllCalled = false;

        private boolean markErrorCalled = false;
        private NSDElement markErrorWithElement;

        @Override
        public JPanel getPanel()
        {
            return null;
        }

        @Override
        public void addUnit(GuiController controller, NSDRoot unit)
        {
            addCalled = true;
            addWithController = controller;
            addWithUnit = unit;
        }

        @Override
        public void removeUnit(int index)
        {
            removeCalled = true;
            removeWithIndex = index;
        }

        @Override
        public void removeAllUnits()
        {
            removeAllCalled = true;
        }

        @Override
        public void markError(NSDElement element)
        {
            markErrorCalled = true;
            markErrorWithElement = element;
        }

        @Override
        public void clearErrorMarks()
        {
        }
    }

    private static class MockFrameManager implements IFrameManager
    {
        private final MockUnitsManager units = new MockUnitsManager();
        private final MockMenuBuilder menu = new MockMenuBuilder();

        private boolean shown = false;

        @Override
        public void show()
        {
            shown = true;
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

    private static File getTempFile(String suffix)
    {
        File dir = new File("./target/test-output");
        dir.mkdirs();
        dir.deleteOnExit();

        File file = new File(dir, "t" + System.currentTimeMillis() + suffix);
        file.deleteOnExit();

        return file;
    }

    @Test
    public void showsFrame()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        assertFalse(frame.shown);

        obj.show();
        assertTrue(frame.shown);
    }

    @Test
    public void addsUnitToFrame()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        NSDRoot unit = new NSDRoot("COMMAND do something");
        obj.add(unit);

        assertTrue(frame.units.addCalled);
        assertSame(obj, frame.units.addWithController);
        assertSame(unit, frame.units.addWithUnit);
    }

    @Test
    public void marksLexerErrors()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        NSDRoot unit0 = new NSDRoot("COMMAND foo");
        obj.add(unit0);
        NSDRoot unit1 = new NSDRoot("COMMAND bar");
        NSDInstruction faulty = new NSDInstruction("lexer \"error");
        unit1.addChild(faulty);
        obj.add(unit1);

        assertTrue(frame.units.markErrorCalled);
        assertSame(faulty, frame.units.markErrorWithElement);
    }

    @Test
    public void marksAstParserErrors()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        NSDRoot unit0 = new NSDRoot("COMMAND foo");
        obj.add(unit0);
        NSDRoot unit1 = new NSDRoot("COMMAND bar");
        NSDInstruction faulty = new NSDInstruction("parse ( error ((");
        unit1.addChild(faulty);
        obj.add(unit1);

        assertTrue(frame.units.markErrorCalled);
        assertSame(faulty, frame.units.markErrorWithElement);
    }

    @Test
    public void marksUnknownBlocks()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        NSDRoot unit0 = new NSDRoot("COMMAND foo");
        obj.add(unit0);
        NSDRoot unit1 = new NSDRoot("COMMAND bar");
        NSDInstruction faulty = new NSDInstruction("missing instruction");
        unit1.addChild(faulty);
        obj.add(unit1);

        assertTrue(frame.units.markErrorCalled);
        assertSame(faulty, frame.units.markErrorWithElement);
    }

    @Test
    public void marksDuplicateDefinitions()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        NSDRoot unit0 = new NSDRoot("COMMAND foo (a) (b)");
        obj.add(unit0);
        NSDRoot unit1 = new NSDRoot("COMMAND foo (a) (b)");
        obj.add(unit1);

        assertTrue(frame.units.markErrorCalled);
        assertTrue(frame.units.markErrorWithElement == unit0 || frame.units.markErrorWithElement == unit1);
    }

    @Test
    public void doesNotRemoveUnitWithoutConfirmation()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        NSDRoot unit = new NSDRoot("COMMAND do something");
        obj.add(unit);

        dialogs.confirmationResult = false;
        obj.remove(unit);

        assertTrue(dialogs.confirmationCalled);
        assertFalse(frame.units.removeCalled);
    }

    @Test
    public void removesUnitFromFrameAfterConfirmation()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        NSDRoot unit0 = new NSDRoot("COMMAND foo");
        obj.add(unit0);
        NSDRoot unit1 = new NSDRoot("COMMAND bar");
        obj.add(unit1);

        dialogs.confirmationResult = true;
        obj.remove(unit1);

        assertTrue(dialogs.confirmationCalled);
        assertTrue(frame.units.removeCalled);
        assertEquals(1, frame.units.removeWithIndex);
    }

    @Test
    public void doesNotRemoveAllWithoutConfirmation()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        NSDRoot unit = new NSDRoot("COMMAND do something");
        obj.add(unit);

        dialogs.confirmationResult = false;
        obj.removeAll();

        assertTrue(dialogs.confirmationCalled);
        assertFalse(frame.units.removeAllCalled);
    }

    @Test
    public void removesAllFromFrameAfterConfirmation()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        NSDRoot unit = new NSDRoot("COMMAND do something");
        obj.add(unit);

        dialogs.confirmationResult = true;
        obj.removeAll();

        assertTrue(dialogs.confirmationCalled);
        assertTrue(frame.units.removeAllCalled);
    }

    @Test
    public void showsImportDialog()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        dialogs.openResult = null;
        obj.openImportDialog();

        assertTrue(dialogs.openCalled);
    }

    @Test
    public void importsUnits()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        dialogs.openResult = new File("./src/test/resources/testunit.nsd");
        obj.openImportDialog();

        assertTrue(frame.units.addCalled);
    }

    @Test
    public void showsExportDialog()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        dialogs.saveResult = null;
        obj.openExportDialog();

        assertTrue(dialogs.saveCalled);
    }

    @Test
    public void doesNotExportFaultyProjects()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        NSDRoot unit = new NSDRoot("COMMAND bar");
        NSDInstruction faulty = new NSDInstruction("parse ( error ((");
        unit.addChild(faulty);
        obj.add(unit);

        dialogs.saveResult = null;
        obj.openExportDialog();

        assertFalse(dialogs.saveCalled);
    }

    @Test
    public void savesExportedProjects()
    {
        File out = getTempFile(".ypr");

        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        dialogs.saveResult = out;
        obj.openExportDialog();

        assertTrue(out.isFile());
    }

    @Test
    public void showsImageSaveDialog()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        dialogs.saveResult = null;
        obj.openImageSaveDialog(
                new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));

        assertTrue(dialogs.saveCalled);
    }

    @Test
    public void savesImages()
    {
        File out = getTempFile(".png");

        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        dialogs.saveResult = out;
        obj.openImageSaveDialog(
                new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB));

        assertTrue(out.isFile());
    }

    @Test
    public void showsAboutDialog()
    {
        BlockRegistry reg = new BlockRegistry();
        MockDialogFactory dialogs = new MockDialogFactory();
        MockFrameManager frame = new MockFrameManager();

        GuiController obj = new GuiController(reg, dialogs, frame);

        obj.openAboutDialog();

        assertTrue(dialogs.messageCalled);
        assertTrue(dialogs.messageText.contains("Copyright"));
    }
}
