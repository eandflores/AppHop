package BO;

import java.io.Serializable;

public class Comuna implements Serializable {

	int id;
	String nombre;
	int region_id;
	String created;
	String modified;

	public Comuna(int id, String nombre, int region_id) {
		this.id = id;
		this.nombre = nombre;
		this.region_id = region_id;
	}

	public Comuna(int id, String nombre, int region_id, String created,
			String modified) {
		this.id = id;
		this.nombre = nombre;
		this.region_id = region_id;
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
