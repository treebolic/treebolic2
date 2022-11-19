/*
 * Copyright (c) 2022. Bernard Bou
 */
package treebolic.generator;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * Menu bar
 *
 * @author Bernard Bou
 */
public class Menubar extends JMenuBar
{
	private static final long serialVersionUID = 1L;

	// D A T A

	/**
	 * Controller (command sink)
	 */
	private Controller controller;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 */
	public Menubar()
	{
		initialize();
	}

	/**
	 * Initialize
	 */
	private void initialize()
	{
		final JMenuItem openMenuItem = makeItem(Messages.getString("Menubar.open"), "open.png", Controller.Code.OPEN, KeyStroke.getKeyStroke('O', InputEvent.CTRL_DOWN_MASK)); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem openUrlMenuItem = makeItem(Messages.getString("Menubar.open_url"), "openurl.png", Controller.Code.OPENURL, null); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem importXslMenuItem = makeItem(Messages.getString("Menubar.open_xsl"), "import.png", Controller.Code.IMPORTXSL, null); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem importProviderMenuItem = makeItem(Messages.getString("Menubar.open_provider"), "importprovider.png", Controller.Code.IMPORTPROVIDER, null); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem newMenuItem = makeItem(Messages.getString("Menubar.new"), "newdoc.png", Controller.Code.NEW, KeyStroke.getKeyStroke('N', InputEvent.CTRL_DOWN_MASK)); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem saveMenuItem = makeItem(Messages.getString("Menubar.save"), "save.png", Controller.Code.SAVE, KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK)); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem saveAsMenuItem = makeItem(Messages.getString("Menubar.saveas"), "saveas.png", Controller.Code.SAVEAS, null); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem exportMenuItem = makeItem(Messages.getString("Menubar.export"), "export.png", Controller.Code.EXPORT, null); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem exportSettingsMenuItem = makeItem(Messages.getString("Menubar.export_rendering"), null, Controller.Code.EXPORTSETTINGS, null); //$NON-NLS-1$
		final JMenuItem makeSiteMenuItem = makeItem(Messages.getString("Menubar.make_site"), "sitemake.png", Controller.Code.MAKESITE, null); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem runSiteMenuItem = makeItem(Messages.getString("Menubar.run_site"), "siterun.png", Controller.Code.RUNSITE, null); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem zipMenuItem = makeItem(Messages.getString("Menubar.zip"), "zip.png", Controller.Code.ZIP, null); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem unZipMenuItem = makeItem(Messages.getString("Menubar.unzip"), "unzip.png", Controller.Code.UNZIP, null); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem serializeMenuItem = makeItem(Messages.getString("Menubar.serialize"), "serialize.png", Controller.Code.SERIALIZE, null); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem deserializeMenuItem = makeItem(Messages.getString("Menubar.deserialize"), "deserialize.png", Controller.Code.DESERIALIZE, null); //$NON-NLS-1$ //$NON-NLS-2$

		final JMenuItem newElementMenuItem = makeItem(Messages.getString("Menubar.new_item"), "new.png", Controller.Code.NEWELEMENT, KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0)); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem removeElementMenuItem = makeItem(Messages.getString("Menubar.remove_item"), "delete.png", Controller.Code.REMOVEELEMENT, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0)); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem normalizeIdsMenuItem = makeItem(Messages.getString("Menubar.normalize_ids"), null, Controller.Code.NORMALIZEIDS, null); //$NON-NLS-1$
		final JMenuItem listImagesMenuItem = makeItem(Messages.getString("Menubar.list_images"), null, Controller.Code.LISTIMAGES, null); //$NON-NLS-1$
		final JMenuItem listLinksMenuItem = makeItem(Messages.getString("Menubar.list_links"), null, Controller.Code.LISTLINKS, null); //$NON-NLS-1$
		final JMenuItem listMountsMenuItem = makeItem(Messages.getString("Menubar.list_mounts"), null, Controller.Code.LISTMOUNTS, null); //$NON-NLS-1$
		final JMenuItem listIdsMenuItem = makeItem(Messages.getString("Menubar.list_ids"), null, Controller.Code.LISTIDS, null); //$NON-NLS-1$

		final JMenuItem settingsMenuItem = makeItem(Messages.getString("Menubar.settings"), "settings.png", Controller.Code.SETTINGS, null); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem baseMenuItem = makeItem(Messages.getString("Menubar.site"), "site.png", Controller.Code.SETTINGSBASE, null); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem urlMenuItem = makeItem(Messages.getString("Menubar.url"), "url.png", Controller.Code.SETTINGSURL, null); //$NON-NLS-1$ //$NON-NLS-2$
		final JCheckBoxMenuItem treebolicRendererMenuItem = makeCheckboxItem(Messages.getString("Menubar.renderer"), null, Controller.Code.OPTIONTREEBOLICRENDERER, null, MainFrame.hasTreebolicRendering); //$NON-NLS-1$
		final JCheckBoxMenuItem validateMenuItem = makeCheckboxItem(Messages.getString("Menubar.validate"), null, Controller.Code.OPTIONVALIDATEXML, null, Controller.validate); //$NON-NLS-1$
		final JCheckBoxMenuItem focusParentMenuItem = makeCheckboxItem(Messages.getString("Menubar.focus"), null, Controller.Code.OPTIONFOCUSPARENT, null, treebolic.generator.tree.Tree.focusParent); //$NON-NLS-1$

		final JMenuItem dtdMenuItem = makeItem(Messages.getString("Menubar.dtd"), "dtd.png", Controller.Code.DTD, null); //$NON-NLS-1$ //$NON-NLS-2$

		final JMenuItem aboutMenuItem = makeItem(Messages.getString("Menubar.about"), "about.png", Controller.Code.ABOUT, null); //$NON-NLS-1$ //$NON-NLS-2$
		final JMenuItem helpMenuItem = makeItem(Messages.getString("Menubar.help"), "help.png", Controller.Code.HELP, null); //$NON-NLS-1$ //$NON-NLS-2$

		final JMenu filesMenu = new JMenu();
		filesMenu.setText(Messages.getString("Menubar.menu_files")); //$NON-NLS-1$
		filesMenu.add(newMenuItem);
		filesMenu.add(openMenuItem);
		filesMenu.add(openUrlMenuItem);
		filesMenu.add(importXslMenuItem);
		filesMenu.add(importProviderMenuItem);
		filesMenu.addSeparator();
		filesMenu.add(saveMenuItem);
		filesMenu.add(saveAsMenuItem);
		filesMenu.add(exportMenuItem);
		filesMenu.add(exportSettingsMenuItem);
		filesMenu.addSeparator();
		filesMenu.add(zipMenuItem);
		filesMenu.add(unZipMenuItem);
		filesMenu.addSeparator();
		filesMenu.add(serializeMenuItem);
		filesMenu.add(deserializeMenuItem);

		final JMenu elementsMenu = new JMenu();
		elementsMenu.setText(Messages.getString("Menubar.menu_items")); //$NON-NLS-1$
		elementsMenu.add(newElementMenuItem);
		elementsMenu.add(removeElementMenuItem);
		elementsMenu.addSeparator();
		elementsMenu.add(normalizeIdsMenuItem);
		elementsMenu.addSeparator();
		elementsMenu.add(listImagesMenuItem);
		elementsMenu.add(listLinksMenuItem);
		elementsMenu.add(listMountsMenuItem);
		elementsMenu.add(listIdsMenuItem);

		final JMenu optionsMenu = new JMenu();
		optionsMenu.setText(Messages.getString("Menubar.menu_options")); //$NON-NLS-1$
		optionsMenu.add(settingsMenuItem);
		optionsMenu.add(baseMenuItem);
		optionsMenu.add(urlMenuItem);
		optionsMenu.addSeparator();
		optionsMenu.add(treebolicRendererMenuItem);
		optionsMenu.add(validateMenuItem);
		optionsMenu.add(focusParentMenuItem);

		final JMenu helpMenu = new JMenu();
		helpMenu.setText(Messages.getString("Menubar.menu_help")); //$NON-NLS-1$
		helpMenu.add(aboutMenuItem);
		helpMenu.add(helpMenuItem);

		final JMenu toolsMenu = new JMenu();
		toolsMenu.setText(Messages.getString("Menubar.menu_tools")); //$NON-NLS-1$
		toolsMenu.add(dtdMenuItem);
		toolsMenu.addSeparator();
		toolsMenu.add(makeSiteMenuItem);
		toolsMenu.add(runSiteMenuItem);

		this.add(filesMenu);
		this.add(elementsMenu);
		this.add(optionsMenu);
		this.add(toolsMenu);
		this.add(helpMenu);
	}

	/**
	 * Set controller
	 *
	 * @param controller
	 *        controller
	 */
	public void setController(final Controller controller)
	{
		this.controller = controller;
	}

	/**
	 * Make menu item
	 *
	 * @param text
	 *        text
	 * @param image
	 *        image
	 * @param command
	 *        command code
	 * @param acceleratorKey
	 *        accelerator key
	 * @return menu item
	 */
	private JMenuItem makeItem(final String text, final String image, final Controller.Code command, final KeyStroke acceleratorKey)
	{
		final JMenuItem item = new JMenuItem();
		item.setText(text);
		if (image != null)
		{
			item.setIcon(new ImageIcon(Menubar.class.getResource("images/" + image))); //$NON-NLS-1$
		}
		if (acceleratorKey != null)
		{
			item.setAccelerator(acceleratorKey);
		}
		item.addActionListener(e -> Menubar.this.controller.execute(command, 0));
		return item;
	}

	/**
	 * Make menu item
	 *
	 * @param text
	 *        text
	 * @param image
	 *        image
	 * @param command
	 *        command code
	 * @param acceleratorKey
	 *        accelerator key
	 * @return menu item
	 */
	private JCheckBoxMenuItem makeCheckboxItem(final String text, final String image, final Controller.Code command, final KeyStroke acceleratorKey, final boolean state)
	{
		final JCheckBoxMenuItem item = new JCheckBoxMenuItem();
		item.setText(text);
		item.setSelected(state);
		if (image != null)
		{
			item.setIcon(new ImageIcon(Menubar.class.getResource("images/" + image))); //$NON-NLS-1$
		}
		if (acceleratorKey != null)
		{
			item.setAccelerator(acceleratorKey);
		}
		item.addActionListener(e -> Menubar.this.controller.execute(command, 0));
		return item;
	}
}