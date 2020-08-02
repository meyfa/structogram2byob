package structogram2byob;

import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import structogram2byob.blocks.BlockRegistry;
import structogram2byob.blocks.BlockRegistryReader;
import structogram2byob.blocks.BlockRegistryReaderException;
import structogram2byob.blocks.hats.StartClickedHatBlock;
import structogram2byob.blocks.special.ChangeVariableBlock;
import structogram2byob.blocks.special.ListBlock;
import structogram2byob.blocks.special.ScriptVariablesBlock;
import structogram2byob.blocks.special.SetVariableBlock;
import structogram2byob.blocks.structures.ForeverBlock;
import structogram2byob.blocks.structures.IfBlock;
import structogram2byob.blocks.structures.IfElseBlock;
import structogram2byob.blocks.structures.RepeatBlock;
import structogram2byob.gui.GuiController;
import structogram2byob.gui.dialogs.IDialogFactory;
import structogram2byob.gui.dialogs.SwingDialogFactory;
import structogram2byob.gui.frame.IFrameManager;
import structogram2byob.gui.frame.SwingFrameManager;


/**
 * Application entry point.
 */
public class Main
{
    /**
     * Application entry point.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args)
    {
        try {
            BlockRegistry reg = createRegistry();
            createUI(reg);
        } catch (IOException | BlockRegistryReaderException e) {
            e.printStackTrace();
        }
    }

    private static void createUI(BlockRegistry blocks)
    {
        if (!EventQueue.isDispatchThread()) {
            EventQueue.invokeLater(() -> createUI(blocks));
            return;
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        IDialogFactory dialogs = new SwingDialogFactory();
        IFrameManager frame = new SwingFrameManager();

        GuiController gui = new GuiController(blocks, dialogs, frame);
        gui.show();
    }

    /**
     * Loads the block registry from the packaged resources, adds the special
     * and structural blocks, and then returns that registry.
     *
     * @return The registry containing all blocks.
     *
     * @throws IOException If an I/O error occurs.
     * @throws BlockRegistryReaderException If the resource file contains malformed block descriptions.
     */
    private static BlockRegistry createRegistry() throws IOException, BlockRegistryReaderException
    {
        try (BlockRegistryReader r = new BlockRegistryReader(Main.class.getResourceAsStream("/functions.txt"))) {
            BlockRegistry reg = r.read();

            reg.register(StartClickedHatBlock.instance);

            reg.register(ScriptVariablesBlock.instance);
            reg.register(SetVariableBlock.instance);
            reg.register(ChangeVariableBlock.instance);
            reg.register(ListBlock.instance);

            reg.register(IfBlock.instance);
            reg.register(IfElseBlock.instance);
            reg.register(RepeatBlock.instance);
            reg.register(ForeverBlock.instance);

            return reg;
        }
    }
}
