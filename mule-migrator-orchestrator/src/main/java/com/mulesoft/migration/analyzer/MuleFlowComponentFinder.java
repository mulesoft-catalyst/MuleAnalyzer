package com.mulesoft.migration.analyzer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MuleFlowComponentFinder {

	private static final String FILENAME = "/Users/mvijayvargia/Downloads/Mule3/Mule/acsessftpservice/src/main/app/globalconfig.xml";

	private static Logger logger = LogManager.getLogger(MuleFlowComponentFinder.class);
	public static void main(String[] args) {

		// Instantiate the Factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			// optional, but recommended
			// process XML securely, avoid attacks like XML External Entities (XXE)
			dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

			// parse XML file
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db.parse(new File(FILENAME));

			// optional, but recommended
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			logger.debug("Root Element :" + doc.getDocumentElement().getNodeName());
			logger.debug("------");

			// get <staff>
			NodeList list = doc.getElementsByTagName("mule");

			for (int temp = 0; temp < list.getLength(); temp++) {

				Node node = list.item(temp);

				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element element = (Element) node;

					NodeList childNodes = element.getChildNodes();

					for (int temp1 = 0; temp1 < childNodes.getLength(); temp1++) {
						Node childNode = list.item(temp);
						logger.debug("childNode.getNodeName()::" + childNode.getNodeName());
					}

				}
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

	}

}
