create database veterinaria_db;
use veterinaria_db;
drop database veterinaria_db;

select * from clientes;
select * from roles;
select * from trabajadores;

-- Desactivar revisiones de claves foráneas para truncar tablas
SET FOREIGN_KEY_CHECKS = 0;

-- 1. TRABAJADORES
TRUNCATE TABLE trabajadores;
INSERT INTO trabajadores (id, dni, nombres, apellidos, correo, telefono, especialidad, horario_disponible, estado_laboral) VALUES
(1, '40251478', 'Carlos Alberto', 'Mendoza Paredes', 'carlos.vet@huellitas.com', '987654321', 'Veterinario', 'Lun-Vie 8am-4pm', 'ACTIVO'),
(2, '72145896', 'Maria Fernanda', 'Lopez Torres', 'maria.asist@huellitas.com', '951753852', 'Asistente', 'Lun-Sab 9am-6pm', 'ACTIVO'),
(3, '10254789', 'Jorge Luis', 'Perez Gomez', 'jorge.grooming@huellitas.com', '963852741', 'Estilista', 'Mar-Dom 10am-7pm', 'ACTIVO'),
(4, '08963214', 'Elena Sofia', 'Diaz Medina', 'elena.cirugia@huellitas.com', '999111222', 'Cirujano', 'Previa Cita', 'VACACIONES'),
(5, '45632178', 'Ana Paula', 'Ruiz Silva', 'ana.recepcion@huellitas.com', '922333444', 'Recepcionista', 'Lun-Vie 8am-5pm', 'ACTIVO'),
(6, '78965412', 'Roberto', 'Gomez Bolaños', 'roberto.vet@huellitas.com', '988777666', 'Veterinario', 'Turno Noche', 'INACTIVO'),
(7, '41256398', 'Sofia', 'Vergara Rios', 'sofia.derma@huellitas.com', '911222333', 'Veterinario', 'Lun-Mie-Vie 2pm-8pm', 'ACTIVO'),
(8, '14785236', 'Javier', 'Soto Mayor', 'javier.asist@huellitas.com', '988555222', 'Asistente', 'Mar-Jue-Sab 8am-8pm', 'ACTIVO'),
(9, '96325874', 'Lucia', 'Campos Elias', 'lucia.grooming@huellitas.com', '977444111', 'Estilista', 'Fines de Semana', 'ACTIVO');

-- 2. CLIENTES (Password: 123456)
TRUNCATE TABLE clientes;
-- Hash BCrypt para '123456': $2a$10$euWC.6Q.0/1.1.1.1.1.1.1.1.1.1.1 (Asegúrate de generar uno válido en tu entorno si este falla)
INSERT INTO clientes (id, nombres, apellidos, dni, correo, telefono, direccion, password, rol) VALUES
(1, 'Chriso', 'Huaman Cruz', '73381544', 'admin@gmail.com', '987654321', 'Balta y Panama', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'ADMIN'),
(2, 'Jesus', 'Huaman Cruz', '73381567', 'cristian@gmail.com', '987654321', 'Reque', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'CLIENTE'),
(3, 'Juan', 'Perez Rodriguez', '12345678', 'juan@gmail.com', '999888777', 'Av. Balta 123', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'CLIENTE'),
(4, 'Maria', 'Gomez Sanchez', '87654321', 'maria@gmail.com', '987654321', 'Calle Real 456', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'CLIENTE'),
(5, 'Esmeralda', 'Vasquez Carrasco', '11223344', 'emmy@gmail.com', '951753852', 'Las Delicias', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'CLIENTE'),
(6, 'Carlos', 'Lopez Torres', '45678912', 'carlos.lopez@email.com', '963852741', 'Urb. Santa Victoria', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'CLIENTE'),
(7, 'Ana', 'Martinez Ruiz', '78912345', 'ana.martinez@email.com', '951357456', 'Calle Los Pinos', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'CLIENTE'),
(8, 'Pedro', 'Castillo Flores', '15975346', 'pedro.castillo@email.com', '987123654', 'Av. Salaverry', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'CLIENTE'),
(9, 'Lucia', 'Fernandez Diaz', '35715948', 'lucia.fer@email.com', '925846317', 'Urb. La Primavera', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'CLIENTE'),
(10, 'Miguel', 'Torres Vega', '85245619', 'miguel.torres@email.com', '914725836', 'Calle San Jose', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'CLIENTE'),
(11, 'Sofia', 'Ramirez Soto', '96325874', 'sofia.ramirez@email.com', '936547821', 'Av. Grau', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'CLIENTE'),
(12, 'Laura', 'Salazar Mejia', '41526378', 'laura.salazar@email.com', '988777666', 'Pimentel', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'CLIENTE'),
(13, 'Diego', 'Alvarado Quispe', '74185296', 'diego.alv@email.com', '977444111', 'La Victoria', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'CLIENTE'),
(14, 'Carmen', 'Rosa Vilela', '36925814', 'carmen.rosa@email.com', '966333222', 'Mochumí', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'CLIENTE'),
(15, 'Fernando', 'Cisneros Paz', '15935748', 'fer.cisneros@email.com', '955222888', 'Lambayeque Centro', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'CLIENTE'),
(16, 'Patricia', 'Urbina Santos', '25845639', 'patty.urbina@email.com', '944111777', 'Monsefú', '$2a$12$mLPxrTk4sntzVGjo5Q//QuhCTf7fwd1K/IpIvil0hTwoI7GAz1Sg2', 'CLIENTE');

-- 3. MASCOTAS
TRUNCATE TABLE mascotas;
INSERT INTO mascotas (id, nombre, especie, raza, sexo, edad, observaciones, cliente_id) VALUES
(1, 'Firulais', 'Perro', 'Mestizo', 'Macho', 5, 'Muy juguetón.', 3),
(2, 'Luna', 'Perro', 'Golden Retriever', 'Hembra', 2, 'Alérgica al pollo.', 3),
(3, 'Michi', 'Gato', 'Siamés', 'Macho', 3, 'No le gustan los extraños.', 4),
(4, 'Peluche', 'Perro', 'Shih Tzu', 'Macho', 3, 'Se parece a un peluche.', 5),
(5, 'Harry', 'Perro', 'Persa', 'Macho', 4, 'Requiere cepillado diario.', 5),
(6, 'Rex', 'Perro', 'Pastor Alemán', 'Macho', 6, 'Guardían de casa.', 6),
(7, 'Nala', 'Gato', 'Angora', 'Hembra', 2, 'Muy cariñosa.', 7),
(8, 'Thor', 'Perro', 'Bulldog', 'Macho', 1, 'Ronca mucho.', 8),
(9, 'Coco', 'Ave', 'Loro', 'Macho', 10, 'Habla mucho.', 9),
(10, 'Simba', 'Gato', 'Común Europeo', 'Macho', 5, 'Cazador.', 10),
(11, 'Rocky', 'Perro', 'Boxer', 'Macho', 3, 'Mucha energía.', 11),
(12, 'Tambor', 'Otro', 'Conejo Cabeza de León', 'Macho', 1, 'Come mucha zanahoria.', 12),
(13, 'Bruno', 'Perro', 'Bulldog Francés', 'Macho', 4, 'Problemas respiratorios leves.', 13),
(14, 'Princesa', 'Gato', 'Persa', 'Hembra', 3, 'Muy delicada.', 14),
(15, 'Max', 'Perro', 'Labrador', 'Macho', 7, 'Sufre de artrosis.', 15),
(16, 'Bolita', 'Otro', 'Hámster', 'Hembra', 1, 'Duerme todo el día.', 16);

-- Reactivar revisiones de claves foráneas
SET FOREIGN_KEY_CHECKS = 1;