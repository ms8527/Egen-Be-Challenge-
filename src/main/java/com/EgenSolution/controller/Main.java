package com.EgenSolution.controller;
import com.EgenSolution.service.UserService;
import spark.Spark;

//Created by Mohit Sahni on 5th July 2016.
public class Main {

	public static void main(String[] args) {

		UserService userService = new UserService("UserManagement", "UserInformation");
		Spark.post("/createUser", (request, response) -> userService.createUser(request.body()));
		Spark.get("/getAllUsers", (req, res) -> userService.getAllUsers());
		Spark.put("/updateUser", (request, response) -> userService.updateUser(request.body()));

	}

}
