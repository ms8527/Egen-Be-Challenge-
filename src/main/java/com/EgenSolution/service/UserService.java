package com.EgenSolution.service;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import com.EgenSolution.Database.DatabaseService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;

//Created by Mohit Sahni on 5th July 2016.
public class UserService {

	private final DatabaseService databaseService;

	public UserService(String databaseName, String collection) {

		databaseService = new DatabaseService(databaseName, collection);

	}

	public String createUser(String userJSON) throws JsonProcessingException {

		System.out.println("Inside UserService.createUser() with json: " + userJSON);
		final Document document = Document.parse(userJSON);

		final String UserId = document.getString("id");

		System.out.println("Checking in DB if user with id: " + UserId + " exist");
		// Creates filter to check the entered userid with the inserted userid
		// from MongoDb
		final Document user = databaseService.getDbCollection().find(Filters.eq("id", UserId)).first();

		// If found, update
		if (user == null) {
			System.out.println("User with id: " + UserId + " does not exist in DB, so creating new User");
			databaseService.getDbCollection().insertOne(document);
			return Json_to_String(new Response("User Created", 201));
		}

		System.out.println("User with id: " + UserId + " already exist in DB, so not creating new User");
		return Json_to_String(new Response("User Creation Failed", 500));
	}

	public String getAllUsers() throws JsonProcessingException {

		final List<Document> documents = new ArrayList<>();
		// Creates a projection that excludes the _id field.
		final MongoCursor<Document> cursor = databaseService.getDbCollection().find()
				.projection(Projections.excludeId()).iterator();
		while (cursor.hasNext()) {
			// System.out.println("Cursor::::::::"+cursor.next().toString());
			documents.add(cursor.next());

		}

		if (documents.size() == 0) {
			return "No Users Present";
		} else {
			// Else pretty print is used for printing each user
			final List<String> users = new ArrayList<>();
			for (final Document document : documents) {
				users.add(Json_to_String(document));
			}
			// to join a Collection of all users we can use the string.join
			return String.join("\n", users);
		}
	}

	public String updateUser(String updatedUserJSON) throws JsonProcessingException {

		System.out.println("Inside UserService.updateUser() with json: " + updatedUserJSON);
		final Document document = Document.parse(updatedUserJSON);

		final String id = document.getString("id");

		System.out.println("Checking in DB if user with id: " + id + " exist");
		final Document user = databaseService.getDbCollection().find(Filters.eq("id", id)).first();

		// If found, update
		if (user != null) {
			System.out.println("User with id: " + id + " exist in DB, so updating it");
			databaseService.getDbCollection().updateOne(user, new Document("$set", document));

			return Json_to_String(new Response("User Updated", 200));
		}

		else {
			System.out.println("User with id: " + id + " does not exist in DB, so sending 404 error");
			return Json_to_String(new Response("User Not Found", 404));
		}

	}

	private String Json_to_String(final Object object) throws JsonProcessingException {
		final ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
	}
}
