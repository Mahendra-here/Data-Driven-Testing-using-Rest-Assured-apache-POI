package DataDrivenTesting;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import java.io.IOException;
import org.testng.annotations.DataProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

public class LMS_API {

	
	// online resourcee https://medium.com/@jidhiya_6779/data-driven-testing-using-rest-assured-fead113f8eeb
	
	{
		baseURI = "https://entr-dev.loialcard.com";
		basePath = "/api/login";
	}

	@Test(dataProvider = "logincredentialsdata")
	public void Login(String username, String password) {
		JSONObject data = new JSONObject();
		data.put("USERNAME", username);
		data.put("PASSWORD", password);
		Response resp_prog_details = given().auth().basic("Admin", "password")
				.header("Content-Type", "application/json").body(data.toJSONString()).when().post().then()
				.assertThat().statusCode(200).log().all().extract().response();
		int statusCode = resp_prog_details.getStatusCode();
		Assert.assertEquals(statusCode, 200);
		System.out.println("The Response code" + statusCode);
		String responseBody = resp_prog_details.getBody().asPrettyString();
		//Assert.assertEquals(responseBody.contains(progdesc), true);
		//Assert.assertEquals(responseBody.contains(progname), true);
	}

	@DataProvider(name = "logincredentialsdata")
	String[][] get_prog_data() throws IOException {
		String path = System.getProperty("user.dir") + "/src/test/java/DataDrivenTesting/ProgData.xlsx";
		int rownum = XLUtils.getRowCount(path, "Sheet1");
		int colnum = XLUtils.getCellCount(path, "Sheet1", 1);		
		String progdata[][] = new String[rownum][colnum];
		for (int i = 1; i <= rownum; i++) {
			for (int j = 0; j < colnum; j++) {
				progdata[i - 1][j] = XLUtils.getCellData(path, "Sheet1", i, j);
			}
		}
		return progdata;
	}
}