/*
 * Copyright (c) 2022. Bernard Bou
 */
package treebolic.propertyview;

import java.awt.Component;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * List editor
 *
 * @author Bernard Bou
 */
class ListEditor extends DefaultCellEditor
{
	private static final long serialVersionUID = 1L;

	/**
	 * String to image map
	 */
	private Map<String, ImageIcon> imageMap = null;

	/**
	 * Constructor
	 */
	public ListEditor()
	{
		super(new JComboBox<String>());

		// renderer
		final DefaultListCellRenderer renderer = new DefaultListCellRenderer()
		{
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("synthetic-access")
			@Override
			public Component getListCellRendererComponent(final JList<?> list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus)
			{
				if (ListEditor.this.imageMap == null)
					return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				final ImageIcon icon = ListEditor.this.imageMap.get(value);
				setText(value == null ? PropertyView.defaultString : (String) value);
				setIcon(icon);
				setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
				setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
				return this;
			}
		};
		renderer.setHorizontalAlignment(SwingConstants.LEFT);
		renderer.setVerticalAlignment(SwingConstants.CENTER);
		renderer.setOpaque(true);
		getComboBox().setRenderer(renderer);

		// if popup closes fire event (standard code depends on if value has changed)
		getComboBox().addPopupMenuListener(new PopupMenuListener()
		{
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e)
			{
				//
			}

			@SuppressWarnings("synthetic-access")
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
			{
				ListEditor.this.fireEditingStopped();
			}

			@SuppressWarnings("synthetic-access")
			@Override
			public void popupMenuCanceled(PopupMenuEvent e)
			{
				ListEditor.this.fireEditingCanceled();
			}
		});
	}

	/**
	 * Get access to combobox component
	 *
	 * @return combo box component
	 */
	@SuppressWarnings("unchecked")
	public JComboBox<String> getComboBox()
	{
		return (JComboBox<String>) this.editorComponent;
	}

	/**
	 * Allow for edit line
	 *
	 * @param flag
	 *        true/false
	 */
	public void setEditable(final boolean flag)
	{
		getComboBox().setEditable(flag);
	}

	/**
	 * Render strings as images (as per map)
	 *
	 * @param imageMap
	 *        string to image map
	 */
	public void setImageMap(final Map<String, ImageIcon> imageMap)
	{
		this.imageMap = imageMap;
	}

	// I N T E R F A C E

	/*
	 * (non-Javadoc)
	 * @see javax.swing.DefaultCellEditor#getCellEditorValue()
	 */
	@Override
	public Object getCellEditorValue()
	{
		String value = (String) super.getCellEditorValue();
		if (PropertyView.defaultString.equals(value))
			value = null;
		// System.out.println("List getCellEditorValue " + value);
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see javax.swing.DefaultCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value0, boolean isSelected, int row, int column)
	{
		Object value = value0;
		if (value == null)
			value = PropertyView.defaultString;
		// System.out.println("List getTableCellEditorComponent " + value);
		return super.getTableCellEditorComponent(table, value.toString(), isSelected, row, column);
	}
}
