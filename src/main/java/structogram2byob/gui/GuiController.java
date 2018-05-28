package structogram2byob.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;

import nsdlib.elements.NSDElement;
import nsdlib.elements.NSDRoot;
import nsdlib.reader.NSDReaderException;
import nsdlib.reader.StructorizerReader;
import scratchlib.project.ScratchProject;
import scratchlib.writer.ScratchWriter;
import structogram2byob.blocks.BlockRegistry;
import structogram2byob.gui.dialogs.IDialog;
import structogram2byob.gui.dialogs.IDialogFactory;
import structogram2byob.gui.frame.IFrameManager;
import structogram2byob.gui.menu.IMenuBuilder;
import structogram2byob.parser.nsd.NSDParser;
import structogram2byob.parser.nsd.NSDParserException;
import structogram2byob.program.Program;
import structogram2byob.program.ScratchConversionException;


/**
 * Class used for instantiating, displaying and controlling the GUI.
 */
public class GuiController
{
    private static final StructorizerReader reader = new StructorizerReader();

    private final BlockRegistry blocks;

    private final IDialogFactory dialogFactory;
    private final IDialog<File> importChooser, exportChooser, saveImageChooser;

    private final IFrameManager frameManager;

    private final List<NSDRoot> diagrams = new ArrayList<>();
    private ScratchProject project;

    /**
     * @param blocks The block registry used by this instance.
     * @param dialogFactory The factory to be used for constructing dialogs.
     * @param frameManager The frame manager to be used.
     */
    public GuiController(BlockRegistry blocks, IDialogFactory dialogFactory,
            IFrameManager frameManager)
    {
        this.blocks = blocks;

        this.dialogFactory = dialogFactory;

        importChooser = dialogFactory.createOpenDialog(
                new ExtensionFileFilter("Structorizer Files", "nsd"));
        exportChooser = dialogFactory.createSaveDialog(
                new ExtensionFileFilter("BYOB Project Files", "ypr"));
        saveImageChooser = dialogFactory.createSaveDialog(
                new ExtensionFileFilter("Portable Network Graphics", "png"));

        this.frameManager = frameManager;

        IMenuBuilder menu = frameManager.getMenu();
        menu.addControl("Import Structogram", this::openImportDialog);
        menu.addControl("Remove All", this::removeAll);
        menu.addSeparator();
        menu.addControl("Export as BYOB Project", this::openExportDialog);
        menu.addFiller();
        menu.addControl("About", this::openAboutDialog);

        updateProject();
    }

    /**
     * Displays the frame.
     */
    public void show()
    {
        frameManager.show();
    }

    /**
     * Shows a confirm dialog with the given message and returns the result.
     *
     * @param msg The message to show.
     * @return Whether the dialog was confirmed by the user.
     */
    private boolean confirm(String msg)
    {
        return dialogFactory.createConfirmationDialog(msg).show();
    }

    /**
     * Tries parsing the current project, initiating appropriate actions in the
     * case of errors.
     */
    private void updateProject()
    {
        this.project = null;
        frameManager.getUnits().clearErrorMarks();

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
                    frameManager.getUnits().markError(el);
                }
                failed = true;
            }

        }

        if (failed) {
            return;
        }

        try {
            this.project = prog.toScratch(blocks);
        } catch (ScratchConversionException e) {
            NSDElement el = e.getElement();
            if (el != null) {
                frameManager.getUnits().markError(el);
            }
        }
    }

    /**
     * Adds the given diagram to the GUI.
     *
     * @param nsd The diagram to add.
     */
    public void add(NSDRoot nsd)
    {
        diagrams.add(nsd);
        frameManager.getUnits().addUnit(this, nsd);

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
            frameManager.getUnits().removeUnit(index);

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
            frameManager.getUnits().removeAllUnits();

            updateProject();

        }
    }

    /**
     * Shows a diagram import dialog to the user.
     */
    public void openImportDialog()
    {
        File file = importChooser.show();
        if (file == null) {
            return;
        }

        try (FileInputStream in = new FileInputStream(file)) {
            add(reader.read(in));
        } catch (NSDReaderException | IOException e) {
            e.printStackTrace();
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

        File file = exportChooser.show();
        if (file == null) {
            return;
        }

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

    /**
     * Shows an image save dialog to the user for saving the given image.
     *
     * @param img The image to save.
     */
    public void openImageSaveDialog(BufferedImage img)
    {
        File file = saveImageChooser.show();
        if (file == null) {
            return;
        }

        if (!file.getName().toLowerCase().endsWith(".png")) {
            file = new File(file.getParentFile(), file.getName() + ".png");
        }

        try {
            ImageIO.write(img, "PNG", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows a dialog with explanatory and licensing information.
     */
    public void openAboutDialog()
    {
        StringBuilder sb = new StringBuilder();

        try (Scanner sc = new Scanner(
                getClass().getResourceAsStream("/about.txt"))) {
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
                sb.append('\n');
            }
        }

        dialogFactory.createMessageDialog(sb.toString()).show();
    }
}
