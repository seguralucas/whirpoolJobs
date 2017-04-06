package exit.services.principal.peticiones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import exit.services.fileHandler.CSVHandler;
import exit.services.fileHandler.DirectorioManager;
import exit.services.principal.peticiones.funciones.FuncionesVTEX;
import exit.services.singletons.RecuperadorMapeoCsv;
import exit.services.singletons.RecuperadorPropiedadedConfiguracionEntidad;

public abstract class GetVTEXAbstract  extends GetAbstractoGenerico{
	
	
	@Override
	Object procesarPeticionOK(BufferedReader in, String id, int responseCode) throws Exception {
		FuncionesVTEX fvtex= new FuncionesVTEX();
		JSONObject jsonObject = ConvertidorJson.convertir(in);
		String[] buscar=RecuperadorMapeoCsv.getInstancia().getCuerpo().split(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getSeparadorCSVREGEX());
		ArrayList<Object> listaObjetos=new ArrayList<Object>();
		for(int i=0;i<buscar.length;i++){
			String[] arrayTipos=fvtex.getArrayTipos(buscar[i]);
			listaObjetos.add(fvtex.getValue(buscar[i],arrayTipos,jsonObject));
		}
		String[] cabeceras=RecuperadorMapeoCsv.getInstancia().getCabecera().split(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getSeparadorCSVREGEX());		
		StringBuilder sb = new StringBuilder();
		int i=0;
		for(Object o:listaObjetos){
			String value = "";
			if(o!=null){
				if(cabeceras.length>i)
					value=fvtex.procesarPorFunciones(o,cabeceras[i]);
				else
					value=o.toString();
				sb.append(value+RecuperadorPropiedadedConfiguracionEntidad.getInstance().getSeparadorCSV());
			}
			else
				sb.append(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getSeparadorCSV());
			i++;
		}
		String lineaAGuardar=sb.toString();
		if(lineaAGuardar.length()!=0)
			lineaAGuardar=lineaAGuardar.substring(0, lineaAGuardar.length()-1);
		CSVHandler csv= new CSVHandler();
		csv.escribirCSV("salida.csv", lineaAGuardar,RecuperadorMapeoCsv.getInstancia().getCabecera(),false);		
		return null;
	}

	@Override
	Object procesarPeticionError(BufferedReader in, String id, int responseCode) throws Exception {
		String path=("error_get_orden_responsecode_"+responseCode+".txt");
	    File fichero = DirectorioManager.getDirectorioFechaYHoraInicio(path);
	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
         	out.println(inputLine);
        }
        out.close();
        return null;
	}

	@Override
	Object procesarPeticionOK(BufferedReader in, int responseCode) throws Exception {
		JSONArray jsonArrayItems;
		if(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getIterarSobre()!=null){
		JSONObject jsonObject = ConvertidorJson.convertir(in);
		jsonArrayItems= (JSONArray) jsonObject.get(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getIterarSobre());
		}
		else
			jsonArrayItems=ConvertidorJson.convertirArray(in);
		ExecutorService workers = Executors.newFixedThreadPool(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getNivelParalelismo());
	    List<Callable<Void>> tasks = new ArrayList<>();
		System.out.println("Subiendo el paralelismo a "+RecuperadorPropiedadedConfiguracionEntidad.getInstance().getNivelParalelismo());
		for(int i=0;i<jsonArrayItems.size();i++){
			final Integer j=i;
			final GetVTEXAbstract getVTEXAbstract= this;
			tasks.add(new Callable<Void>() {
		        public Void call() {
    		JSONObject jsonItem=(JSONObject)jsonArrayItems.get(j);
			String id=(String)jsonItem.get(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getIdIteracion());
			System.out.println(id);
			getVTEXAbstract.realizarRequestAbstract(RecuperadorPropiedadedConfiguracionEntidad.getInstance().getUrl(), id);
			return null;
		        }
			});
		}
		System.out.println(jsonArrayItems.size());
	    workers.invokeAll(tasks);
	    workers.shutdown();
		return jsonArrayItems.size(); //Devuelve la cantidad de registros encontradas
	}
	
	abstract Object realizarRequestAbstract(String url, String id);
	
	@Override
	Object procesarPeticionError(BufferedReader in, int responseCode) throws Exception {
		String path=("error_get_list_orden_responsecode_"+responseCode+".txt");
	    File fichero = DirectorioManager.getDirectorioFechaYHoraInicio(path);
	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fichero, true)));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
         	out.println(inputLine);
        }
        out.close();
		return 0;
	}
}