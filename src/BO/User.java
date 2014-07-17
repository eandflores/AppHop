package BO;

import java.io.Serializable;

public class User implements Serializable {

	int id;
	String rut;
	String nombre;
	String apellido_paterno;
	String apellido_materno;
	String fecha_nacimiento;
	String email;
	String username;
	String password;
	String telefono_fijo; // int
	String telefono_movil; // int
	String poblacion;
	String calle;
	String numero; //int
	boolean estado;
	String img;
	int rol_id;
	int comuna_id;
	String created;
	String modified;

	public User(int id, String rut, String nombre, String apellido_paterno,
			String apellido_materno, String fecha_nacimiento, String email,
			String username, String password, String telefono_fijo,
			String telefono_movil, String poblacion, String calle, String numero,
			boolean estado,String img, int rol_id, int comuna_id,
			String created, String modified) {

		this.id = id;
		this.rut = rut;
		this.nombre = nombre;
		this.apellido_paterno = apellido_paterno;
		this.apellido_materno = apellido_materno;
		this.fecha_nacimiento = fecha_nacimiento;
		this.email = email;
		this.username = username;
		this.password = password;
		this.telefono_fijo = telefono_fijo;
		this.telefono_movil = telefono_movil;
		this.poblacion = poblacion;
		this.calle = calle;
		this.numero = numero;
		this.estado = estado;
		this.img = img;
		this.rol_id = rol_id;
		this.comuna_id = comuna_id;
		this.created = created;
		this.modified = modified;

	}
	
	public int getId() {
		return this.id;
	}
	
	public String getRut() {
		return this.rut;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getApellidoPaterno() {
		return this.apellido_paterno;
	}
	
	public void setApellidoPaterno(String apellido_paterno) {
		this.apellido_paterno = apellido_paterno;
	}
	
	public String getApellidoMaterno() {
		return this.apellido_materno;
	}
	
	public String getApellidos(){
		return this.apellido_paterno+" "+this.apellido_materno;
	} 
	
	public void setApellidoMaterno(String apellido_materno) {
		this.apellido_materno = apellido_materno;
	}
	
	public String getFechaNacimiento() {
		return this.fecha_nacimiento;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getTelefonoFijo() {
		return this.telefono_fijo;
	}
	
	public void setTelefonoFijo(String telefono_fijo) {
		this.telefono_fijo = telefono_fijo;
	}
	
	public String getTelefonoMovil() {
		return this.telefono_movil;
	}
	
	public void setTelefonoMovil(String telefono_movil) {
		this.telefono_movil = telefono_movil;
	}
	
	public String getPoblacion() {
		return this.poblacion;
	}
	
	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}
	
	public String getCalle() {
		return this.calle;
	}
	
	public void setCalle(String calle) {
		this.calle = calle;
	}
	
	public String getNumero() {
		return this.numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public boolean getEstado() {
		return this.estado;
	}
	public String getImg() {
		return this.img;
	}

	public int getRol() {
		return this.rol_id;
	}
	
	public int getComuna() {
		return this.comuna_id;
	}
	
	public void setComuna(int comuna_id) {
		this.comuna_id = comuna_id;
	}
	
	public String getCreated() {
		return this.created;
	}
	
	public String getModified() {
		return this.modified;
	}
	
	@Override
	public String toString() {
		return this.rut + " - " + this.nombre + " " + this.apellido_paterno
				+ " " + this.apellido_materno;
	}
}
