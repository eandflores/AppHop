package BO;

import java.io.Serializable;

public class Region implements Serializable {
	int id;
	String numero;
	String nombre;
	String created;
	String modified;

	public Region(int id, String numero, String nombre) {
		this.id = id;
		this.numero = numero;
		this.nombre = nombre;
	}

	public Region(int id, String numero, String nombre, String created,
			String modified) {
		this.id = id;
		this.numero = numero;
		this.nombre = nombre;
		this.created = created;
		this.modified = modified;
	}

	public int getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return this.numero + " " + this.nombre;
	}
}
