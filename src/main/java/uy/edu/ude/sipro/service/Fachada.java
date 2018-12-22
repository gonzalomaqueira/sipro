package uy.edu.ude.sipro.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import uy.edu.ude.sipro.entidades.Enumerados.CategoriaProyectoEnum;
import uy.edu.ude.sipro.entidades.Enumerados.EstadoProyectoEnum;
import uy.edu.ude.sipro.entidades.Enumerados.TipoElemento;
import uy.edu.ude.sipro.seguridad.SecurityUtils;
import uy.edu.ude.sipro.busquedas.BusquedaService;
import uy.edu.ude.sipro.busquedas.DatosFiltro;
import uy.edu.ude.sipro.busquedas.ResultadoBusqueda;
import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Enumerados;
import uy.edu.ude.sipro.entidades.Perfil;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.entidades.Usuario;
import uy.edu.ude.sipro.service.interfaces.DocenteService;
import uy.edu.ude.sipro.service.interfaces.ElementoService;
import uy.edu.ude.sipro.service.interfaces.PerfilService;
import uy.edu.ude.sipro.service.interfaces.ProyectoService;
import uy.edu.ude.sipro.service.interfaces.UsuarioService;
import uy.edu.ude.sipro.utiles.ConversorValueObject;
import uy.edu.ude.sipro.valueObjects.DocenteVO;
import uy.edu.ude.sipro.valueObjects.ElementoReporteVO;
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
	@Autowired
	private BusquedaService busquedaSevice;

	/**************************************************************** Proyectos */
	
	public List<ProyectoVO> obtenerProyectos()
	{
		return ConversorValueObject.convertirListaProyectoVO(proyectoService.obtenerProyectos());
	}
	
	public ProyectoDetalleVO obtenerProyectoPorId(int idProyecto)
	{
		return ConversorValueObject.convertirProyectoDetalleVO(proyectoService.obtenerProyectoPorId(idProyecto));
	}
	
	// CORREGIR en vista
	public void altaProyecto(String carrera, String codigoUde, Set<DocenteVO> correctores, int nota, String rutaArchivo) 
	{
		proyectoService.agregar(codigoUde, carrera, correctores, nota, rutaArchivo);
	}
	

	public void modificarProyectoCompleto(int id, String codigoUde, String titulo, int anio, String carrera, int nota, String resumen, 
			ArrayList<String> alumnos, ArrayList<String> tutorString, List<DocenteVO> correctores, ArrayList<String> bibliografia,
			CategoriaProyectoEnum categoria, List<ElementoVO> elementosRelacionados) throws Exception
	{
		proyectoService.modificar(  id,
									codigoUde,
									titulo,
									anio, 
									carrera, 
									nota, 
									resumen, 
									alumnos, 
									tutorString,
									ConversorValueObject.convertirListaDocenteVOaDocente(correctores),
									bibliografia,
									categoria,
									ConversorValueObject.convertirListaElementoVOaElemento(elementosRelacionados));
	}
	
	public void borrarProyecto(int id) throws Exception
	{
		proyectoService.eliminar(id);
	}
	
	public void ProcesarProyecto(int idProyecto) throws Exception
	{
		proyectoService.procesarProyecto(idProyecto);
	}
	
	public String buscarProyecto(String keywords) throws Exception
	{
		return null;
	}
	
	public boolean existeProyecto(String codigoUde)
	{
		Proyecto proy = proyectoService.buscarProyecto(codigoUde);
		return (proy != null);
	}
	
	/**************************************************************** Usuarios */	
	
	public List<UsuarioVO> obtenerUsuarios()
	{	
		return ConversorValueObject.convertirListaUsuarioVO(usuarioService.obtenerUsuarios());
	}
	
	public void altaUsuario(String usuario, String contrasenia, String nombre, String apellido, String email, PerfilVO perfil) 
	{
		Perfil p = new Perfil();
		p.setId(perfil.getId());
		usuarioService.agregar(usuario, contrasenia, nombre, apellido, email, p);
	}	
	
	public void modificarUsuario(int id, String usuario, String nombre, String apellido, String email, PerfilVO perfil) 
	{
		Perfil p = new Perfil();
		p.setId(perfil.getId());
		usuarioService.modificarSinContrasenia(id, usuario, nombre, apellido, email, p);
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
	
	public boolean existeUsuario(String usuario)
	{
		Usuario usu = usuarioService.buscarUsuario(usuario);
		return (usu != null);
	}

	public List<PerfilVO> obtenerPerfiles()
	{
		return ConversorValueObject.convertirListaPerfilVO(perfilService.obtenerPerfiles());
	}
	
	public UsuarioVO obtenerUsuarioPorId(int id)
	{
		return ConversorValueObject.convertirUsuarioVO(usuarioService.buscarUsuarioPorId(id));
	}
	
	public UsuarioVO obtenerUsuarioLogeado()
	{
		UsuarioVO usuarioRetorno= new UsuarioVO();
		Usuario usuario=SecurityUtils.getCurrentUser(usuarioService);
		usuarioRetorno.setId(usuario.getId());
		usuarioRetorno.setPerfil(ConversorValueObject.convertirPerfilVO(usuario.getPerfil()));
		usuarioRetorno.setUsuario(usuario.getUsuario());
		usuarioRetorno.setNombre(usuario.getNombre());
		usuarioRetorno.setApellido(usuario.getApellido());
		usuarioRetorno.setEmail(usuario.getEmail());
		usuarioRetorno.setContrasenia(usuario.getContrasenia());
		return usuarioRetorno;		
	}
	
	
	/**************************************************************** Elementos */
	
	public List<ElementoVO> obtenerElementos()
	{
		return ConversorValueObject.convertirListaElementoVO(elementoService.obtenerElementos());
	}

	public void altaElemento(String nombre, boolean esCategoria, TipoElemento tipoElemento, List<SubElementoVO> elementosRelacionados, List<SinonimoVO> sinonimos) throws Exception 
	{
		elementoService.altaElemento(nombre, esCategoria, tipoElemento, new HashSet<SubElementoVO>(elementosRelacionados), new HashSet<SinonimoVO>(sinonimos));
		busquedaSevice.actualizarSinonimosElemntosES(new ArrayList<Elemento>(elementoService.obtenerElementos()));
	}

	public void modificarElemento(int id, String nombre, boolean esCategoria, TipoElemento tipoElemento, List<SubElementoVO> elementosRelacionados, List<SinonimoVO> sinonimos) throws Exception
	{
		elementoService.modificar(id, nombre, esCategoria, tipoElemento, new HashSet<SubElementoVO>(elementosRelacionados), new HashSet<SinonimoVO>(sinonimos));
		busquedaSevice.actualizarSinonimosElemntosES(new ArrayList<Elemento>(elementoService.obtenerElementos()));
	}

	public void eliminarElemento(int id) throws Exception
	{
		elementoService.eliminar(id);
		busquedaSevice.actualizarSinonimosElemntosES(new ArrayList<Elemento>(elementoService.obtenerElementos()));
	}
	
	public boolean actualizarSinonimosElemntosES() throws Exception
	{
		return busquedaSevice.actualizarSinonimosElemntosES(new ArrayList<Elemento>(elementoService.obtenerElementos()));
	}

	/**************************************************************** Docentes */
	
	public List<DocenteVO> obtenerDocentes()
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
	
	public boolean existeDocente(String nombre, String apellido)
	{
		return docenteService.existeDocente(nombre, apellido);
	}
	
	/************************************************************** Busquedas */
	
	public ArrayList<ResultadoBusqueda> buscarElementosProyectoES (String busqueda, DatosFiltro datosFiltro) throws Exception
	{
		return busquedaSevice.realizarBusquedaES(busqueda, datosFiltro);
	}

	/************************************************************** Otros */ 
	
	public void creacionIncideES() throws Exception
	{
		busquedaSevice.creacionIncideES();
	}
	
	public void sincronizacionDatosInicial() throws Exception
	{
		System.out.println("entró a método sincronizacionDatosInicial()");
		busquedaSevice.actualizarSinonimosElemntosES(new ArrayList<Elemento>(elementoService.obtenerElementos()));			
		
		Set<Proyecto> proyectosBD = proyectoService.obtenerProyectos();
		ArrayList<Integer> idsProyectosIndizados = busquedaSevice.obtenerListaProyectosES();
		
		boolean encontre;
		if(proyectosBD != null && !proyectosBD.isEmpty())
		{
			for(Proyecto proy : proyectosBD)
			{
				encontre=false;
				if(idsProyectosIndizados!=null && !idsProyectosIndizados.isEmpty())
				{
					for(int id : idsProyectosIndizados)
					{
						if(proy.getId() == id)
						{
							encontre= true;
							break;
						}
					}
				}
				
				if(!encontre && proy.getEstado().equals(EstadoProyectoEnum.PROCESADO))
				{
					proyectoService.cargarDatosProyectoES(proy);
				}
			}
		}

		if(idsProyectosIndizados!=null && !idsProyectosIndizados.isEmpty())
		{
			for(int id : idsProyectosIndizados)
			{
				encontre=false;
				if(proyectosBD != null && !proyectosBD.isEmpty())
				{
					for(Proyecto proy : proyectosBD)
					{
						if(proy.getId() == id)
						{
							encontre= true;
							break;
						}
					}
				}
				if(!encontre)
				{
					busquedaSevice.bajaProyectoES(id);
				}
			}
		}
	}

	public List<ProyectoVO> obtenerListaProyectosFiltro(DatosFiltro datosFiltro) 
	{
		Set<Proyecto> listaRetorno = proyectoService.obtenerProyectos();
		
		listaRetorno = listaRetorno.stream().filter(x -> x.getEstado().equals(Enumerados.EstadoProyectoEnum.PROCESADO)
				   && x.getNota() >= datosFiltro.getNotaIni() && x.getNota() <= datosFiltro.getNotaFin()
			  	   && x.getAnio() >= datosFiltro.getAnioIni() && x.getAnio() <= datosFiltro.getAnioFin()).collect(Collectors.toSet());
		
		if (datosFiltro.getTutorObjeto() != null)
		{
			listaRetorno = listaRetorno.stream().filter(x -> x.getTutor() != null
					&& x.getTutor().getNombreCompleto().equals(datosFiltro.getTutorObjeto().getNombreCompleto()))
					.collect(Collectors.toSet());
		}

		if (datosFiltro.getCorrectorObjeto() != null)
		{
			listaRetorno = listaRetorno.stream().filter(x -> x.getCorrectores() != null && !x.getCorrectores().isEmpty()
					&& x.getCorrectores().stream().anyMatch(y -> y.getNombreCompleto().equals(datosFiltro.getCorrectorObjeto().getNombreCompleto())))
					.collect(Collectors.toSet());
		}
		
		if (datosFiltro.getListaElementos() != null && !datosFiltro.getListaElementos().isEmpty())
		{
			listaRetorno = listaRetorno.stream().filter(x -> x.getElementosRelacionados() != null && !x.getElementosRelacionados().isEmpty()
					&& x.getElementosRelacionados().stream().anyMatch(y -> datosFiltro.getStringListaElementos().contains(y.getNombre())))
					.collect(Collectors.toSet());
		}
		
		return ConversorValueObject.convertirListaProyectoVO(listaRetorno);
	}
	
	///////////////////////////////////////inicio
	
	public void crearUsuariosInicio()
	{
		boolean existeAdmin=false;
		boolean existeInvit=false;
		List <UsuarioVO> usuarios= this.obtenerUsuarios();
		for(UsuarioVO usu : usuarios)
		{
			if(usu.getUsuario().equals("admin"))
			{
				existeAdmin=true;
			}
			if(usu.getUsuario().equals("invitado"))
			{
				existeInvit=true;
			}
			if(existeAdmin && existeInvit)
			{
				break;
			}
		}
		if(!existeAdmin)
		{
			this.altaUsuario("admin", "admin", "admin", "admin", "admin@admin.com", new PerfilVO(1,"Administrador") );
		}
		if(!existeInvit)
		{
			this.altaUsuario("invitado", "invitado", "invitado", "invitado", "invitado@Invitado.com", new PerfilVO(2,"Invitado") );
		}
	}
	
	public void crearPerfilesInicio()
	{
		perfilService.agregar(1, "Administrador");
		perfilService.agregar(2, "Invitado");
		perfilService.agregar(3, "Bibliotecario");
		perfilService.agregar(4, "Tutor");
		perfilService.agregar(5, "Alumno");
		perfilService.agregar(6, "Docente");
	}

	public List<ElementoReporteVO> reporteElementos(Enumerados.TipoElemento tipoElemento) 
	{			
		List<ElementoReporteVO> listaRetorno= new ArrayList<>();
		Set<Elemento> listaElementos= elementoService.obtenerElementos();
		int totalElementosAsociados = 0;
		for(Elemento elem: listaElementos)
		{
			if (elem.getTipoElemento() == tipoElemento)
			{
				int cantidad = elem.getProyectos() == null ? 0 : elem.getProyectos().size();
				ElementoReporteVO elementoReporte= new ElementoReporteVO(elem.getNombre(), cantidad);
				listaRetorno.add(elementoReporte);
				totalElementosAsociados = totalElementosAsociados + cantidad;
			}
		}
		listaRetorno = listaRetorno.stream().filter(x -> x.getCantidad() > 0).collect(Collectors.toList());		
		
		for(ElementoReporteVO elem : listaRetorno)
		{
			if (totalElementosAsociados > 0)
			{
				elem.setPorcentaje(round(((float)elem.getCantidad() * 100 / totalElementosAsociados), 1));
			}
		}
		return listaRetorno.stream().sorted().collect(Collectors.toList());
	}

	private static float round (float value, int precision) {
	    int scale = (int) Math.pow(10, precision);
	    return (float) Math.round(value * scale) / scale;
	}
	
}
