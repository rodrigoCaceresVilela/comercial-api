package br.com.rcv.comercial.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.rcv.comercial.model.Categoria;
import br.com.rcv.comercial.repository.CategoriaRepository;

@RestController
@RequestMapping("categorias")
public class CategoriaController {

	@Autowired
	private CategoriaRepository repository;
	
	@GetMapping
	public List<Categoria> listar() {
		return repository.findAll();
	}
	
	@GetMapping("/{id}")
//	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')") -- Pode ser feito por método ou via configuração no ResourceServerConfig
	public ResponseEntity<Categoria> buscar(@PathVariable Long id) {
		Optional<Categoria> categoria = repository.findById(id);

		return categoria.isPresent() ? ResponseEntity.ok(categoria.get()) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
//	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	public ResponseEntity<Categoria> create(@Valid @RequestBody Categoria categoria) {
		Optional<Categoria> categoriaExistente = repository.findByNome(categoria.getNome());
		
		if(categoriaExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe uma categoria com este nome.");
		}

		Categoria categoriaSalva = repository.save(categoria);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(categoriaSalva.getId()).toUri();
		
		return ResponseEntity.created(uri).body(categoriaSalva);
	}
	
	@PutMapping
//	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	public void update(@Valid @RequestBody Categoria oportunidade) {
		repository.saveAndFlush(oportunidade);
	}
	
	@DeleteMapping("/{id}")
//	@PreAuthorize("hasAuthority('ROLE_REMOVER_CATEGORIA') and #oauth2.hasScope('write')")
	public void delete(@PathVariable Long id) {
		Optional<Categoria> oportunidade = repository.findById(id);
		
		if(oportunidade.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria inexistente.");
		}
		
		repository.delete(oportunidade.get());
	}
}