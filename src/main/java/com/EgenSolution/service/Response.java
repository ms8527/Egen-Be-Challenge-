package com.EgenSolution.service;

//Created by Mohit Sahni on 5th July 2016.
public class Response {

	private final String responseMessage;
	private final int responseCode;

	public Response(final String responseMessage, final int responseCode) {
		this.responseMessage = responseMessage;
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return this.responseMessage;
	}

	public int getStatus() {
		return this.responseCode;
	}
}
