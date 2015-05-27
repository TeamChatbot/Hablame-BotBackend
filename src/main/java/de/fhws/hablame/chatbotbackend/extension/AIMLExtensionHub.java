package de.fhws.hablame.chatbotbackend.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.alicebot.ab.AIMLProcessorExtension;
import org.alicebot.ab.ParseState;
import org.w3c.dom.Node;

/**
 * This class works as a collection of the other realized extensions 
 * and implements the {@link AIMLProcessorExtension}.<br>
 * So when the chatbot gets instantiated all the extensions get loaded over 
 * this class' {@link #createFromExtensions(List)} method.
 */
public class AIMLExtensionHub implements AIMLProcessorExtension {
	
	/**
	 * private attribute in form of a {@link List} of {@link AIMLProcessorExtension}
	 */
	private List<AIMLProcessorExtension> extensions = new ArrayList<AIMLProcessorExtension>();
	
	//TODO: check if private constructor like original is necessary, 
	//or there is another (better) solution (e.g. the constructor)
	
	/**
	 * Static method to create the {@link AIMLExtensionHub} with all given {@link #extensions}
	 * @param extensions
	 * @return
	 */
	public static AIMLExtensionHub createFromExtensions(List<AIMLProcessorExtension> extensions) {
		AIMLExtensionHub ret = new AIMLExtensionHub();
	    ret.extensions.addAll( extensions );
	    return ret;
	}

	/**
	 * <b>DOKU:</b><br>
	 * <i>Provide the AIMLProcessor with a list of extension tag names.</i>
	 * In this case all the tags from each extension in one {@link Set}.
	 */
	@Override
	public Set<String> extensionTagSet() {
		Set<String> set = new TreeSet<String>();
		for (AIMLProcessorExtension extension : extensions) {
	      set.addAll( extension.extensionTagSet() );
	    }
	    return set;
	}

	/**
	 * <b>DOKU:</b><br>
	 * <i>recursively evaluate AIML from a node corresponding an extension tag</i>
	 */
	@Override
	public String recursEval(Node node, ParseState parseState) {
		String ret = null;
		for(AIMLProcessorExtension extension : extensions) {
			if (extension.extensionTagSet().contains(node.getNodeName())) {
				if (ret != null) {
					//simply copied the warning from original
					System.err.println("Warning: Overwriting return message "+ret);
					System.err.println(getClass().getName() + " contains multiple occurences of tag "+node.getNodeName());
					System.err.println("Latest occurence of tag in class "+extensions.getClass().getName());
				}
				//recursively call the method
				ret = extension.recursEval(node, parseState);
			}
		}
		return ret;
	}

}
