CREATE DATABASE IF NOT EXISTS gymfitnessdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gymfitnessdb;

CREATE TABLE IF NOT EXISTS miembro (
    idMiembro BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    correo VARCHAR(100),
    direccion VARCHAR(150),
    fechaRegistro DATE,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
);

CREATE TABLE IF NOT EXISTS empleado (
    idEmpleado BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(15) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    cargo VARCHAR(50),
    telefono VARCHAR(20),
    correo VARCHAR(100),
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
);

CREATE TABLE IF NOT EXISTS usuario (
    idUsuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO'
);

CREATE TABLE IF NOT EXISTS membresia (
    idMembresia BIGINT AUTO_INCREMENT PRIMARY KEY,
    idMiembro BIGINT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    costo DECIMAL(10,2) NOT NULL,
    fechaInicio DATE,
    fechaFin DATE,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT fk_membresia_miembro FOREIGN KEY (idMiembro) REFERENCES miembro(idMiembro) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS pago (
    idPago BIGINT AUTO_INCREMENT PRIMARY KEY,
    idMiembro BIGINT NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    fechaPago DATE,
    metodoPago VARCHAR(50),
    concepto VARCHAR(100),
    CONSTRAINT fk_pago_miembro FOREIGN KEY (idMiembro) REFERENCES miembro(idMiembro) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS asistencia (
    idAsistencia BIGINT AUTO_INCREMENT PRIMARY KEY,
    idMiembro BIGINT NOT NULL,
    fecha DATE NOT NULL,
    horaEntrada VARCHAR(10),
    observacion VARCHAR(150),
    CONSTRAINT fk_asistencia_miembro FOREIGN KEY (idMiembro) REFERENCES miembro(idMiembro) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS clase (
    idClase BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(150),
    horario VARCHAR(100),
    cupos INT NOT NULL DEFAULT 0,
    idEmpleado BIGINT,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT fk_clase_empleado FOREIGN KEY (idEmpleado) REFERENCES empleado(idEmpleado) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS inscripcion_clase (
    idInscripcion BIGINT AUTO_INCREMENT PRIMARY KEY,
    idClase BIGINT NOT NULL,
    idMiembro BIGINT NOT NULL,
    fechaInscripcion DATE NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    CONSTRAINT uk_clase_miembro UNIQUE (idClase, idMiembro),
    CONSTRAINT fk_inscripcion_clase FOREIGN KEY (idClase) REFERENCES clase(idClase) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_inscripcion_miembro FOREIGN KEY (idMiembro) REFERENCES miembro(idMiembro) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT IGNORE INTO miembro (dni, nombres, apellidos, telefono, correo, direccion, fechaRegistro, estado) VALUES
('70000001', 'Carlos', 'Ramirez', '999111222', 'carlos@gmail.com', 'Av. Lima 123', '2025-01-10', 'ACTIVO'),
('70000002', 'Lucia', 'Fernandez', '999222333', 'lucia@gmail.com', 'Jr. Sol 456', '2025-01-12', 'ACTIVO'),
('70000003', 'Jorge', 'Vasquez', '999333444', 'jorge@gmail.com', 'Av. Norte 789', '2025-01-15', 'INACTIVO');

INSERT IGNORE INTO empleado (dni, nombres, apellidos, cargo, telefono, correo, estado) VALUES
('80000001', 'Ana', 'Torres', 'Entrenador', '988111222', 'ana@gym.com', 'ACTIVO'),
('80000002', 'Luis', 'Castro', 'Recepcionista', '988222333', 'luis@gym.com', 'ACTIVO'),
('80000003', 'Marta', 'Lopez', 'Instructor Zumba', '988333444', 'marta@gym.com', 'ACTIVO');

INSERT IGNORE INTO membresia (idMiembro, tipo, costo, fechaInicio, fechaFin, estado) VALUES
(1, 'Mensual', 120.00, '2025-02-01', '2025-02-28', 'ACTIVO'),
(2, 'Trimestral', 300.00, '2025-02-01', '2025-04-30', 'ACTIVO'),
(3, 'Mensual', 120.00, '2025-01-01', '2025-01-31', 'VENCIDO');

INSERT IGNORE INTO pago (idMiembro, monto, fechaPago, metodoPago, concepto) VALUES
(1, 120.00, '2025-02-01', 'EFECTIVO', 'Pago membresia mensual'),
(2, 300.00, '2025-02-01', 'YAPE', 'Pago membresia trimestral'),
(1, 50.00, '2025-02-10', 'TARJETA', 'Pago entrenamiento personal');

INSERT IGNORE INTO asistencia (idMiembro, fecha, horaEntrada, observacion) VALUES
(1, '2025-02-05', '07:00', 'Ingreso normal'),
(2, '2025-02-05', '08:15', 'Clase funcional'),
(1, '2025-02-06', '07:05', 'Ingreso normal');

INSERT IGNORE INTO clase (nombre, descripcion, horario, cupos, idEmpleado, estado) VALUES
('Zumba', 'Clase de baile y cardio', 'Lunes-Miercoles-Viernes 18:00', 25, 3, 'ACTIVO'),
('Spinning', 'Clase de resistencia y cardio', 'Martes-Jueves 19:00', 20, 1, 'ACTIVO'),
('Funcional', 'Entrenamiento funcional integral', 'Sabado 09:00', 30, 1, 'ACTIVO');

INSERT IGNORE INTO inscripcion_clase (idClase, idMiembro, fechaInscripcion, estado) VALUES
(1, 1, '2025-02-03', 'ACTIVO'),
(2, 2, '2025-02-03', 'ACTIVO'),
(3, 1, '2025-02-04', 'ACTIVO');

-- Los usuarios se crean al iniciar Spring Boot con contraseñas BCrypt.
