/*
 * Copyright (c) 2022. Bernard Bou
 */

package treebolic.glue;

/**
 * Rectangle2D, derived from awt's Rectangle2D
 *
 * @author Bernard Bou
 */
public class Rectangle2D extends java.awt.geom.Rectangle2D.Double implements treebolic.glue.iface.Rectangle2D<Point2D, Rectangle2D>
{
	private static final long serialVersionUID = -138247987760984392L;

	/**
	 * Constructor
	 *
	 * @param x left corner
	 * @param y top corner
	 * @param w width
	 * @param h height
	 */
	public Rectangle2D(final int x, final int y, final int w, final int h)
	{
		super(x, y, w, h);
	}

	/**
	 * Default constructor
	 */
	public Rectangle2D()
	{
		super();
	}

	// public double getX();

	// public double getY();

	// public double getWidth();

	// public double getHeight();

	// public int getMinX();

	// public int getMinY();

	// public double getCenterX();

	// public double getCenterY();

	@Override
	public boolean intersects(final Rectangle2D rect)
	{
		return super.intersects(rect);
	}

	/**
	 * Outcode: bottom
	 */
	public static final int OUT_BOTTOM = 1;

	/**
	 * Outcode: left
	 */
	public static final int OUT_LEFT = 2;

	/**
	 * Outcode: right
	 */
	public static final int OUT_RIGHT = 4;

	/**
	 * Outcode: top
	 */
	public static final int OUT_TOP = 8;

	@Override
	public int outcode(final Point2D point)
	{
		final int code = super.outcode(point);
		int result = 0;

		if ((code & java.awt.geom.Rectangle2D.OUT_TOP) != 0)
		{
			result |= Rectangle2D.OUT_TOP;
		}
		if ((code & java.awt.geom.Rectangle2D.OUT_BOTTOM) != 0)
		{
			result |= Rectangle2D.OUT_BOTTOM;
		}
		if ((code & java.awt.geom.Rectangle2D.OUT_LEFT) != 0)
		{
			result |= Rectangle2D.OUT_LEFT;
		}
		if ((code & java.awt.geom.Rectangle2D.OUT_RIGHT) != 0)
		{
			result |= Rectangle2D.OUT_RIGHT;
		}

		return result;
	}
}
