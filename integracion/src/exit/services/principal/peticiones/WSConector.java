package exit.services.principal.peticiones;


import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import org.json.simple.JSONObject;

import Decoder.BASE64Encoder;
import exit.services.singletons.RecuperadorPropiedadedConfiguracionEntidad;
import exit.services.singletons.peticiones.Peticion;
import exit.services.singletons.peticiones.RecuperadorPeticiones;



public class WSConector {
	 private HttpURLConnection conexion;
	 
	 private URL url;
	
	 public WSConector(String method,String url,String contentType) throws Exception{
		 	this.url = new URL(url);
		 	initConecction(method,contentType);
	 }
	 
	 public WSConector(String method,String url) throws Exception{
		 	this(method,url,null);
	 }
	 
	 public WSConector(String method) throws Exception{
		 	this(method,RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUrl());
	 }
	
	private void initConecction(String method, String contentType) throws Exception{
		HttpURLConnection conn=null;
		if(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUsaProxy().equalsIgnoreCase("SI")){
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getIpProxy(), Integer.parseInt(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getPuertoProxy())));
			conn = (HttpURLConnection) url.openConnection(proxy);
		}
		else
			conn = (HttpURLConnection) url.openConnection();
		Peticion peticion = null;
		if(method.equalsIgnoreCase("POST")){
			peticion=RecuperadorPeticiones.getInstance().getPost();
		}
		else if(method.equalsIgnoreCase("UPDATERIGHTNOW")){
			peticion=RecuperadorPeticiones.getInstance().getUpdate();
		}
		else if(method.equalsIgnoreCase("GET")){
			peticion=RecuperadorPeticiones.getInstance().getGet();
		}
		else if(method.equalsIgnoreCase("DELETE")){
			peticion=RecuperadorPeticiones.getInstance().getDelete();
		}
		conn.setRequestMethod(peticion.getPeticion());
		if(peticion.getCabecera()!=null && peticion.getCabecera().length()>0)
			try{completarCabecera(conn,ConvertidorJson.convertir(peticion.getCabecera()));}	catch (Exception e) {e.printStackTrace();}

		if(contentType!=null)
			conn.setRequestProperty("Content-Type", contentType);
		conn.setRequestProperty("charset", "UTF-8");
		conn.setDoOutput(true);

/*		String userPassword = RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUser() + ":" + RecuperadorPropiedadedConfiguracionEntidad.getInstance().getPassword();
		BASE64Encoder encode= new BASE64Encoder();
		String encoding = encode.encode(userPassword.getBytes());
		conn.setRequestProperty("Authorization", "Basic " + encoding);	 
		conn.setRequestProperty("OSvC-CREST-Suppress-All", "true");	 */
		completarCabecera(conn,ConvertidorJson.convertir(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getCabecera()));
		this.conexion= conn;
		
	}
	
	private void completarCabecera(HttpURLConnection conn,JSONObject json) throws Exception{
		for (Object key : json.keySet()) {
			if(key instanceof String && json.get(key) instanceof String)
				conn.setRequestProperty((String) key, (String)json.get(key));
			
		}
	}

	public HttpURLConnection getConexion() {
		return conexion;
	}
	 
	

	 
	 
	 
	 
	
}
