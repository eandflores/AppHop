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
	int region_id;
	int comuna_id;
	String created;
	String modified;

	public Local(int id, String nombre, String calle, int numero,
			String telefono_fijo, String telefono_movil, String email,
			String sitio_web, boolean estado, String img,
			int categoria_local_id, int user_id, int region_id, int comuna_id,
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
		this.region_id = region_id;
		this.comuna_id = comuna_id;
		this.created = created;
		this.modified = modified;
	}

	public int getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return this.nombre;
	}
}
