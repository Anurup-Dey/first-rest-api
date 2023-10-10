package com.anurup.springboot.firstrestapi.survey;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Base64;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SurveyResourceIntegrationTest {

	private static String SPECIFIC_QUESTION_URL = "/surveys/Survey1/questions/Question1";
	private static String GENERIC_QUESTION_URL = "/surveys/Survey1/questions";
	private static String SPECIFIC_SURVEY_URL = "/surveys/survey1";
	private static String GENERIC_SURVEY_URL = "/surveys";

	
	

	@Autowired
	private TestRestTemplate template;

	String str = """

						{
			"id": "Question1",
			"description": "Most Popular Cloud Platform Today",
			"options": [
			"AWS",
			"Azure",
			"Google Cloud",
			"Oracle Cloud"
			],
			"correctAnswer": "AWS"
			}
						""";
	

	@Test
	void retreiveAllSurveysTest() throws JSONException {
		ResponseEntity<String> responseEntity = template.getForEntity(GENERIC_SURVEY_URL, String.class);
		String expectedResponse = """
								[
							{
							"id": "Survey1",
							"title": "My Favorite Survey",
							"description": "Description of the Survey",
							"questions": [
							{
							"id": "Question1",
							"description": "Most Popular Cloud Platform Today",
							"options": [
							"AWS",
							"Azure",
							"Google Cloud",
							"Oracle Cloud"
							],
							"correctAnswer": "AWS"
							},
							{
							"id": "Question2",
							"description": "Fastest Growing Cloud Platform",
							"options": [
							"AWS",
							"Azure",
							"Google Cloud",
							"Oracle Cloud"
							],
							"correctAnswer": "Google Cloud"
							},
							{
							"id": "Question3",
							"description": "Most Popular DevOps Tool",
							"options": [
							"Kubernetes",
							"Docker",
							"Terraform",
							"Azure DevOps"
							],
							"correctAnswer": "Kubernetes"
							}
							]
							}
				]
								""";

		// Status
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		// Content-Type:"application/json",
		assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

		JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
	}

	@Test
	void retreiveSurveyByIdTest() throws JSONException {
		ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_SURVEY_URL, String.class);
		String expectedResponse = """
				
			{
			"id": "Survey1",
			"title": "My Favorite Survey",
			"description": "Description of the Survey",
			"questions": [
			{
			"id": "Question1",
			"description": "Most Popular Cloud Platform Today",
			"options": [
			"AWS",
			"Azure",
			"Google Cloud",
			"Oracle Cloud"
			],
			"correctAnswer": "AWS"
			},
			{
			"id": "Question2",
			"description": "Fastest Growing Cloud Platform",
			"options": [
			"AWS",
			"Azure",
			"Google Cloud",
			"Oracle Cloud"
			],
			"correctAnswer": "Google Cloud"
			},
			{
			"id": "Question3",
			"description": "Most Popular DevOps Tool",
			"options": [
			"Kubernetes",
			"Docker",
			"Terraform",
			"Azure DevOps"
			],
			"correctAnswer": "Kubernetes"
			}
			]
			}

				""";

		// Status
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		// Content-Type:"application/json",
		assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

		JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
	}

	@Test
	void retreiveSurveyQuestionByIdTest() throws JSONException {
		ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_QUESTION_URL, String.class);
		String expectedResponse = """
				{
					"id":"Question1",
					"description":"Most Popular Cloud Platform Today",
					"correctAnswer":"AWS"
				}
				""";

		// Status
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		// Content-Type:"application/json",
		assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

		JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
	}

	@Test
	void retreiveAllSurveyQuestionsTest() throws JSONException {
		ResponseEntity<String> responseEntity = template.getForEntity(GENERIC_QUESTION_URL, String.class);
		String expectedResponse = """

				[
					{
						"id": "Question1"
					},
					{
						"id": "Question2"
					},
					{
						"id": "Question3"
					}
				]
								""";

		// Status
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		// Content-Type:"application/json",
		assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));

		JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
	}
	
	@Test
	void addNewSurveyQuestion_basicScenario() {

		String requestBody = """
					{
					  "description": "Your Favorite Language",
					  "options": [
					    "Java",
					    "Python",
					    "JavaScript",
					    "Haskell"
					  ],
					  "correctAnswer": "Java"
					}
				""";

		
		//
		
		HttpHeaders headers = createHttpContentTypeAndAuthorizationHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, headers);
		
		ResponseEntity<String> responseEntity 
			= template.exchange(GENERIC_QUESTION_URL, HttpMethod.POST, httpEntity, String.class);
		
		assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
		
		String locationHeader = responseEntity.getHeaders().get("Location").get(0);
		assertTrue(locationHeader.contains("/surveys/Survey1/questions/"));
		
		//DELETE
		//locationHeader
		
		template.delete(locationHeader);
		
	}

	private HttpHeaders createHttpContentTypeAndAuthorizationHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Basic " + performBasicAuthEncoding("admin","password"));
		return headers;
	}
	String performBasicAuthEncoding(String user, String password) {
		String combined = user + ":" + password;
		byte[] encodedBytes = Base64.getEncoder().encode(combined.getBytes());
		return new String(encodedBytes);
	}
	
}
