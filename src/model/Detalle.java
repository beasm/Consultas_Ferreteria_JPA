package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the detalle database table.
 * 
 */
@Entity
@NamedQuery(name="Detalle.findAll", query="SELECT d FROM Detalle d")
public class Detalle implements Serializable {
	private static final long serialVersionUID = 1L;

	///Anotacion: indica q el atributo DetallePK es Clave Primaria y es un objeto definido en otra clase
	@EmbeddedId      
	private DetallePK id; //Atributo id es 1objeto de la clase DetallePK  

	private float precio;

	private int unidades;

	//bi-directional many-to-one association to Factura
	@ManyToOne
	@JoinColumn(name="NUMERO")
	private Factura factura;

	//bi-directional many-to-one association to Producto
	@ManyToOne
	@JoinColumn(name="CODIGO")
	private Producto producto;

	public Detalle() {
	}

	public DetallePK getId() {
		return this.id;
	}

	public void setId(DetallePK id) {
		this.id = id;
	}

	public float getPrecio() {
		return this.precio;
	}

	public void setPrecio(float precio) {
		this.precio = precio;
	}

	public int getUnidades() {
		return this.unidades;
	}

	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}

	public Factura getFactura() {
		return this.factura;
	}

	public void setFactura(Factura factura) {
		this.factura = factura;
	}

	public Producto getProducto() {
		return this.producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

}