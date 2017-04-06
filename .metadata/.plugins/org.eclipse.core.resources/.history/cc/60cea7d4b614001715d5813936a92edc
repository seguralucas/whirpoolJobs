package exit.services.principal.peticiones;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;

import exit.services.fileHandler.CSVHandler;
import exit.services.json.JSONHandler;
import exit.services.singletons.RecuperadorPropiedadedConfiguracionEntidad;

public abstract class UpdateAbstractoRightNow {
	
	public Object realizarPeticion( JSONHandler json){
		return realizarPeticion(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUrl(),null,json);
	}
	
	public Object realizarPeticion(Long id, JSONHandler json){
		return realizarPeticion(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUrl(),id,json);
	}
	 
	 public Object realizarPeticion(String url,Long id, JSONHandler json){
	        try{
	        	WSConector ws = new WSConector("UPDATERIGHTNOW",url+"/"+id,"application/json");
	        	HttpURLConnection conn=ws.getConexion();
	        	DataOutputStream wr = new DataOutputStream(
	        			conn.getOutputStream());
	        	wr.write(json.toStringNormal().getBytes("UTF-8"));
	        	wr.flush();
	        	wr.close();
	            int responseCode = conn.getResponseCode();
	            BufferedReader in;
	            Object o;
	            if(responseCode == 200){
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getInputStream()));
	            	o=procesarPeticionOK(in, id,responseCode,  json);
	            	
	            }
	            else{
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getErrorStream()));
	            	o=procesarPeticionError(in,id,responseCode, json);
	            }
	            return o==null?in:o;	 
	            }	                
           catch (ConnectException e) {
				CSVHandler csv= new CSVHandler();
				try {
					csv.escribirCSV(CSVHandler.PATH_ERROR_SERVER_NO_ALCANZADO_BORRADO, "ID no eliminado: "+id);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return null;
			}
           catch (Exception e) {
				CSVHandler csv= new CSVHandler();
				csv.escribirErrorException(json,e.getStackTrace());
				return null;
			}
       }
		abstract Object procesarPeticionOK(BufferedReader in, Long id,int responseCode, JSONHandler json) throws Exception;
		abstract Object procesarPeticionError(BufferedReader in, Long id, int responseCode, JSONHandler json) throws Exception;
}
