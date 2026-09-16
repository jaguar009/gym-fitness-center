package pe.cibertec.gym.repository;

import pe.cibertec.gym.model.GymClass;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GymClassRepository extends JpaRepository<GymClass, Long> {
    long countByEstado(String estado);
    @EntityGraph(attributePaths = "employee")
    List<GymClass> findAllByOrderByNombreAsc();

    @Override
    @EntityGraph(attributePaths = "employee")
    Optional<GymClass> findById(Long id);
}
