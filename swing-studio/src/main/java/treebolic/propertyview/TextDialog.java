/*
 * Copyright (c) 2022. Bernard Bou
 */
package treebolic.propertyview;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import treebolic.commons.Utils;

/**
 * Text dialog
 *
 * @author Bernard Bou
 */
public class TextDialog extends JDialog
{
	private static final long serialVersionUID = 1L;

	/**
	 * Ok result
	 */
	private boolean ok;

	// C O M P O N E N T S

	/**
	 * Action on ok
	 */
	private final ActionListener okAction;

	/**
	 * Action on cancel
	 */
	private final ActionListener cancelAction;

	/**
	 * Text area
	 */
	private JTextArea textArea;

	/**
	 * Constructor
	 *
	 * @param okAction     ok action
	 * @param cancelAction cancel action
	 */
	public TextDialog(final ActionListener okAction, final ActionListener cancelAction)
	{
		super();
		this.okAction = okAction;
		this.cancelAction = cancelAction;

		initialize();
	}

	/**
	 * Initialize
	 */
	protected void initialize()
	{
		setTitle(Messages.getString("TextDialog.title"));
		setResizable(true);

		// text
		this.textArea = new JTextArea();
		this.textArea.setLineWrap(true);
		this.textArea.setToolTipText(Messages.getString("TextDialog.tooltip"));
		final JScrollPane scrollPane = new JScrollPane(this.textArea);
		scrollPane.setPreferredSize(new Dimension(300, 100));

		// buttons
		final JButton oKButton = new JButton(Messages.getString("TextDialog.ok"));
		final JButton cancelButton = new JButton(Messages.getString("TextDialog.cancel"));

		final JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(cancelButton);
		buttonPanel.add(oKButton);

		// event handling
		oKButton.addActionListener(event -> {
			TextDialog.this.ok = true;
			TextDialog.this.okAction.actionPerformed(event);
			setVisible(false);
		});
		cancelButton.addActionListener(event -> {
			TextDialog.this.cancelAction.actionPerformed(event);
			setVisible(false);
		});

		// assemble
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(buttonPanel, BorderLayout.SOUTH);

		setContentPane(panel);
	}

	/**
	 * Set text
	 *
	 * @param text text
	 */
	public void setText(final String text)
	{
		this.textArea.setText(text);
		this.textArea.setCaretPosition(0);
	}

	/**
	 * Get text
	 *
	 * @return text
	 */
	public String getText()
	{
		if (this.ok)
		{
			return this.textArea.getText();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.Component#setVisible(boolean)
	 */
	@Override
	public void setVisible(final boolean flag)
	{
		if (flag)
		{
			this.ok = false;

			pack();
			Utils.center(this);
		}
		super.setVisible(flag);
	}
}
