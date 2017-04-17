package exit.services.singletos.funciones;

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
}
