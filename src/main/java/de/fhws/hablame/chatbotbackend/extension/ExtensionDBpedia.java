package de.fhws.hablame.chatbotbackend.extension;

import java.util.Set;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.AIMLProcessorExtension;
import org.alicebot.ab.ParseState;
import org.alicebot.ab.Utilities;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.w3c.dom.Node;

import de.fhws.hablame.chatbotbackend.utility.ExtensionStringHolder;

/**
 * This class is an extension to query factual knowledge from dbpedia using sparql
 * @author Christian
 */
public class ExtensionDBpedia implements AIMLProcessorExtension {
	
	private Set<String> extensionTagNames = Utilities.stringSet(ExtensionStringHolder.POPULATIONTAG2);

	@Override
	public Set<String> extensionTagSet() {
		return extensionTagNames;
	}
	
	@Override
	public String recursEval(Node node, ParseState parseState) {
		String answer = "";
		String input = AIMLProcessor.evalTagContent(node, parseState, null);
		//User Question: WIE VIELE BEWOHNER HAT *
		if (node.getNodeName().equals(ExtensionStringHolder.POPULATIONTAG2)) {
			answer = Integer.toString(getPopulation(input));
		}
		return answer;
	}
	
	/**
	 * Method to get the actual population of a city/country by making a sparql query against the dbpedia endpoint (only working for english input)
	 */
	private int getPopulation(String input) {
		int population = 0;
		String service = "http://dbpedia.org/sparql";
		String query = "PREFIX dbo: <http://dbpedia.org/ontology/>" + 
						"PREFIX dbp: <http://dbpedia.org/property/>" + 
						"PREFIX dbr: <http://dbpedia.org/resource/>" + 
						"select ?populationTotal where {dbr:" + input + " dbo:populationTotal ?populationTotal}";
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService(service, query);
		ResultSet resultSet = queryExecution.execSelect();
		
		while(resultSet.hasNext()) {
			QuerySolution querySolution = resultSet.nextSolution();
			Literal literal = ((Literal) querySolution.getLiteral("populationTotal"));
			population = literal.getInt();
		}	
		queryExecution.close();
		return population;
	}
}