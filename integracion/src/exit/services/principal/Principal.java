package exit.services.principal;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import exit.services.fileHandler.DirectorioManager;
import exit.services.principal.ejecutores.ParalelizadorDistintosFicheros;
import exit.services.singletons.ApuntadorDeEntidad;
import exit.services.singletons.RecuperadorFormato;
import exit.services.singletons.RecuperadorMapeoCsv;
import exit.services.singletons.RecuperadorPropiedadedConfiguracionEntidad;

public class Principal {
	public static final String UPDATE_CONTACTOS="UPDATE_CONTACTOS";
	public static final String INSERTAR_CONTACTOS="INSERTAR_CONTACTOS";
	public static final String INSERTAR_INCIDENTES="INSERTAR_INCIDENTES";
	public static final String BORRAR_INCIDENTES="BORRAR_INCIDENTES";
	

	
	
	public static void main(String[] args) throws Exception {
		long time_start, time_end;
    	time_start = System.currentTimeMillis();
    	while(ApuntadorDeEntidad.getInstance().siguienteEntidad()){
	    	RecuperadorPropiedadedConfiguracionEntidad.getInstance().mostrarConfiguracion();
	    	switch(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getAction().toUpperCase()){
	    		case RecuperadorPropiedadedConfiguracionEntidad.ACCION_CSVASERVICIO:csvAServicio();break;
	    		case RecuperadorPropiedadedConfiguracionEntidad.ACCION_SERVICIOAACSV:servicioACsv(); break;
	    	}
	    }
	    	time_end = System.currentTimeMillis();
	    	System.out.println(ManagementFactory.getThreadMXBean().getThreadCount() );
	    	double tiempoDemorado=(time_end - time_start)/1000/60 ;
    		if(tiempoDemorado>1){
        		FileWriter fw = new FileWriter(DirectorioManager.getDirectorioFechaYHoraInicio("duracion.txt"));
    			fw.write("El proceso de updateo demor� un total de: "+tiempoDemorado+" minutos");
        		fw.close();
    		}    	
/***********************************************************/
		//***Borrar ficheros de ejecucion***/
/***********************************************************/
	//	FilesAProcesarManager.getInstance().deleteCSVAProcesar();
	}

	
	private static void csvAServicio(){
		ParalelizadorDistintosFicheros hiloApartre = new ParalelizadorDistintosFicheros();
      	try {
      		if(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getMetodoPreEjecutor()!=null)
      			PreEjecutor.ejecutar(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getMetodoPreEjecutor(), RecuperadorPropiedadedConfiguracionEntidad.getInstance().getParametroPreEjecutor());
      		hiloApartre.insertar();
      		if(RecuperadorPropiedadedConfiguracionEntidad.getInstance().isBorrarDataSetAlFinalizar()){
      			File file = new File(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getPathCSVRegistros());
      			file.delete();
      		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void servicioACsv(){
		Ejecutor e= new Ejecutor();
		try{
			Integer cantRegistros=-1;
			RecuperadorMapeoCsv.getInstancia();
			if(RecuperadorPropiedadedConfiguracionEntidad.getInstance().isPaginado()){
				while(cantRegistros!=0){
					cantRegistros=(Integer)e.ejecutar(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getMetodoEjecutor(),RecuperadorPropiedadedConfiguracionEntidad.getInstance().getParametroEjecutor());
					RecuperadorPropiedadedConfiguracionEntidad.getInstance().incresePaginaActual();
					System.out.println("Pagina actual: "+RecuperadorPropiedadedConfiguracionEntidad.getInstance().getPaginaActual());
				}
			}
			else{
				cantRegistros=(Integer)e.ejecutar(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getMetodoEjecutor(),RecuperadorPropiedadedConfiguracionEntidad.getInstance().getParametroEjecutor());
			}
			System.out.println("Fin");
		}
		catch (Exception d) {
			d.printStackTrace();
		}
	}
	
	
}
