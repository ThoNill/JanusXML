package toni.druck.xml;

import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Element;


/*****
 * 
 * @author Thomas Nill
 * 
 * Inkludiert eine andere XML Datei anstatt des Inckude Elements.
 * Dises Element wird durch den Inhalt von filename ersetzt.
 * 
 */
public class IncludeElement extends Element {
	private static int zaehler = 1;
	private String sternValue;
	private String filename;

	public IncludeElement() {
		super();
		inc();
	}


	public IncludeElement(String name,String filename) {
		super(name);
		this.filename = filename;
		inc();
	}

	private synchronized void inc() {
		sternValue = "" + zaehler;
		zaehler++;
	}

	public void replace() {
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(filename);
		if (in != null) {
			Element template = XMLDocumentLoader.LoadRoot(in);
			XMLDocumentLoader.ersetzeElementDurchTemplate(this, template, sternValue);
			
		}
	}

	static public boolean hasTemplate(String name) {
		InputStream in = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(name);
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}
}
