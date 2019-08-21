package com.example.demo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

class MyHandler implements HttpHandler {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MyHandler.class);

	public void handle(HttpExchange t) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("handle(HttpExchange) - start");
		}

		InputStream is = t.getRequestBody();
		String req = IOUtils.toString(is, "UTF-8");
		logger.info("handle(HttpExchange) req=" + req);
		t.sendResponseHeaders(200, req.length());
		OutputStream os = t.getResponseBody();
		os.write(req.getBytes());
		os.close();
		is.close();

		if (logger.isDebugEnabled()) {
			logger.debug("handle(HttpExchange) - end");
		}
	}
}