create database veterinaria_db;
use veterinaria_db;
drop database veterinaria_db;

select * from clientes;
select * from roles;
select * from trabajadores;

INSERT INTO clientes (apellidos, correo, direccion, dni, nombres, telefono, password, rol) 
VALUES (
    'Huaman Cruz', 
    'cristian@gmail.com', 
    'Reque', 
    '73381567', 
    'Chriso', 
    '987654321', 
    '$2a$12$pSUaHIWuXw7a6MigCt6HluctAmShiKQrrIJt4B5lQ42RKX7P5PIXW',
    'CLIENTE'
);

INSERT INTO clientes (apellidos, correo, direccion, dni, nombres, telefono, password, rol) 
VALUES (
    'Huaman Cruz', 
    'admin@gmail.com', 
    'Balta y Panama', 
    '73381544', 
    'Chriso', 
    '987654321', 
    '$2a$12$pSUaHIWuXw7a6MigCt6HluctAmShiKQrrIJt4B5lQ42RKX7P5PIXW',
    'ADMIN'
);
INSERT INTO clientes ( dni, nombres, apellidos, telefono, correo, direccion, password, rol) VALUES 
('12345678', 'Juan', 'Perez Rodriguez', '999888777', 'juan@gmail.com', 'Av. Balta 123', '$2a$12$drPrDhaWFAguFsoAQM3PIO1J9lyH41CQ61OfNOrkozDs8K1vPS3Ci', 'CLIENTE'),
('87654321', 'Maria', 'Gomez Sanchez', '987654321', 'maria@gmail.com', 'Calle Real 456', '$2a$12$drPrDhaWFAguFsoAQM3PIO1J9lyH41CQ61OfNOrkozDs8K1vPS3Ci', 'CLIENTE'),
('11223344', 'Esmeralda', 'Vasquez Carrasco', '951753852', 'emmy@gmail.com', 'Las Delicias', '$2a$12$drPrDhaWFAguFsoAQM3PIO1J9lyH41CQ61OfNOrkozDs8K1vPS3Ci', 'CLIENTE');

INSERT INTO mascotas (nombre, especie, raza, sexo, edad, observaciones, cliente_id) VALUES 
-- Mascotas de Juan (ID 3)
('Firulais', 'Perro', 'Mestizo', 'Macho', 5, 'Es muy juguetón, le gustan las pelotas.', 3),
('Luna', 'Perro', 'Golden Retriever', 'Hembra', 2, 'Alérgica al pollo.', 3),

-- Mascota de Maria (ID 3)
('Michi', 'Gato', 'Siamés', 'Macho', 3, 'No le gustan los extraños. Araña si lo tocan mucho.', 4),

-- Mascotas de Carlos (ID 5)
('Peluche', 'Perro', 'Pastor Aleman', 'Macho', 3, 'No quiere a Esmeralda XD.', 5),
('Harry', 'Perro', 'Persa', 'Macho', 4, 'Requiere cepillado diario.', 5);

INSERT INTO trabajadores (dni, nombres, apellidos, correo, telefono, especialidad, horario_disponible, estado_laboral) 
VALUES 
('40251478', 'Carlos Alberto', 'Mendoza Paredes', 'carlos.vet@huellitas.com', '987654321', 'Veterinario', 'Lun-Vie 8:00am - 4:00pm', 'ACTIVO'),
('72145896', 'Maria Fernanda', 'Lopez Torres', 'maria.asist@huellitas.com', '951753852', 'Asistente', 'Lun-Sab 9:00am - 6:00pm', 'ACTIVO'),
('10254789', 'Jorge Luis', 'Perez Gomez', 'jorge.grooming@huellitas.com', '963852741', 'Estilista', 'Mar-Dom 10:00am - 7:00pm', 'ACTIVO'),
('08963214', 'Elena Sofia', 'Diaz Medina', 'elena.cirugia@huellitas.com', '999111222', 'Cirujano', 'Previa Cita', 'VACACIONES'),
('45632178', 'Ana Paula', 'Ruiz Silva', 'ana.recepcion@huellitas.com', '922333444', 'Recepcionista', 'Lun-Vie 8:00am - 5:00pm', 'ACTIVO'),
('78965412', 'Roberto', 'Gomez Bolaños', 'roberto.vet@huellitas.com', '988777666', 'Veterinario', 'Turno Noche', 'INACTIVO');

