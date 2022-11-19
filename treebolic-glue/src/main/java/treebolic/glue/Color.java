/*
 * Copyright (c) 2022. Bernard Bou
 */

package treebolic.glue;

/**
 * Color
 *
 * @author Bernard Bou
 */
public class Color implements treebolic.glue.iface.Color<Color>
{
	/**
	 * White color
	 */
	public static final Color WHITE = null;

	/**
	 * Black color
	 */
	public static final Color BLACK = null;

	/**
	 * Red color
	 */
	public static final Color RED = null;

	/**
	 * Green color
	 */
	public static final Color GREEN = null;

	/**
	 * Blue color
	 */
	public static final Color BLUE = null;

	/**
	 * Orange color
	 */
	public static final Color ORANGE = null;

	/**
	 * Yellow color
	 */
	public static final Color YELLOW = null;

	/**
	 * Pink color
	 */
	public static final Color PINK = null;

	/**
	 * Cyan color
	 */
	public static final Color CYAN = null;

	/**
	 * Magenta color
	 */
	public static final Color MAGENTA = null;

	/**
	 * Gray color
	 */
	public static final Color GRAY = null;

	/**
	 * Light gray color
	 */
	public static final Color LIGHT_GRAY = null;

	/**
	 * Dark gray color
	 */
	public static final Color DARK_GRAY = null;

	/**
	 * Constructor
	 */
	public Color()
	{
		throw new NotImplementedException();
	}

	/**
	 * Constructor
	 *
	 * @param rgb rgb int value
	 */
	public Color(int rgb)
	{
		throw new NotImplementedException();
	}

	@Override
	public void set(final int r, final int g, final int b)
	{
		throw new NotImplementedException();
	}

	@Override
	public void set(final int rgb)
	{
		throw new NotImplementedException();
	}

	@Override
	public void parse(final String string)
	{
		throw new NotImplementedException();
	}

	@Override
	public Color makeBrighter()
	{
		throw new NotImplementedException();
	}

	@Override
	public Color makeDarker()
	{
		throw new NotImplementedException();
	}

	@Override
	public int getRGB()
	{
		throw new NotImplementedException();
	}

	@Override
	public boolean isNull()
	{
		throw new NotImplementedException();
	}
}
