package BO;

public class Producto {
	int id;
	String nombre;
	int categoria_producto_id;
	int user_id;
	String created;
	String modified;

	public Producto(int id, String nombre, int categoria_producto_id,
			int user_id) {
		this.id = id;
		this.nombre = nombre;
		this.categoria_producto_id = categoria_producto_id;
		this.user_id = user_id;
	}

	public Producto(int id, String nombre, int categoria_producto_id,
			int user_id, String created, String modified) {
		this.id = id;
		this.nombre = nombre;
		this.categoria_producto_id = categoria_producto_id;
		this.user_id = user_id;
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
