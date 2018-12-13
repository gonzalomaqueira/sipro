package uy.edu.ude.sipro.utiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import uy.edu.ude.sipro.entidades.Docente;
import uy.edu.ude.sipro.entidades.Elemento;
import uy.edu.ude.sipro.entidades.Perfil;
import uy.edu.ude.sipro.entidades.Proyecto;
import uy.edu.ude.sipro.entidades.Sinonimo;
import uy.edu.ude.sipro.entidades.Usuario;
import uy.edu.ude.sipro.valueObjects.DocenteVO;
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
									  proyecto.getCodigoUde(),
									  proyecto.getAnio(),
									  proyecto.getTitulo(),
									  proyecto.getCarrera(),
									  convertirListaDocenteVO(proyecto.getCorrectores()),
									  proyecto.getNota(),
									  proyecto.getAlumnos(),
									  convertirDocenteVO(proyecto.getTutor()),
									  proyecto.getTutorString(),
									  proyecto.getRutaArchivo(),
									  proyecto.getResumen(),
									  proyecto.getBibliografia(),
									  proyecto.getFechaAlta(),
									  proyecto.getFechaUltimaModificacion(),
									  convertirListaElementoVO(proyecto.getElementosRelacionados()),
									  proyecto.getCategoria());
						
	}
	
	public static ProyectoVO convertirProyectoVO(Proyecto proyecto)
	{
		return new ProyectoVO(proyecto.getId(), 
							  proyecto.getAnio(),
							  proyecto.getCodigoUde(),
							  proyecto.getCarrera(),
							  proyecto.getNota(),
							  proyecto.getTitulo(),
							  proyecto.getEstado(),
							  proyecto.getCategoria());
	}

	public static List<ProyectoVO> convertirListaProyectoVO(Set<Proyecto> listaProyectos)
	{
		List<ProyectoVO> listaProyectosVO = new ArrayList<ProyectoVO>();

		for(Proyecto proyecto : listaProyectos)
		{
			listaProyectosVO.add(convertirProyectoVO(proyecto));
		}		
		Collections.sort(listaProyectosVO);
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
	
	public static List<UsuarioVO> convertirListaUsuarioVO(Set<Usuario> listaUsuarios)
	{
		List<UsuarioVO> listaUsuariosVO= new ArrayList<UsuarioVO>();
		
		for(Usuario usuario : listaUsuarios)
		{
			listaUsuariosVO.add(convertirUsuarioVO(usuario));
		}		
		Collections.sort(listaUsuariosVO);
		return listaUsuariosVO;
	}
	
	public static PerfilVO convertirPerfilVO(Perfil perfil)
	{
		PerfilVO perfilVO = new PerfilVO();
		perfilVO.setId(perfil.getId());
		perfilVO.setDescripcion(perfil.getDescripcion());
		
		return perfilVO;
	}
	
	public static List<PerfilVO> convertirListaPerfilVO(Set<Perfil> listaPerfiles)
	{
		List<PerfilVO> listaPerfilesVO = new ArrayList<PerfilVO>();
		
		for(Perfil perfil : listaPerfiles)
		{
			listaPerfilesVO.add(convertirPerfilVO(perfil));
		}
		Collections.sort(listaPerfilesVO);
		return listaPerfilesVO;
	}

	public static SinonimoVO convertirSinonimoVO(Sinonimo sinonimo)
	{
		SinonimoVO sinonimoVO = new SinonimoVO();
		sinonimoVO.setId(sinonimo.getId());
		sinonimoVO.setNombre(sinonimo.getNombre());
		
		return sinonimoVO;
	}
	
	private static List<SinonimoVO> convertirListaSinonimosVO(Set<Sinonimo> listaSinonimos)
	{
		List<SinonimoVO> listaSinonimosVO = new ArrayList<SinonimoVO>();
		for(Sinonimo sinonimo : listaSinonimos)
		{
			listaSinonimosVO.add(convertirSinonimoVO(sinonimo));
		}
		Collections.sort(listaSinonimosVO);
		return listaSinonimosVO;
	}
	
	public static List<ElementoVO> convertirListaElementoVO(Set<Elemento> listaElementos)
	{
		List<ElementoVO> listaElementosVO = new ArrayList<ElementoVO>();
		for(Elemento elemento : listaElementos)
		{
			listaElementosVO.add(convertirElementoVO(elemento));
		}
		Collections.sort(listaElementosVO);
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
	
	private static List<SubElementoVO> convertirListaSubElementoVO (Set<Elemento> listaElementos)
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

	public static Set<Elemento> convertirListaSubElementoVOaElemento(Set<SubElementoVO> elementosRelacionados)
	{
		Set<Elemento> vRetorno= new HashSet<Elemento>();
		for(SubElementoVO elemento : elementosRelacionados)
		{
			Elemento elem= new Elemento();
			elem.setId(elemento.getId());
			elem.setNombre(elemento.getNombre());
			vRetorno.add(elem);
		}
		return vRetorno;
	}
	
	public static DocenteVO convertirDocenteVO(Docente corrector)
	{
		DocenteVO correctorVO = new DocenteVO();
		if (corrector != null)
		{						
			correctorVO.setId(corrector.getId());
			correctorVO.setNombre(corrector.getNombre());
			correctorVO.setApellido(corrector.getApellido());
		}
		return correctorVO;
	}
	
	public static List<DocenteVO> convertirListaDocenteVO(Set<Docente> listaDocentes)
	{
		List<DocenteVO> listaDocentesVO = new ArrayList<DocenteVO>();
		for(Docente docente : listaDocentes)
		{
			listaDocentesVO.add(convertirDocenteVO(docente));
		}
		Collections.sort(listaDocentesVO);
		return listaDocentesVO;
	}
	
	public static Docente convertirDocenteVOaDocente(DocenteVO docente)
	{
		Docente doc= null;
		if (docente != null)
		{
			doc = new Docente();
			doc.setId(docente.getId());
			doc.setNombre(docente.getNombre());
			doc.setApellido(docente.getApellido());
		}
		return doc;
	}
	
	public static Set<Docente> convertirListaDocenteVOaDocente(List<DocenteVO> docentes)
	{
		Set<Docente> vRetorno = null;
		if (docentes != null)
		{
			vRetorno= new HashSet<Docente>();
			for(DocenteVO docente : docentes)
			{
				vRetorno.add(convertirDocenteVOaDocente(docente));
			}
		}
		return vRetorno;
	}
}
