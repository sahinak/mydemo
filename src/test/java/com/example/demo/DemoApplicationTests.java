package com.example.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.sun.net.httpserver.HttpServer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DemoApplicationTests {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(DemoApplicationTests.class);

	final int PORT = 5000;
	private HttpServer server;
	private MyHandler handler;
	@LocalServerPort
	private int randomPort;
	private final String name = "image";
	private final String value = "test";
	String decodedValue = null;

	@Before
	public void startMockServer() throws JSONException, IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("startMockServer() - start");
		}
		server = HttpServer.create(new InetSocketAddress(PORT), 0);
		handler = new MyHandler();
		server.createContext("/api/image", handler);
		server.setExecutor(null); // creates a default executor
		server.start();

		if (logger.isDebugEnabled()) {
			logger.debug("startMockServer() - end");
		}
	}

	@After
	public void stopMockServer() {
		if (logger.isDebugEnabled()) {
			logger.debug("stopMockServer() - start");
		}

		server.stop(0);

		if (logger.isDebugEnabled()) {
			logger.debug("stopMockServer() - end");
		}
	}

	@Test
	public void test0() {
	}

	@Test
	public void test1() throws URISyntaxException, JSONException {
		if (logger.isDebugEnabled()) {
			logger.debug("test1() - start");
		}

		RestTemplate restTemplate = new RestTemplate();
		final String baseUrl = "http://localhost:" + randomPort + "/api/image";
		URI uri = new URI(baseUrl);
		JSONObject request = new JSONObject();
		JSONObject response = new JSONObject();
		request.put(name, value);
		String json = request.toString();
		String b64Value = new String(Base64.getEncoder().encode(value.getBytes())); // Run assert on this

		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Data attached to the request.
		HttpEntity<String> requestBody = new HttpEntity<String>(json, headers);

		// Send request with POST method.
		ResponseEntity<String> res = restTemplate.postForEntity(uri, requestBody, String.class);
		if (res.getStatusCode() == HttpStatus.OK) {
			response = new JSONObject(res.getBody());
		}
		logger.info("test1 vale from response=" + response.getString(name));
		Assert.assertEquals(response.get(name), b64Value);
		this.decodedValue = new String(Base64.getDecoder().decode((String) response.get(name)));
		logger.info("test1 vale from decodedR=" + this.decodedValue);
		Assert.assertEquals(value, decodedValue);

		if (logger.isDebugEnabled()) {
			logger.debug("test1() - end");
		}
	}
}
