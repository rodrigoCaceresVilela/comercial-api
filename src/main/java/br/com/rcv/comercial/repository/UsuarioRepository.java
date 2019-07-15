package br.com.rcv.comercial.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.rcv.comercial.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByEmail(String email);
}
