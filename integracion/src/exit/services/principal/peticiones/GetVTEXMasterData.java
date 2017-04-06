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
import exit.services.singletons.RecuperadorEspecificacionesCSV;
import exit.services.singletons.RecuperadorMapeoCsv;
import exit.services.singletons.RecuperadorPropiedadedConfiguracionEntidad;
import exit.services.singletos.funciones.FuncionesWhirpool;

public class GetVTEXMasterData extends GetVTEXAbstract{

	@Override
	Object realizarRequestAbstract(String url, String id) {
		return this.realizarPeticion(url+"/documents", id+"?_fields=_all");
	}

	
}