package toni.druck.xml;

import org.jdom2.Document;
import org.jdom2.Element;



/*****
 * 
 * @author Thomas Nill
 * 
 * Klasse um den JDOM2 Tree zu durchlaufen und seine Elemente zu besuchen
 * 
 */
public abstract class TreeWalker {

	/**
	 * Ein Dokument durchlaufen = beim Root Element starten
	 * 
	 * @param doc
	 */
	public void walkAlong(Document doc) {
		Element root = doc.getRootElement();
		walkAlong(root);
	}

	/**
	 * Ein Element durchlaufen
	 * 
	 * @param elem
	 */
	protected void walkAlong(Element elem) {
		if (elem.getChildren().size() == 0)
			return;

		goUp(elem);
		for (Object e : elem.getChildren()) {
			if (e instanceof Element) {
				bearbeite((Element) e);
				walkAlong((Element) e);
			}
		}
		goDown(elem);

	}

	protected void goUp(Element elem) {
	}

	abstract protected void bearbeite(Element e);
	
	protected void goDown(Element elem) {
	}
}
