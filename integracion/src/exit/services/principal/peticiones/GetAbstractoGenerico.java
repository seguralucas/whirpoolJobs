package exit.services.principal.peticiones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;

import exit.services.fileHandler.CSVHandler;
import exit.services.singletons.ApuntadorDeEntidad;
import exit.services.singletons.RecuperadorPropiedadedConfiguracionEntidad;

public abstract class GetAbstractoGenerico {	
	public Object realizarPeticion(){
		return realizarPeticion(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUrl(),null);
	}
	
	public Object realizarPeticionString(String url){
		return realizarPeticion(url,null);
	}
	
	 public Object realizarPeticion(String url,Long id){
	        try{
	        	WSConector ws;
	        	if(id!=null)
	        		 ws = new WSConector("GET",url+"/"+id);
	        	else
	        		 ws = new WSConector("GET",url);
	        	HttpURLConnection conn=ws.getConexion();
	            int responseCode = conn.getResponseCode();
	            BufferedReader in;
	            Object o;
	            if(responseCode == 200){
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getInputStream()));
	            	if(id!=null)
	            		o=procesarPeticionOK(in, id,responseCode);
	            	else
	            		o=procesarPeticionOK(in,responseCode);
	            	
	            }
	            else{
	            	in = new BufferedReader(
		                    new InputStreamReader(conn.getErrorStream()));
	            	if(id!=null)
	            		o=procesarPeticionError(in,id,responseCode);
	            	else
	            		o=procesarPeticionError(in,responseCode);
	            }
	            return o;	 
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
				if(id!=null)
					csv.escribirErrorException("Error al recuperar el registro con id: "+id,e.getStackTrace(),false);
				else
					csv.escribirErrorException("Error al recuperar registros de la entidad "+ApuntadorDeEntidad.getInstance().getEntidadActual(),e.getStackTrace(),false);
				return null;
			}
      }
		abstract Object procesarPeticionOK(BufferedReader in, Long id,int responseCode) throws Exception;
		abstract Object procesarPeticionError(BufferedReader in, Long id, int responseCode) throws Exception;
		abstract Object procesarPeticionOK(BufferedReader in, int responseCode) throws Exception;
		abstract Object procesarPeticionError(BufferedReader in, int responseCode) throws Exception;

}
