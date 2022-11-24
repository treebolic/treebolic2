/*
 * Copyright (c) 2022. Bernard Bou
 */
package treebolic.generator.tree;

import treebolic.generator.Messages;
import treebolic.model.Settings;

/**
 * Top element wrapper
 *
 * @author Bernard Bou
 */
public class TopWrapper extends SettingsWrapper
{
	/**
	 * Constructor
	 *
	 * @param settings settings
	 */
	public TopWrapper(final Settings settings)
	{
		super(settings);
	}

	@Override
	public String toString()
	{
		return Messages.getString("TopWrapper.label");
	}
}
