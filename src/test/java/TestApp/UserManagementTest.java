package TestApp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.Assert;

import com.EgenSolution.service.UserService;

public class UserManagementTest {

	private static final String Dummy = "dummy";
	JsonDump jd = new JsonDump();

	// Start the server and Mongo db
	public static void SparkMongoSet() {
		System.out.println("Start the Server and Mongo");
		UserService us = new UserService(Dummy, Dummy);
	}

	public void ModuleTest() throws IOException {
		// Testing of create user web service
		String resType = "";

		resType = perform("/createUser", jd.User_Create_JSON, "PUT");
		Assert.assertEquals(jd.Msg_User_Created, resType);

		resType = perform("/getAllUsers", null, "GET");
		Assert.assertEquals(jd.Msg_Users_Dont_Exit, resType);

		resType = perform("/updateUser", null, "GET");
		Assert.assertEquals(jd.Msg_User_Updated, resType);

	}

	@SuppressWarnings("unused")
	private String perform(String requestURL, String userCreateJson, String requestType) throws IOException {
		HttpURLConnection con = null;
		// Connection setUp
		final URL url = new URL("http://localhost:4567" + requestURL);
		con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod(requestType);
		con.setRequestProperty("Content-Type", "application/json");

		// if requested Json is not null then send JSON data and find set the
		// connection properties
		if (userCreateJson != null && !userCreateJson.isEmpty()) {
			con.setRequestProperty("Content-Length", Integer.toString(userCreateJson.getBytes().length));
			con.setRequestProperty("Content-Language", "en-US");
			// Collect the JSON write in into op stream and send the request
			DataOutputStream writeJson = new DataOutputStream(con.getOutputStream());
			writeJson.writeBytes(userCreateJson);
			writeJson.close();
		}

		// Get the JSON response
		DataInputStream din = null;
		if (con.getResponseCode() != 200) {
			din = (DataInputStream) con.getErrorStream();
		} else
			din = (DataInputStream) con.getInputStream();
		String requiredOp = din.toString();
		return requiredOp;
	}
}
