package serviciorest.modelo.persistencia;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import serviciorest.modelo.entidad.Videojuego;

@Component
public class DaoVideojuego {

	public List<Videojuego> listaJuegos;
	public int id;
	
	public DaoVideojuego() {
		System.out.println("DaoPersona -> Creando la lista de personas!");
		listaJuegos = new ArrayList<Videojuego>();
		Videojuego j1 = new Videojuego(id++,"WoW","Blizzard",10);
		Videojuego j2 = new Videojuego(id++,"LoL","Riot",9);
		Videojuego j3 = new Videojuego(id++,"Cyberpunk"," CD Projekt",8);
		Videojuego j4 = new Videojuego(id++,"Assassin's Creed","Ubisoft", 3 );
		Videojuego j5 = new Videojuego(id++,"Lost Ark","Tripod Studio", 7);
		listaJuegos.add(j1);
		listaJuegos.add(j2);
		listaJuegos.add(j3);
		listaJuegos.add(j4);
		listaJuegos.add(j5);
	}
	
	public String addJuego(Videojuego v) {

		for (Videojuego p : listaJuegos) {
			if (p.getNombre().equalsIgnoreCase(v.getNombre())) {
				return null;
			}
		}
		v.setId(id++);
		listaJuegos.add(v);
		return "Añadido";
	}
	 
	public Videojuego delete(int posicion) {
		try {
			return listaJuegos.remove(posicion);
		} catch (IndexOutOfBoundsException e) {
			System.out.println("delete -> Videojuego fuera de rango");
			return null;
		}
	}
	
	
	public Videojuego update(Videojuego v) {
		try {
			Videojuego vAux = listaJuegos.get(v.getId());
			vAux.setNombre(v.getNombre());
			vAux.setCompañia(v.getCompañia());
			vAux.setNota(v.getNota());
			
			return vAux;
		} catch (IndexOutOfBoundsException iobe) {
			System.out.println("update -> Persona fuera de rango");
			return null;
		}
	}
	
	public Videojuego get(int id) {
		for(Videojuego p:  listaJuegos) {
			if(p.getId()==id) {
				return p;
			}
		}return null;
	}
	
	public List<Videojuego> list() {
		return listaJuegos;
	}
	
	
	
}
