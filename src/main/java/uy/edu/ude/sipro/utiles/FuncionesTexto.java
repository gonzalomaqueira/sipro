package uy.edu.ude.sipro.utiles;

import java.util.ArrayList;
import java.util.Arrays;
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
		
		for (String linea : textoOriginal)
        {			
    	    if (esTitulo(linea))
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
		for (String linea: seccion.getContenido())
		{
			if (linea.contains(texto))
				return true;
		}
		return false;
	}
	
	public static boolean esNuloOVacio(String texto)
	{
		return (texto==null || texto.trim().equals(""));
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
}
