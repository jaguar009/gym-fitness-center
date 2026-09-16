package pe.cibertec.gym.repository;

import pe.cibertec.gym.model.ClassEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassEnrollmentRepository extends JpaRepository<ClassEnrollment, Long> {
    boolean existsByGymClassIdAndMemberId(Long classId, Long memberId);
    long countByGymClassIdAndEstado(Long classId, String estado);
}
