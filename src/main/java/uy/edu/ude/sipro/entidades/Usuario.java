package uy.edu.ude.sipro.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "Usuarios", uniqueConstraints = {@UniqueConstraint(name = "uq_usuario_Usuarios", columnNames = "usuario")})
public class Usuario
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="IdUsuario")
	private int id;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(unique = true, name = "Usuario")
	private String usuario;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "Contrasenia")
	private String contrasenia;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "Nombre")
	private String nombre;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name = "Apellido")
	private String apellido;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(name ="Email")
	private String email;

	@ManyToOne
	@JoinColumn(name = "IdPerfil")
	private Perfil perfil;
	
	@Column(name = "Activo")
	private boolean activo;

	public Usuario() 
	{
		this.activo = true;
	}
	
	public Usuario( @NotNull @Size(min = 1, max = 255) String usuario,
					@NotNull @Size(min = 1, max = 255) String contrasenia,
					@NotNull @Size(min = 1, max = 255) String nombre, 
					@NotNull @Size(min = 1, max = 255) String apellido,
					@NotNull @Size(min = 1, max = 255) String email, 
					@NotNull @Size(min = 1, max = 255) Perfil perfil)
	{
		this.usuario = usuario;
		this.contrasenia=contrasenia;
		this.nombre = nombre;
		this.apellido = apellido;
		this.email = email;
		this.perfil = perfil;
		this.activo = true;
	}

	public int getId() { return id; }
	public void setId(int id) {	this.id = id; }

	public String getUsuario() { return usuario; }
	public void setUsuario(String usuario) { this.usuario = usuario; }
	
	public String getContrasenia() { return contrasenia; }
	public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	public String getApellido() { return apellido; }
	public void setApellido(String apellido) { this.apellido = apellido; }

	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	
	public Perfil getPerfil() { return perfil; }
	public void setPerfil(Perfil perfil) { this.perfil = perfil; }

	public boolean estaActivo() { return activo; }
	public void setActivo(boolean activo) { this.activo = activo; }

	@Override
	public String toString()
	{
		return String.format(
				"Usuario [id=%s, usuario=%s, contrasenia=%s, nombre=%s, apellido=%s, email=%s, perfil=%s, activo=%s]",
				id, usuario, contrasenia, nombre, apellido, email, perfil, activo);
	}
	
}
