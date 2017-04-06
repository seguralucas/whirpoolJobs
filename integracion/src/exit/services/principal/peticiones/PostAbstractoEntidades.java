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
import exit.services.singletons.peticiones.RecuperadorPeticiones;

public abstract class PostAbstractoEntidades {
	public Object realizarPeticion(JSONHandler json){
		return realizarPeticion(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUrl(),json);
	}
	
	 public Object realizarPeticion(String url, JSONHandler json){
	        try{
	        	WSConector ws = new WSConector("POST",url,"application/json");
	        	HttpURLConnection conn=ws.getConexion();
	        	DataOutputStream wr = new DataOutputStream(
	        			conn.getOutputStream());
	        	wr.write(json.toStringNormal().getBytes("UTF-8"));
	        	wr.flush();
	        	wr.close();
	            int responseCode = conn.getResponseCode();
	            BufferedReader in;
	            Object o;
	            if(responseCode == RecuperadorPeticiones.getInstance().getPost().getCodigoResponseEsperado()){
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getInputStream()));
	            	o=procesarPeticionOK(in, json,responseCode);
	            	
	            }
	            else{
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getErrorStream()));
	            	o=procesarPeticionError(in,json,responseCode);
	            }
	            return o==null?in:o;	 
	            }	                
            catch (ConnectException e) {
				CSVHandler csv= new CSVHandler();
				try {
					csv.escribirCSV(CSVHandler.PATH_ERROR_SERVER_NO_ALCANZADO, json.getLine());
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
		abstract Object procesarPeticionOK(BufferedReader in, JSONHandler json,int responseCode) throws Exception;
		abstract Object procesarPeticionError(BufferedReader in, JSONHandler json, int responseCode) throws Exception;	 
}
