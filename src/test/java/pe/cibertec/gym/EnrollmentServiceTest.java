package pe.cibertec.gym;

import pe.cibertec.gym.model.GymClass;
import pe.cibertec.gym.model.Member;
import pe.cibertec.gym.repository.ClassEnrollmentRepository;
import pe.cibertec.gym.repository.GymClassRepository;
import pe.cibertec.gym.repository.MemberRepository;
import pe.cibertec.gym.service.EnrollmentService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {
    @Test
    void rejectsDuplicateEnrollment() {
        var enrollmentRepository = mock(ClassEnrollmentRepository.class);
        var classRepository = mock(GymClassRepository.class);
        var memberRepository = mock(MemberRepository.class);
        var gymClass = new GymClass();
        gymClass.setCupos(10);
        var member = new Member();
        when(classRepository.findByIdForUpdate(1L)).thenReturn(Optional.of(gymClass));
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        member.setId(1L);
        when(enrollmentRepository.findByGymClassIdOrderByFechaInscripcionDescIdDesc(1L)).thenReturn(java.util.List.of(new pe.cibertec.gym.model.ClassEnrollment(gymClass, member)));

        assertThrows(IllegalStateException.class, () -> new EnrollmentService(enrollmentRepository, classRepository, memberRepository).enroll(1L, 1L));
        verify(enrollmentRepository, never()).save(any());
    }
}
