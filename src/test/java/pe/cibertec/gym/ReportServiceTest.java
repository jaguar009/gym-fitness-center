package pe.cibertec.gym;

import pe.cibertec.gym.model.Member;
import pe.cibertec.gym.model.Payment;
import pe.cibertec.gym.repository.MemberRepository;
import pe.cibertec.gym.repository.PaymentRepository;
import pe.cibertec.gym.service.ReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class ReportServiceTest {
    @Autowired private ReportService reportService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private PaymentRepository paymentRepository;

    @BeforeEach
    void clean() {
        paymentRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    void generatesMemberAndPaymentPdfs() {
        Member member = new Member();
        member.setDni("90000001");
        member.setNombres("Maria");
        member.setApellidos("Lopez");
        member.setEstado("ACTIVO");
        memberRepository.save(member);

        Payment payment = new Payment();
        payment.setMember(member);
        payment.setMonto(new BigDecimal("120.00"));
        payment.setFechaPago(LocalDate.now());
        payment.setMetodoPago("EFECTIVO");
        payment.setConcepto("Membresia");
        paymentRepository.save(payment);

        byte[] memberPdf = reportService.memberReport("Maria", "ACTIVO");
        byte[] paymentPdf = reportService.paymentReport(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), "EFECTIVO");

        assertTrue(new String(memberPdf, 0, 4).equals("%PDF"));
        assertTrue(new String(paymentPdf, 0, 4).equals("%PDF"));
    }
}
