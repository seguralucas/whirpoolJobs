import com.fasterxml.jackson.databind.ObjectMapper;

public class PropiedadesConfiguracion {

	public PropiedadesConfiguracion(){
		 ObjectMapper mapper = new ObjectMapper();
	      String jsonString = "{\"name\":\"Mahesh\", \"age\":21}";
	      
	      //map json to student
			
	      try{
	         Student student = mapper.readValue(jsonString, Student.class);
	         
	         System.out.println(student);
	         
	         mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
	         jsonString = mapper.writeValueAsString(student);
	         
	         System.out.println(jsonString);
	      }
	      catch (JsonParseException e) { e.printStackTrace();}
	      catch (JsonMappingException e) { e.printStackTrace(); }
	      catch (IOException e) { e.printStackTrace(); }
	   }
	}
	
}
