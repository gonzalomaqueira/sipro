package uy.edu.ude.sipro.utiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/*************************************************************************

Utilitario core del sistema, donde se implementan métodos estáticos para trabajar con el texto.

**************************************************************************/
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
        documentoPorSecciones.add(armarSeccionBibliografia(textoOriginal));
         
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
    	    	if(!FuncionesTexto.esTituloBibliografia(linea))
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
    	    		break;
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
		
	public static SeccionTexto armarSeccionBibliografia(String texto[])
	{
        SeccionTexto seccion = null;
        ArrayList<String> contenido = new ArrayList<String>();
        boolean encontreTituloBibliografia = false;
        
		for (String linea : texto)
        {
			if (FuncionesTexto.esTituloBibliografia(linea))
    	    {
    	    	encontreTituloBibliografia = true;
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
    	    	if (encontreTituloBibliografia)
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
		String patron = "[\\\\!\"#$%&()*+,./;<=>?@\\[\\]^_{|}~]+";
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
		if (FuncionesTexto.esTitulo(linea) &&  (linea.toLowerCase().trim().equals("resumen") || 
												linea.toLowerCase().trim().equals("abstract") || 
												linea.toLowerCase().trim().equals("resumen ejecutivo")))
		{
			return true;
		}
		return false;
	}
	
	public static boolean esTituloAlumnos(String linea)
	{
		if (FuncionesTexto.esTitulo(linea) && (	linea.toLowerCase().trim().equals("alumnos") || 
												linea.toLowerCase().trim().equals("integrantes") ||
												linea.toLowerCase().trim().equals("alumnos:") || 
												linea.toLowerCase().trim().equals("integrantes:")||
												linea.toLowerCase().trim().equals("equipo:")||
												linea.toLowerCase().trim().equals("equipo")))
		{
			return true;
		}
		return false;
	}
	
	public static boolean esTituloTutor(String linea)
	{
		if (FuncionesTexto.esTitulo(linea) && ( linea.toLowerCase().trim().equals("tutor") || linea.toLowerCase().trim().equals("tutor:") ))
		{
			return true;
		}
		return false;
	}
	
	public static boolean esTituloBibliografia(String linea)
	{
		if (FuncionesTexto.esTitulo(linea) && (	linea.toLowerCase().trim().equals("bibliografia") || 
												linea.toLowerCase().trim().equals("bibliografía") || 
												linea.toLowerCase().trim().equals("referencias")  ||
												linea.toLowerCase().trim().equals("bibliografia:") || 
												linea.toLowerCase().trim().equals("bibliografía:") || 
												linea.toLowerCase().trim().equals("referencias:")))
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
			salida= source.toLowerCase().contains(subItem.toLowerCase());
		}
		return salida;
   }
	
	public static boolean esNumerico(String str)  
	{  
	  try  
	  {  
		  Double.parseDouble(str);
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
	
	public static boolean esFechaDocumento(String str)
	{
		if(strContieneNumeroEnRango(str, 1, 31) &&
		   strContieneNumeroEnRango(str, Constantes.FECHA_VALIDA_DESDE, 2050) &&
		   empiezaMayuscula(str) &&
		   strContieneMes(str))
		{
			return true;
		}
		return false;
	}
	
	public static boolean strContieneNumeroEnRango(String str, int ini, int fin)
	{
		if (ini <= fin)
		{
			for(int x=ini; x<fin; x++)
			{
				if(isContain(str, Integer.toString(x)))
				{
					return true;
				}
			}	
		}
		return false;
	}
	
	public static boolean strContieneMes(String str)
	{
		if (isContain(str, "enero") ||
			isContain(str, "febrero") ||
			isContain(str, "marzo") ||
			isContain(str, "abril") ||
			isContain(str, "mayo") ||
			isContain(str, "junio") ||
			isContain(str, "julio") ||
			isContain(str, "agosto") ||
			isContain(str, "setiembre") || isContain(str, "septiembre") ||
			isContain(str, "octubre") ||
			isContain(str, "noviembre") ||
			isContain(str, "diciembre"))
		{
			return true;
		}
		return false;
	}

	public static String limpiarTexto(String[] textoOriginal) 
	{
		String retorno="";
		for(String linea : textoOriginal)
		{
			retorno= retorno + " " +  linea;
		}
		retorno=limpiarTexto(retorno);
		
		return retorno;
	}
	
	public static String limpiarTexto(String linea)
	{
		String retorno=linea;
		retorno = retorno.replaceAll("[\u0000-\u001f]", " ");
		retorno= retorno.replaceAll("\\s+"," ");
		retorno= retorno.replaceAll("\"", "");
		retorno = retorno.replaceAll("\n", "").replace("\r", "");
		retorno = limpiarTextoFull(retorno);
				
		return retorno;
	}

	public static String convertirArrayStringsAString(ArrayList<String> arrayStrings)
	{
		String retorno="";
		
		if (arrayStrings != null)
		{
			for(String linea : arrayStrings)
			{
				retorno = retorno + " " + linea;
			}
		}
		return retorno.trim();
	}

	public static String[] eliminarBibliografia(String[] texto) 
	{
		String[] retorno = texto;
		ArrayList<String> aux = new ArrayList<>();
		
		for (String linea : texto)
        {
			if (!esTituloBibliografia(linea))
			{
				aux.add(linea);
			}
			else
			{
				break;
			}
        }		
		if (aux != null && !aux.isEmpty())
		{					
			retorno = new String[aux.size()];
			retorno = aux.toArray(retorno);
		}
		
		return retorno;
	}

	public static String limpiarTextoFull(String texto) 
	{
		StringBuilder aux = new StringBuilder(texto);  
			
		for (int n=0; n <aux.toString().length (); n++)
		{
			char c=aux.toString().charAt(n);
			byte b = (byte)c;
			if(b>=0 && b < 32 || b == 92)
				aux.setCharAt(n, ' ');	
		}
	
		return aux.toString();
	}
	
}