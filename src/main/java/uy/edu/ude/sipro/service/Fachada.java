package uy.edu.ude.sipro.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;
import uy.edu.ude.sipro.entidades.Perfil;
import uy.edu.ude.sipro.service.interfaces.DocenteService;
import uy.edu.ude.sipro.service.interfaces.ElementoService;
import uy.edu.ude.sipro.service.interfaces.PerfilService;
import uy.edu.ude.sipro.service.interfaces.ProyectoService;
import uy.edu.ude.sipro.service.interfaces.UsuarioService;
import uy.edu.ude.sipro.utiles.ConversorValueObject;
import uy.edu.ude.sipro.valueObjects.DocenteVO;
import uy.edu.ude.sipro.valueObjects.ElementoVO;
import uy.edu.ude.sipro.valueObjects.PerfilVO;
import uy.edu.ude.sipro.valueObjects.ProyectoDetalleVO;
import uy.edu.ude.sipro.valueObjects.ProyectoVO;
import uy.edu.ude.sipro.valueObjects.SinonimoVO;
import uy.edu.ude.sipro.valueObjects.SubElementoVO;
import uy.edu.ude.sipro.valueObjects.UsuarioVO;

@Service
public class Fachada {
	
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private PerfilService perfilService;
	@Autowired
	private ProyectoService proyectoService;
	@Autowired
	private ElementoService elementoService;
	@Autowired
	private DocenteService docenteService;

	/**************************************************************** Proyectos */
	
	public Set<ProyectoVO> obtenerProyectos()
	{
		return ConversorValueObject.convertirListaProyectoVO(proyectoService.obtenerProyectos());
	}
	
	public ProyectoDetalleVO obtenerProyectoPorId(int idProyecto)
	{
		return ConversorValueObject.convertirProyectoDetalleVO(proyectoService.obtenerProyectoPorId(idProyecto));
	}
	
	public void altaProyecto(String nombre, String carrera, Set<DocenteVO> correctores, int nota, String rutaArchivo) 
	{
		proyectoService.agregar(nombre, carrera, correctores, nota, rutaArchivo);
	}
	
	public void modificarProyecto(int id, String nombre, int anio, String carrera, int nota, String rutaArchivo) 
	{
		proyectoService.modificar(id, nombre, anio, carrera, nota, rutaArchivo);
	}
	
	public void modificarProyectoCompleto(int id, String nombre, int anio, String carrera, int nota, String resumen, 
			ArrayList<String> alumnos, DocenteVO tutor, Set<DocenteVO> correctores) 
	{
		proyectoService.modificar(  id, 
									nombre, 
									anio, 
									carrera, 
									nota, 
									resumen, 
									alumnos, 
									ConversorValueObject.convertirDocenteVOaDocente(tutor),
									ConversorValueObject.convertirListaDocenteVOaDocente(correctores));
	}
	
	public void borrarProyecto(int id)
	{
		proyectoService.eliminar(id);
	}
	
	public void ProcesarProyecto(int idProyecto)
	{
		proyectoService.procesarProyecto(idProyecto);
	}
	
	/**************************************************************** Usuarios */	
	
	public Set<UsuarioVO> obtenerUsuarios()
	{
		return ConversorValueObject.convertirListaUsuarioVO(usuarioService.obtenerUsuarios());
	}
	
	public void altaUsuario(String usuario, String contrasenia, String nombre, String apellido, String email, PerfilVO perfil) 
	{
		Perfil p = new Perfil();
		p.setId(perfil.getId());
		usuarioService.agregar(usuario, contrasenia, nombre, apellido, email, p);
	}	
	
	public void modificarUsuario(int id, String usuario, String contrasenia, String nombre, String apellido, String email, PerfilVO perfil) 
	{
		Perfil p = new Perfil(); 
		p.setId(perfil.getId());
		usuarioService.modificar(id, usuario, contrasenia, nombre, apellido, email, p);
	}
	
	public void eliminarUsuario(int id)
	{
		usuarioService.eliminar(id);
	}

	public Set<PerfilVO> obtenerPerfiles()
	{
		return ConversorValueObject.convertirListaPerfilVO(perfilService.obtenerPerfiles());
	}

	
	/**************************************************************** Elementos */
	
	public Set<ElementoVO> obtenerElementos()
	{
		return ConversorValueObject.convertirListaElementoVO(elementoService.obtenerElementos());
	}

	public void altaElemento(String nombre, boolean esCategoria, TipoElemento tipoElemento, Set<SubElementoVO> elementosRelacionados, Set<SinonimoVO> sinonimos) 
	{
		elementoService.altaElemento(nombre, esCategoria, tipoElemento, elementosRelacionados, sinonimos);
	}

	public void modificarElemento(int id, String nombre, boolean esCategoria, TipoElemento tipoElemento, Set<SubElementoVO> elementosRelacionados, Set<SinonimoVO> sinonimos)
	{
		elementoService.modificar(id, nombre, esCategoria, tipoElemento, elementosRelacionados, sinonimos);
	}

	public void eliminarElemento(int id) 
	{
		elementoService.eliminar(id);
	}

	/**************************************************************** Docentes */
	
	public Set<DocenteVO> obtenerDocentes()
	{
		return ConversorValueObject.convertirListaDocenteVO(docenteService.obtenerDocentes());
	}

	public void altaDocente(String nombre, String apellido) 
	{
		docenteService.agregar(nombre, apellido);
	}

	public void eliminarDocente(int id)
	{
		docenteService.eliminar(id);
	}

}
