package br.com.rcv.comercial.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.rcv.comercial.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

	Optional<Categoria> findByNome(String nome);
}
