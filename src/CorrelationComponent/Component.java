package CorrelationComponent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An emulation of a general DEECo component.
 * 
 * @author Dominik Skoda <skoda@d3s.mff.cuni.cz>
 */
public class Component {

	/**
	 * The type of the component (e.g. "firefighter", "vehicle", ...).
	 */
	private String componentType;
	/**
	 * The ID of the component. Unique for each component.
	 */
	private int componentId;
	/**
	 * The knowledge of the component. The key here is the label of a piece of knowledge
	 * and the value is the knowledge value.
	 */
	private Map<String, Object> knowledge;

	/**
	 * Create an instance of a component with specified ID and type.
	 * @param componentId The ID of the component. Needs to be unique for each component.
	 * @param componentType The type of the component.
	 */
	public Component(int componentId, String componentType) {
		knowledge = new HashMap<String, Object>();

		this.componentId = componentId;
		this.componentType = componentType;
	}

	/**
	 * Set the given value to the piece of knowledge identified by the given label.
	 * @param label Identifies the piece of knowledge that will be set.
	 * @param value The value to be set.
	 */
	public void setKnowledge(String label, Object value) {
		this.knowledge.put(label, value);
	}

	/**
	 * Returns all the knowledge of the component.
	 * @return All the knowledge of the component.
	 */
	public Map<String, Object> getKnowledge() {
		return knowledge;
	}

	/**
	 * Returns the value of the knowledge identified by the given label.
	 * If the knowledge of the component doesn't contain knowledge for the
	 * specified label a null is returned.
	 * @param label Identifies the requested piece of knowledge.
	 * @return The value of the knowledge identified by the given label.
	 * If the knowledge for the given label is not present null is returned.
	 */
	public Object getKnowledge(String label) {
		if(knowledge.containsKey(label)){
			return knowledge.get(label);
		}

		return null;
	}
	
	/**
	 * Indicates whether the component contains knowledge for the given label.
	 * @param label identifies the knowledge.
	 * @return True if the component contains knowledge identified by the given label.
	 * False otherwise.
	 */
	public boolean containsKnowledge(String label){
		return knowledge.containsKey(label);
	}

	/**
	 * Returns the ID of the component.
	 * @return The ID of the component.
	 */
	public int getId() {
		return componentId;
	}

	/**
	 * Returns the type of the comonent.
	 * @return The type of the comonent.
	 */
	public String getType() {
		return componentType;
	}

	/**
	 * Provides all the labels of knowledge contained in the component.
	 * @return All the labels of knowledge contained in the component.
	 */
	public Set<String> getKnowledgeLabels() {
		return knowledge.keySet();
	}

}
