package toni.druck.xml;

import org.jdom2.Element;


/*****
 * 
 * @author Thomas Nill
 * 
 * Ein JDOM2 Element, welches eine Referenz auf ein externes Objekt enthält
 * 
 */
public class JDOMElementWithReference extends Element {
	private Object reference;

	public JDOMElementWithReference(String name, Object reference) {
		super(name);
		this.reference = reference;
	}

	public Object getReference() {
		return reference;
	}


}
