package treebolic.glue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Color implements treebolic.glue.iface.Color<Color>, Serializable
{
	private static final long serialVersionUID = 5704334480899935769L;

	public static final Color WHITE = new Color(java.awt.Color.WHITE);

	public static final Color BLACK = new Color(java.awt.Color.BLACK);

	public static final Color RED = new Color(java.awt.Color.RED);

	public static final Color GREEN = new Color(java.awt.Color.GREEN);

	public static final Color BLUE = new Color(java.awt.Color.BLUE);

	public static final Color ORANGE = new Color(java.awt.Color.ORANGE);

	public static final Color YELLOW = new Color(java.awt.Color.YELLOW);

	public static final Color PINK = new Color(java.awt.Color.PINK);

	public static final Color CYAN = new Color(java.awt.Color.CYAN);

	public static final Color MAGENTA = new Color(java.awt.Color.MAGENTA);

	public static final Color GRAY = new Color(java.awt.Color.GRAY);

	public static final Color LIGHT_GRAY = new Color(java.awt.Color.LIGHT_GRAY);

	public static final Color DARK_GRAY = new Color(java.awt.Color.DARK_GRAY);

	transient public java.awt.Color color;

	/**
	 * Constructor from java.awt.color
	 *
	 * @param color
	 *        java.awt.color
	 */
	public Color(final java.awt.Color color)
	{
		this.color = color;
	}

	public Color(final int rgb)
	{
		this.color = new java.awt.Color(rgb);
	}

	public Color()
	{
		this.color = null;
	}

	/**
	 * Constructor
	 *
	 * @param r
	 *        red
	 * @param g
	 *        green
	 * @param b
	 *        blue
	 */
	@Override
	public void set(final int r, final int g, final int b)
	{
		this.color = new java.awt.Color(r, g, b);
	}

	@Override
	public void set(final int rgb)
	{
		this.color = new java.awt.Color(rgb);
	}

	@Override
	public void parse(final String string)
	{
		this.color = java.awt.Color.decode("0x" + string); //$NON-NLS-1$
	}

	// @Override
	// public Color makeBrighter()
	// {
	// if (this.color == null)
	// return null;
	// return new Color(this.color.brighter());
	// }
	//
	// @Override
	// public Color makeDarker()
	// {
	// if (this.color == null)
	// return null;
	// return new Color(this.color.darker());
	// }

	private static final float DARKERFACTOR = 0.85F;

	/*
	 * (non-Javadoc)
	 * @see treebolic.glue.iface.Color#makeBrighter()
	 */
	@Override
	public Color makeBrighter()
	{
		int r = this.color.getRed();
		int g = this.color.getGreen();
		int b = this.color.getBlue();
		final int alpha = this.color.getAlpha();

		final int i = (int) (1.0 / (1.0 - Color.DARKERFACTOR));
		if (r == 0 && g == 0 && b == 0)
			return new Color(new java.awt.Color(i, i, i, alpha));
		if (r > 0 && r < i)
		{
			r = i;
		}
		if (g > 0 && g < i)
		{
			g = i;
		}
		if (b > 0 && b < i)
		{
			b = i;
		}

		return new Color(new java.awt.Color(Math.min((int) (r / Color.DARKERFACTOR), 255), Math.min((int) (g / Color.DARKERFACTOR), 255), Math.min((int) (b / Color.DARKERFACTOR), 255), alpha));
	}

	/*
	 * (non-Javadoc)
	 * @see treebolic.glue.iface.Color#makeDarker()
	 */
	@Override
	public Color makeDarker()
	{
		return new Color(new java.awt.Color(Math.max((int) (this.color.getRed() * Color.DARKERFACTOR), 0), Math.max((int) (this.color.getGreen() * Color.DARKERFACTOR), 0), Math.max((int) (this.color.getBlue() * Color.DARKERFACTOR), 0),
				this.color.getAlpha()));
	}

	@Override
	public int getRGB()
	{
		return this.color.getRGB();
	}

	@Override
	public boolean isNull()
	{
		return this.color == null;
	}

	// O V E R R I D E S E R I A L I Z A T I O N

	private void writeObject(final ObjectOutputStream out) throws IOException
	{
		out.writeObject(this.color.getRGB());
	}

	private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException
	{
		this.color = new java.awt.Color((Integer) in.readObject());
	}
}
