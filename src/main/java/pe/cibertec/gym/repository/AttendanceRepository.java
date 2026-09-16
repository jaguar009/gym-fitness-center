package pe.cibertec.gym.repository;

import pe.cibertec.gym.model.Attendance;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @EntityGraph(attributePaths = "member")
    List<Attendance> findAllByOrderByFechaDescIdDesc();
}
