package uy.edu.ude.sipro.utiles;

import org.springframework.beans.factory.annotation.Value;

import com.vaadin.spring.annotation.SpringComponent;

/*************************************************************************

Se declaran las constantes utilizadas en el sistema

**************************************************************************/
@SpringComponent
public final class Constantes 
{
	public static String RUTA_ARCHIVOS;
	public static String ElasticSearch_Url_Base;
	public static String ElasticSearch_Index;
	
	public static final int FECHA_VALIDA_DESDE = 1995;	
	public static final int ElasticSearch_Timeout = 3000;
	public static final int LARGO_TITULO_MAX = 70;
	public static final int CANTIDAD_DETALLES_BUSQUEDA = 3;
	public static final int LARGO_MAXIMO_RESUMEN_BUSQUEDA = 300;
	public static final double ANIO_INICIO_BUSQUEDA = 2008;
	
	
	@Value("${ruta.archivos}")
	public void setRutaArchivo(String ruta)
	{
		RUTA_ARCHIVOS= ruta;
	}
	
	@Value("${elasticsearch.url.base}")
	public void setESurl(String ruta)
	{
		ElasticSearch_Url_Base= "http://"+ ruta + "/";
	}
	
	@Value("${elasticsearch.index}")
	public void setESindex(String ruta)
	{
		ElasticSearch_Index= ruta+"/";
	}
}
