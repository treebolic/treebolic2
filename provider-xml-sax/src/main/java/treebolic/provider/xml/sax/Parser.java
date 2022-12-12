/*
 * Copyright (c) 2022. Bernard Bou
 */

package treebolic.provider.xml.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import treebolic.annotations.NonNull;
import treebolic.annotations.Nullable;
import treebolic.model.*;

/**
 * XML SAX parser
 */
public class Parser
{
	/**
	 * SAX handler
	 */
	public static class SaxHandler extends DefaultHandler
	{
		private static final String TREEBOLIC = "treebolic";
		private static final String TREE = "tree";
		private static final String NODES = "nodes";
		private static final String NODE = "node";
		private static final String EDGES = "edges";
		private static final String TREEEDGE = "treeedge";
		private static final String EDGE = "edge";
		private static final String LABEL = "label";
		private static final String CONTENT = "content";
		private static final String IMG = "img";
		private static final String A = "a";
		private static final String MOUNTPOINT = "mountpoint";
		private static final String DEFAULT_TREEEDGE = "default.treeedge";
		private static final String DEFAULT_EDGE = "default.edge";
		private static final String TOOLS = "tools";
		private static final String MENU = "menu";
		private static final String MENUITEM = "menuitem";

		private final Stack<String> states = new Stack<>();

		private Stack<MutableNode> stack;

		private Map<String, MutableNode> nodes;

		private INode root;

		private List<IEdge> edges;

		private final Settings settings = new Settings();

		@Nullable
		private MutableEdge edge = null;

		@Nullable
		private StringBuilder textSb = null;

		@Nullable
		private String link;

		@Nullable
		private MountPoint.Mounting mountpoint;

		@Override
		public void startDocument()
		{
		}

		@Override
		public void startElement(String uri, String localName, @NonNull String qName, @NonNull Attributes attributes)
		{
			switch (qName)
			{
				case TREEBOLIC:
				{
					setAttribute(attributes, "toolbar", Parser::parseBoolean, (v) -> settings.hasToolbarFlag = v);
					setAttribute(attributes, "statusbar", Parser::parseBoolean, (v) -> settings.hasStatusbarFlag = v);
					setAttribute(attributes, "popupmenu", Parser::parseBoolean, (v) -> settings.hasPopUpMenuFlag = v);
					setAttribute(attributes, "tooltip", Parser::parseBoolean, (v) -> settings.hasToolTipFlag = v);
					setAttribute(attributes, "tooltip-displays-content", Parser::parseBoolean, (v) -> settings.toolTipDisplaysContentFlag = v);
					setAttribute(attributes, "focus-on-hover", Parser::parseBoolean, (v) -> settings.focusOnHoverFlag = v);
					setAttribute(attributes, "focus", (v) -> settings.focus = v);
					setAttribute(attributes, "xmoveto", Parser::parseFloat, (v) -> settings.xMoveTo = v);
					setAttribute(attributes, "ymoveto", Parser::parseFloat, (v) -> settings.xMoveTo = v);
					setAttribute(attributes, "xshift", Parser::parseFloat, (v) -> settings.xShift = v);
					setAttribute(attributes, "yshift", Parser::parseFloat, (v) -> settings.yShift = v);
					break;
				}

				case TREE:
				{
					setAttribute(attributes, "backcolor", Parser::parseColor, (v) -> settings.backColor = v);
					setAttribute(attributes, "forecolor", Parser::parseColor, (v) -> settings.foreColor = v);
					setAttribute(attributes, "orientation", (v) -> settings.orientation = v);
					setAttribute(attributes, "expansion", Parser::parseFloat, (v) -> settings.expansion = v);
					setAttribute(attributes, "sweep", Parser::parseFloat, (v) -> settings.sweep = v);
					setAttribute(attributes, "preserve-orientation", Parser::parseBoolean, (v) -> settings.preserveOrientationFlag = v);
					setAttribute(attributes, "fontface", (v) -> settings.fontFace = v);
					setAttribute(attributes, "fontsize", Parser::parseInt, (v) -> settings.fontSize = v);
					setAttribute(attributes, "scalefonts", Parser::parseBoolean, (v) -> settings.downscaleFontsFlag = v);
					setAttribute(attributes, "fontscaler", Parser::parseFloats, (v) -> settings.fontDownscaler = v);
					setAttribute(attributes, "scaleimages", Parser::parseBoolean, (v) -> settings.downscaleImagesFlag = v);
					setAttribute(attributes, "imagescaler", Parser::parseFloats, (v) -> settings.imageDownscaler = v);
					break;
				}

				case NODES:
				{
					nodes = new HashMap<>();
					stack = new Stack<>();

					setAttribute(attributes, "backcolor", Parser::parseColor, (v) -> settings.nodeBackColor = v);
					setAttribute(attributes, "forecolor", Parser::parseColor, (v) -> settings.nodeForeColor = v);
					setAttribute(attributes, "border", Parser::parseBoolean, (v) -> settings.borderFlag = v);
					setAttribute(attributes, "ellipsize", Parser::parseBoolean, (v) -> settings.ellipsizeFlag = v);
					break;
				}

				case NODE:
				{
					String id = attributes.getValue("id");
					INode parent = stack.empty() ? null : stack.peek();
					@NonNull MutableNode node = new MutableNode(parent, id);
					nodes.put(id, node);
					stack.push(node);
					if (parent == null)
					{
						root = node;
					}

					setAttribute(attributes, "backcolor", Parser::parseColor, node::setBackColor);
					setAttribute(attributes, "forecolor", Parser::parseColor, node::setForeColor);
					setAttribute(attributes, "weight", Parser::parseDouble, node::setWeight);
					break;
				}

				case EDGES:
				{
					edges = new ArrayList<>();

					setAttribute(attributes, "arcs", Parser::parseBoolean, (v) -> settings.edgesAsArcsFlag = v);
					break;
				}

				case EDGE:
				{
					String from = attributes.getValue("from");
					String to = attributes.getValue("to");
					INode fromNode = nodes.get(from);
					INode toNode = nodes.get(to);
					edge = new MutableEdge(fromNode, toNode);
					edges.add(edge);

					setStyleAttribute(attributes, "stroke", "fromterminator", "toterminator", "line", "hidden", edge::setStyle);
					setAttribute(attributes, "color", Parser::parseColor, edge::setColor);
					break;
				}

				case TREEEDGE:
				{
					@NonNull MutableNode node = stack.peek();
					setStyleAttribute(attributes, "stroke", "fromterminator", "toterminator", "line", "hidden", node::setEdgeStyle);
					setAttribute(attributes, "color", Parser::parseColor, node::setEdgeColor);
					break;
				}

				case DEFAULT_TREEEDGE:
				{
					setStyleAttribute(attributes, "stroke", "fromterminator", "toterminator", "line", "hidden", v -> settings.treeEdgeStyle = v);
					setAttribute(attributes, "color", Parser::parseColor, (v) -> settings.treeEdgeColor = v);
					break;
				}

				case DEFAULT_EDGE:
				{
					setStyleAttribute(attributes, "stroke", "fromterminator", "toterminator", "line", "hidden", (v) -> settings.edgeStyle = v);
					setAttribute(attributes, "color", Parser::parseColor, (v) -> settings.edgeColor = v);
					break;
				}

				case A:
				{
					// node
					// mountpoint
					// menuitem
					String href = attributes.getValue("href"); // href CDATA #REQUIRED
					String target = attributes.getValue("target"); // target CDATA #IMPLIED
					link = href;
					break;
				}

				case IMG:
				{
					// tree
					// nodes
					// node
					// treeedge
					// edge
					// default.treeedge
					// default.edge
					String src = attributes.getValue("src"); // src CDATA #REQUIRED
					String state = states.peek();
					switch (state)
					{
						case NODE:
						{
							stack.peek().setImageFile(src);
							break;
						}
						case TREEEDGE:
						{
							stack.peek().setEdgeImageFile(src);
							break;
						}
						case EDGE:
						{
							edge.setImageFile(src);
							break;
						}
						case TREE:
						{
							settings.backgroundImageFile = src;
							break;
						}
						case NODES:
						{
							settings.defaultNodeImage = src;
							break;
						}
						case DEFAULT_TREEEDGE:
						{
							settings.defaultTreeEdgeImage = src;
							break;
						}
						case DEFAULT_EDGE:
						{
							settings.defaultEdgeImage = src;
							break;
						}
					}
					break;
				}

				case LABEL:
				{
					// node
					// treeedge
					// edge
					// menuitem
					break;
				}

				case CONTENT:
				{
					// node
					break;
				}

				case MOUNTPOINT:
				{
					mountpoint = new MountPoint.Mounting();
					setAttribute(attributes, "now", Parser::parseBoolean, (v) -> mountpoint.now = v);
					break;
				}

				case TOOLS:
				{
					break;
				}

				case MENU:
				{
					settings.menu = new ArrayList<>();
					break;
				}

				case MENUITEM:
				{
					String action = attributes.getValue("action"); // action (goto|search|focus|GOTO|SEARCH|FOCUS) #REQUIRED
					String match_target = attributes.getValue("match-target"); // match-target CDATA #IMPLIED
					String match_scope = attributes.getValue("match-scope"); // match-scope (label|content|link|id|LABEL|CONTENT|LINK|ID) #IMPLIED
					String match_mode = attributes.getValue("match-mode"); // match-mode (equals|startswith|includes|EQUALS|STARTSWITH|INCLUDES) #IMPLIED
					MenuItem menuItem = new MenuItem();
					menuItem.action = MenuItem.Action.valueOf(action);
					menuItem.target = match_target;
					menuItem.matchScope = Utils.stringToScope(match_scope);
					menuItem.matchMode = Utils.stringToMode(match_mode);
					assert settings.menu != null;
					settings.menu.add(menuItem);
					// label
					// link
					break;
				}
			}

			states.push(qName);
		}

		@Override
		public void endElement(String uri, String localName, @NonNull String qName)
		{
			states.pop();

			switch (qName)
			{
				case NODE:
				{
					stack.pop();
					break;
				}

				case EDGE:
				{
					edge = null;
					break;
				}

				case LABEL:
				{
					// node
					// treeedge
					// edge
					// menuitem
					String text = getText();
					String state = states.peek();
					switch (state)
					{
						case NODE:
						{
							stack.peek().setLabel(text);
							break;
						}
						case TREEEDGE:
						{
							stack.peek().setEdgeLabel(text);
							break;
						}
						case EDGE:
						{
							assert edge != null;
							edge.setLabel(text);
							break;
						}
						case MENUITEM:
						{
							assert settings.menu != null;
							settings.menu.get(settings.menu.size() - 1).label = text;
							break;
						}
					}
					break;
				}

				case CONTENT:
				{
					// node
					String text = getText();
					stack.peek().setContent(text);
					break;
				}

				case IMG:
				{
					if (NODE.equals(states.peek()))
					{
						// img comes after label
						MutableNode node = stack.peek();
						String img = node.getImageFile();
						if (img != null && !img.isEmpty())
						{
							String content = node.getContent();
							if (content != null && !content.isEmpty())
							{
								node.setContent(toContent(content, node.getImageFile()));
							}
						}
					}
					break;
				}

				case A:
				{
					// node
					// mountpoint
					// menuitem
					String state = states.peek();
					switch (state)
					{
						case NODE:
							stack.peek().setLink(link);
							link = null;
							break;

						case MENUITEM:
							settings.menu.get(settings.menu.size() - 1).link = link;
							link = null;
							break;

						case MOUNTPOINT:
							mountpoint.url = link;
							link = null;
							break;
					}
					break;
				}

				case MOUNTPOINT:
				{
					stack.peek().setMountPoint(mountpoint);
					mountpoint = null;
					break;
				}

				case MENUITEM:
				{
					MenuItem menuItem = null;
					break;
				}
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
		{
			if (textSb == null)
			{
				textSb = new StringBuilder();
			}
			else
			{
				textSb.append(ch, start, length);
			}
		}

		@NonNull
		private String getText()
		{
			assert textSb != null;
			String text = textSb.toString().trim();
			textSb.setLength(0);
			return text;
		}

		/**
		 * Get result
		 *
		 * @return model
		 */
		@NonNull
		public Model getResult()
		{
			return new Model(new Tree(root, edges), settings);
		}
	}

	private static Integer parseColor(final String color)
	{
		if (color == null)
		{
			return null;
		}
		return Integer.parseInt(color, 16);
	}

	private static Integer parseInt(final String val)
	{
		if (val == null)
		{
			return null;
		}
		return Integer.parseInt(val);
	}

	private static Float parseFloat(final String val)
	{
		if (val == null)
		{
			return null;
		}
		return Float.parseFloat(val);
	}

	private static float[] parseFloats(final String val)
	{
		if (val == null)
		{
			return null;
		}
		return Utils.stringToFloats(val);
	}

	private static Double parseDouble(final String val)
	{
		if (val == null)
		{
			return null;
		}
		return Double.parseDouble(val);
	}

	private static Boolean parseBoolean(final String val)
	{
		if (val == null)
		{
			return null;
		}
		return Boolean.parseBoolean(val);
	}

	private static void setAttribute(final Attributes attributes, final String qName, final Consumer<String> consumer)
	{
		String val = attributes.getValue(qName);
		if (val != null)
		{
			consumer.accept(val);
		}
	}

	private static <T> void setAttribute(final Attributes attributes, final String qName, final Function<String, T> transformer, final Consumer<T> consumer)
	{
		String val = attributes.getValue(qName);
		if (val != null)
		{
			T val2 = transformer.apply(val);
			if (val2 != null)
			{
				consumer.accept(val2);
			}
		}
	}

	/**
	 * Set style attribute
	 *
	 * @param attributes          attributes
	 * @param strokeQName         stroke qname
	 * @param fromTerminatorQName from-terminator qname
	 * @param toTerminatorQName   to-terminator qname
	 * @param lineQName           line qname
	 * @param hiddenQName         hidden qname
	 * @param consumer            consumer of produced value
	 */
	private static void setStyleAttribute(final Attributes attributes, @SuppressWarnings("SameParameterValue") final String strokeQName, @SuppressWarnings("SameParameterValue") final String fromTerminatorQName, @SuppressWarnings("SameParameterValue") final String toTerminatorQName, @SuppressWarnings("SameParameterValue") final String lineQName, @SuppressWarnings("SameParameterValue") final String hiddenQName, final Consumer<Integer> consumer)
	{
		String stroke = attributes.getValue(strokeQName);
		String fromTerminator = attributes.getValue(fromTerminatorQName);
		String toTerminator = attributes.getValue(toTerminatorQName);
		String line = attributes.getValue(lineQName);
		String hidden = attributes.getValue(hiddenQName);

		Integer sval = Utils.parseStyle(stroke, fromTerminator, toTerminator, line, hidden);
		if (sval != null)
		{
			consumer.accept(sval);
		}
	}

	private static @NonNull String toContent(@NonNull final String content, @Nullable final String imageSrc)
	{
		if (imageSrc != null && !imageSrc.isEmpty())
		{
			@NonNull String sb = "<p><img src='" + //
					imageSrc + //
					"' style='float:left;margin-right:10px;'/>" + //
					content + //
					"</p>";
					/*
					sb.append("<table><tr><td valign='top'><img src='");
					sb.append(imageSrc);
					sb.append("'/></td><td>");
					sb.append(content);
					sb.append("</td></tr></table>");
					*/
			return sb;
		}
		return content;
	}

	/**
	 * Make parser
	 *
	 * @return SAX parser
	 * @throws ParserConfigurationException parser configuration exception
	 * @throws SAXException                 sax exception
	 */
	public static SAXParser makeParser() throws ParserConfigurationException, SAXException
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		// @formatter:off
		try	{ factory.setFeature(XMLConstants.ACCESS_EXTERNAL_DTD, false);} catch(@NonNull final Exception ignored){}
		try	{ factory.setFeature(XMLConstants.ACCESS_EXTERNAL_SCHEMA, false);} catch(@NonNull final Exception ignored){}
		try	{ factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false); } catch(@NonNull final Exception ignored){}
		// @formatter:on
		return factory.newSAXParser();
	}

	/**
	 * Make model
	 *
	 * @param filePath command-line arguments
	 * @return model
	 * @throws ParserConfigurationException parser configuration exception
	 * @throws SAXException                 sax exception
	 * @throws IOException                  io exception
	 */
	public static Model makeModel(@NonNull final String filePath) throws ParserConfigurationException, SAXException, IOException
	{
		@NonNull SAXParser saxParser = makeParser();
		@NonNull SaxHandler handler = new SaxHandler();
		saxParser.parse(filePath, handler);
		return handler.getResult();
	}

	/**
	 * Main
	 *
	 * @param args command-line arguments
	 * @throws ParserConfigurationException parser configuration exception
	 * @throws SAXException                 sax exception
	 * @throws IOException                  io exception
	 */
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException
	{
		@NonNull Model model = makeModel(args[0]);
		System.out.println(ModelDump.toString(model));
	}
}
