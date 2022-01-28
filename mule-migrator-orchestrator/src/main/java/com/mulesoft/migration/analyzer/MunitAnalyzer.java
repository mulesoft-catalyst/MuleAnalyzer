package com.mulesoft.migration.analyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mulesoft.migration.beans.FileMetaDataBean;

public class MunitAnalyzer {

	private static Logger logger = LogManager.getLogger(MunitAnalyzer.class);
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		// Get Document Builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		// Build Document
		String flowFile = "/Users/dsuneja/AnypointStudio/architectWS/jobapplicationapi/src/test/munit/jobapplication-test-suite.xml";
		// String dwlFilename =
		// "/Users/mvijayvargia/Documents/POC/ap-workspaces/trails-api/cdhs-trails2decl-prvdr-batch-app/src/main/mule/cdhs-trails2decl-prvdr-batch-app.xml";
		Document document = builder.parse(new File(flowFile));
//	      Document document = builder.parse(new File("/Users/mvijayvargia/Downloads/Mule3/Mule/acsessftpservice/src/main/app/globalconfig.xml"));

		// Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();

		// Here comes the root node
		Element root = document.getDocumentElement();

		NodeList nList = document.getElementsByTagName("mule");
		//System.out.println("=============START ANALYZING THE FILE===============");

		Map<String, List<String>> componentMap = new HashMap<>();

		visitChildNodes(nList, componentMap, "Test", new FileMetaDataBean());

		//System.out.println(componentMap);
	}

	public static void visitChildNodes(NodeList nList, Map<String, List<String>> componentMap, String fileName,
			FileMetaDataBean fileMetaDataBean) {

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String nodeName = node.getNodeName();
				logger.debug("node name is " + nodeName);

				if (node.hasChildNodes() && nodeName.equalsIgnoreCase("munit:test")) {
					fileMetaDataBean.setNumberOfTests(fileMetaDataBean.getNumberOfTests() + 1);
					addNodeToMap(componentMap, nodeName);
				} else {
					visitChildNodes(node.getChildNodes(), componentMap, fileName, fileMetaDataBean);
				}
			}

		}
	}

	private static void addNodeToMap(Map<String, List<String>> componentMap, String nodeName) {
		List<String> list = componentMap.get(nodeName);
		if (list == null || list.size() == 0) {
			list = new ArrayList<>();

		}
		list.add((new Integer(list.size() + 1)).toString());
		componentMap.put(nodeName, list);

	}
}
