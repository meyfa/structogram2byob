package structogram2byob.gui.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;

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

    /**
     * @param diagram The structogram to be displayed by this panel.
     */
    public StructogramPanel(NSDRoot diagram)
    {
        this.render = diagram.toRenderPart();

        setBackground(Color.WHITE);
        setOpaque(true);

        int bw = 16;
        setBorder(BorderFactory.createEmptyBorder(bw, bw, bw, bw));

        recalculateSize();
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
        p.setBackground(new RenderColor(255, 127, 127));
    }

    private void recalculateSize()
    {
        if (render == null) {
            return;
        }

        Insets insets = getInsets();

        render.layout(renderer.createContext());
        Size s = render.getSize();

        int w = s.width + insets.left + insets.right;
        int h = s.height + insets.top + insets.bottom;
        Dimension dim = new Dimension(w, h);

        setMinimumSize(dim);
        setMaximumSize(dim);
        setPreferredSize(dim);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if (render == null) {
            return;
        }

        Insets insets = getInsets();

        BufferedImage img = renderer.render(render);
        g.drawImage(img, insets.left, insets.top, this);
    }

    /**
     * Draws this panel's diagram to an instance of {@link BufferedImage} and
     * returns it.
     *
     * @return The rendered image.
     */
    public BufferedImage render()
    {
        return renderer.render(render);
    }
}
