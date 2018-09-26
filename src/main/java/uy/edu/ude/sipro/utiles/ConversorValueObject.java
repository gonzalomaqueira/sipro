package uy.edu.ude.sipro.utiles;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Perfil;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.entidades.Sinonimo;
import uy.edu.ude.sipro.entidades.Usuario;
import uy.edu.ude.sipro.valueObjects.ElementoVO;
import uy.edu.ude.sipro.valueObjects.PerfilVO;
import uy.edu.ude.sipro.valueObjects.ProyectoDetalleVO;
import uy.edu.ude.sipro.valueObjects.ProyectoVO;
import uy.edu.ude.sipro.valueObjects.SinonimoVO;
import uy.edu.ude.sipro.valueObjects.SubElementoVO;
import uy.edu.ude.sipro.valueObjects.UsuarioVO;

public class ConversorValueObject 
{
	public static ProyectoDetalleVO convertirProyectoDetalleVO(Proyecto proyecto)
	{
		return new ProyectoDetalleVO( proyecto.getId(), 
									  proyecto.getNombre(),
									  proyecto.getAnio(),
									  proyecto.getCarrera(),
									  proyecto.getNota(),
									  proyecto.getAlumnos(),
									  proyecto.getTutor(),
									  proyecto.getRutaArchivo(),
									  proyecto.getResumen(),
									  proyecto.getFechaAlta(),
									  proyecto.getFechaUltimaModificacion(),
									  convertirListaElementoVO(proyecto.getElementosRelacionados()));
		
	}
	
	public static ProyectoVO convertirProyectoVO(Proyecto proyecto)
	{
		return new ProyectoVO(proyecto.getId(), 
							  proyecto.getNombre(),
							  proyecto.getAnio(),
							  proyecto.getCarrera(),
							  proyecto.getNota(),
							  proyecto.getEstado());
	}
	
	public static List<ProyectoVO> convertirListaProyectoVO(List<Proyecto> listaProyectos)
	{
		List<ProyectoVO> listaProyectosVO = new ArrayList<ProyectoVO>();

		for(Proyecto proyecto : listaProyectos)
		{
			listaProyectosVO.add(convertirProyectoVO(proyecto));
		}		
		return listaProyectosVO;
	}
	
	public static UsuarioVO convertirUsuarioVO(Usuario usuario)
	{
		UsuarioVO usuarioVO = new UsuarioVO();
		usuarioVO.setId(usuario.getId());
		usuarioVO.setUsuario(usuario.getUsuario());
		usuarioVO.setNombre(usuario.getNombre());
		usuarioVO.setApellido(usuario.getApellido());
		usuarioVO.setEmail(usuario.getEmail());
		usuarioVO.setPerfil(convertirPerfilVO(usuario.getPerfil()));
		
		return usuarioVO;
	}
	
	public static List<UsuarioVO> convertirListaUsuarioVO(List<Usuario> listaUsuarios)
	{
		List<UsuarioVO> listaUsuariosVO= new ArrayList<UsuarioVO>();
		
		for(Usuario usuario : listaUsuarios)
		{
			listaUsuariosVO.add(convertirUsuarioVO(usuario));
		}		
		return listaUsuariosVO;
	}
	
	public static PerfilVO convertirPerfilVO(Perfil perfil)
	{
		PerfilVO perfilVO = new PerfilVO();
		perfilVO.setId(perfil.getId());
		perfilVO.setDescripcion(perfil.getDescripcion());
		
		return perfilVO;
	}
	
	public static List<PerfilVO> convertirListaPerfilVO(List<Perfil> listaPerfiles)
	{
		List<PerfilVO> listaPerfilesVO = new ArrayList<PerfilVO>();
		
		for(Perfil perfil : listaPerfiles)
		{
			listaPerfilesVO.add(convertirPerfilVO(perfil));
		}		
		return listaPerfilesVO;
	}

	public static SinonimoVO convertirSinonimoVO(Sinonimo sinonimo)
	{
		SinonimoVO sinonimoVO = new SinonimoVO();
		sinonimoVO.setId(sinonimo.getId());
		sinonimoVO.setNombre(sinonimo.getNombre());
		
		return sinonimoVO;
	}
	
	private static List<SinonimoVO> convertirListaSinonimosVO(List<Sinonimo> listaSinonimos)
	{
		List<SinonimoVO> listaSinonimosVO = new ArrayList<SinonimoVO>();
		for(Sinonimo sinonimo : listaSinonimos)
		{
			listaSinonimosVO.add(convertirSinonimoVO(sinonimo));
		}		
		return listaSinonimosVO;
	}
	
	public static List<ElementoVO> convertirListaElementoVO(List<Elemento> listaElementos)
	{
		List<ElementoVO> listaElementosVO = new ArrayList<ElementoVO>();
		for(Elemento elemento : listaElementos)
		{
			listaElementosVO.add(convertirElementoVO(elemento));
		}		
		return listaElementosVO;
	}
	
	private static ElementoVO convertirElementoVO(Elemento elemento)
	{
		ElementoVO elementoVO = new ElementoVO();
		elementoVO.setId(elemento.getId());
		elementoVO.setNombre(elemento.getNombre());
		elementoVO.setEsCategoria(elemento.isEsCategoria());
		elementoVO.setTipoElemento(elemento.getTipoElemento());
		elementoVO.setSinonimos(convertirListaSinonimosVO(elemento.getSinonimos()));
		elementoVO.setElementosRelacionados(convertirListaSubElementoVO(elemento.getElementosRelacionados()));
		
		return elementoVO;
	}
	
	private static List<SubElementoVO> convertirListaSubElementoVO (List<Elemento> listaElementos)
	{
		List<SubElementoVO> listaSubElementosVO = new ArrayList<SubElementoVO>();
		for(Elemento elemento : listaElementos)
		{
			listaSubElementosVO.add(convertirSubElementoVO(elemento));
		}
		return listaSubElementosVO;
	}
	
	private static SubElementoVO convertirSubElementoVO(Elemento elemento)
	{
		SubElementoVO SubelementoVO = new SubElementoVO();
		SubelementoVO.setId(elemento.getId());
		SubelementoVO.setNombre(elemento.getNombre());

		return SubelementoVO;
	}

	public static List<Elemento> convertirListaSubElementoVOaElemento(List<SubElementoVO> elementosRelacionados)
	{
		List<Elemento> vRetorno= new ArrayList<Elemento>();
		for(SubElementoVO elemento : elementosRelacionados)
		{
			Elemento elem= new Elemento();
			elem.setId(elemento.getId());
			elem.setNombre(elemento.getNombre());
			vRetorno.add(elem);
		}
		return vRetorno;
	}





}
