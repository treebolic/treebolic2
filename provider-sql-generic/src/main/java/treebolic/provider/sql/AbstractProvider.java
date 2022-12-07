/**
 * Title : Treebolic SQL provider
 * Description : Treebolic SQL provider
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 */
package treebolic.provider.sql;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import treebolic.ILocator;
import treebolic.annotations.NonNull;
import treebolic.annotations.Nullable;
import treebolic.glue.iface.Colors;
import treebolic.model.*;
import treebolic.provider.IProvider;
import treebolic.provider.IProviderContext;
import treebolic.provider.LoadBalancer;
import treebolic.provider.ProviderUtils;

/**
 * Provider for SQL
 *
 * @param <B> database type
 * @param <C> cursor type
 * @param <E> exception type
 * @author Bernard Bou
 */
public abstract class AbstractProvider< //
		B extends AbstractProvider.Database<C, E>, //
		C extends AbstractProvider.Cursor<E>, //
		E extends Exception //
		> //
		implements IProvider
{
	/**
	 * Cursor type
	 *
	 * @param <E> exception thrown
	 */
	public interface Cursor<E extends Exception> extends AutoCloseable
	{
		/**
		 * Move to next row
		 *
		 * @return true if row is available
		 * @throws E exception
		 */
		boolean moveToNext() throws E;

		/**
		 * Get current position
		 *
		 * @return position
		 * @throws E exception
		 */
		int getPosition() throws E;

		/**
		 * Get column index
		 *
		 * @param columnName column name
		 * @return column index
		 * @throws E exception
		 */
		int getColumnIndex(String columnName) throws E;

		/**
		 * Test if colum is null
		 *
		 * @param columnIndex column index
		 * @return true if column IS NULL
		 * @throws E exception
		 */
		@SuppressWarnings("BooleanMethodIsAlwaysInverted")
		boolean isNull(int columnIndex) throws E;

		/**
		 * Get string
		 *
		 * @param columnIndex column index
		 * @return string
		 * @throws E exception
		 */
		String getString(int columnIndex) throws E;

		/**
		 * @param columnIndex column index
		 * @return integer
		 * @throws E exception
		 */
		Integer getInt(int columnIndex) throws E;

		/**
		 * @param columnIndex column index
		 * @return float
		 * @throws E exception
		 */
		Float getFloat(int columnIndex) throws E;

		/**
		 * @param columnIndex column index
		 * @return double
		 * @throws E exception
		 */
		Double getDouble(int columnIndex) throws E;

		/**
		 * Close
		 */
		void close();
	}

	/**
	 * Interface for database
	 *
	 * @param <C> cursor type
	 * @param <E> exception type
	 */
	public interface Database<C extends Cursor<E>, E extends Exception>
	{
		/**
		 * Query
		 *
		 * @param sql sql statement
		 * @return cursor
		 * @throws E exception
		 */
		C query(String sql) throws E;

		/**
		 * Close
		 */
		void close();
	}

	/**
	 * Open database
	 *
	 * @param properties properties
	 * @return database
	 */
	abstract protected B openDatabase(Properties properties);

	// S T A T I C S

	/**
	 * Property files
	 */
	static private final String DEFAULT_PROPERTY_FILE = "query.properties"; //$NON-NLS-1$

	/**
	 * Get nodes SQL statement
	 */
	static private final String DEFAULT_NODES_SQL = "SELECT * FROM %nodes%;"; //$NON-NLS-1$

	/**
	 * Get tree edges SQL statement
	 */
	static private final String DEFAULT_TREEEDGES_SQL = "SELECT * FROM %edges% WHERE %istree% = 1;"; //$NON-NLS-1$

	/**
	 * Get edges SQL statement
	 */
	static private final String DEFAULT_EDGES_SQL = "SELECT * FROM %edges% WHERE %istree% = 0;"; //$NON-NLS-1$

	/**
	 * Get settings SQL statement
	 */
	static private final String DEFAULT_SETTINGS_SQL = "SELECT * FROM %settings%;"; //$NON-NLS-1$

	/**
	 * Get menu item SQL statement
	 */
	static private final String DEFAULT_MENU_SQL = "SELECT * FROM %menu% WHERE %menuid% = 0;"; //$NON-NLS-1$

	/**
	 * Goto url scheme
	 */
	private static final String GOTO_SCHEME = "goto:"; //$NON-NLS-1$

	/**
	 * Mount url scheme
	 */
	private static final String MOUNT_SCHEME = "mount:"; //$NON-NLS-1$

	/**
	 * LoadBalancer : Edge style
	 */
	static private final int LOADBALANCING_EDGE_STYLE = IEdge.SOLID | /* IEdge.FROMDEF | IEdge.FROMCIRCLE | */IEdge.TOTRIANGLE | IEdge.TOFILL | IEdge.TODEF;

	// D A T A

	/**
	 * Provider context
	 */
	@Nullable
	private IProviderContext context;

	/**
	 * Node map by id
	 */
	@NonNull
	private final Hashtable<String, TreeMutableNode> nodesById;

	/**
	 * Base
	 */
	protected URL base;

	/**
	 * Properties
	 */
	@Nullable
	protected Properties properties;

	// C O N S T R U C T O R

	/**
	 * Constructor
	 */
	public AbstractProvider()
	{
		this.context = null;
		this.properties = null;
		this.nodesById = new Hashtable<>();
	}

	/**
	 * Initialize
	 *
	 * @param source0 source
	 * @return true if successful
	 */
	private boolean initialize(final String source0, final URL base, @NonNull final Properties parameters)
	{
		System.out.println("Sqlx provider source: " + source0); //$NON-NLS-1$
		String source = source0;
		boolean isGoto = false;
		if (source.startsWith(GOTO_SCHEME))
		{
			System.out.println("Sqlx GOTO"); //$NON-NLS-1$
			source = source.substring(GOTO_SCHEME.length());
			isGoto = true;
		}
		boolean isMount = false;
		if (source.startsWith(MOUNT_SCHEME))
		{
			System.out.println("Sqlx MOUNT"); //$NON-NLS-1$
			source = source.substring(MOUNT_SCHEME.length());
			isMount = true;
		}

		// base
		this.base = base;

		// source=query-file,path(,)
		@NonNull final String[] fields = source.split(","); //$NON-NLS-1$
		String queryFile = fields[0];

		// ensure query file if no properties
		if (this.properties == null)
		{
			if (queryFile == null)
			{
				queryFile = parameters.getProperty("query"); //$NON-NLS-1$
			}
			if (queryFile == null)
			{
				queryFile = AbstractProvider.DEFAULT_PROPERTY_FILE;
			}
		}

		// parse query file
		if (queryFile.length() != 0)
		{
			// url
			@Nullable final URL url = ProviderUtils.makeURL(queryFile, base, parameters, this.context);

			// load properties
			this.properties = url != null ? SqlProperties.load(url) : SqlProperties.load(queryFile);

			// add parameters
			if (this.properties != null)
			{
				this.properties.putAll(parameters);
			}
		}

		// query properties must be non null at this point
		if (this.properties == null)
		{
			return false;
		}

		// 2nd part of source
		if (fields.length > 1)
		{
			String path = fields[1];
			if (path.startsWith("where:")) //$NON-NLS-1$
			{
				// where-clause is truncate clause
				@NonNull String where = path.substring(6);
				this.properties.put(SqlProperties.TRUNCATE_NODES, where);
				this.properties.put(SqlProperties.TRUNCATE_TREEEDGES, where);
			}
		}

		// 3rd (empty) part of source stops pruning
		boolean hasTrailingComma = source.endsWith(","); //$NON-NLS-1$

		// apply pruning
		this.properties.put(SqlProperties.PRUNE, isMount || isGoto || hasTrailingComma);

		return true;
	}

	// I N T E R F A C E

	/*
	 * (non-Javadoc)
	 * @see treebolic.model.IProvider#set(treebolic.model.IProviderContext)
	 */
	@Override
	public void setContext(@Nullable final IProviderContext context)
	{
		this.context = context;
	}

	/*
	 * (non-Javadoc)
	 * @see treebolic.provider.IProvider#setLocator(treebolic.ILocator)
	 */
	@Override
	public void setLocator(final ILocator locator)
	{
		// do not need
	}

	/*
	 * (non-Javadoc)
	 * @see treebolic.provider.IProvider#setHandle(java.lang.Object)
	 */
	@Override
	public void setHandle(final Object handle)
	{
		// do not need
	}

	/*
	 * (non-Javadoc)
	 * @see treebolic.provider.IProvider#makeModel(java.lang.String, java.net.URL, java.util.Properties)
	 */
	@Nullable
	@Override
	public Model makeModel(final String source, final URL base, @NonNull final Properties parameters)
	{
		if (initialize(source, base, parameters))
		{
			return queryModel();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see treebolic.provider.IProvider#makeTree(java.lang.String, java.net.URL, java.util.Properties, boolean)
	 */
	@Nullable
	@Override
	public Tree makeTree(final String source, final URL base, @NonNull final Properties parameters, final boolean checkRecursion)
	{
		if (initialize(source, base, parameters))
		{
			return queryTree();
		}
		return null;
	}

	// H E L P E R

	/**
	 * Make URL
	 *
	 * @param properties properties
	 * @return url string
	 */
	@Nullable
	protected String makeDatabasePath(@NonNull final Properties properties)
	{
		String databaseName = properties.getProperty("database"); //$NON-NLS-1$
		if (databaseName != null && databaseName.length() > 0)
		{
			System.out.println("Base: " + this.base); //$NON-NLS-1$
			System.out.println("Name: " + databaseName); //$NON-NLS-1$
			@NonNull File dbFile = new File(databaseName);
			if (dbFile.exists())
			{
				return dbFile.getAbsolutePath();
			}

			try
			{
				// URI uri = this.base.toURI();
				// File base = new File(uri);
				// File f = new File(base, databaseName);

				@NonNull URL url = new URL(this.base, databaseName);
				System.out.println("Url: " + url); //$NON-NLS-1$
				dbFile = new File(url.toURI());
				System.out.println("File: " + dbFile); //$NON-NLS-1$
				return dbFile.getAbsolutePath();
			}
			catch (MalformedURLException | URISyntaxException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	// Q U E R Y

	/**
	 * Query model
	 *
	 * @return model
	 */
	@SuppressWarnings("boxing")
	@Nullable
	private Model queryModel()
	{
		assert this.properties != null;
		this.properties.put("database", makeDatabasePath(this.properties)); //$NON-NLS-1$
		try
		{
			// connect
			final B db = openDatabase(this.properties);

			// tree
			@NonNull final Tree tree = queryTree(db);

			// settings
			@NonNull final Settings settings = querySettings(db);
			final INode root = tree.getRoot();
			if (root != null)
			{
				@Nullable final List<INode> children = root.getChildren();
				if (children != null && children.size() == 1)
				{
					settings.orientation = "south"; //$NON-NLS-1$
					settings.yMoveTo = -0.4F;
				}
			}

			// close connection
			db.close();

			// result
			return new Model(tree, settings);
		}
		catch (final Exception e)
		{
			System.err.println("Sqlx queryModel: " + e.getMessage()); //$NON-NLS-1$
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Query tree
	 *
	 * @return tree
	 */
	@Nullable
	private Tree queryTree()
	{
		try
		{
			// connect
			final B db = openDatabase(this.properties);

			// tree
			@NonNull final Tree tree = queryTree(db);

			// close connection
			db.close();

			// result
			return tree;
		}
		catch (final Exception e)
		{
			System.err.println("Sqlx queryTree: " + e.getMessage()); //$NON-NLS-1$
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Query tree
	 *
	 * @param db connection
	 * @return tree
	 * @throws E exception
	 */
	@NonNull
	private Tree queryTree(@NonNull final B db) throws E
	{
		@NonNull final INode root = queryNodesAndEdges(db);
		@Nullable final List<IEdge> edges = queryEdges(db);
		return new Tree(root, edges);
	}

	/**
	 * Query nodes
	 *
	 * @param db connection
	 * @return tree root node
	 * @throws E exception
	 */
	@NonNull
	@SuppressWarnings("boxing")
	private MutableNode queryNodesAndEdges(@NonNull final B db) throws E
	{
		assert this.properties != null;

		// request type
		boolean prune = (Boolean) this.properties.get(SqlProperties.PRUNE);
		boolean balanceLoad = !this.properties.containsKey(SqlProperties.BALANCE_LOAD) || "true".equals(this.properties.get(SqlProperties.BALANCE_LOAD));

		// sql
		String nodesSql = this.properties.getProperty("nodesSql", AbstractProvider.DEFAULT_NODES_SQL); //$NON-NLS-1$
		nodesSql = narrowNodeSql(nodesSql, prune);
		nodesSql = macroExpand(nodesSql);
		System.out.println(nodesSql);

		String treeEdgesSql = this.properties.getProperty("treeEdgesSql", AbstractProvider.DEFAULT_TREEEDGES_SQL); //$NON-NLS-1$
		treeEdgesSql = narrowTreeEdgeSql(treeEdgesSql, prune);
		treeEdgesSql = macroExpand(treeEdgesSql);
		System.out.println(treeEdgesSql);

		// field names
		@NonNull final String idName = getName("nodes.id"); //$NON-NLS-1$
		@NonNull final String labelName = getName("nodes.label"); //$NON-NLS-1$
		@NonNull final String contentName = getName("nodes.content"); //$NON-NLS-1$
		@NonNull final String backcolorName = getName("nodes.backcolor"); //$NON-NLS-1$
		@NonNull final String forecolorName = getName("nodes.forecolor"); //$NON-NLS-1$
		@NonNull final String imageName = getName("nodes.image"); //$NON-NLS-1$
		@NonNull final String linkName = getName("nodes.link"); //$NON-NLS-1$
		@NonNull final String targetName = getName("nodes.target"); //$NON-NLS-1$
		@NonNull final String weightName = getName("nodes.weight"); //$NON-NLS-1$
		@NonNull final String mountpointName = getName("nodes.mountpoint"); //$NON-NLS-1$

		@NonNull final String fromName = getName("edges.from"); //$NON-NLS-1$
		@NonNull final String toName = getName("edges.to"); //$NON-NLS-1$
		@NonNull final String edgeLabelName = getName("edges.label"); //$NON-NLS-1$
		@NonNull final String edgeImageName = getName("edges.image"); //$NON-NLS-1$
		@NonNull final String edgeColorName = getName("edges.color"); //$NON-NLS-1$
		@NonNull final String edgeLineName = getName("edges.line"); //$NON-NLS-1$
		@NonNull final String edgeHiddenName = getName("edges.hidden"); //$NON-NLS-1$
		@NonNull final String edgeStrokeName = getName("edges.stroke"); //$NON-NLS-1$
		@NonNull final String edgeFromTerminatorName = getName("edges.fromterminator"); //$NON-NLS-1$
		@NonNull final String edgeToTerminatorName = getName("edges.toterminator"); //$NON-NLS-1$

		// id to node map
		this.nodesById.clear();

		// N O D E S
		final C nodesCursor = db.query(nodesSql);
		final int idIndex = nodesCursor.getColumnIndex(idName);
		while (nodesCursor.moveToNext())
		{
			// read id
			final String id = nodesCursor.getString(idIndex);
			if (id == null)
			{
				System.err.println("Sqlx queryNodes: " + nodesCursor.getPosition() + "th edge record with null id"); //$NON-NLS-1$ //$NON-NLS-2$
				continue;
			}

			// make node
			@NonNull final TreeMutableNode node = new TreeMutableNode(null, id);
			this.nodesById.put(id, node);

			// data
			node.setLabel(readString(nodesCursor, labelName));
			node.setContent(readString(nodesCursor, contentName));
			node.setBackColor(readColor(nodesCursor, backcolorName));
			node.setForeColor(readColor(nodesCursor, forecolorName));
			node.setImageFile(readString(nodesCursor, imageName));
			node.setLink(readString(nodesCursor, linkName));
			node.setTarget(readString(nodesCursor, targetName));
			@Nullable final Double weight = readDouble(nodesCursor, weightName);
			if (weight != null)
			{
				node.setWeight(-weight);
			}

			// mountpoint
			@Nullable final String value = readString(nodesCursor, mountpointName);
			if (value != null)
			{
				@NonNull final MountPoint.Mounting mountPoint = new MountPoint.Mounting();
				mountPoint.url = value;
				node.setMountPoint(mountPoint);
				@Nullable final Boolean now = readBoolean(nodesCursor, "now"); //$NON-NLS-1$
				if (now != null && now)
				{
					System.err.println("Sqlx queryNodes: Recursive mounting not implemented"); //$NON-NLS-1$
				}
			}
		}
		nodesCursor.close();

		// T R E E . E D G E S
		@NonNull final List<TreeMutableNode> parentLessNodes = new ArrayList<>();
		final C treeEdgesCursor = db.query(treeEdgesSql);
		final int fromIndex = treeEdgesCursor.getColumnIndex(fromName);
		final int toIndex = treeEdgesCursor.getColumnIndex(toName);
		while (treeEdgesCursor.moveToNext())
		{
			// from/to ids
			final String fromId = treeEdgesCursor.getString(fromIndex);
			final String toId = treeEdgesCursor.getString(toIndex);
			if ((fromId == null || fromId.length() == 0) && (toId == null || toId.length() == 0))
			{
				System.err.println("Sqlx queryNodes: " + treeEdgesCursor.getPosition() + "th tree edge record with null ids : from-id = <" + fromId + "> and to-id=<" + toId + ">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				continue;
			}

			// to node
			final TreeMutableNode toNode = this.nodesById.get(toId);

			// from node
			final TreeMutableNode fromNode = fromId == null || fromId.length() == 0 ? null : this.nodesById.get(fromId);

			// ill-formed nodes
			if (fromNode == null && toNode == null)
			{
				System.err.println("Sqlx queryNodes: " + treeEdgesCursor.getPosition() + "th tree edge record with not found end nodes : from-id = <" + fromId + "> and to-id=<" + toId + ">"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				continue;
			}

			// root candidate
			if (fromNode == null)
			{
				parentLessNodes.add(toNode);
				continue;
			}

			// make tree
			@NonNull List<INode> children = fromNode.getChildren();
			children.add(toNode);
			toNode.setParent(fromNode);

			// data
			toNode.setEdgeLabel(readString(treeEdgesCursor, edgeLabelName));
			toNode.setEdgeImageFile(readString(treeEdgesCursor, edgeImageName));
			toNode.setEdgeColor(readColor(treeEdgesCursor, edgeColorName));
			@Nullable final Boolean lineFlag = readBoolean(treeEdgesCursor, edgeLineName);
			@Nullable final Boolean hiddenFlag = readBoolean(treeEdgesCursor, edgeHiddenName);
			toNode.setEdgeStyle(Utils.parseStyle(readString(treeEdgesCursor, edgeStrokeName), readString(treeEdgesCursor, edgeFromTerminatorName), readString(treeEdgesCursor, edgeToTerminatorName), lineFlag == null ?
					null :
					lineFlag.toString(), hiddenFlag == null ? null : hiddenFlag.toString()));
		}
		treeEdgesCursor.close();

		// if one root return it
		TreeMutableNode rootNode;
		if (parentLessNodes.size() == 1)
		{
			rootNode = parentLessNodes.get(0);
		}
		else
		{
			// anchor parentless nodes to a root node
			rootNode = makeRootNode();
			for (@NonNull TreeMutableNode parentLessNode : parentLessNodes)
			{
				@NonNull List<INode> children = rootNode.getChildren();
				children.add(parentLessNode);
				parentLessNode.setParent(rootNode);

				// edge
				parentLessNode.setEdgeColor(Colors.RED);
			}
		}

		// mountpoint for root must be null if it is to be mounted
		rootNode.setMountPoint(null);

		// load balance
		if (balanceLoad)
		{
			balanceLoad(rootNode);
		}

		return rootNode;
	}

	/**
	 * Load balance nodes
	 *
	 * @param node node to balance
	 */
	private void balanceLoad(@NonNull final TreeMutableNode node)
	{
		// recurse
		for (INode child : node.getChildren())
		{
			balanceLoad((TreeMutableNode) child);
		}

		@NonNull final List<INode> children = node.getChildren();
		if (children.size() > 10)
		{
			@Nullable final Integer backColor = node.getBackColor();
			@Nullable final Integer foreColor = node.getForeColor();
			@Nullable final Integer edgeColor = node.getEdgeColor();
			@NonNull final LoadBalancer balancer = new LoadBalancer(new int[]{10, 3}, 3);
			balancer.setGroupNode(null, backColor, foreColor, edgeColor, LOADBALANCING_EDGE_STYLE, -1, null, null);
			@Nullable final List<INode> newChildren = balancer.buildHierarchy(children, 0);
			children.clear();
			node.addChildren(newChildren);
		}
	}

	/**
	 * Query edges
	 *
	 * @param db connection
	 * @return edge list
	 * @throws E exception
	 */
	@Nullable
	private List<IEdge> queryEdges(@NonNull final B db) throws E
	{
		assert this.properties != null;

		// request type
		boolean prune = (Boolean) this.properties.get(SqlProperties.PRUNE);

		// sql
		String edgesSql = this.properties.getProperty("edgesSql", AbstractProvider.DEFAULT_EDGES_SQL); //$NON-NLS-1$
		edgesSql = narrowEdgeSql(edgesSql, prune);
		edgesSql = macroExpand(edgesSql);
		System.out.println(edgesSql);

		// field names
		@NonNull final String fromIdName = getName("edges.from"); //$NON-NLS-1$
		@NonNull final String toIdName = getName("edges.to"); //$NON-NLS-1$
		@NonNull final String edgeLabelName = getName("edges.label"); //$NON-NLS-1$
		@NonNull final String edgeImageName = getName("edges.image"); //$NON-NLS-1$
		@NonNull final String edgeColorName = getName("edges.color"); //$NON-NLS-1$
		@NonNull final String edgeLineName = getName("edges.line"); //$NON-NLS-1$
		@NonNull final String edgeHiddenName = getName("edges.hidden"); //$NON-NLS-1$
		@NonNull final String edgeStrokeName = getName("edges.stroke"); //$NON-NLS-1$
		@NonNull final String edgeFromTerminatorName = getName("edges.fromterminator"); //$NON-NLS-1$
		@NonNull final String edgeToTerminatorName = getName("edges.toterminator"); //$NON-NLS-1$

		@Nullable List<IEdge> edgeList = null;

		// EDGES
		final C edgesCursor = db.query(edgesSql);
		final int fromIdIndex = edgesCursor.getColumnIndex(fromIdName);
		final int toIdIndex = edgesCursor.getColumnIndex(toIdName);
		final int edgeStrokeIndex = edgesCursor.getColumnIndex(edgeStrokeName);
		final int edgeFromTerminatorIndex = edgesCursor.getColumnIndex(edgeFromTerminatorName);
		final int edgeToTerminatorIndex = edgesCursor.getColumnIndex(edgeToTerminatorName);
		while (edgesCursor.moveToNext())
		{
			// read from/to ids
			final String fromId = edgesCursor.getString(fromIdIndex);
			final String toId = edgesCursor.getString(toIdIndex);
			if (fromId == null || toId == null)
			{
				System.err.println("Sqlx queryEdges: " + edgesCursor.getPosition() + "th edge record with null from/to id"); //$NON-NLS-1$ //$NON-NLS-2$
				continue;
			}

			// check from/to
			final MutableNode fromNode = this.nodesById.get(fromId);
			final MutableNode toNode = this.nodesById.get(toId);
			if (fromNode == null || toNode == null)
			{
				System.err.println("Sqlx queryEdges: " + edgesCursor.getPosition() + "th edge record with null from/to node"); //$NON-NLS-1$ //$NON-NLS-2$
				continue;
			}

			// make edge
			@NonNull final MutableEdge edge = new MutableEdge(fromNode, toNode);
			edge.setColor(readColor(edgesCursor, "color")); //$NON-NLS-1$
			if (edgeList == null)
			{
				edgeList = new ArrayList<>();
			}
			edgeList.add(edge);

			// attributes
			edge.setLabel(readString(edgesCursor, edgeLabelName));
			edge.setImageFile(readString(edgesCursor, edgeImageName));
			edge.setColor(readColor(edgesCursor, edgeColorName));
			@Nullable final Boolean lineFlag = readBoolean(edgesCursor, edgeLineName);
			@Nullable final Boolean hiddenFlag = readBoolean(edgesCursor, edgeHiddenName);
			edge.setStyle(Utils.parseStyle(edgesCursor.getString(edgeStrokeIndex), edgesCursor.getString(edgeFromTerminatorIndex), edgesCursor.getString(edgeToTerminatorIndex), lineFlag == null ? null : lineFlag.toString(), hiddenFlag == null ?
					null :
					hiddenFlag.toString()));
		}
		edgesCursor.close();
		return edgeList;
	}

	/**
	 * Query settings
	 *
	 * @param db connection
	 * @return settings
	 */
	@NonNull
	private Settings querySettings(@NonNull final B db)
	{
		assert this.properties != null;

		// sql
		String settingsSql = this.properties.getProperty("settingsSql", AbstractProvider.DEFAULT_SETTINGS_SQL); //$NON-NLS-1$
		settingsSql = macroExpand(settingsSql);
		String menuSql = this.properties.getProperty("menuSql", AbstractProvider.DEFAULT_MENU_SQL); //$NON-NLS-1$
		menuSql = macroExpand(menuSql);

		// field names
		@NonNull final String backImageName = getName("settings.backimage"); //$NON-NLS-1$
		@NonNull final String backcolorName = getName("settings.backcolor"); //$NON-NLS-1$
		@NonNull final String forecolorName = getName("settings.forecolor"); //$NON-NLS-1$
		@NonNull final String fontFaceName = getName("settings.fontface"); //$NON-NLS-1$
		@NonNull final String fontSizeName = getName("settings.fontsize"); //$NON-NLS-1$
		@NonNull final String scaleFontsName = getName("settings.scalefonts"); //$NON-NLS-1$
		@NonNull final String fontScalerName = getName("settings.fontscaler"); //$NON-NLS-1$
		@NonNull final String scaleImagesName = getName("settings.scaleimages"); //$NON-NLS-1$
		@NonNull final String imageScalerName = getName("settings.imagescaler"); //$NON-NLS-1$
		@NonNull final String orientationName = getName("settings.orientation"); //$NON-NLS-1$
		@NonNull final String expansionName = getName("settings.expansion"); //$NON-NLS-1$
		@NonNull final String sweepName = getName("settings.sweep"); //$NON-NLS-1$
		@NonNull final String preserveOrientationName = getName("settings.preserveorientation"); //$NON-NLS-1$
		@NonNull final String hasToolbarName = getName("settings.hastoolbar"); //$NON-NLS-1$
		@NonNull final String hasStatusbarName = getName("settings.hasstatusbar"); //$NON-NLS-1$
		@NonNull final String hasPopupMenuName = getName("settings.haspopupmenu"); //$NON-NLS-1$
		@NonNull final String hasTooltipName = getName("settings.hastooltip"); //$NON-NLS-1$
		@NonNull final String tooltipDisplaysContentName = getName("settings.tooltipdisplayscontent"); //$NON-NLS-1$
		@NonNull final String focusOnHoverName = getName("settings.focusonhover"); //$NON-NLS-1$
		@NonNull final String focusName = getName("settings.focus"); //$NON-NLS-1$
		@NonNull final String xMoveToName = getName("settings.xmoveto"); //$NON-NLS-1$
		@NonNull final String yMoveToName = getName("settings.ymoveto"); //$NON-NLS-1$
		@NonNull final String xShiftName = getName("settings.xshift"); //$NON-NLS-1$
		@NonNull final String yShiftName = getName("settings.yshift"); //$NON-NLS-1$
		@NonNull final String nodeBackcolorName = getName("settings.nodebackcolor"); //$NON-NLS-1$
		@NonNull final String nodeForecolorName = getName("settings.nodeforecolor"); //$NON-NLS-1$
		@NonNull final String nodeImageName = getName("settings.nodeimage"); //$NON-NLS-1$
		@NonNull final String nodeBorderName = getName("settings.nodeborder"); //$NON-NLS-1$
		@NonNull final String nodeEllipsizeName = getName("settings.nodeellipsize"); //$NON-NLS-1$
		@NonNull final String nodeLabelMaxLinesName = getName("settings.nodelabelmaxlines"); //$NON-NLS-1$
		@NonNull final String nodeLabelExtraLineFactorName = getName("settings.nodelabelextralinefactor"); //$NON-NLS-1$
		@NonNull final String treeEdgeColorName = getName("settings.treeedgecolor"); //$NON-NLS-1$
		@NonNull final String treeEdgeLineName = getName("settings.treeedgeline"); //$NON-NLS-1$
		@NonNull final String treeEdgeHiddenName = getName("settings.treeedgehidden"); //$NON-NLS-1$
		@NonNull final String treeEdgeStrokeName = getName("settings.treeedgestroke"); //$NON-NLS-1$
		@NonNull final String treeEdgeFromTerminatorName = getName("settings.treeedgefromterminator"); //$NON-NLS-1$
		@NonNull final String treeEdgeToTerminatorName = getName("settings.treeedgetoterminator"); //$NON-NLS-1$
		@NonNull final String treeEdgeImageName = getName("settings.treeedgeimage"); //$NON-NLS-1$
		@NonNull final String edgesAsArcsName = getName("settings.edgearc"); //$NON-NLS-1$
		@NonNull final String edgeColorName = getName("settings.edgecolor"); //$NON-NLS-1$
		@NonNull final String edgeLineName = getName("settings.edgeline"); //$NON-NLS-1$
		@NonNull final String edgeHiddenName = getName("settings.edgehidden"); //$NON-NLS-1$
		@NonNull final String edgeStrokeName = getName("settings.edgestroke"); //$NON-NLS-1$
		@NonNull final String edgeFromTerminatorName = getName("settings.edgefromterminator"); //$NON-NLS-1$
		@NonNull final String edgeToTerminatorName = getName("settings.edgetoterminator"); //$NON-NLS-1$
		@NonNull final String edgeImageName = getName("settings.edgeimage"); //$NON-NLS-1$

		@NonNull final String menuActionName = getName("menu.action"); //$NON-NLS-1$
		@NonNull final String menuLabelName = getName("menu.label"); //$NON-NLS-1$
		@NonNull final String menuTargetName = getName("menu.target"); //$NON-NLS-1$
		@NonNull final String menuScopeName = getName("menu.scope"); //$NON-NLS-1$
		@NonNull final String menuModeName = getName("menu.mode"); //$NON-NLS-1$
		@NonNull final String menuLinkName = getName("menu.link"); //$NON-NLS-1$

		try
		{
			@NonNull final Settings settings = new Settings();

			// first record
			final C settingsCursor = db.query(settingsSql);
			if (settingsCursor.moveToNext())
			{
				// boolean
				settings.hasToolbarFlag = readBoolean(settingsCursor, hasToolbarName);
				settings.hasStatusbarFlag = readBoolean(settingsCursor, hasStatusbarName);
				settings.hasPopUpMenuFlag = readBoolean(settingsCursor, hasPopupMenuName);
				settings.hasToolTipFlag = readBoolean(settingsCursor, hasTooltipName);
				settings.toolTipDisplaysContentFlag = readBoolean(settingsCursor, tooltipDisplaysContentName);
				settings.focusOnHoverFlag = readBoolean(settingsCursor, focusOnHoverName);
				settings.preserveOrientationFlag = readBoolean(settingsCursor, preserveOrientationName);
				settings.edgesAsArcsFlag = readBoolean(settingsCursor, edgesAsArcsName);
				settings.borderFlag = readBoolean(settingsCursor, nodeBorderName);
				settings.ellipsizeFlag = readBoolean(settingsCursor, nodeEllipsizeName);
				settings.labelMaxLines = readInteger(settingsCursor, nodeLabelMaxLinesName);
				settings.labelExtraLineFactor = readFloat(settingsCursor, nodeLabelExtraLineFactorName);
				settings.downscaleFontsFlag = readBoolean(settingsCursor, scaleFontsName);
				settings.downscaleImagesFlag = readBoolean(settingsCursor, scaleImagesName);

				// integer
				settings.fontSize = readInteger(settingsCursor, fontSizeName);

				// floats
				settings.fontDownscaler = readFloats(settingsCursor, fontScalerName);
				settings.imageDownscaler = readFloats(settingsCursor, imageScalerName);

				// double
				settings.xMoveTo = readFloat(settingsCursor, xMoveToName);
				settings.yMoveTo = readFloat(settingsCursor, yMoveToName);
				settings.xShift = readFloat(settingsCursor, xShiftName);
				settings.yShift = readFloat(settingsCursor, yShiftName);
				settings.expansion = readFloat(settingsCursor, expansionName);
				settings.sweep = readFloat(settingsCursor, sweepName);

				// colors
				settings.backColor = readColor(settingsCursor, backcolorName);
				settings.foreColor = readColor(settingsCursor, forecolorName);
				settings.nodeBackColor = readColor(settingsCursor, nodeBackcolorName);
				settings.nodeForeColor = readColor(settingsCursor, nodeForecolorName);
				settings.treeEdgeColor = readColor(settingsCursor, treeEdgeColorName);
				settings.edgeColor = readColor(settingsCursor, edgeColorName);

				// styles
				@Nullable Boolean lineFlag = readBoolean(settingsCursor, treeEdgeLineName);
				@Nullable Boolean hiddenFlag = readBoolean(settingsCursor, treeEdgeHiddenName);
				settings.treeEdgeStyle = Utils.parseStyle(readString(settingsCursor, treeEdgeStrokeName), readString(settingsCursor, treeEdgeFromTerminatorName), readString(settingsCursor, treeEdgeToTerminatorName), lineFlag == null ?
						null :
						lineFlag.toString(), hiddenFlag == null ? null : hiddenFlag.toString());
				hiddenFlag = readBoolean(settingsCursor, edgeHiddenName);
				lineFlag = readBoolean(settingsCursor, edgeLineName);
				settings.edgeStyle = Utils.parseStyle(readString(settingsCursor, edgeStrokeName), readString(settingsCursor, edgeFromTerminatorName), readString(settingsCursor, edgeToTerminatorName), lineFlag == null ? null : lineFlag.toString(),
						hiddenFlag == null ?
								null :
								hiddenFlag.toString());

				// string
				settings.orientation = readString(settingsCursor, orientationName);
				settings.focus = readString(settingsCursor, focusName);
				settings.fontFace = readString(settingsCursor, fontFaceName);
				settings.orientation = readString(settingsCursor, orientationName);
				settings.backgroundImageFile = readString(settingsCursor, backImageName);
				settings.defaultNodeImage = readString(settingsCursor, nodeImageName);
				settings.defaultTreeEdgeImage = readString(settingsCursor, treeEdgeImageName);
				settings.defaultEdgeImage = readString(settingsCursor, edgeImageName);
			}
			settingsCursor.close();

			// first record
			final C menuCursor = db.query(menuSql);
			while (menuCursor.moveToNext())
			{
				@NonNull final MenuItem menuItem = new MenuItem();
				menuItem.label = readString(menuCursor, menuLabelName);
				menuItem.matchTarget = readString(menuCursor, menuTargetName);
				menuItem.link = readString(menuCursor, menuLinkName);
				@Nullable final String actionString = readString(menuCursor, menuActionName);
				@Nullable final String scopeString = readString(menuCursor, menuScopeName);
				@Nullable final String modeString = readString(menuCursor, menuModeName);
				Utils.parseMenuItem(menuItem, actionString, scopeString, modeString);

				// add
				if (settings.menu == null)
				{
					settings.menu = new ArrayList<>();
				}
				settings.menu.add(menuItem);
			}
			menuCursor.close();

			return settings;
		}
		catch (final Exception e)
		{
			System.err.println("Sqlx querySettings: Cannot read Settings." + e.getMessage()); //$NON-NLS-1$
		}
		// return default
		return new Settings();
	}

	/**
	 * Make root node
	 *
	 * @return root node
	 */
	@NonNull
	private TreeMutableNode makeRootNode()
	{
		assert this.properties != null;
		@NonNull final TreeMutableNode rootNode = new TreeMutableNode(null, "root"); //$NON-NLS-1$
		rootNode.setLabel(this.properties.getProperty("root_label")); //$NON-NLS-1$
		rootNode.setBackColor(Utils.stringToColor(this.properties.getProperty("root_bcolor"))); //$NON-NLS-1$
		rootNode.setForeColor(Utils.stringToColor(this.properties.getProperty("root_fcolor"))); //$NON-NLS-1$
		rootNode.setImageFile(this.properties.getProperty("root_image")); //$NON-NLS-1$
		return rootNode;
	}

	// R E S U L T S E T . H E L P E R S

	/**
	 * Read string
	 *
	 * @param cursor record set
	 * @param name   field name
	 * @return String value
	 */
	@Nullable
	private String readString(@NonNull final C cursor, final String name)
	{
		try
		{
			final int index = cursor.getColumnIndex(name);
			if (index != -1)
			{
				return cursor.getString(index);
			}
		}
		catch (final Exception e)
		{
			// do nothing
		}
		return null;
	}

	/**
	 * Read integer
	 *
	 * @param cursor record set
	 * @param name   field name
	 * @return Integer value
	 */
	@Nullable
	private Integer readInteger(@NonNull final C cursor, final String name)
	{
		try
		{
			final int index = cursor.getColumnIndex(name);
			if (index != -1 && !cursor.isNull(index))
			{
				return cursor.getInt(index);
			}
		}
		catch (final Exception e)
		{
			// do nothing
		}
		return null;
	}

	/**
	 * Read floats
	 *
	 * @param cursor record set
	 * @param name   field name
	 * @return float array
	 */
	private float[] readFloats(@NonNull final C cursor, final String name)
	{
		try
		{
			final int index = cursor.getColumnIndex(name);
			if (index != -1)
			{
				final String value = cursor.getString(index);
				if (value != null)
				{
					return Utils.stringToFloats(value);
				}
			}
		}
		catch (final Exception e)
		{
			// do nothing
		}
		return null;
	}

	/**
	 * Read double
	 *
	 * @param cursor record set
	 * @param name   field name
	 * @return Double value
	 */
	@Nullable
	private Float readFloat(@NonNull final C cursor, final String name)
	{
		try
		{
			final int index = cursor.getColumnIndex(name);
			if (index != -1 && !cursor.isNull(index))
			{
				return cursor.getFloat(index);
			}
		}
		catch (final Exception e)
		{
			// do nothing
		}
		return null;
	}

	/**
	 * Read double
	 *
	 * @param cursor record set
	 * @param name   field name
	 * @return Double value
	 */
	@Nullable
	private Double readDouble(@NonNull final C cursor, final String name)
	{
		try
		{
			final int index = cursor.getColumnIndex(name);
			if (index != -1 && !cursor.isNull(index))
			{
				return cursor.getDouble(index);
			}
		}
		catch (final Exception e)
		{
			// do nothing
		}
		return null;
	}

	/**
	 * Read boolean
	 *
	 * @param cursor record set
	 * @param name   field name
	 * @return Boolean value
	 */
	@Nullable
	@SuppressWarnings("boxing")
	private Boolean readBoolean(@NonNull final C cursor, final String name)
	{
		try
		{
			final int index = cursor.getColumnIndex(name);
			if (index != -1 && !cursor.isNull(index))
			{
				return cursor.getInt(index) != 0;
			}
		}
		catch (final Exception e)
		{
			// do nothing
		}
		return null;
	}

	/**
	 * Read color
	 *
	 * @param cursor record set
	 * @param came   field name
	 * @return Color value
	 */
	@Nullable
	private Integer readColor(@NonNull final C cursor, final String came)
	{
		try
		{
			final int index = cursor.getColumnIndex(came);
			if (index != -1)
			{
				final String value = cursor.getString(index);
				if (value != null)
				{
					return Utils.stringToColor(value);
				}
			}
		}
		catch (final Exception e)
		{
			// do nothing
		}
		return null;
	}

	// W H E R E

	/**
	 * Narrow SQL statement
	 *
	 * @param sql0 sql statement
	 */
	@NonNull
	private String narrowSql(@NonNull final String sql0, @NonNull final String... keys)
	{
		@NonNull StringBuilder sb = new StringBuilder();

		// remove trailing ; if any
		@NonNull String sql = sql0;
		int semiColon = sql.lastIndexOf(';');
		if (semiColon > -1)
		{
			// strip trailing ';'
			sql = sql0.substring(0, semiColon);
		}
		sb.append(sql);
		sb.append(' ');

		boolean start = true;
		for (@Nullable final String key : keys)
		{
			if (key == null)
			{
				continue;
			}
			assert this.properties != null;
			final String clause = this.properties.getProperty(key);
			if (clause != null && clause.length() > 0)
			{
				// add head
				if (start)
				{
					start = false;

					// detect where-clause
					int wherePos = sb.indexOf("WHERE"); //$NON-NLS-1$

					// add WHERE keyword or AND if already there
					sb.append(wherePos == -1 ? "WHERE" : "AND"); //$NON-NLS-1$ //$NON-NLS-2$
					sb.append(' ');
				}
				else
				{
					sb.append(' ');
					sb.append("AND"); //$NON-NLS-1$
					sb.append(' ');
				}

				// add where clause
				sb.append(clause);
			}
		}

		// System.out.println(sql0);
		// System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * Narrow SQL statement for nodes
	 *
	 * @param sql0  nodes sql statement
	 * @param prune whether to prune
	 */
	@NonNull
	private String narrowNodeSql(@NonNull final String sql0, boolean prune)
	{
		return narrowSql(sql0, SqlProperties.TRUNCATE_NODES, prune ? null : SqlProperties.PRUNE_NODES);
	}

	/**
	 * Narrow SQL statement for tree edges
	 *
	 * @param sql0  tree edges sql statement
	 * @param prune whether to prune
	 */
	@NonNull
	private String narrowTreeEdgeSql(@NonNull final String sql0, boolean prune)
	{
		return narrowSql(sql0, SqlProperties.TRUNCATE_TREEEDGES, prune ? null : SqlProperties.PRUNE_TREEEDGES);
	}

	/**
	 * Narrow SQL statement for edges
	 *
	 * @param sql0  edges sql statement
	 * @param prune whether to prune
	 */
	@NonNull
	private String narrowEdgeSql(@NonNull final String sql0, boolean prune)
	{
		return narrowSql(sql0, SqlProperties.TRUNCATE_EDGES, prune ? null : SqlProperties.PRUNE_EDGES);
	}

	// C O L U M N S

	/**
	 * Get name of column from properties
	 *
	 * @param key key
	 * @return value if not null or key after last '.'
	 */
	@NonNull
	private String getName(@NonNull final String key)
	{
		assert this.properties != null;
		final String value = (String) this.properties.get(key);
		if (value != null)
		{
			return value;
		}

		final int index = key.lastIndexOf('.');
		return index == -1 ? key : key.substring(index + 1);
	}

	// M A C R O

	/**
	 * Macro pattern: ${macro}
	 */
	static final Pattern PATTERN = Pattern.compile("\\$\\{[^}]*}"); //$NON-NLS-1$

	/**
	 * Build macro name-value pairs
	 *
	 * @param str string to expand macro in
	 * @return name-val map
	 */
	@NonNull
	private Map<String, String> makeMacroMap(@NonNull final String str)
	{
		// macro map (local to this sentence)
		@NonNull final Map<String, String> macroMap = new HashMap<>();
		for (@NonNull final Matcher matcher = PATTERN.matcher(str); matcher.find(); )
		{
			final String match = matcher.group();
			@NonNull final String key = match.substring(2, match.length() - 1);
			if (!macroMap.containsKey(key))
			{
				assert this.properties != null;
				String value = (String) this.properties.get(key);
				if (PATTERN.matcher(str).find())
				{
					if (value != null)
					{
						value = macroExpand(value);
					}
					else
					{
						System.err.println(match + " has no value"); //$NON-NLS-1$
					}
				}
				macroMap.put(key, value);
			}
		}
		return macroMap;
	}

	/**
	 * Expand macros (recursively)
	 *
	 * @param str string to expand macro in
	 * @return string with expanded macros
	 */
	@NonNull
	private String macroExpand(@NonNull final String str)
	{
		// macro map (local to this sentence)
		@NonNull final Map<String, String> macroMap = makeMacroMap(str);

		// macro substitution
		@NonNull String result = str;
		for (final String key : macroMap.keySet())
		{
			String value = macroMap.get(key);
			if (value == null)
			{
				value = key;
			}
			result = result.replaceAll("\\$\\{" + key + "}", value); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return result;
	}
}
