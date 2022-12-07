/*
 * Copyright (c) 2022. Bernard Bou
 */

package treebolic.provider.xml.stax;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import treebolic.annotations.NonNull;
import treebolic.annotations.Nullable;
import treebolic.model.*;


/**
 * StAX parser
 */
public class Parser
{
	private static final String TREEBOLIC = "treebolic";

	private static final String TREE = "tree";
	private static final String NODES = "nodes";
	private static final String NODE = "node";
	private static final String EDGES = "edges";
	private static final String EDGE = "edge";
	private static final String LABEL = "label";

	/**
	 * StAX handler
	 *
	 * @param reader reader
	 * @return model
	 * @throws XMLStreamException xml stream exception
	 */
	@NonNull
	public static Model parse(@NonNull XMLEventReader reader) throws XMLStreamException
	{
		@Nullable Stack<MutableNode> stack = new Stack<>();

		@Nullable Map<String, MutableNode> nodes = null;

		@Nullable INode root = null;

		@Nullable List<IEdge> edges = null;

		@Nullable MutableNode node = null;

		@Nullable MutableEdge edge = null;

		while (reader.hasNext())
		{
			XMLEvent nextEvent = reader.nextEvent();

			if (nextEvent.isStartElement())
			{
				StartElement startElement = nextEvent.asStartElement();
				switch (startElement.getName().getLocalPart())
				{
					case NODES:
						nodes = new HashMap<>();
						break;

					case NODE:
						Attribute idAttribute = startElement.getAttributeByName(new QName("id"));
						String id = idAttribute.getValue();
						INode parent = stack.empty() ? null : stack.peek();
						node = new MutableNode(parent, id);
						assert nodes != null;
						nodes.put(id, node);
						stack.push(node);
						if (parent == null)
						{
							root = node;
						}
						break;

					case EDGES:
						edges = new ArrayList<>();
						break;

					case EDGE:
						Attribute fromAttribute = startElement.getAttributeByName(new QName("from"));
						Attribute toAttribute = startElement.getAttributeByName(new QName("to"));
						String from = fromAttribute.getValue();
						String to = toAttribute.getValue();
						assert nodes != null;
						INode fromNode = nodes.get(from);
						INode toNode = nodes.get(to);
						edge = new MutableEdge(fromNode, toNode);
						assert edges != null;
						edges.add(edge);
						break;

					case LABEL:
						break;
				}
			}

			else if (nextEvent.isEndElement())
			{
				EndElement endElement = nextEvent.asEndElement();
				switch (endElement.getName().getLocalPart())
				{
					case NODE:
					{
						stack.pop();
						break;
					}
					case NODES:
					{
						node = null;
						break;
					}
					case EDGES:
					{
						edge = null;
						break;
					}
				}
			}

			else if (nextEvent.isCharacters())
			{
				String text = nextEvent.asCharacters().getData();
				if (!text.isEmpty())
				{
					text = text.replace('\n', ' ');
					text = text.trim();
					if (!text.isEmpty())
					{
						if (node != null)
						{
							node.setLabel(text);
						}
						else if (edge != null)
						{
							edge.setLabel(text);
						}
						else
						{
							System.err.println("TEXT " + text);
						}
					}
				}
			}
		}
		return new Model(new Tree(root, edges), new Settings());
	}

	/**
	 * Main
	 *
	 * @param args command-line arguments
	 * @throws IOException        io exception
	 * @throws XMLStreamException xml stream exception
	 */
	public static void main(@NonNull String[] args) throws IOException, XMLStreamException
	{
		try (@NonNull FileReader fr = new FileReader(args[0]))
		{
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader reader = factory.createXMLEventReader(fr);
			@NonNull Model model = parse(reader);

			System.out.println(ModelDump.toString(model));
		}
	}
}
