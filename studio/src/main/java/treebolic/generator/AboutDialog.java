/*
 * Copyright (c) 2022. Bernard Bou
 */
package treebolic.generator;

import javax.swing.WindowConstants;

import treebolic.Generator;

/**
 * About dialog
 *
 * @author Bernard Bou
 */
public class AboutDialog extends treebolic.commons.AboutDialog
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public AboutDialog()
	{
		super(Messages.getString("AboutDialog.title"), Generator.getVersion()); //$NON-NLS-1$
	}

	/**
	 * Standalone entry point
	 *
	 * @param args
	 *        program arguments
	 */
	static public void main(final String[] args)
	{
		final AboutDialog dialog = new AboutDialog();
		dialog.setModal(true);
		dialog.setVisible(true);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		System.exit(0);
	}
}
