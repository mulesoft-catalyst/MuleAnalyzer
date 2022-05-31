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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mulesoft.migration.beans.FileMetaDataBean;

public class ParseUnknownXMLStructure {
	
	private static Logger logger = LogManager.getLogger(ParseUnknownXMLStructure.class);
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		// Get Document Builder
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		// Build Document
		String flowFile = "/Users/dsuneja/mule3/mule3Projects/IndependentReq/src/main/app/IndependentReq.xml";
		String dwlFilename = "/Users/mvijayvargia/Documents/POC/ap-workspaces/trails-api/cdhs-trails2decl-prvdr-batch-app/src/main/mule/cdhs-trails2decl-prvdr-batch-app.xml";
		Document document = builder.parse(new File(flowFile));
//      Document document = builder.parse(new File("/Users/mvijayvargia/Downloads/Mule3/Mule/acsessftpservice/src/main/app/globalconfig.xml"));

		// Normalize the XML Structure; It's just too important !!
		document.getDocumentElement().normalize();

		// Here comes the root node
		Element root = document.getDocumentElement();
//      System.out.println(root.getNodeName());

		// Get all employees
		NodeList nList = document.getElementsByTagName("mule");
		//System.out.println("=============START ANALYZING THE FILE===============");

		Map<String, List<String>> componentMap = new HashMap<String, List<String>>();
		visitChildNodes(nList, componentMap, "Test", new FileMetaDataBean());

		//System.out.println(componentMap);
	}

	// This function is called recursively
	public static void visitChildNodes(NodeList nList, Map<String, List<String>> componentMap, String fileName,
			FileMetaDataBean fileMetaDataBean) {

		for (int temp = 0; temp < nList.getLength(); temp++) {
//    	 System.out.println("--------------------------------------------------------------------------");
			Node node = nList.item(temp);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String nodeName = node.getNodeName();
				if (!(nodeName.equalsIgnoreCase("mule") || nodeName.equalsIgnoreCase("flow"))) { // Ignoring flow and
																									// mule xml tag
																									// names as
																									// connector
																									// configuration
																									// doesn't start
																									// with mule/flow
					addNodeToMap(componentMap, nodeName);
				}

//            System.out.println("Node Name = " + node.getNodeName() );
				// Check all attributes
				if (!(nodeName.equalsIgnoreCase("mule")) && node.hasAttributes()) {
					// get attributes names and values
					NamedNodeMap nodeMap = node.getAttributes();
					for (int i = 0; i < nodeMap.getLength(); i++) {
						Node tempNode = nodeMap.item(i);
						String arrtibuteName = tempNode.getNodeName();
						String arrtibuteValue = tempNode.getNodeValue();
						if (arrtibuteName.endsWith("name")) {
//                	   System.out.println("Attributes name : " + arrtibuteName+ "; Value = " + arrtibuteValue);
						}

					}
//               System.out.println("################################################");

				}

				if (node.hasChildNodes() && !(nodeName.equalsIgnoreCase("flow")
						|| nodeName.equalsIgnoreCase("choice-exception-strategy"))) {
					// We got more childs; Let's visit them as well
					visitChildNodes(node.getChildNodes(), componentMap, fileName, fileMetaDataBean);
				} else {
					visitFlowNodes(node.getChildNodes(), componentMap, fileMetaDataBean);
				}

			}
		}
	}

	private static void visitFlowNodes(NodeList nList, Map<String, List<String>> componentMap,
			FileMetaDataBean fileMetaDataBean) {

//	  System.out.println("Flow Node Start -------------------------------------->");

		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);
			if (node.getNodeType() == Node.ELEMENT_NODE) {

				String nodeName = node.getNodeName();
				if (nodeName.endsWith("set-payload")) {

//        		System.out.println("Node Type::"+ node.getNodeType()+ "::" + Node.TEXT_NODE);
//        		System.out.println("++++++++++++++++++++++ DWL Code Detected++++++++++++++++++" );
//        		System.out.println("NodeValue::"+ node.getNodeValue());
//        		System.out.println("TextContent::"+ node.getTextContent());
					String dwlCode = node.getTextContent();
					long numLines = dwlCode.split("\r\n|\r|\n").length;
					fileMetaDataBean.setDataWeaveCodeLength(fileMetaDataBean.getDataWeaveCodeLength() + numLines);
					logger.debug("# of lines of code ::" + fileMetaDataBean.getDataWeaveCodeLength());
				}

//            System.out.println("Node Name = " + node.getNodeName() );
				addNodeToMap(componentMap, nodeName);
				visitFlowNodes(node.getChildNodes(), componentMap, fileMetaDataBean);

			}
		}

//      System.out.println("<<-------------------------------------- Flow Node End");

	}

	private static void addNodeToMap(Map<String, List<String>> componentMap, String nodeName) {
		List<String> list = componentMap.get(nodeName);
		if (list == null || list.size() == 0) {
			list = new ArrayList<String>();
			list.add(nodeName);
		}
		list.add(nodeName);
		componentMap.put(nodeName, list);

	}
}
