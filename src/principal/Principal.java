package principal;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import model.Cliente;
import model.Detalle;
import model.DetallePK;
import model.Factura;
import model.Producto;

public class Principal {
	private static Scanner lector;
	private static EntityManagerFactory factoria;
	private static EntityManager em;

	public static void main(String[] args) {
		factoria = Persistence.createEntityManagerFactory("ActividadUF3");
		em = factoria.createEntityManager();
		lector = new Scanner(System.in);
		String opcion;
		do {
			System.out.println();
			System.out.println();
			// System.out.println("GESTIÓN FERRETERÍA");
			// System.out.println("-----------------------------");
			// System.out.println("1. Inventario");
			// System.out.println("2. Consultar todas las facturas");
			// System.out.println("3. Información de un cliente");
			// System.out.println("4. Añadir nuevo cliente");
			// System.out.println("5. Añadir nueva factura");
			// System.out.println("6. Listado de productos para reponer");
			// System.out.println("7. Terminar programa");
			System.out.println("CONSULTAS FERRETERÍA");
			System.out.println("-----------------------------");
			System.out.println("1. Listado de cientes");
			System.out.println("2. Consultar facturas");
			System.out.println("3. Suma de los importes de todas las ventas");
			System.out.println("4. Descripcion de cada articulo junto al numero total de unidades vendidas");
			System.out.println("5. Terminar programa");

			System.out.println("¿Qué opción eliges?");
			opcion = lector.nextLine();
			switch (opcion) {
			case "1":
				// inventario();
				listadoClientes();
				break;
			case "2":
				// facturasDetalles();
				consultarFactura();
				break;
			case "3":
				// consultaCliente();
				importeVentas();
				break;
			case "4":
				// aniadirCliente();
				ventas();
				break;
			// case "5":
			// aniadirFactura();
			// break;
			// case "6":
			// listadoReposicion();
			// break;
			// case "7":
			case "5":
				System.out.println("Hasta pronto");
			}
			// } while (!opcion.equals("7"));
		} while (!opcion.equals("5"));
		lector.close();
	}

	private static void listadoClientes() {
		TypedQuery<Cliente> query = em.createNamedQuery("Cliente.findAll", Cliente.class);
		List<Cliente> cliente = query.getResultList();
		for (Cliente c : cliente) {
			System.out.println(c.getNif() + " - " + c.getNombre() + " - " + c.getTlf());
		}

	}

	private static void consultarFactura() {
		System.out.println("Imtroduce numero de factura: ");
		Integer numero = lector.nextInt();
		lector.nextLine();
		Factura factura = em.find(Factura.class, numero);
		if (factura == null) {
			System.out.println("No se ha encontrado la factura = " + numero);
		} else {
			System.out.println("Fecha: " + factura.getFecha());
			System.out.println("Pagado: " + factura.getPagado());
			System.out.println("Cliente: " + factura.getCliente().getNif() + " - " + factura.getCliente().getNombre());
			System.out.println();
			float total = 0;
			for (Detalle d : factura.getDetalles()) {
				total += d.getPrecio() * d.getUnidades();
				System.out.println(" " + d.getProducto().getDescripcion() + ", " + d.getUnidades() + " unidades a "
						+ d.getPrecio() + " €");
			}
			System.out.println();
			System.out.println(" IMPORTE TOTAL: " + total);

		}

	}

	private static void importeVentas() {
		TypedQuery<Detalle> query = em.createNamedQuery("Detalle.findAll", Detalle.class);
		List<Detalle> detalle = query.getResultList();
		float total = 0;
		for (Detalle d : detalle) {
			total += d.getPrecio() * d.getUnidades();
		}
		System.out.println("Importe total de las ventas: " + total);

	}
	
	

	private static void ventas() {
//		List<Detalle> detalles = obtenerDetalles();// Método que devuelve una lista de detalles metida a mano,
		TypedQuery<Detalle> query = em.createNamedQuery("Detalle.findAll", Detalle.class);
		List<Detalle> detalles = query.getResultList();
		// que simula lo que haríamos con JPA
		HashMap<Producto, Integer> mapa = new HashMap<>();

		for (Detalle detalle : detalles) {
			Producto p = detalle.getProducto();
			int cantidad = detalle.getUnidades();
			// Veo si ya existe elproducto en el mapa
			if (mapa.containsKey(p)) {
				// Como ya existe, tengo que actualizar la cantidad
				int cantidad_previa = mapa.get(p);
				int cantidad_actualizada = cantidad_previa + cantidad;
				mapa.replace(p, cantidad_actualizada);
			} else {
				mapa.put(p, cantidad);
			}
		}
		recorrerMapa(mapa);

	}

	private static void recorrerMapa(HashMap<Producto, Integer> mapa) {
		// Recorro el mapa mostrando los datos
		for (Map.Entry<Producto, Integer> entrada : mapa.entrySet()) {
			System.out.println(entrada.getKey().getDescripcion() + "....." + entrada.getValue());
		}
	}

	// public static void inventario() {
	// TypedQuery<Producto> query = em.createNamedQuery("Producto.findAll",
	// Producto.class);
	// List<Producto> productos = query.getResultList();
	// for (Producto p : productos) {
	// System.out.println(p.getCodigo() + " - " + p.getDescripcion() + " - " +
	// p.getPrecio() + " € - "
	// + p.getStock() + "unidades.");
	// }
	// }
	//
	// public static void facturasDetalles() {
	// TypedQuery<Factura> query = em.createNamedQuery("Factura.findAll",
	// Factura.class);
	// List<Factura> facturas = query.getResultList();
	// for (Factura f : facturas) {
	// System.out.println(f.getNumero() + " - " + f.getFecha() + " - " +
	// f.getCliente().getNombre());
	// for (Detalle d : f.getDetalles()) {
	// Producto p = d.getProducto();
	// System.out.println(" " + p.getCodigo() + " - " + p.getDescripcion() + " - " +
	// d.getPrecio() + "€ "
	// + d.getUnidades());
	// }
	// }
	// }
	//
	// public static void consultaCliente() {
	// System.out.println("Escribe NIF del cliente buscado: ");
	// String nif = lector.nextLine();
	// Cliente cli = em.find(Cliente.class, nif);
	// if (cli == null) {
	// System.out.println("No se ha encontrado el cliente con NIF = " + nif);
	// } else {
	// System.out.println("Nombre: " + cli.getNombre());
	// System.out.println("Domicilio: " + cli.getDomicilio());
	// System.out.println("Teléfono: " + cli.getTlf());
	// System.out.println("Ciudad: " + cli.getCiudad());
	// System.out.println("FACTURAS: ");
	// for (Factura f : cli.getFacturas()) {
	// System.out.println(" " + f.getNumero() + " " + f.getFecha());
	// }
	// }
	// }
	//
	// public static void aniadirCliente() {
	// System.out.println("NIF del nuevo cliente: ");
	// String nif = lector.nextLine();
	// Cliente cli = em.find(Cliente.class, nif);
	// if (cli == null) {
	// cli = new Cliente();
	// cli.setNif(nif);
	// System.out.println("Nombre: ");
	// cli.setNombre(lector.nextLine());
	// System.out.println("Dirección: ");
	// cli.setDomicilio(lector.nextLine());
	// System.out.println("Teléfono: ");
	// cli.setTlf(lector.nextLine());
	// System.out.println("Ciudad: ");
	// cli.setCiudad(lector.nextLine());
	// EntityTransaction et = em.getTransaction();
	// et.begin();
	// em.persist(cli);
	// et.commit();
	// } else {
	// System.out.println("Ya existe un cliente con NIF: " + nif);
	// }
	// }
	//
	// public static void aniadirFactura() {
	// System.out.println("NIF del cliente: ");
	// String nif = lector.nextLine();
	// Cliente cli = em.find(Cliente.class, nif);
	// if (cli == null) {
	// System.out.println("No existe el cliente con NIF = " + nif);
	// System.out.println("Seleccione primero la opción 4");
	// } else {
	// System.out.println("Cliente: " + cli.getNombre());
	// TypedQuery<Integer> query = em.createQuery("SELECT MAX(f.numero) FROM Factura
	// f", Integer.class);
	// Integer numFactura = query.getSingleResult() + 1;
	// System.out.println("Número de factura: " + numFactura);
	// // Construimos objeto factura.
	// Factura f = new Factura();
	// f.setNumero(numFactura);
	// f.setCliente(cli);
	// f.setPagado(false);
	// f.setFecha(new Date());
	// // Añadimos la factura al cliente.
	// cli.addFactura(f);
	// // Iniciamos la transacción y persistimos la factura.
	// EntityTransaction et = em.getTransaction();
	// et.begin();
	// em.persist(f);
	// // Construimos los detalles de la factura.
	// String continuar;
	// do {
	// System.out.println("Código de artículo: ");
	// String codigo = lector.nextLine();
	// Producto pro = em.find(Producto.class, codigo);
	// if (pro == null) {
	// System.out.println("No se encuentra el producto con código " + codigo);
	// } else {
	// System.out.println("Descripción: " + pro.getDescripcion());
	// System.out.println("Precio: " + pro.getPrecio());
	// System.out.println("¿Cuantas unidades desea? ");
	// int unidades = lector.nextInt();
	// lector.nextLine(); // Recoge el retorno de carro.
	// // Creamos el objeto Detalle con su primary key compuesta.
	// DetallePK pk = new DetallePK();
	// pk.setCodigo(codigo);
	// pk.setNumero(numFactura);
	// Detalle d = new Detalle();
	// d.setId(pk);
	// d.setPrecio(pro.getPrecio());
	// d.setUnidades(unidades);
	// d.setProducto(pro);
	// d.setFactura(f);
	// // Persistimos el detalle.
	// em.persist(d);
	// // Añadimos el detalle a la factura.
	// f.addDetalle(d);
	// }
	// System.out.println("¿Deseas seguir comprando (S/N)? ");
	// continuar = lector.nextLine();
	// } while (continuar.toUpperCase().equals("S"));
	// et.commit();
	// }
	// }
	//
	// public static void listadoReposicion() {
	// TypedQuery<Object[]> query = em.createQuery(
	// "SELECT pro.codigo, pro.descripcion, pro.minimo-pro.stock FROM Producto pro
	// WHERE pro.stock < pro.minimo", Object[].class);
	// List<Object[]> resultados = query.getResultList();
	// for (Object[] p : resultados) {
	// System.out.println(p[0] + " - " + p[1] + " - " + p[2]);
	// }
	// }
}