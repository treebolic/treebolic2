/**
 * Title : Treebolic
 * Description : Treebolic
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 */
package treebolic.glue.component;

import treebolic.glue.ActionListener;
import treebolic.glue.NotImplementedException;

/**
 * Toolbar
 *
 * @author Bernard Bou
 */
public class Toolbar implements Component, treebolic.glue.iface.component.Toolbar<ActionListener>
{
	/**
	 * (Ordered) toolbar
	 *
	 * @return list of buttons
	 */
	static public Button[] toolbar()
	{
		throw new NotImplementedException();
	}

	// C O N S T R U C T O R

	/**
	 * Constructor
	 *
	 * @param handle Handle required for component creation (unused)
	 */
	public Toolbar(final Object handle)
	{
		throw new NotImplementedException();
	}

	@Override
	public void addButton(treebolic.glue.iface.component.Toolbar.Button button, ActionListener listener)
	{
		throw new NotImplementedException();
	}
}
