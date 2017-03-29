package exit.services.singletons;



public class ApuntadorDeEntidad {
	private String[] entidades;
	private int puntero;
	private static ApuntadorDeEntidad instance;
	private ApuntadorDeEntidad(){
		entidades= RecuperadorPropiedadesConfiguracionGenerales.getInstance().getEntidadesAIntegrar().split(",");
		puntero=-1;
	}
    public static synchronized ApuntadorDeEntidad getInstance() {
    	if(instance==null)
    		instance=new ApuntadorDeEntidad();
    	return instance;
    }
    
    public String getEntidadActual(){
    	if(puntero>=entidades.length)
    		return null;
    	return entidades[puntero].trim();
    }
    
    public boolean hasNext(){
    	return (puntero+1)<entidades.length;
    }
    
    public boolean siguienteEntidad(){
    	if((puntero+1)>=entidades.length)
    		return false;
    	puntero++;
    	RecuperadorPropiedadedConfiguracionEntidad.getInstance().reiniciar();
    	RecuperadorFormato.getInstancia().reiniciar();
    	RecuperadorPropierdadesJson.getInstancia().reiniciar();
    	AlmacenadorFechaYHora.reiniciar();
    	return true;
    	
    }
}
