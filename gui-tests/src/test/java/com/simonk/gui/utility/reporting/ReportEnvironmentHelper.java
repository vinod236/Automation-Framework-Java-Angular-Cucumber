package com.simonk.gui.utility.reporting;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ReportEnvironmentHelper {
	private static final Logger LOG = LogManager.getLogger(ReportEnvironmentHelper.class);

	private HashMap<String, String> envProps;
	private DocumentBuilderFactory docFactory;
	private DocumentBuilder docBuilder;
	private Document doc;
	private Element rootElement;
	private static final String xmlPath = "src/test/resources/allure_settings/environment.xml";
	private Element parameter;
	private Element name;
	private Element key;
	private Element value;
	private TransformerFactory transformerFactory;
	private Transformer transformer;
	private DOMSource source;

	public ReportEnvironmentHelper(HashMap<String, String> props) {
		logConfig(props);
		envProps = props;
		prepareData();
		saveXml();
	}

	private void logConfig(HashMap<String, String> props) {
		for (String name : props.keySet()) {
			  if (!props.get(name).isEmpty()) {
				LOG.info("Framework configurations: " + name + " ["+ props.get(name) + "]");
			  } else { 
				LOG.warn("Framework configurations: " + name + " [Not specified in the framework configuration]");
			  }
		}
		
	}

	private void prepareData() {
		try {
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();

			doc = docBuilder.newDocument();
			rootElement = doc.createElement("qa:environment");
			rootElement.setAttribute("xmlns:qa",
					"urn:model.commons.qatools.yandex.ru");
			doc.appendChild(rootElement);
			for (Entry<String, String> entry : envProps.entrySet()) {
				if (entry.getValue().isEmpty()) {
					entry.setValue("Not specified in the config");
				}

				parameter = doc.createElement("parameter");
				rootElement.appendChild(parameter);

				name = doc.createElement("name");
				name.appendChild(doc.createTextNode(entry.getKey()));
				parameter.appendChild(name);

				key = doc.createElement("key");
				key.appendChild(doc.createTextNode(entry.getKey()));
				parameter.appendChild(key);

				value = doc.createElement("value");
				value.appendChild(doc.createTextNode(entry.getValue()));
				parameter.appendChild(value);
			}
		} catch (ParserConfigurationException pce) {
			LOG.fatal("Parse Exception", pce);
		}
	}

	private void saveXml() {
		try {
			transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(xmlPath));
			transformer.transform(source, result);
		} catch (TransformerException tfe) {
			LOG.fatal("Transformer Exception", tfe);
		}

	}

}
