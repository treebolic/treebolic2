/*
 * Copyright (c) 2022. Bernard Bou
 */
package treebolic.fungi.browser;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

import treebolic.annotations.NonNull;
import treebolic.fungi.Browser;

/**
 * Context
 *
 * @author Bernard Bou
 */
public class Context extends treebolic.application.Context
{
	static final String URL_SCHEME = "fungi:";

	/**
	 * Source modification listener
	 */
	public interface SourceListener
	{
		/**
		 * Update callback
		 *
		 * @param source source
		 */
		void onUpdate(final String source);
	}

	/**
	 * Listener
	 */
	final private SourceListener listener;

	/**
	 * Data directory url
	 */
	private URL dataDirUrl;

	/**
	 * Constructor
	 *
	 * @param application application mainframe
	 * @param source      source
	 * @param base        base
	 * @param imageBase   image base
	 * @param listener    source modification listener
	 */
	public Context(final MainFrame application, final String source, final String base, final String imageBase, final SourceListener listener)
	{
		super(application, source, base, imageBase);
		this.listener = listener;

		// base is defined
		if (base == null || base.isEmpty())
		{
			throw new RuntimeException("Null base");
		}

		// data url
		try
		{
			this.dataDirUrl = new File(base).toURI().toURL();
			// System.out.println("[datadir url] " + this.dataDirUrl);
		}
		catch (MalformedURLException exception)
		{
			this.dataDirUrl = null;
		}
	}

	/**
	 * Make data location
	 *
	 * @return directory
	 */
	static public File makeDataLocation()
	{
		// base=parent(classes)/database
		String location = Browser.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		try
		{
			location = URLDecoder.decode(location, "UTF-8");
		}
		catch (UnsupportedEncodingException exception)
		{
			System.err.println(exception + " " + location);
		}
		location = new File(location).getParent();

		// dir
		final File dir = new File(location, "database/");

		// make
		//noinspection ResultOfMethodCallIgnored
		dir.mkdirs();

		return dir;
	}

	/**
	 * Get source
	 *
	 * @return source
	 */
	public String getSource()
	{
		return this.source;
	}

	@Override
	public URL getBase()
	{
		return this.dataDirUrl;
	}

	@Override
	public URL getImagesBase()
	{
		return this.dataDirUrl;
	}

	@Override
	public Properties getParameters()
	{
		final Properties properties = super.getParameters();
		assert properties != null;
		//noinspection ConstantConditions
		properties.put("edible", MainFrame.class.getResource("/database/edible.png").toString());
		//noinspection ConstantConditions
		properties.put("good", MainFrame.class.getResource("/database/good.png").toString());
		//noinspection ConstantConditions
		properties.put("poisonous", MainFrame.class.getResource("/database/poisonous.png").toString());
		//noinspection ConstantConditions
		properties.put("deadly", MainFrame.class.getResource("/database/deadly.png").toString());
		return properties;
	}

	@Override
	public boolean linkTo(@NonNull String linkUrl, String linkTarget)
	{
		System.out.println(Messages.getString("Context.linkto") + linkUrl);

		// if url is handled by client, return query to client, which will handle it by initiating another query
		if (linkUrl.startsWith(URL_SCHEME))
		{
			final String source2 = linkUrl.substring(URL_SCHEME.length());
			requery(source2);
			return true;
		}
		return super.linkTo(linkUrl, linkTarget);
	}

	/**
	 * Requery
	 *
	 * @param source0 new source
	 */
	protected void requery(String source0)
	{
		final String[] fields0 = source0.split(",");
		final String[] fields = this.source.split(",");
		this.source = fields[0] + ',' + (fields0.length > 1 ? fields0[1] : source0);
		if (this.listener != null)
		{
			this.listener.onUpdate(this.source);
		}

		System.out.println(Messages.getString("Context.requery") + source0);
		this.application.getWidget().reinit(source0);
	}

	@Override
	public String getStyle()
	{
		return ".content { }" + ".mount {color: #A1B28F; font-size: small; }" + ".link {color: #5576C7; font-size: small; }" + ".linking {color: #007D82; font-size: small; }" + ".searching {color: #007D82; font-size: small; }" + ".more {color: gray; margin-bottom: 5px; }" + "a.active_link {color: black; text-decoration: none; }" + "";
	}
}
