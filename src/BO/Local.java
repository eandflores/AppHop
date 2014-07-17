package BO;

import java.io.Serializable;

public class Local implements Serializable {
	int id;
	String nombre;
	String calle;
	int numero;
	String telefono_fijo; // int
	String telefono_movil; // int
	String email;
	String sitio_web;
	boolean estado;
	String img;
	int categoria_local_id;
	int user_id;
	int comuna_id;
	String created;
	String modified;

	public Local(int id, String nombre, String calle, int numero,
			String telefono_fijo, String telefono_movil, String email,
			String sitio_web, boolean estado, String img,
			int categoria_local_id, int user_id, int comuna_id,
			String created, String modified) {

		this.id = id;
		this.nombre = nombre;
		this.calle = calle;
		this.numero = numero;
		this.telefono_fijo = telefono_fijo;
		this.telefono_movil = telefono_movil;
		this.email = email;
		this.sitio_web = sitio_web;
		this.estado = estado;
		this.img = img;
		this.categoria_local_id = categoria_local_id;
		this.user_id = user_id;
		this.comuna_id = comuna_id;
		this.created = created;
		this.modified = modified;
	}

	public int getId() {
		return this.id;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String getCalle() {
		return this.calle;
	}
	
	public int getNumero() {
		return this.numero;
	}
	
	public String getTelefonoFijo() {
		return this.telefono_fijo;
	}
	
	public String getTelefonoMovil() {
		return this.telefono_movil;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getSitioWeb() {
		return this.sitio_web;
	}
	
	public boolean getEstado() {
		return this.estado;
	}
	
	public String getImg() {
		return this.img;
	}
	
	public int getCategoria() {
		return this.categoria_local_id;
	}
	
	public int getUser() {
		return this.user_id;
	}
	
	public int getComuna() {
		return this.comuna_id;
	}
	
	public String getCreated() {
		return this.created;
	}
	
	public String getModified() {
		return this.modified;
	}
	
	public String getDireccion() {
		return this.calle+" "+this.numero;
	}
	
	public String getTelefono() {
		String telefonos = "";
		
		if(this.telefono_fijo != null && this.telefono_movil != null){
			telefonos = this.telefono_fijo+" "+this.telefono_movil;
		}
		else if(this.telefono_fijo != null){
			telefonos = this.telefono_fijo;
		}
		else{
			telefonos = this.telefono_movil;
		}
		
		return telefonos;
	}

	@Override
	public String toString() {
		return this.nombre;
	}
}
