package com.enterprise.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

//A sample test client 

public class RestClient {
	public static void main(String args[]) {
		DefaultHttpClient httpClient;

		httpClient = new DefaultHttpClient();
		try {

			// GET result
			HttpGet getRequest = new HttpGet(
					"http://localhost:8080/todoList/api/todos/");
			getRequest.addHeader("accept", "application/json");
			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code :"
						+ response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(response.getEntity().getContent())));
			String output;
			System.out.println("GET result\n-------------------\n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			br.close();
			httpClient.getConnectionManager().shutdown();

			// POST Result
			httpClient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://localhost:8080/todoList/api/todos");

			try {
				httppost.setHeader("Content-type", "application/json");
				httppost.setEntity(new StringEntity("{\"task\":\"" + "sindhu"
						+ "\",\"status\":\"" + "monica" + "\"}"));
				HttpResponse postresponse = httpClient.execute(httppost);
				System.out.println("Posted!");
			} catch (Exception e) {
				e.printStackTrace();
			}
			httpClient.getConnectionManager().shutdown();

			// PUT Result
			try {
				httpClient = new DefaultHttpClient();
				HttpPut httpput = new HttpPut(
						"http://localhost:8080/todoList/api/todos/13");
				httpput.setHeader("Content-type", "application/json");
				httpput.setEntity(new StringEntity("{\"status\":\"" + "done"
						+ "\"}"));
				HttpResponse putResponse = httpClient.execute(httpput);
				System.out.println("Updated!");
			} catch (Exception e) {
				e.printStackTrace();
			}
			httpClient.getConnectionManager().shutdown();

			// DELETE Result
			httpClient = new DefaultHttpClient();
			HttpDelete httpDelete = new HttpDelete(
					"http://localhost:8080/todoList/api/todos/12");
			HttpResponse httpResponse = httpClient.execute(httpDelete);
			HttpEntity deleteentity = httpResponse.getEntity();
			final String deleteresponse = EntityUtils.toString(deleteentity);
			System.out.println("Delete Response = " + deleteresponse);

			httpClient.getConnectionManager().shutdown();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
