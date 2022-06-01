package com.mulesoft.migration.analyzer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mulesoft.migration.beans.ProjectMetaDataBean;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MuleConfigParser {

	public static void main(String[] args) {

		String FILENAME = "/Users/mvijayvargia/Downloads/Mule3/Mule/sfloginservice/mule-project.xml";
		ProjectMetaDataBean metaData = null;
		getMuleProjectMetaData(FILENAME, metaData);

	}

	public static Map<String, String> getMuleProjectMetaData(String FILENAME, ProjectMetaDataBean projectMetaDataBean) {
		// Instantiate the Factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Map<String, String> projectMetaData = new HashMap<String, String>();
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

//	          System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
//	          System.out.println("------");

			// get <staff>
			NodeList list = doc.getElementsByTagName("mule-project");

			for (int temp = 0; temp < list.getLength(); temp++) {

				Node node = list.item(temp);

				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element element = (Element) node;

					// get staff's attribute
					String runtimeId = element.getAttribute("runtimeId");
					String schemaVersion = element.getAttribute("schemaVersion");

					projectMetaData.put("runtimeId", runtimeId);
					projectMetaData.put("schemaVersion", schemaVersion);
					projectMetaDataBean.setMuleVersion(runtimeId);
//	                  System.out.println("runtimeId::" +  runtimeId);
//	                  System.out.println("schemaVersion::" +  schemaVersion);

				}
			}

		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return projectMetaData;
	}

}
