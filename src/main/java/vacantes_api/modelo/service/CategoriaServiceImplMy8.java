package vacantes_api.modelo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vacantes_api.modelo.entity.Categoria;
import vacantes_api.modelo.repository.ICategoriaRepository;

@Service
public class CategoriaServiceImplMy8 extends GenericoCRUDServiceImplMy8<Categoria, Integer>
        implements ICategoriaService {

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Override
    protected ICategoriaRepository getRepository() {
        return categoriaRepository;
    }

    @Override
    public Optional<Categoria> findByName(String name) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByName'");
    }
}
