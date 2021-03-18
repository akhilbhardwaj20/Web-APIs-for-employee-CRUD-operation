package com.enterprise.rest;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//CRUD implementation using ReST
//Client: RestClient.java or POSTMAN

@Path("/todos")
public class todos {
	Connection connection = null;

	/*
	 * R-Retrieve 
	 * Verb: GET (get a specific record) 
	 * URL: http://localhost:8080/ToDo/api/todos/5
	 */
	@Path("/{id}")
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML})
	public String sendTodo(@PathParam("id") String dynamicId)
			throws SQLException {
		SQLConnection c = new SQLConnection();
		connection = c.connect();

		JSONObject json = new JSONObject();
		try {
			PreparedStatement query = null;
			query = connection.prepareStatement("select task, status "
					+ "from todos " + "where id = ? ");
			query.setInt(1, Integer.parseInt(dynamicId));
			ResultSet rs = query.executeQuery();
			while (rs.next()) {
				json.put("id", dynamicId);
				json.put("task", rs.getString(1));
				json.put("status", rs.getString(2));
			}
			connection.close();
			return (json.toString());

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * R-Retrieve 
	 * Verb: GET (get all the records) 
	 * URL: http://localhost:8080/ToDo/api/todos
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML})
	public String sendTodoList() throws JSONException, SQLException {
		SQLConnection c = new SQLConnection();
		connection = c.connect();
		PreparedStatement query = null;
		query = connection.prepareStatement("select * " + "from todos ");

		JSONArray jsons = new JSONArray();

		ArrayList<JSONObject> todolist = new ArrayList<JSONObject>();

		ResultSet rs = query.executeQuery();
		while (rs.next()) {
			JSONObject json1 = new JSONObject();
			json1.put("id", rs.getInt(1));
			json1.put("task", rs.getString(2));
			json1.put("status", rs.getString("status"));
			todolist.add(json1);
		}
		System.out.println("TODO List size: " + todolist.size());
		for (int i = 0; i < todolist.size(); i++) {
			jsons.put(i, todolist.get(i));
		}
		connection.close();

		return jsons.toString();
	}

	/*
	 * C-Create 
	 * Verb: POST (insert new data) 
	 * URL: http://localhost:8080/ToDo/api/todos/ 
	 * Input: {"task":"blah",
	 * 		   "status":"blah"}
	 */
	@POST
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public String createTodoTask(String str) throws JSONException, SQLException {
		JSONObject obj1 = new JSONObject(str);

		SQLConnection c = new SQLConnection();
		connection = c.connect();
		PreparedStatement query = null;
		query = connection.prepareStatement("insert into todos "
				+ "(task, status) " + "VALUES (?, ?)");

		query.setString(1, obj1.getString("task"));
		query.setString(2, obj1.getString("status"));
		query.executeUpdate();

		connection.close();

		return obj1.toString();
	}

	/*
	 * U-Update 
	 * Verb: PUT (update the status of a specific record) 
	 * URL: http://localhost:8080/ToDo/api/todos/12
	 * Input: {"status":"blahblah"}
	 */
	@Path("/{id}")
	@PUT
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
	public String updateTask(@PathParam("id") int id,String str) throws JSONException, SQLException {
		JSONObject obj1 = new JSONObject(str);

		SQLConnection c = new SQLConnection();
		PreparedStatement query = null;
		connection = c.connect();
		query = connection.prepareStatement("update todos " + "set status = ? "
				+ "where id = ? ");

		query.setString(1, obj1.getString("status"));
		query.setInt(2, (id));
		query.executeUpdate();

		connection.close();
		return obj1.toString();
	}

	/*
	 * D-Delete 
	 * Verb: DELETE (delete a specific record) 
	 * URL: http://localhost:8080/ToDo/api/todos/5
	 */
	@Path("/{id}")
	@DELETE
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_HTML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
	public void deleteTask(@PathParam("id") int taskId) throws JSONException,
			SQLException {
		SQLConnection c = new SQLConnection();
		PreparedStatement query = null;
		connection = c.connect();
		query = connection.prepareStatement("delete from todos "
				+ "where id = ? ");
		query.setInt(1, (taskId));
		query.executeUpdate();
		connection.close();
	}
}