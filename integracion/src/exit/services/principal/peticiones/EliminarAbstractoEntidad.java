package exit.services.principal.peticiones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;

import exit.services.fileHandler.CSVHandler;
import exit.services.singletons.RecuperadorPropiedadedConfiguracionEntidad;

public abstract class EliminarAbstractoEntidad{
	
	public Object realizarPeticion(){
		return realizarPeticion(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUrl(),null);
	}
	
	public Object realizarPeticion(Long id){
		return realizarPeticion(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUrl(),id);
	}
	 
	 public Object realizarPeticion(String url,Long id){
	        try{
	        	WSConector ws = new WSConector("DELETE",url+"/"+id,"application/json");
	        	HttpURLConnection conn=ws.getConexion();
	            int responseCode = conn.getResponseCode();
	            BufferedReader in;
	            Object o;
	            if(responseCode == 200){
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getInputStream()));
	            	o=procesarPeticionOK(in, id,responseCode);
	            	
	            }
	            else{
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getErrorStream()));
	            	o=procesarPeticionError(in,id,responseCode);
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
				csv.escribirErrorException("Error al eliminar id: "+id,e.getStackTrace(),false);
				return null;
			}
       }
		abstract Object procesarPeticionOK(BufferedReader in, Long id,int responseCode) throws Exception;
		abstract Object procesarPeticionError(BufferedReader in, Long id, int responseCode) throws Exception;}
