package uy.edu.ude.sipro.utiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class FuncionesTexto 
{
	public static List<SeccionTexto> armarDocumentoPorSecciones(String textoOriginal[])
	{
		List<SeccionTexto> documentoPorSecciones = new ArrayList<SeccionTexto>();
        SeccionTexto seccion = null;
        ArrayList<String> contenido = new ArrayList<String>();
        boolean encontreTitulo = false;
		
        documentoPorSecciones.add(armarSeccionAlumnos(textoOriginal));
        documentoPorSecciones.add(armarSeccionTutor(textoOriginal));
         
        List<String> Textolista = new ArrayList<String>(Arrays.asList(textoOriginal));
        
        for (String linea : textoOriginal)
        {
        	if (!FuncionesTexto.esTituloResumen(linea))
        	{
        		Textolista.remove(0);
        	}
        	else
        		break;
        }
        
		for (String linea : Textolista)
        {
    	    if (FuncionesTexto.esTitulo(linea))
    	    {
    	    	encontreTitulo = true;
    	    	if (seccion != null)
    	    	{
    	    		seccion.setContenido(contenido);
    	    		documentoPorSecciones.add(seccion);
    	    	}
    	    	seccion = new SeccionTexto();
    	    	contenido = new ArrayList<String>();
    	    	seccion.setTitulo(linea);
			}
    	    else
    	    {
    	    	if (encontreTitulo && contenido != null)
    	    	{
    	    		contenido.add(linea);
    	    	}        	    	
    	    }
        }
		if (seccion != null)
		{
			seccion.setContenido(contenido);
			documentoPorSecciones.add(seccion);
		}
		
		return documentoPorSecciones;
	}
	
	public static SeccionTexto armarSeccionAlumnos(String texto[])
	{
        SeccionTexto seccion = null;
        ArrayList<String> contenido = new ArrayList<String>();
        boolean encontreTituloAlumno = false;
        
		for (String linea : texto)
        {
    	    if (FuncionesTexto.esTituloTutor(linea))
    	    	break;
    	    
			if (FuncionesTexto.esTituloAlumnos(linea))
    	    {
    	    	encontreTituloAlumno = true;
    	    	if (seccion != null)
    	    	{
    	    		seccion.setContenido(contenido);
    	    	}
    	    	seccion = new SeccionTexto();
    	    	contenido = new ArrayList<String>();
    	    	seccion.setTitulo(linea);
			}
    	    else
    	    {
    	    	if (encontreTituloAlumno && !FuncionesTexto.esTituloTutor(linea))
    	    	{
    	    		contenido.add(linea);
    	    	}        	    	
    	    }
        }
		if (seccion != null)
		{
			seccion.setContenido(contenido);
		}
		return seccion;
	}
	
	public static SeccionTexto armarSeccionTutor(String texto[])
	{
        SeccionTexto seccion = null;
        ArrayList<String> contenido = new ArrayList<String>();
        boolean encontreTituloAlumno = false;
        
		for (String linea : texto)
        {
    	    if (FuncionesTexto.esTituloResumen(linea))
    	    	break;
    	    
			if (FuncionesTexto.esTituloTutor(linea))
    	    {
    	    	encontreTituloAlumno = true;
    	    	if (seccion != null)
    	    	{
    	    		seccion.setContenido(contenido);
    	    	}
    	    	seccion = new SeccionTexto();
    	    	contenido = new ArrayList<String>();
    	    	seccion.setTitulo(linea);
			}
    	    else
    	    {
    	    	if (encontreTituloAlumno && !FuncionesTexto.esTituloResumen(linea))
    	    	{
    	    		contenido.add(linea);
    	    	}        	    	
    	    }
        }
		if (seccion != null)
		{
			seccion.setContenido(contenido);
		}
		return seccion;
	}	
		
	
	public static boolean esTitulo (String linea)
	{
		return (contarPalabras(linea) > 0 && contarPalabras(linea) < 4
			&& empiezaMayuscula(linea)
			&& !contieneNumero(linea)
			&& !contieneCaracterEspecial(linea) 
			&& !terminaPunto(linea));
	}
	
	public static int contarPalabras(String linea)
	{
	    int wordCount = 0;
	    boolean word = false;
	    linea = linea.trim();
	    int endOfLine = linea.length() - 1;

	    for (int i = 0; i < linea.length(); i++) {
	        if (Character.isLetter(linea.charAt(i)) && i != endOfLine) {
	            word = true;
	        } else if (!Character.isLetter(linea.charAt(i)) && word) {
	            wordCount++;
	            word = false;
	        } else if (Character.isLetter(linea.charAt(i)) && i == endOfLine) {
	            wordCount++;
	        }
	    }
	    return wordCount;
	}
	
	public static boolean empiezaMayuscula(String linea)
	{
		linea=linea.trim();
		if(!linea.isEmpty())
			return Character.isUpperCase(linea.charAt(0));
		else 
			return false;
	}
	
	public static boolean contieneNumero (String linea)
	{
		linea=linea.trim();
		return linea.matches(".*\\d+.*");
	}
	
	public static boolean terminaPunto(String linea)
	{
		linea=linea.trim();
		if(!linea.isEmpty())
			return linea.charAt(linea.length() - 1) == '.';
		else 
			return false;
	}
	
	public static boolean contieneCaracterEspecial(String linea)
	{
		String patron = "[\\\\!\"#$%&()*+,./:;<=>?@\\[\\]^_{|}~]+";
		Pattern p = Pattern.compile(patron);
		Matcher m = p.matcher(linea);
		return m.find();
	}
	
	public static String DevolverTitulos(String contenidoOriginal, String delimitador1, String delimitador2)
	{   
	    String contenidoSalida = StringUtils.substringBetween(contenidoOriginal, delimitador1, delimitador2);
	    contenidoSalida = delimitador1 + "\r\n" + contenidoSalida + "\r\n" + delimitador2;
	    return contenidoSalida;
 	}
	
	public static String DevolverTextoDelimitado(String contenidoOriginal, String delimitador1, String delimitador2)
	{   
	    String contenidoSalida = StringUtils.substringBetween(contenidoOriginal, delimitador1, delimitador2);
	    return contenidoSalida;
 	}
	
	public static boolean esTituloResumen(String linea)
	{
		if (FuncionesTexto.esTitulo(linea) &&  (linea.trim().equals("Resumen") || linea.trim().equals("Abstract")))
		{
			return true;
		}
		return false;
	}
	
	public static boolean esTituloAlumnos(String linea)
	{
		if (FuncionesTexto.esTitulo(linea) && (linea.trim().equals("Alumnos") || linea.trim().equals("Integrantes")))
		{
			return true;
		}
		return false;
	}
	
	public static boolean esTituloTutor(String linea)
	{
		if (FuncionesTexto.esTitulo(linea) && linea.trim().equals("Tutor"))
		{
			return true;
		}
		return false;
	}
	
	public static boolean esTituloBibliografia(String linea)
	{
		if (FuncionesTexto.esTitulo(linea) && linea.trim().equals("Tutor"))
		{
			return true;
		}
		return false;
	}
	
	public static ArrayList<String> eliminarLineasVacias (ArrayList<String> texto)
	{
		String[] textoAux = texto.toArray(new String[texto.size()]);
		for (String linea: textoAux)
		{
			if (linea.trim().equals(""))
				texto.remove(linea);
		}
		return texto;
	}
	
	public static ArrayList<String> eliminarEspacios (ArrayList<String> texto)
	{
		int posicion = 0;
		for (String linea: texto)
		{
			texto.set(posicion, linea.trim());
			posicion++;
		}
		return texto;
	}
	
	public static ArrayList<String> separarAlumnos (ArrayList<String> texto)
	{
		boolean separar = false;
		if (FuncionesTexto.contieneEmail(texto.get(texto.size()-1)))
		{
			separar = true;
		}
		
		if (separar)
		{
			ArrayList<String> textoAux = new ArrayList<String>();
			for (String linea: texto)
			{
				textoAux.add(linea);
				if (FuncionesTexto.contieneEmail(linea))
					textoAux.add("");
			}
			textoAux.remove(textoAux.size()-1);
			texto = textoAux;			
		}
		
		return texto;
	}
	
	public static boolean contieneEmail(String linea)
	{		
		return linea.contains("@");
	}

	public static boolean seccionContieneTexto(SeccionTexto seccion, String texto) 
	{
		if (seccion != null)
		{
			for (String linea: seccion.getContenido())
			{
				if (isContain(linea, texto))
					return true;
			}
		}
		return false;
	}
	
	public static boolean esNuloOVacio(String texto)
	{
		return (texto==null || texto.trim().equals(""));
	}	
	
	public static int devolverPrimerAnioTexto(String[] textoOriginal)
	{
		int vRetorno = 0;
		for (String linea : textoOriginal)
        {			
			if (!contieneEmail(linea) && devolverAnio(linea) > 0)
			{
				vRetorno = devolverAnio(linea);
				break;
			}
        }
		return vRetorno;
	}
	
	private static int devolverAnio(String linea)
	{
		int vRetorno = 0;
		Date vfecha = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(vfecha);
		int anioHasta = cal.get(Calendar.YEAR);
		
		Pattern p = Pattern.compile("[0-9]+");
		Matcher m = p.matcher(linea);
		while (m.find()) 
		{
		    int num = Integer.parseInt(m.group());
		    if (num >= Constantes.FECHA_VALIDA_DESDE && num <= anioHasta + 1)
		    {
		    	vRetorno = num;
		    	break;
		    }		    
		}
		return vRetorno;
	}
	
	public static String convertirArrayAStringEspacios(ArrayList<String> listaStrings)
	{
		String vRetorno = "";
		if (listaStrings != null && !listaStrings.isEmpty())
		{	
			for (String linea : listaStrings)
			{
				vRetorno = vRetorno + linea.trim() + " ";
			}
		}
		return vRetorno;
	}
	
	public static String convertirArrayAStringSaltoLinea(ArrayList<String> listaStrings)
	{
		String vRetorno = "";
		if (listaStrings != null && !listaStrings.isEmpty())
		{			
			for (String linea : listaStrings)
			{
				vRetorno = vRetorno + linea.trim() + "\n";
			}
		}
		return vRetorno;
	}
	
	public static ArrayList<String> convertirStringAArrayList(String texto)
	{
		return new ArrayList<String>(Arrays.asList(texto.split("\n")));
	}
	
	public static boolean ListaContieneString (ArrayList<String> hashSet, String string)	
	{
		for (String str : hashSet)
		{
			if (isContain(StringUtils.stripAccents(str).trim(), StringUtils.stripAccents(string).trim()))
			{
				return true;
			}
		}
		return false;
	}
	
	private static boolean isContain(String source, String subItem)
	{
		boolean salida=false;

		if(!contieneCaracterEspecial(subItem))
		{
			String regex = "\\b"+subItem+"\\b";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(source);
			salida=matcher.find();
		}
		else
		{
			salida= source.contains(subItem);
		}
		return salida;
   }
	
	public static boolean esNumerico(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	
	public static boolean esNotaValida(String str)
	{
		if(esNumerico(str) && (Integer.parseInt(str)<=12 && Integer.parseInt(str)>=0))
		{
			return true;
		}
		return false;

	}
}