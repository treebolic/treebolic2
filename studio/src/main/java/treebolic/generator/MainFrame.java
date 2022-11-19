/*
 * Copyright (c) 2022. Bernard Bou
 */
package treebolic.generator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import javax.swing.tree.TreeCellRenderer;

import treebolic.IContext;
import treebolic.IWidget;
import treebolic.Widget;
import treebolic.generator.domtree.DomTreeView;
import treebolic.generator.tree.TreeView;

/**
 * Main frame
 *
 * @author Bernard Bou
 */
public class MainFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	/**
	 * Tree icon
	 */
	static Icon treeIcon = new ImageIcon(MainFrame.class.getResource("images/treetab.png")); //$NON-NLS-1$

	/**
	 * Tree icon
	 */
	static Icon treebolicIcon = new ImageIcon(MainFrame.class.getResource("images/treebolictab.png")); //$NON-NLS-1$

	/**
	 * Rendering mode
	 */
	static public boolean hasTreebolicRendering = true;

	/**
	 * Property view
	 */
	private PropertyView propertyView;

	/**
	 * Treebolic widget
	 */
	private IWidget widget;

	/**
	 * Controller
	 */
	private final Controller controller;

	/**
	 * Tree view
	 */
	private TreeView treeView;

	/**
	 * Dom tree view
	 */
	private DomTreeView domTreeView;

	/**
	 * Text view
	 */
	private TextView textView;

	/**
	 * Tabbed pane
	 */
	private JTabbedPane tabbedPane;

	/**
	 * Toolbar panel
	 */
	private JPanel toolbarPanel;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 */
	public MainFrame(final String[] args)
	{
		super();
		this.controller = new Controller(args);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		initialize();

		// pack frame with preferred size data
		pack();

		// show frame
		setVisible(true);

		// open
		final String documentPath = this.controller.getParameters().getProperty("doc"); //$NON-NLS-1$
		if (documentPath != null && !documentPath.isEmpty())
		{
			this.controller.open(documentPath);
		}
	}

	/**
	 * Make mainframe
	 */
	private void initialize()
	{
		this.widget = makeWidget(this.controller);
		this.treeView = makeTreeView(this.controller);
		this.propertyView = makePropertyView(this.controller);
		this.textView = makeTextView();
		this.domTreeView = makeDomTreeView();
		this.toolbarPanel = makeToolbarPanel(this.controller);

		this.treeView.setPreferredSize(new Dimension(400, 0));

		final JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(makeTabbedPane(this.controller), BorderLayout.CENTER);
		contentPane.add(this.toolbarPanel, BorderLayout.EAST);

		setJMenuBar(makeMenubar(this.controller));
		setContentPane(contentPane);
		setTitle(Messages.getString("MainFrame.title")); //$NON-NLS-1$

		// connect controller
		this.controller.connect(this.widget);
		this.controller.connect(this.propertyView);
		this.controller.connect(this.treeView);
		this.controller.connect(this.textView);
		this.controller.connect(this.domTreeView);
		this.controller.connect(this.tabbedPane);
		this.controller.connect(this);
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.Window#processWindowEvent(java.awt.event.WindowEvent)
	 */
	@Override
	protected void processWindowEvent(final WindowEvent event)
	{
		if (event.getID() == WindowEvent.WINDOW_CLOSING)
		{
			this.controller.exit();
		}
		super.processWindowEvent(event);
	}

	// C O M P O N E N T S

	/**
	 * Make the menu bar
	 *
	 * @return javax.swing.JMenuBar
	 */
	private Menubar makeMenubar(final Controller controller)
	{
		final Menubar menubar = new Menubar();
		menubar.setController(controller);
		return menubar;
	}

	/**
	 * Make the toolbar space
	 *
	 * @return javax.swing.JToolBar
	 */
	private JPanel makeToolbarPanel(final Controller controller)
	{
		final JPanel toolbarPanel = new JPanel();
		toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.Y_AXIS));
		toolbarPanel.add(makeToolbar(controller));
		toolbarPanel.add(makeTreeToolbar(controller));
		return toolbarPanel;
	}

	/**
	 * Make the toolbar
	 *
	 * @return javax.swing.JToolBar
	 */
	private Toolbar makeToolbar(final Controller controller)
	{
		final Toolbar toolBar = new Toolbar();
		toolBar.setController(controller);
		return toolBar;
	}

	/**
	 * Make the toolbar
	 *
	 * @return javax.swing.JToolBar
	 */
	private TreeToolbar makeTreeToolbar(final Controller controller)
	{
		final TreeToolbar toolBar = new TreeToolbar();
		toolBar.setController(controller);
		return toolBar;
	}

	/**
	 * Make the tabbed pane
	 *
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane makeTabbedPane(final Controller controller)
	{
		this.tabbedPane = new JTabbedPane();
		//$NON-NLS-1$
		makeTab(this.tabbedPane, Messages.getString("MainFrame.tab_tree"), MainFrame.treeIcon, makeEditor(controller), MainFrame.this.controller::updateTree);
		//$NON-NLS-1$
		makeTab(this.tabbedPane, Messages.getString("MainFrame.tab_view"), MainFrame.treebolicIcon, makeView(controller), MainFrame.this.controller::updateWidget);
		//$NON-NLS-1$
		makeTab(this.tabbedPane, Messages.getString("MainFrame.tab_xmltree"), null, new JScrollPane(this.domTreeView), MainFrame.this.controller::updateDom);
		//$NON-NLS-1$
		makeTab(this.tabbedPane, Messages.getString("MainFrame.tab_xml"), null, new JScrollPane(this.textView), MainFrame.this.controller::updateText);
		this.tabbedPane.addChangeListener(controller);
		this.tabbedPane.addChangeListener(event -> {
			final int selectedTabIndex = MainFrame.this.tabbedPane.getSelectedIndex();
			MainFrame.this.toolbarPanel.getComponent(1).setVisible(selectedTabIndex == 0);
		});
		return this.tabbedPane;
	}

	/**
	 * Make tab
	 *
	 * @param tabbedPane
	 *        tabbed pane
	 * @param title
	 *        title
	 * @param icon
	 *        tab icon
	 * @param component
	 *        component
	 * @param action
	 *        component update runnable
	 */
	private void makeTab(final JTabbedPane tabbedPane, final String title, final Icon icon, final Component component, final Runnable action)
	{
		tabbedPane.addTab(title, icon, component, null);
		this.controller.updateMap.put(component, action);
	}

	/**
	 * Make the html view
	 *
	 * @return javax.swing.JPanel
	 */
	private TreeView makeTreeView(final Controller controller)
	{
		final TreeView treeView = new TreeView();
		treeView.connect(controller);
		treeView.setPreferredSize(new Dimension(300, 500));
		return treeView;
	}

	/**
	 * Make the treebolic view
	 *
	 * @return treebolic view
	 */
	private JPanel makeView(final Controller controller)
	{
		final JPanel view = new JPanel();
		view.setLayout(new BorderLayout());
		view.add((Component) this.widget, BorderLayout.CENTER);
		return view;
	}

	/**
	 * Make the widget
	 *
	 * @return widget
	 */
	private Widget makeWidget(final IContext context)
	{
		return new Widget(context, null);
	}

	/**
	 * Make the text view
	 *
	 * @return text view
	 */
	private TextView makeTextView()
	{
		return new TextView();
	}

	/**
	 * Make the property view
	 *
	 * @return property view
	 */
	private PropertyView makePropertyView(final Controller controller)
	{
		final PropertyView propertyView = new PropertyView();
		propertyView.setImageRepository(controller.makeImageRepositoryURL());
		propertyView.setCellEditorListener(controller);
		return propertyView;
	}

	/**
	 * Make the Dom tree view
	 *
	 * @return Dom tree view
	 */
	private DomTreeView makeDomTreeView()
	{
		final TreeCellRenderer renderer = MainFrame.hasTreebolicRendering ? new treebolic.generator.domtree.treebolic.Renderer() : new treebolic.generator.domtree.Renderer();
		return new DomTreeView(renderer);
	}

	/**
	 * Make the editor
	 *
	 * @return javax.swing.JComponent
	 */
	private JComponent makeEditor(final Controller controller)
	{
		final JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		pane.setDividerSize(8);
		pane.setOneTouchExpandable(true);
		pane.add(this.treeView, JSplitPane.LEFT);
		pane.add(makeSubEditor(controller), JSplitPane.RIGHT);
		return pane;
	}

	/**
	 * Make thesubeditor
	 *
	 * @return javax.swing.JComponent
	 */
	private JComponent makeSubEditor(final Controller controller)
	{
		final JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(this.propertyView, BorderLayout.CENTER);
		panel.add(makeDefaultsPanel(controller), BorderLayout.SOUTH);
		return panel;
	}

	/**
	 * Make the defaults panel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel makeDefaultsPanel(final Controller controller)
	{
		final JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(makeGlobalsButton(controller, Messages.getString("MainFrame.globals"), "top.png", Controller.Code.SELECTTOP)); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(makeGlobalsButton(controller, Messages.getString("MainFrame.tree"), "tree.png", Controller.Code.SELECTTREE)); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(makeGlobalsButton(controller, Messages.getString("MainFrame.nodes"), "nodes.png", Controller.Code.SELECTNODES)); //$NON-NLS-1$ //$NON-NLS-2$
		panel.add(makeGlobalsButton(controller, Messages.getString("MainFrame.edges"), "edges.png", Controller.Code.SELECTEDGES)); //$NON-NLS-1$ //$NON-NLS-2$
		return panel;
	}

	/**
	 * Make the globals button
	 *
	 * @return javax.swing.JButton
	 */
	private JButton makeGlobalsButton(final Controller controller, final String label, final String image, final Controller.Code code)
	{
		final JButton button = new JButton();
		button.setIcon(new ImageIcon(getClass().getResource("images/" + image))); //$NON-NLS-1$
		button.setToolTipText(label);
		button.setBorder(null);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.addActionListener(e -> controller.execute(code, 0));
		return button;
	}
}
