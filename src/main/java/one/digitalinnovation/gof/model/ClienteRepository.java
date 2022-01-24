package one.digitalinnovation.gof.model;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {
    //Interface que prove todos os metodos de acesso a dados
}
