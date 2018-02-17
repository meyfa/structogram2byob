package structogram2byob.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import nsdlib.elements.NSDElement;
import nsdlib.elements.NSDRoot;
import nsdlib.reader.NSDReaderException;
import nsdlib.reader.StructorizerReader;
import scratchlib.project.ScratchProject;
import scratchlib.writer.ScratchWriter;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.gui.managers.GuiFrameManager;
import structogram2byob.parser.nsd.NSDParser;
import structogram2byob.parser.nsd.NSDParserException;
import structogram2byob.program.Program;


/**
 * Class used for instantiating, displaying and controlling the GUI.
 */
public class GuiController
{
    private static JFileChooser importChooser, exportChooser, saveImageChooser;
    private static final StructorizerReader reader = new StructorizerReader();

    private final BlockRegistry blocks;
    private final GuiFrameManager frameMan;

    private final List<NSDRoot> diagrams = new ArrayList<>();
    private ScratchProject project;

    /**
     * @param blocks The block registry used by this instance.
     */
    public GuiController(BlockRegistry blocks)
    {
        this.blocks = blocks;
        this.frameMan = new GuiFrameManager(this);

        if (importChooser == null) {
            importChooser = new JFileChooser();
            importChooser.setFileFilter(
                    new ExtensionFileFilter("Structorizer Files", "nsd"));
            importChooser.setAcceptAllFileFilterUsed(true);
        }

        if (exportChooser == null) {
            exportChooser = new JFileChooser();
            exportChooser.setFileFilter(
                    new ExtensionFileFilter("BYOB Project Files", "ypr"));
            exportChooser.setAcceptAllFileFilterUsed(true);
        }

        if (saveImageChooser == null) {
            saveImageChooser = new JFileChooser();
            saveImageChooser.setFileFilter(new ExtensionFileFilter(
                    "Portable Network Graphics", "png"));
            saveImageChooser.setAcceptAllFileFilterUsed(false);
        }
    }

    /**
     * @return The block registry used by this instance.
     */
    public BlockRegistry getBlockRegistry()
    {
        return blocks;
    }

    /**
     * Displays the frame.
     */
    public void show()
    {
        frameMan.show();
    }

    /**
     * Shows a confirm dialog with the given message and returns the result.
     *
     * @param msg The message to show.
     * @return Whether the dialog was confirmed by the user.
     */
    private boolean confirm(String msg)
    {
        return JOptionPane.showConfirmDialog(frameMan.getFrame(), msg,
                "Confirm action", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }

    /**
     * Tries parsing the current project, initiating appropriate actions in the
     * case of errors.
     */
    private void updateProject()
    {
        Program prog = new Program();

        boolean failed = false;
        for (int i = 0; i < diagrams.size(); ++i) {

            NSDRoot nsd = diagrams.get(i);
            NSDParser parser = new NSDParser(nsd);

            try {
                prog.addUnit(parser.parse());
            } catch (NSDParserException e) {
                NSDElement el = e.getElement();
                if (el != null) {
                    frameMan.getUnitsPanel().markError(i, el);
                }
                failed = true;
            }

        }

        this.project = failed ? null : prog.toScratch(blocks);
    }

    /**
     * Adds the given diagram to the GUI.
     *
     * @param nsd The diagram to add.
     */
    public void add(NSDRoot nsd)
    {
        diagrams.add(nsd);
        frameMan.getUnitsPanel().doAdd(nsd);

        updateProject();
    }

    /**
     * Shows a confirm dialog to the user, asking whether to remove the given
     * diagram. If the action is confirmed, the diagram is removed.
     *
     * @param nsd The diagram to remove.
     */
    public void remove(NSDRoot nsd)
    {
        if (confirm("Do you really want to remove that project unit?")) {

            int index = diagrams.indexOf(nsd);

            diagrams.remove(index);
            frameMan.getUnitsPanel().doRemove(index);

            updateProject();

        }
    }

    /**
     * Shows a confirm dialog to the user, asking whether to remove all
     * diagrams. If the action is confirmed, all diagrams are removed.
     */
    public void removeAll()
    {
        if (confirm("Do you really want to remove all project units?")) {

            diagrams.clear();
            frameMan.getUnitsPanel().doClear();

            updateProject();

        }
    }

    /**
     * Shows a diagram import dialog to the user.
     */
    public void openImportDialog()
    {
        int result = importChooser.showOpenDialog(frameMan.getFrame());
        if (result == JFileChooser.APPROVE_OPTION) {

            File file = importChooser.getSelectedFile();

            try (FileInputStream in = new FileInputStream(file)) {
                add(reader.read(in));
            } catch (NSDReaderException | IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Shows a BYOB project export dialog to the user.
     */
    public void openExportDialog()
    {
        if (project == null) {
            return;
        }

        int result = exportChooser.showSaveDialog(frameMan.getFrame());
        if (result == JFileChooser.APPROVE_OPTION) {

            File file = exportChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".ypr")) {
                file = new File(file.getParentFile(), file.getName() + ".ypr");
            }

            ScratchWriter w = new ScratchWriter(file);
            try {
                w.write(project);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Shows an image save dialog to the user for saving the given image.
     *
     * @param img The image to save.
     */
    public void openImageSaveDialog(BufferedImage img)
    {
        int result = saveImageChooser.showSaveDialog(frameMan.getFrame());
        if (result == JFileChooser.APPROVE_OPTION) {

            File file = saveImageChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".png")) {
                file = new File(file.getParentFile(), file.getName() + ".png");
            }

            try {
                ImageIO.write(img, "PNG", file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
