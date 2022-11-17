/**
 * Title : Treebolic provider
 * Description : Treebolic Provider
 * Version : 3.x
 * Copyright : (c) 2001-2014
 * Terms of use : see license agreement at http://treebolic.sourceforge.net/en/license.htm
 * Author : Bernard Bou
 *
 * Update : Jul 10, 2014
 */

package treebolic.provider.owl.owlapi;

import java.util.Set;

import org.coode.owlapi.manchesterowlsyntax.ManchesterOWLSyntaxEditorParser;
import org.semanticweb.owlapi.expression.OWLEntityChecker;
import org.semanticweb.owlapi.expression.ShortFormEntityChecker;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.BidirectionalShortFormProvider;
import org.semanticweb.owlapi.util.BidirectionalShortFormProviderAdapter;
import org.semanticweb.owlapi.util.ShortFormProvider;

class QueryParser
{
	private final OWLOntology rootOntology;

	private final BidirectionalShortFormProvider bidiShortFormProvider;

	/**
	 * Constructs a DLQueryParser using the specified ontology and short form provider to map entity IRIs to short names.
	 *
	 * @param rootOntology
	 *        The root ontology. This essentially provides the domain vocabulary for the query.
	 * @param shortFormProvider
	 *        A short form provider to be used for mapping back and forth between entities and their short names (renderings).
	 */
	public QueryParser(final OWLOntology rootOntology, final ShortFormProvider shortFormProvider)
	{
		this.rootOntology = rootOntology;
		final OWLOntologyManager manager = rootOntology.getOWLOntologyManager();
		final Set<OWLOntology> importsClosure = rootOntology.getImportsClosure();
		// Create a bidirectional short form provider to do the actual mapping.
		// It will generate names using the input short form provider.
		this.bidiShortFormProvider = new BidirectionalShortFormProviderAdapter(manager, importsClosure, shortFormProvider);
	}

	/**
	 * Parses a class expression string to obtain a class expression.
	 *
	 * @param classExpressionString
	 *        The class expression string
	 * @return The corresponding class expression if the class expression string is malformed or contains unknown entity names.
	 */
	public OWLClassExpression parseClassExpression(final String classExpressionString)
	{
		final OWLDataFactory dataFactory = this.rootOntology.getOWLOntologyManager().getOWLDataFactory();

		// Set up the real parser
		final ManchesterOWLSyntaxEditorParser parser = new ManchesterOWLSyntaxEditorParser(dataFactory, classExpressionString);
		parser.setDefaultOntology(this.rootOntology);

		// Specify an entity checker that wil be used to check a class expression contains the correct names.
		final OWLEntityChecker entityChecker = new ShortFormEntityChecker(this.bidiShortFormProvider);
		parser.setOWLEntityChecker(entityChecker);

		// Do the actual parsing
		return parser.parseClassExpression();
	}
}