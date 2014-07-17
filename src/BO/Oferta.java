package BO;

import java.io.Serializable;

public class Oferta implements Serializable{
	
	int id;
	String precio; //int
	String descripcion;
	int user_id;
	int producto_id;
	int local_id;
	String created;
	String modified;
	
	String producto_nombre;
	
	public Oferta(int id, String precio,String descripcion, int user_id,
			int producto_id,int local_id,String created,String modified) {
		
		this.id = id;
		this.precio = precio;
		this.descripcion = descripcion;
		this.user_id = user_id;
		this.producto_id = producto_id;
		this.local_id = local_id;
		this.created = created;
		this.modified = modified;
	}
	
	public Oferta(int id, String precio,String descripcion, int user_id,
			int producto_id,int local_id,String created,String modified,
			String producto_nombre) {
		
		this.id = id;
		this.precio = precio;
		this.descripcion = descripcion;
		this.user_id = user_id;
		this.producto_id = producto_id;
		this.local_id = local_id;
		this.created = created;
		this.modified = modified;
		
		this.producto_nombre = producto_nombre;
	}
	
	@Override
	public String toString() {
		return "Producto: "+this.producto_nombre+"\n"+
				"Precio: "+this.precio+"\n"+
				"Descripción: "+this.descripcion;
	}
}
