package serviciorest.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import serviciorest.modelo.entidad.Videojuego;
import serviciorest.modelo.persistencia.DaoVideojuego;

@RestController
public class ControladorVideojuego {

	@Autowired
	private DaoVideojuego daoVideojuego;

	@GetMapping(path = "videojuegos/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Videojuego> getVideojuego(@PathVariable("id") int id) {
		System.out.println("Buscando videojuego con id: " + id);
		Videojuego v = daoVideojuego.get(id);
		if (v != null) {
			return new ResponseEntity<Videojuego>(v, HttpStatus.OK);// 200 OK
		} else {
			return new ResponseEntity<Videojuego>(HttpStatus.NOT_FOUND);// 404 NOT FOUND
		}
	}

	@PostMapping(path = "videojuegos", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Videojuego> altaPersona(@RequestBody Videojuego v) {
		System.out.println("Alta videojuego: objeto videojuego: " + v);
		String prueba = daoVideojuego.addJuego(v);
		System.out.println(prueba);
		if (prueba != null) {
			return new ResponseEntity<Videojuego>(v, HttpStatus.CREATED);// 201 created
		} else {

			return new ResponseEntity<Videojuego>(HttpStatus.CONFLICT);// 409 conflict
		}
	}

	@GetMapping(path = "videojuegos", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Videojuego>> list() {

		System.out.println("Listando los videojuegos");
		System.out.println(daoVideojuego.list());

		return new ResponseEntity<List<Videojuego>>(daoVideojuego.list(), HttpStatus.OK);
	}

	@PutMapping(path = "videojuegos/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Videojuego> update(@PathVariable("id") int id, @RequestBody Videojuego v) {
		System.out.println("ID a modificar: " + id);
		System.out.println("Datos a modificar: " + v);
		v.setId(id);
		Videojuego vUpdate = daoVideojuego.update(v);
		if (vUpdate != null) {
			return new ResponseEntity<Videojuego>(HttpStatus.OK);// 200 OK
		} else {
			return new ResponseEntity<Videojuego>(HttpStatus.NOT_FOUND);// 404 NOT FOUND
		}
	}

	@DeleteMapping(path = "videojuegos/{id}")
	public ResponseEntity<Videojuego> borrarJuego(@PathVariable int id) {
		System.out.println("ID a borrar: " + id);
		Videojuego v = daoVideojuego.delete(id);
		if (v != null) {
			return new ResponseEntity<Videojuego>(v, HttpStatus.OK);// 200 OK
		} else {
			return new ResponseEntity<Videojuego>(HttpStatus.NOT_FOUND);// 404 NOT FOUND
		}
	}

}
