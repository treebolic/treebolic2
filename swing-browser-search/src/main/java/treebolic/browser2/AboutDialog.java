/*
 * Copyright (c) 2023. Bernard Bou
 */
package treebolic.browser2;

import javax.swing.*;

import treebolic.Browser2;
import treebolic.annotations.NonNull;

/**
 * About dialog
 *
 * @author Bernard Bou
 */
public class AboutDialog extends treebolic.commons.AboutDialog
{
	/**
	 * Constructor
	 */
	public AboutDialog()
	{
		super(Messages.getString("AboutDialog.app"), Browser2.getVersion());
	}

	/**
	 * Standalone entry point
	 *
	 * @param args program arguments
	 */
	static public void main(final String[] args)
	{
		@NonNull final AboutDialog dialog = new AboutDialog();
		dialog.setModal(true);
		dialog.setVisible(true);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		System.exit(0);
	}
}
