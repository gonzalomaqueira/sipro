package uy.edu.ude.sipro.utiles;

import java.util.ArrayList;

/*************************************************************************

Clase que define una sección de texto para estructurar información de documentos

**************************************************************************/
public class SeccionTexto 
{
	public String titulo;
	public ArrayList<String> contenido;
	
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public ArrayList<String> getContenido() {
		return contenido;
	}

	public void setContenido(ArrayList<String> contenido) {
		this.contenido = contenido;
	}
}