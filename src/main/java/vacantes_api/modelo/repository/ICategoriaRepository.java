package vacantes_api.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vacantes_api.modelo.entity.Categoria;

public interface ICategoriaRepository extends JpaRepository<Categoria, Integer> {

}
