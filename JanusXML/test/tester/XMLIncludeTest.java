package tester;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.log4j.Logger;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.junit.Assert;
import org.junit.Test;

import toni.druck.xml.XMLDocumentLoader;

public class XMLIncludeTest {
    private static final Logger LOG = Logger.getLogger(XMLIncludeTest.class);
    private static final String AUSNAHME_AUFGETRETEN = "erwartete Ausnahme aufgetreten";
    private static final String UNERWARTETE_AUSNAHME = "unerwartete Ausnahme";

	@Test
	public void loadDocument1() {
		try {
			new XMLDocumentLoader()
					.createDocument("testdaten/testtemplates.xml");
		} catch (Exception e) {
		    LOG.error(UNERWARTETE_AUSNAHME,e);
			Assert.fail("Exception " + e.getMessage());
		}
	}

	@Test
	public void fehlt1() {
		try {
			Document page = new XMLDocumentLoader()
					.createDocument("testdaten/testtemplates1.xml");
			Assert.fail("Keine Exception ");
		} catch (Exception e) {
		    LOG.error(AUSNAHME_AUFGETRETEN,e);
		}
	}
	
	@Test
	public void fehlt2() {
		try {
			Document page = new XMLDocumentLoader()
					.createDocumentWithoutException("testdaten/testtemplates1.xml");
			assertNull(page);
		} catch (Exception e) {
		    LOG.error(UNERWARTETE_AUSNAHME,e);
			Assert.fail("Exception " + e.getMessage());
		}
	}

	@Test
	public void loadDocument2() {
		try {
			Document page = new XMLDocumentLoader()
					.createDocument("testdaten/testtemplates.xml");
			Element elem = getElement(page, "include");
			assertNull(elem);
			elem = getElement(page, "footer");
			assertNotNull(elem);

		} catch (Exception e) {
		    LOG.error(UNERWARTETE_AUSNAHME,e);
			Assert.fail("Exception " + e.getMessage());
		}
	}

	@Test
	public void loadDocument3() {
		try {
			Document page = new XMLDocumentLoader()
					.createDocument("testdaten/testtemplates.xml");
			Element elem = getElement(page, "gehtVerloren");
			assertNull(elem);
			elem = getElement(page, "kommtDazu");
			assertNotNull(elem);
		} catch (Exception e) {
		    LOG.error(UNERWARTETE_AUSNAHME,e);
			Assert.fail("Exception " + e.getMessage());
		}
	}

	public Element getElement(Document document,String name) {

		Element root = document.getRootElement();
		for (Content content : root.getDescendants()) {
			if (content instanceof Element) {
				Element elem = (Element) content;
				if (name.equals(elem.getAttributeValue("name"))) {
					return elem;
				}
			}
		}
		return null;
	}
}
