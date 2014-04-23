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
	int numero;
	int cant_votos_positivos;
	int cant_votos_negativos;
	boolean estado;
	String img;
	int rol_id;
	int region_id;
	int comuna_id;
	String local_id; // int
	String created;
	String modified;

	public User(int id, String rut, String nombre, String apellido_paterno,
			String apellido_materno, String fecha_nacimiento, String email,
			String username, String password, String telefono_fijo,
			String telefono_movil, String poblacion, String calle, int numero,
			int cant_votos_positivos, int cant_votos_negativos, boolean estado,
			String img, int rol_id, int region_id, int comuna_id,
			String local_id, String created, String modified) {

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
		this.cant_votos_positivos = cant_votos_positivos;
		this.cant_votos_negativos = cant_votos_negativos;
		this.estado = estado;
		this.img = img;
		this.rol_id = rol_id;
		this.region_id = region_id;
		this.comuna_id = comuna_id;
		this.local_id = local_id;
		this.created = created;
		this.modified = modified;

	}

	public int getId() {
		return this.id;
	}

	public int getRol() {
		return this.rol_id;
	}

	@Override
	public String toString() {
		return this.rut + " - " + this.nombre + " " + this.apellido_paterno
				+ " " + this.apellido_materno;
	}
}
