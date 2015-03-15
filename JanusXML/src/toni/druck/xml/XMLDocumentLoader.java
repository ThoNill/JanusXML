package toni.druck.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Vector;

import org.janus.data.DefaultClassFactory;
import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Parent;
import org.jdom2.input.SAXBuilder;



/*****
 * 
 * @author Thomas Nill
 * 
 * Läd ein JDOM2 Dokument aus einer Datei/Resource
 * Includiert auch andere XML-Dateien
 * 
 */
public class XMLDocumentLoader  {



	public Document createDocument(String name) {
		if (!name.endsWith(".xml")) {
			name = name + ".xml";
		}
		InputStream resource = DefaultClassFactory.FACTORY.getResource(name);

		Document doc = LadeDocument(resource);
		Element root = doc.getRootElement();
		erstzeIncludes(root);
		return doc;
	}
	
	public Document createDocumentWithoutException(String name) {
		try {
			return createDocument(name);
			
		} catch (Exception ex) {
			return null;
		}
	}

	
	static public Document LadeDocument(InputStream in) {
		if (in == null) {
			throw new NullPointerException();
		}
		try {
			PackagePathFactory factory = new PackagePathFactory();
			factory.setPrefix("toni.druck.elements.");
			factory.setDir("templates/");

			SAXBuilder builder = new SAXBuilder();
			builder.setJDOMFactory(factory);
			Document document = builder.build(in);
			Element root = document.getRootElement();
			erstzeIncludes(root);

			return document;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	static public Element LoadRoot(InputStream in) {
		try {
			return LadeDocument(in).getRootElement();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	static void ersetzeElementDurchTemplate(Element source, Element template,
			String sternValue) {
		ersetzePlacesDurchIntos(source, template);
		for (Attribute a : source.getAttributes()) {
			ersetzeAlleAttribute(template, a.getName(), a.getValue(),
					sternValue);
		}
		ersetzeZielDurchDieKinderDerQuelle(source, template);
	}

	static private HashMap<String, Element> extractHashOfElementsWithName(
			Element source, String name) {
		HashMap<String, Element> childs = new HashMap<String, Element>();
		for (Content c : source.getDescendants()) {
			if (c instanceof Element && name.equals(((Element) c).getName())) {
				Element e = (Element) c;
				childs.put(e.getAttributeValue("name"), e);
			}
		}
		return childs;
	}

	static private Vector<Element> extractVectorOfElementsWithName(
			Element source, String name) {
		Vector<Element> childs = new Vector<Element>();
		for (Content c : source.getContent()) {
			if (c instanceof Element && name.equals(((Element) c).getName())) {
				childs.add((Element) c);
			}
		}
		return childs;
	}

	static private void ersetzePlacesDurchIntos(Element source, Element template) {
		Vector<Element> intos = extractVectorOfElementsWithName(source, "INTO");
		HashMap<String, Element> places = extractHashOfElementsWithName(
				template, "PLACE");
		for (Element into : intos) {
			Element target = places.get(into.getAttributeValue("name"));
			if (target != null) {
				ersetzeZielDurchDieKinderDerQuelle(target, into);
			} else {
				new RuntimeException(source.getName() + " hat keinen Platz "
						+ into.getAttributeValue("name"));
			}
		}
	}

	static private void ersetzeAttribut(Attribute attrib, String oldValue,
			String newValue) {
		if (oldValue.equals(attrib.getValue())) {
			attrib.setValue(newValue);
		}
	}

	static private void ersetzeSternInAttribut(Attribute attrib, String value) {
		String oldValue = attrib.getValue();
		if (oldValue.indexOf('*') >= 0) {
			attrib.setValue(oldValue.replaceAll("\\*", value));
		}
	}

	static private void ersetzeAttributeEinesElements(Element elem,
			String oldValue, String newValue, String sternValue) {
		for (Attribute a : elem.getAttributes()) {
			ersetzeAttribut(a, oldValue, newValue);
			ersetzeSternInAttribut(a, sternValue);
		}
	}

	static private void ersetzeAlleAttribute(Element elem, String oldValue,
			String newValue, String sternValue) {
		ersetzeAttributeEinesElements(elem, oldValue, newValue, sternValue);
		for (Content c : elem.getDescendants()) {
			if (c instanceof Element) {
				ersetzeAttributeEinesElements((Element) c, oldValue, newValue,
						sternValue);
			}
		}
	}

	static private void erstzeIncludes(Element elem) {
		Vector<IncludeElement> includes = new Vector<IncludeElement>();
		for (Content c : elem.getDescendants()) {
			if (c instanceof IncludeElement) {
				includes.add((IncludeElement) c);
			}
		}
		for (IncludeElement c : includes) {
			c.replace();
		}

	}

	static private void ersetzeZielDurchDieKinderDerQuelle(Element target,
			Element source) {
		Parent p = target.getParent();
		int index = p.indexOf(target);
		target.detach();

		Vector<Content> childs = new Vector<Content>();
		for (Content c : source.getChildren()) {
			childs.add(c);
		}

		for (Content c : childs) {
			c.detach();
			p.addContent(index, c);
			index++;
		}
	}

}
