package exit.services.principal.peticiones.vtex.funciones;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exit.services.fileHandler.CSVHandler;
import exit.services.principal.peticiones.AbstractHTTP;
import exit.services.principal.peticiones.EPeticiones;
import exit.services.principal.peticiones.vtex.GetVTEXEmailDesencriptado;
import exit.services.principal.peticiones.vtex.GetVTEXOMS;
import exit.services.singletons.RecuperadorPropiedadedConfiguracionEntidad;

public class FuncionesWhirpool {

	public String splitearOrden(String cadena) {
		char[] chars=cadena.toCharArray();
		String salida="";
		boolean bandera=false;
		for(int i=0;i<chars.length;i++){
			if(chars[i] >='0' && chars[i]<='9'){
				bandera=true;
				salida+=chars[i];
			}
			else if(bandera)
				return salida;
		}
		return salida;
	}
	
	public String procesarFechaVTEX(String  dateOMS){
		/*Busca transformar la fecha del formato 2017-04-28T10:25:22.0000000+00:00
		al formato 2017-04-28T10 10:25:22*/
		String[] aux=dateOMS.split("T");
		String fecha=aux[0];
		String[] time=aux[1].split(":");
		String seconds=time[2].split("\\.")[0];
		return fecha+" "+time[0]+":"+time[1]+":"+seconds;
	}
	
	/*public Object ejecutorServicioACsvVTEXOMS(String cantidadDias) throws UnsupportedEncodingException{
		AbstractHTTP getVTEXGenerico= new GetVTEXOMS();
		String identificadorAtr=RecuperadorPropiedadedConfiguracionEntidad.getInstance().getIdentificadorAtributo();
		String cabeceraUrl=RecuperadorPropiedadedConfiguracionEntidad.getInstance().getFiltros().replaceAll(identificadorAtr+"NRO_PAG"+identificadorAtr, String.valueOf(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getPaginaActual()));
		Integer cantDias=Integer.parseInt(cantidadDias);
		String parametroDate=getParametroDateOMG(cantDias);
		String parametrosFianl;
		if(cantDias==-1)
			parametrosFianl=cabeceraUrl;
		else
			parametrosFianl=cabeceraUrl+"&"+parametroDate;
	
		System.out.println(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUrl()+"?"+parametrosFianl);		
		return getVTEXGenerico.realizarPeticion(EPeticiones.GET,RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUrl()+"?"+parametrosFianl);
	}*/
	
/*	public String descriptarEmailVtex(String emailEncriptado, JSONObject params){
		try{
		String instancia=(String)params.get("instancia");
		String url=(String)params.get("url");
		AbstractHTTP getEmailDescriptado= new GetVTEXEmailDesencriptado();
		Object result=getEmailDescriptado.realizarPeticion(EPeticiones.GET,url+"?alias="+emailEncriptado+"&an="+instancia);
		if(result==null)
			throw new Exception();
		return (String)result;
		}
		catch(Exception e){
			e.printStackTrace();
			System.out.println("Se guarda el valor del email encriptado");
			CSVHandler csv= new CSVHandler();
			try {
				csv.escribirCSV(CSVHandler.PATH_LOG_GENERICO, "No se pudo descriptar el email: "+ emailEncriptado+". Se proceder� a guardarlo encriptado");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return emailEncriptado;
		}
	}*/
	
	public String descriptarEmailVtex(String emailEncriptado, JSONObject params){
		try{
			String instancias[]=((String)params.get("instancia")).split(",");
			String url=(String)params.get("url");
			int i=0;
			String result=emailEncriptado;
			while(i<instancias.length && result.contains("ct.vtex.com.br")){
				String instancia=instancias[i];
				AbstractHTTP getEmailDescriptado= new GetVTEXEmailDesencriptado();
				try{
				String aux=(String)getEmailDescriptado.realizarPeticion(EPeticiones.GET,url+"?alias="+result+"&an="+instancia);
				result=aux==null?result:aux;
				}
				catch(Exception e){
					System.out.println("No descripto con: "+instancia);
				}
				i++;
			}
			if(result.contains("ct.vtex.com.br"))
				System.out.println("Se va a guardar el mail encriptado");
			else
				System.out.println("Se logr� desencriptar el e-mail");
			return (String)result;
		}
		catch(Exception e){ 
			return emailEncriptado;
		}
	}
	
	
	
	
}
