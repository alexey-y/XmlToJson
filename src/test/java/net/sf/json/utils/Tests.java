package net.sf.json.utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

public class Tests {

	
	@Test
	public void testBasicTask() throws Exception
	{
		Stream<String> stream = Files.lines(Paths.get(Thread.currentThread().getContextClassLoader().getResource("manifest.xml").toURI()));
		String contentAsLines = stream.collect(Collectors.joining());
		String json = XmlToJson.parseAsJson(XmlToJson.createDOMFromText(contentAsLines));
		System.out.println(json);

		ReadContext ctx = JsonPath.parse(json);
		
		List<Map> protectionHeaders = ctx.read("$..ProtectionHeader");
		System.out.println(protectionHeaders);
	}

}
