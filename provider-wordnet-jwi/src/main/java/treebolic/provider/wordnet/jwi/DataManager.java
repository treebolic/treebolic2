/*
 * Copyright (c) 2019. Bernard Bou <1313ou@gmail.com>
 */

package treebolic.provider.wordnet.jwi;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import treebolic.annotations.NonNull;

/**
 * Data manager
 *
 * @author Bernard Bou
 */
public class DataManager extends BaseDataManager
{
	/**
	 * WordNet 3.1
	 */
	static public final String WN31_TAG = "WN31";

	/**
	 * Open English WordNet
	 */
	static public final String OEWN_TAG = "OEWN";

	static private final String WN31_ARCHIVE = "/wordnet31.zip";

	static private final String OEWN_ARCHIVE = "/oewn2022.zip";

	static private final String CACHESUBDIR = "wordnet";

	static private final DataManager INSTANCE = new DataManager();

	/**
	 * Get instance of singleton data manager
	 *
	 * @return instance of singleton data manager
	 */
	static public DataManager getInstance()
	{
		return INSTANCE;
	}

	/**
	 * Constructor
	 */
	private DataManager()
	{
	}

	/**
	 * Get data dir
	 *
	 * @param data      data (tag or url)
	 * @param cacheHome cache home directory (parent to cache)
	 * @return cached dictionary data
	 * @throws IOException io exception
	 */
	@Override
	public File getDataDir(final String data, final File cacheHome) throws IOException
	{
		URL zipUrl = getSourceZipURL(data);
		if (zipUrl == null)
		{
			throw new IOException("No resource for " + data);
		}
		return setup(zipUrl, cacheHome, false);
	}

	/**
	 * Deploy zip to cache
	 *
	 * @param data      data (tag or url)
	 * @param cacheHome cache home directory (parent to cache)
	 * @return cached dictionary data
	 * @throws IOException io exception
	 */
	@SuppressWarnings("UnusedReturnValue")
	@Override
	public File deploy(final String data, final File cacheHome) throws IOException
	{
		URL zipUrl = getSourceZipURL(data);
		return setup(zipUrl, cacheHome, true);
	}

	/**
	 * Setup
	 *
	 * @param zipUrl    source zip url
	 * @param cacheHome cache home directory (parent to cache)
	 * @param doCleanup whether to clean up
	 * @return cached dictionary data
	 */
	@NonNull
	private File setup(@NonNull final URL zipUrl, @NonNull final File cacheHome, final boolean doCleanup) throws IOException
	{
		final File cache = new File(cacheHome, DataManager.CACHESUBDIR);

		// directory
		boolean doUnzip = true;
		if (cache.exists())
		{
			if (doCleanup)
			// force clean up, doUnzip remains true
			{
				cleanup(cache);
			}
			else
			// doUnzip may toggle if check says so
			{
				doUnzip = !DataManager.check(cache);
			}
		}
		else
		{
			// create output directory is not exists, doUnzip remains true
			// noinspection ResultOfMethodCallIgnored
			cache.mkdir();
		}

		// unzip
		if (doUnzip)
		{
			expand(zipUrl, null, cache);
		}

		// return cache
		return cache;
	}

	/**
	 * Make source zip url
	 *
	 * @param data0 data
	 * @return source zip url
	 * @throws MalformedURLException malformed url exception
	 */
	private URL getSourceZipURL(String data0) throws MalformedURLException
	{
		String data = data0;
		if (data == null)
		{
			data = WN31_TAG;
		}

		switch (data)
		{
			case WN31_TAG:
				// source archive in class path
				return DataManager.class.getResource(WN31_ARCHIVE);
			case OEWN_TAG:
				// source archive in class path
				return DataManager.class.getResource(OEWN_ARCHIVE);
			default:
				// source archive at URL
				System.out.println(data);
				return new URL(data);
		}
	}
}
