package clienterest.entidad;

import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class Videojuego {

	private int id;
	private String nombre;
	private String compañia;
	private int nota;

	public Videojuego(String nombre, String compañia, int nota) {
		super();

		this.nombre = nombre;
		this.compañia = compañia;
		this.nota = nota;

	}

	public Videojuego() {
		super();

	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCompañia() {
		return compañia;
	}

	public void setCompañia(String compañia) {
		this.compañia = compañia;
	}

	public int getNota() {
		return nota;
	}

	public void setNota(int nota) {
		this.nota = nota;
	}

	@Override
	public String toString() {
		return "Videojuego [nombre=" + nombre + ", compañia=" + compañia + ", nota=" + nota + ", id=" + id + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(compañia, id, nombre, nota);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Videojuego other = (Videojuego) obj;
		return Objects.equals(compañia, other.compañia) && id == other.id && Objects.equals(nombre, other.nombre)
				&& nota == other.nota;
	}
}
