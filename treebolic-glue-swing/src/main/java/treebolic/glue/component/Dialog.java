/**
 *
 */
package treebolic.glue.component;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import treebolic.glue.iface.ActionListener;
import treebolic.glue.iface.component.Converter;

/**
 * WebDialog
 *
 * @author Bernard Bou
 */
public class Dialog extends JDialog implements treebolic.glue.iface.component.Dialog
{
	private static final long serialVersionUID = 3504737418377115665L;

	final private JLabel headerLabel;

	final private JEditorPane contentPane;

	final private JComponent contentComponent;

	final private StyleSheet styleSheet;

	private Converter converter;

	static private final int MAXWIDTH = 300;

	static private final int MAXHEIGHT = 600;

	/**
	 * Constructor
	 */
	public Dialog()
	{
		super();

		setTitle("Treebolic"); //$NON-NLS-1$

		this.headerLabel = new JLabel();
		this.headerLabel.setFont(Constants.FONT_WEB_HEADER);

		this.contentPane = new JEditorPane();
		this.contentPane.setEditable(false);
		this.contentPane.setContentType("text/html"); //$NON-NLS-1$

		// stylesheet
		final HTMLEditorKit kit = new HTMLEditorKit();
		this.contentPane.setEditorKit(kit);
		this.styleSheet = kit.getStyleSheet();

		// The JEditorPane cannot compute its final preferred width and height simultaneously, it has to know one before it can compute the other. On the first
		// pass, the JEditorPane computes its preferred height based on the assumption that its width will be unlimited, so it returns the height of a single
		// line (since the text contains no line breaks.) On the second pass, the width has already been set, and now that it knows the maximum width it can
		// compute how tall it needs to be. So the simplest solution is just to set the width to the maximum
		// whenever you set the text.
		this.contentPane.setSize(Dialog.MAXWIDTH, Integer.MAX_VALUE);

		this.contentComponent = new JScrollPane(this.contentPane, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.contentComponent.setPreferredSize(null);

		final JButton oKButton = new JButton(Messages.getString("WebDialog.ok")); //$NON-NLS-1$
		oKButton.addActionListener(new java.awt.event.ActionListener()
		{
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent e)
			{
				setVisible(false);
			}
		});

		final JPanel commandPanel = new JPanel();
		commandPanel.setLayout(new FlowLayout());
		commandPanel.add(oKButton);

		// assemble
		final GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)//
				.addComponent(this.headerLabel)//
				.addComponent(this.contentComponent, 0, Dialog.MAXWIDTH, Short.MAX_VALUE)//
				.addComponent(commandPanel));
		layout.setVerticalGroup(layout.createSequentialGroup()//
				.addComponent(this.headerLabel)//
				.addComponent(this.contentComponent, 0, GroupLayout.DEFAULT_SIZE, Dialog.MAXHEIGHT)//
				.addComponent(commandPanel));//
	}

	@Override
	public void setHandle(final Object handle)
	{
		//
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.Dialog#setVisible(boolean)
	 */
	@Override
	public void setVisible(final boolean flag)
	{
		if (flag)
		{
			pack();
			center();
		}
		super.setVisible(flag);
	}

	/*
	 * (non-Javadoc)
	 * @see treebolic.glue.iface.component.Dialog#setListener(treebolic.glue.iface.ActionListener)
	 */
	@Override
	public void setListener(final ActionListener actionListener)
	{
		this.contentPane.addHyperlinkListener(new HyperlinkListener()
		{
			@Override
			public void hyperlinkUpdate(final HyperlinkEvent event)
			{
				if (event.getEventType() == EventType.ACTIVATED)
				{
					String target = null;
					final URL url = event.getURL();
					if (url != null)
					{
						target = url.toString();
					}
					else
					{
						target = event.getDescription();
					}
					if (target != null)
					{
						actionListener.onAction(target);
					}
				}
			}
		});
	}

	/**
	 * Center on screen
	 */
	public void center()
	{
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final Dimension componentSize = getSize();
		if (componentSize.height > screenSize.height)
		{
			componentSize.height = screenSize.height;
		}
		if (componentSize.width > screenSize.width)
		{
			componentSize.width = screenSize.width;
		}
		setLocation((screenSize.width - componentSize.width) / 2, (screenSize.height - componentSize.height) / 2);
	}

	/*
	 * (non-Javadoc)
	 * @see treebolic.glue.iface.component.Dialog#display()
	 */
	@Override
	public void display()
	{
		setModal(true);
		setVisible(true);
	}

	/*
	 * (non-Javadoc)
	 * @see treebolic.glue.iface.component.Dialog#set(java.lang.CharSequence, java.lang.CharSequence[])
	 */
	@Override
	public void set(final CharSequence header, final CharSequence... contents)
	{
		this.headerLabel.setText(header.toString());
		if (contents == null || contents.length == 0)
		{
			this.contentComponent.setVisible(false);
			return;
		}
		String content;
		if (this.converter != null)
			content = this.converter.convert(contents);
		else
			content = String.join("\n", contents);
		this.contentPane.setText(content);
		this.contentPane.setCaretPosition(0);
	}

	/*
	 * (non-Javadoc)
	 * @see treebolic.glue.iface.component.Dialog#setConverter(treebolic.glue.iface.component.Converter)
	 */
	@Override
	public void setConverter(final Converter converter0)
	{
		this.converter = converter0;
	}

	/*
	 * (non-Javadoc)
	 * @see treebolic.glue.iface.component.Dialog#setStyle(java.lang.String)
	 */
	@Override
	public void setStyle(final String style)
	{
		final String[] rules = style.split("\n"); //$NON-NLS-1$
		for (final String rule : rules)
		{
			this.styleSheet.addRule(rule);
		}
	}
}
