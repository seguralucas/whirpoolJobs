package exit.services.principal.peticiones;


import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

import Decoder.BASE64Encoder;
import exit.services.singletons.RecuperadorPropiedadedConfiguracionEntidad;



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
		if(method.equalsIgnoreCase("POST")){
				conn.setRequestMethod("POST");
		}
		else if(method.equalsIgnoreCase("UPDATERIGHTNOW")){
			conn.setRequestMethod("POST");
			conn.setRequestProperty("X-HTTP-Method-Override", "PATCH");
		}
		else if(method.equalsIgnoreCase("GET")){
			conn.setRequestMethod("GET");
		}
		else if(method.equalsIgnoreCase("DELETE")){
			conn.setRequestMethod("DELETE");
		}
		if(contentType!=null)
			conn.setRequestProperty("Content-Type", contentType);
		conn.setRequestProperty("charset", "UTF-8");
		conn.setDoOutput(true);
		String userPassword = RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUser() + ":" + RecuperadorPropiedadedConfiguracionEntidad.getInstance().getPassword();
		BASE64Encoder encode= new BASE64Encoder();
		String encoding = encode.encode(userPassword.getBytes());
		conn.setRequestProperty("Authorization", "Basic " + encoding);	 
		conn.setRequestProperty("OSvC-CREST-Suppress-All", "true");	 
		this.conexion= conn;
		
	}

	public HttpURLConnection getConexion() {
		return conexion;
	}
	 
	

	 
	 
	 
	 
	
}
