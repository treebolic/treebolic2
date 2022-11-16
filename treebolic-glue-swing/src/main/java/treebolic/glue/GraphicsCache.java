/**
 *
 */
package treebolic.glue;

import treebolic.glue.component.Component;

public class GraphicsCache implements treebolic.glue.iface.GraphicsCache<Graphics>
{
	private final java.awt.Image image;

	public GraphicsCache(final Component component, final Graphics graphics, final int width, final int height)
	{
		final java.awt.Component awtComponent = (java.awt.Component) component;
		this.image = awtComponent.createImage(width, height);
	}

	@Override
	public Graphics getGraphics()
	{
		return new Graphics(this.image.getGraphics());
	}

	@Override
	public void put(final Graphics graphics)
	{
		graphics.g.drawImage(this.image, 0, 0, null);
	}
}
