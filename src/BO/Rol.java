package BO;

import java.io.Serializable;

public class Rol implements Serializable{

	int id;
	String nombre;
	String created;
	String modified;

	
	public Rol(int id, String nombre, String created,String modified) {
		this.id = id;
		this.nombre = nombre;
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