package structogram2byob.gui.units;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import nsdlib.elements.NSDElement;
import nsdlib.elements.NSDRoot;
import nsdlib.rendering.RenderColor;
import nsdlib.rendering.Size;
import nsdlib.rendering.parts.RenderPart;
import nsdlib.rendering.renderer.awt.AwtRenderer;


/**
 * Panel GUI element for displaying an instance of {@link NSDRoot}.
 */
public class StructogramPanel extends JPanel
{
    private static final long serialVersionUID = 8748487968046846164L;

    private static final AwtRenderer renderer = new AwtRenderer();

    private final RenderPart render;
    private final Set<RenderPart> errorMarks = new HashSet<>();

    /**
     * @param diagram The structogram to be displayed by this panel.
     */
    public StructogramPanel(NSDRoot diagram)
    {
        this.render = diagram.toRenderPart();

        setBackground(Color.WHITE);
        setOpaque(true);

        // set size

        final int bw = 16;
        setBorder(BorderFactory.createEmptyBorder(bw, bw, bw, bw));

        render.layout(renderer.createContext());
        Size s = render.getSize();

        Dimension dim = new Dimension(s.width + 2 * bw, s.height + 2 * bw);

        setMinimumSize(dim);
        setMaximumSize(dim);
        setPreferredSize(dim);
    }

    /**
     * Results in the given element being rendered with a red background in
     * future paint calls.
     *
     * @param element The element.
     */
    public void markError(NSDElement element)
    {
        RenderPart p = render.findForSource(element);
        if (p == null) {
            return;
        }
        p.setBackground(new RenderColor(255, 127, 127));
        errorMarks.add(p);
    }

    /**
     * Removes all error marks that were previously set.
     */
    public void clearErrorMarks()
    {
        for (RenderPart p : errorMarks) {
            p.setBackground(null);
        }
        errorMarks.clear();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Insets insets = getInsets();
        int width = getWidth() - insets.left - insets.right;
        int height = getHeight() - insets.top - insets.bottom;

        if (g instanceof Graphics2D) {
            ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        g.drawImage(render(), insets.left, insets.top, width, height, this);
    }

    /**
     * Draws this panel's diagram to an instance of {@link BufferedImage} and
     * returns it.
     *
     * @return The rendered image.
     */
    public BufferedImage render()
    {
        double screenScale = Toolkit.getDefaultToolkit().getScreenResolution() / 96.0;
        double renderScale = Math.max(1, screenScale);

        return renderer.render(render, renderScale);
    }
}
