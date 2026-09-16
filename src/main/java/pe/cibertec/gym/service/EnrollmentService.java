package pe.cibertec.gym.service;

import pe.cibertec.gym.model.ClassEnrollment;
import pe.cibertec.gym.repository.ClassEnrollmentRepository;
import pe.cibertec.gym.repository.GymClassRepository;
import pe.cibertec.gym.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnrollmentService {
    private final ClassEnrollmentRepository enrollmentRepository;
    private final GymClassRepository classRepository;
    private final MemberRepository memberRepository;

    public EnrollmentService(ClassEnrollmentRepository enrollmentRepository, GymClassRepository classRepository,
                             MemberRepository memberRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.classRepository = classRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void enroll(Long classId, Long memberId) {
        var gymClass = classRepository.findById(classId)
                .orElseThrow(() -> new IllegalArgumentException("La clase no existe"));
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("El miembro no existe"));

        if (!"ACTIVO".equals(gymClass.getEstado()) || !"ACTIVO".equals(member.getEstado())) {
            throw new IllegalStateException("La clase y el miembro deben estar activos");
        }
        if (enrollmentRepository.existsByGymClassIdAndMemberId(classId, memberId)) {
            throw new IllegalStateException("El miembro ya está inscrito en esta clase");
        }
        long occupiedSlots = enrollmentRepository.countByGymClassIdAndEstado(classId, "ACTIVO");
        if (occupiedSlots >= gymClass.getCupos()) {
            throw new IllegalStateException("La clase no tiene cupos disponibles");
        }
        enrollmentRepository.save(new ClassEnrollment(gymClass, member));
    }
}
