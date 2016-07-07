package com.EgenSolution.Database;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;


public class DatabaseService {
	// Created by Mohit Sahni on 5th July 2016.
	private final MongoCollection<Document> dbCollection;

	public DatabaseService(String databaseName, String collection) {

		final MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost"));
		dbCollection = mongoClient.getDatabase(databaseName).getCollection(collection);
	}

	public MongoCollection<Document> getDbCollection() {
		return dbCollection;
	}

}
