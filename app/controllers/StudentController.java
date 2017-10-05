package controllers;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import model.Student;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class StudentController extends Controller {

	private int idGenerator = 0;
	
	private List<Student> students = new ArrayList<Student>();

    private <T> T bindObject(JsonNode json, Class<T> entityClass) {
    	return Json.mapper().convertValue(json, entityClass);
    }
	
	public Result getStudents() {
		
		response().setHeader("Access-Control-Allow-Origin", "*");
		
		return ok(Json.toJson(students));
	}
	
	@BodyParser.Of(BodyParser.Json.class)
	public Result postStudent() {
		
		response().setHeader("Access-Control-Allow-Origin", "*");
		
		JsonNode json = request().body().asJson();
		Student student = bindObject(json, Student.class);
		student.id = idGenerator++;
		students.add(student);
		return ok(Json.toJson(student));
	}
	
	public Result preflight() {
		response().setHeader("Access-Control-Allow-Origin", "*");
		response().setHeader("Allow", "*");
		response().setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
		response().setHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept, Referer, User-Agent");
		return ok();
	}
	
	public Result emailUnique() {
		
		response().setHeader("Access-Control-Allow-Origin", "*");
		
		JsonNode json = request().body().asJson();
		String email = json.get("email").asText();
		// TODO: server-side validate email here...
		boolean unique = true;
		for (Student student : students) {
			if (student.email.equals(email)) {
				unique = false;
				break;
			}
		}
		ObjectNode node = Json.mapper().createObjectNode();
		node.put("unique", unique);
		return ok(Json.toJson(node));
	}
	
}
