package pe.cibertec.gym.repository;

import pe.cibertec.gym.model.Membership;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MembershipRepository extends JpaRepository<Membership, Long> {
    @EntityGraph(attributePaths = "member")
    List<Membership> findAllByOrderByFechaFinAsc();

    @Override
    @EntityGraph(attributePaths = "member")
    Optional<Membership> findById(Long id);
}
