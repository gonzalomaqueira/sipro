package uy.edu.ude.sipro.valueObjects;

public class UsuarioVO implements Comparable 
{
	private int id;
	private String usuario;
	private String nombre;
	private String apellido;
	private String email;
	private String contrasenia;
	private String repetirContrasenia;
	private PerfilVO perfil;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public PerfilVO getPerfil() {
		return perfil;
	}
	public void setPerfil(PerfilVO perfil) {
		this.perfil = perfil;
	}
	public String getContrasenia() {
		return contrasenia;
	}
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}
	public String getRepetirContrasenia() {
		return repetirContrasenia;
	}
	public void setRepetirContrasenia(String repetirContrasenia) {
		this.repetirContrasenia = repetirContrasenia;
	}
	@Override
	public int compareTo(Object comparado)
	{
		return this.getNombre().toLowerCase().compareTo(((UsuarioVO)comparado).getNombre().toLowerCase());
	}
}
