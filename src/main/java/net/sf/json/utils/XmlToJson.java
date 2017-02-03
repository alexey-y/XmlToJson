package net.sf.json.utils;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.TextNode;

public class XmlToJson {
	
	public static Document createDOMFromText(String text) throws Exception
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder.parse(new InputSource( new StringReader( text )));
	}
	
	public static Document createDOMFromUrl(String url)
			throws Exception {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder.parse(url);
	}
		
	public static String parseAsJson(Document doc) throws Exception {
		doc.getDocumentElement().normalize();

		Element root = doc.getDocumentElement();

		Map rootAttrs = parse(root);
		
		ObjectMapper JSON_MAPPER = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
//		JSON_MAPPER.writeValue(new File("out.json"), rootAttrs);
		
		String json = JSON_MAPPER.writeValueAsString(rootAttrs);
		return json;
	}
	
	private static Map parse(Element root) {
//		String nodeName = root.getNodeName();
		Map rootAttrs = createAttrs(root);
		
		NodeList children = root.getChildNodes();
		
		for (int temp = 0; temp < children.getLength(); temp++) {

			Node node = children.item(temp);
			
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				continue;
			}

			Element element = (Element) node;
			String childName = element.getNodeName();
			List childrenWithSameName = (List) rootAttrs.get(childName);
			if (childrenWithSameName==null)
			{
				childrenWithSameName = new ArrayList();
				rootAttrs.put(childName, childrenWithSameName);
			}
			Map childAsMap = parse(element);
			childrenWithSameName.add(childAsMap);

		}
		
		return rootAttrs;
	}

	private static Map createAttrs(Element element) {
		Map asMap = new HashMap();
		String value = element.getNodeValue();
		if (value!=null)
		{
			asMap.put("_value", value);
		}
		else if (element.getFirstChild() instanceof Text)
		{	
			Text text = (Text) element.getFirstChild();
			String textValue = text.getNodeValue();
			if (textValue!=null)
			{	
				asMap.put("_value", textValue);
			}
		}
		NamedNodeMap attributes = element.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attributeNode = attributes.item(i);
			asMap.put(attributeNode.getNodeName(), attributeNode.getNodeValue());
		}
		return asMap;
	}
}
