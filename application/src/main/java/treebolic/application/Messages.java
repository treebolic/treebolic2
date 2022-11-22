/*
 * Copyright (c) 2022. Bernard Bou
 */

package treebolic.application;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import treebolic.annotations.NonNull;

/**
 * Language dependency, bundle reader
 *
 * @author Bernard Bou
 */
public class Messages
{
	private static final String BUNDLE_NAME = "treebolic.application.messages";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private Messages()
	{
		// do nothing
	}

	/**
	 * Get localized message
	 *
	 * @param key message key
	 * @return localized message
	 */
	@NonNull
	public static String getString(@NonNull final String key)
	{
		try
		{
			return Messages.RESOURCE_BUNDLE.getString(key);
		}
		catch (final MissingResourceException e)
		{
			return '!' + key + '!';
		}
	}
}
