package com.example.schedule.Config;

import com.example.schedule.Model.*;
import com.example.schedule.Repository.JPA.*;
import com.example.schedule.Repository.Mongo.EnfermedadRepository;
import com.example.schedule.Repository.Mongo.ProductoRepository;
import com.example.schedule.Repository.Mongo.ServicioRepository;
import com.example.schedule.Repository.Mongo.TestimonioRepository;
import com.example.schedule.Repository.Mongo.VacunaCatalogoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DataLoader {

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Bean
        CommandLineRunner initDatabase(
                        ProductoRepository productoRepository,
                        ServicioRepository servicioRepository,
                        ClienteRepository clienteRepository,
                        MascotaRepository mascotaRepository,
                        TrabajadorRepository trabajadorRepository,
                        CitaRepository citaRepository,
                        VentaRepository ventaRepository,
                        TestimonioRepository testimonioRepository,
                        EnfermedadRepository enfermedadRepository,
                        VacunaCatalogoRepository vacunaCatalogoRepository) {
                return args -> {

                        // --- 1. CARGAR CLIENTES (1 Admin + 15 Clientes) ---
                        if (clienteRepository.count() == 0) {
                                // ADMIN
                                Cliente admin = crearCliente("Chriso", "Huaman Cruz", "73381544", "admin@gmail.com",
                                                "987654321", "Balta y Panama", "ADMIN");

                                // CLIENTES
                                List<Cliente> clientes = new ArrayList<>(Arrays.asList(
                                                admin,
                                                crearCliente("Jesus", "Huaman Cruz", "73381567", "cristian@gmail.com",
                                                                "987654321", "Reque", "CLIENTE"),
                                                crearCliente("Juan", "Perez Rodriguez", "12345678", "juan@gmail.com",
                                                                "999888777", "Av. Balta 123", "CLIENTE"),
                                                crearCliente("Maria", "Gomez Sanchez", "87654321", "maria@gmail.com",
                                                                "987654321", "Calle Real 456", "CLIENTE"),
                                                crearCliente("Esmeralda", "Vasquez Carrasco", "11223344",
                                                                "emmy@gmail.com", "951753852", "Las Delicias",
                                                                "CLIENTE"),
                                                crearCliente("Carlos", "Lopez Torres", "45678912",
                                                                "carlos.lopez@email.com", "963852741",
                                                                "Urb. Santa Victoria", "CLIENTE"),
                                                crearCliente("Ana", "Martinez Ruiz", "78912345",
                                                                "ana.martinez@email.com", "951357456",
                                                                "Calle Los Pinos", "CLIENTE"),
                                                crearCliente("Pedro", "Castillo Flores", "15975346",
                                                                "pedro.castillo@email.com", "987123654",
                                                                "Av. Salaverry", "CLIENTE"),
                                                crearCliente("Lucia", "Fernandez Diaz", "35715948",
                                                                "lucia.fer@email.com", "925846317", "Urb. La Primavera",
                                                                "CLIENTE"),
                                                crearCliente("Miguel", "Torres Vega", "85245619",
                                                                "miguel.torres@email.com", "914725836",
                                                                "Calle San Jose", "CLIENTE"),
                                                crearCliente("Sofia", "Ramirez Soto", "96325874",
                                                                "sofia.ramirez@email.com", "936547821", "Av. Grau",
                                                                "CLIENTE"),
                                                // NUEVOS CLIENTES
                                                crearCliente("Laura", "Salazar Mejia", "41526378",
                                                                "laura.salazar@email.com", "988777666", "Pimentel",
                                                                "CLIENTE"),
                                                crearCliente("Diego", "Alvarado Quispe", "74185296",
                                                                "diego.alv@email.com", "977444111", "La Victoria",
                                                                "CLIENTE"),
                                                crearCliente("Carmen", "Rosa Vilela", "36925814",
                                                                "carmen.rosa@email.com", "966333222", "Mochumí",
                                                                "CLIENTE"),
                                                crearCliente("Fernando", "Cisneros Paz", "15935748",
                                                                "fer.cisneros@email.com", "955222888",
                                                                "Lambayeque Centro", "CLIENTE"),
                                                crearCliente("Patricia", "Urbina Santos", "25845639",
                                                                "patty.urbina@email.com", "944111777", "Monsefú",
                                                                "CLIENTE")));
                                clienteRepository.saveAll(clientes);

                                // --- 2. CARGAR MASCOTAS ---
                                List<Mascota> mascotas = Arrays.asList(
                                                crearMascota("Firulais", "Perro", "Mestizo", "Macho", 5,
                                                                "Muy juguetón.", clientes.get(2)),
                                                crearMascota("Luna", "Perro", "Golden Retriever", "Hembra", 2,
                                                                "Alérgica al pollo.", clientes.get(2)),
                                                crearMascota("Michi", "Gato", "Siamés", "Macho", 3,
                                                                "No le gustan los extraños.", clientes.get(3)),
                                                crearMascota("Peluche", "Perro", "Shih Tzu", "Macho", 3,
                                                                "Se parece a un peluche.", clientes.get(4)),
                                                crearMascota("Harry", "Perro", "Persa", "Macho", 4,
                                                                "Requiere cepillado diario.", clientes.get(4)),
                                                crearMascota("Rex", "Perro", "Pastor Alemán", "Macho", 6,
                                                                "Guardían de casa.", clientes.get(5)),
                                                crearMascota("Nala", "Gato", "Angora", "Hembra", 2, "Muy cariñosa.",
                                                                clientes.get(6)),
                                                crearMascota("Thor", "Perro", "Bulldog", "Macho", 1, "Ronca mucho.",
                                                                clientes.get(7)),
                                                crearMascota("Coco", "Ave", "Loro", "Macho", 10, "Habla mucho.",
                                                                clientes.get(8)),
                                                crearMascota("Simba", "Gato", "Común Europeo", "Macho", 5, "Cazador.",
                                                                clientes.get(9)),
                                                crearMascota("Rocky", "Perro", "Boxer", "Macho", 3, "Mucha energía.",
                                                                clientes.get(10)),
                                                // NUEVAS MASCOTAS
                                                crearMascota("Tambor", "Otro", "Conejo Cabeza de León", "Macho", 1,
                                                                "Come mucha zanahoria.", clientes.get(11)),
                                                crearMascota("Bruno", "Perro", "Bulldog Francés", "Macho", 4,
                                                                "Problemas respiratorios leves.", clientes.get(12)),
                                                crearMascota("Princesa", "Gato", "Persa", "Hembra", 3, "Muy delicada.",
                                                                clientes.get(13)),
                                                crearMascota("Max", "Perro", "Labrador", "Macho", 7,
                                                                "Sufre de artrosis.", clientes.get(14)),
                                                crearMascota("Bolita", "Otro", "Hámster", "Hembra", 1,
                                                                "Duerme todo el día.", clientes.get(15)));
                                mascotaRepository.saveAll(mascotas);
                                System.out.println("✅ Clientes y Mascotas cargados.");
                        }

                        // --- 3. CARGAR TRABAJADORES ---
                        if (trabajadorRepository.count() == 0) {
                                List<Trabajador> trabajadores = Arrays.asList(
                                                crearTrabajador("40251478", "Carlos Alberto", "Mendoza Paredes",
                                                                "carlos.vet@huellitas.com", "987654321", "Veterinario",
                                                                "Lun-Vie 8am-4pm", "ACTIVO"),
                                                crearTrabajador("72145896", "Maria Fernanda", "Lopez Torres",
                                                                "maria.asist@huellitas.com", "951753852", "Asistente",
                                                                "Lun-Sab 9am-6pm", "ACTIVO"),
                                                crearTrabajador("10254789", "Jorge Luis", "Perez Gomez",
                                                                "jorge.grooming@huellitas.com", "963852741",
                                                                "Estilista", "Mar-Dom 10am-7pm", "ACTIVO"),
                                                crearTrabajador("08963214", "Elena Sofia", "Diaz Medina",
                                                                "elena.cirugia@huellitas.com", "999111222", "Cirujano",
                                                                "Previa Cita", "VACACIONES"),
                                                crearTrabajador("45632178", "Ana Paula", "Ruiz Silva",
                                                                "ana.recepcion@huellitas.com", "922333444",
                                                                "Recepcionista", "Lun-Vie 8am-5pm", "ACTIVO"),
                                                crearTrabajador("78965412", "Roberto", "Gomez Bolaños",
                                                                "roberto.vet@huellitas.com", "988777666", "Veterinario",
                                                                "Turno Noche", "INACTIVO"),
                                                // NUEVOS TRABAJADORES
                                                crearTrabajador("41256398", "Sofia", "Vergara Rios",
                                                                "sofia.derma@huellitas.com", "911222333", "Veterinario",
                                                                "Lun-Mie-Vie 2pm-8pm", "ACTIVO"), // Especialista piel
                                                crearTrabajador("14785236", "Javier", "Soto Mayor",
                                                                "javier.asist@huellitas.com", "988555222", "Asistente",
                                                                "Mar-Jue-Sab 8am-8pm", "ACTIVO"),
                                                crearTrabajador("96325874", "Lucia", "Campos Elias",
                                                                "lucia.grooming@huellitas.com", "977444111",
                                                                "Estilista", "Fines de Semana", "ACTIVO"));
                                trabajadorRepository.saveAll(trabajadores);
                                System.out.println("✅ Trabajadores cargados.");
                        }

                        // --- 4. CARGAR PRODUCTOS (Mongo) ---
                        if (productoRepository.count() == 0) {
                                List<Producto> productos = Arrays.asList(
                                                crearProd("Antiparasitarios",
                                                                "Protección completa contra pulgas y garrapatas.",
                                                                35.00, 100,
                                                                "https://www.latiendadefrida.com/cdn/shop/files/1200ftw-104722_1200x1200.jpg?v=1729201367"),
                                                crearProd("Alimento Premium",
                                                                "Nutrición balanceada adulto raza mediana.", 85.00, 50,
                                                                "https://naricitas.pet/wp-content/uploads/2023/07/Canbo-Dog-Balance-Adulto-Razas-Medianas-y-Grandes-de-Pollo-3kg.jpg"),
                                                crearProd("Shampoo Medicado",
                                                                "Tratamientos dermatológicos piel sensible.", 45.00, 30,
                                                                "https://oechsle.vteximg.com.br/arquivos/ids/17503990/imageUrl_1.jpg?v=638506051219770000"),
                                                crearProd("Suplementos", "Vitaminas y probióticos para cachorros.",
                                                                55.00, 40,
                                                                "https://cloudinary.images-iherb.com/image/upload/f_auto,q_auto:eco/images/srk/srk16803/l/8.jpg"),
                                                crearProd("Primeros Auxilios", "Kit de emergencia veterinaria.", 65.00,
                                                                20,
                                                                "https://tucanvetfuengirola.com/wp-content/uploads/2024/02/tucan-blog.jpg"),
                                                crearProd("Juguetes Dental", "Juguetes limpieza dental resistente.",
                                                                25.00, 150,
                                                                "https://cloudinary.images-iherb.com/image/upload/f_auto,q_auto:eco/images/arh/arh96853/y/9.jpg"),
                                                crearProd("Collar Reflectivo", "Collares para paseos nocturnos.", 15.00,
                                                                80,
                                                                "https://www.superpet.pe/on/demandware.static/-/Sites-SuperPet-master-catalog/default/dw008aa105/images/coastal-collar-reflectivo-3-4-12621-bwg12.jpg"),
                                                crearProd("Snacks Saludables", "Premios nutritivos sabor pollo.", 20.00,
                                                                200,
                                                                "https://aculi.pe/wp-content/uploads/2021/02/Hills-PD-Acu-li-pet-shop-store-alimento-Snacks-bocaditos-premios-para-Perros-Adultos.jpg"),
                                                // NUEVOS PRODUCTOS
                                                crearProd("Bravecto 10-20kg", "Pastilla masticable antipulgas 3 meses.",
                                                                120.00, 60,
                                                                "https://www.superpet.pe/on/demandware.static/-/Sites-SuperPet-master-catalog/default/dw221fedfd/images/msd-bravecto-500mg-10-20kg.jpg"),
                                                crearProd("Nexgard Spectra", "Antipulgas y desparasitante interno.",
                                                                65.00, 80,
                                                                "https://mascotify.pe/wp-content/uploads/2020/10/NEXGARD-SPECTRA-30kg-a-60kg-Antipulgas-y-antiparasitario.png"),
                                                crearProd("Hills Science Diet", "Alimento prescripción riñones k/d.",
                                                                150.00, 25,
                                                                "https://perupets.com/215-large_default/hills-science-diet-adult-large-breed.jpg"),
                                                crearProd("Ricocan Cordero", "Alimento nacional premium 15kg.", 95.00,
                                                                40,
                                                                "https://promart.vteximg.com.br/arquivos/ids/7299463-1000-1000/image-c8f0958b06eb410dac36165cac3e581f.jpg?v=638249684594670000"),
                                                crearProd("Arena para Gatos", "Aglutinante aroma lavanda 10kg.", 30.00,
                                                                100,
                                                                "https://oechsle.vteximg.com.br/arquivos/ids/19981432-1000-1000/imageUrl_1.jpg?v=638683291603970000"),
                                                crearProd("Rascador Torre", "Árbol para gatos 3 niveles.", 180.00, 15,
                                                                "https://images.unsplash.com/photo-1545249390-6bdfa286032f?w=400"),
                                                crearProd("Cama Ortopédica", "Cama viscoelástica para perros mayores.",
                                                                200.00, 10,
                                                                "https://rimage.ripley.com.pe/home.ripley/Attachment/MKP/1465/PMP00002101393/full_image-1.jpeg"),
                                                crearProd("Pipeta Frontline", "Antipulgas tópico gatos.", 35.00, 90,
                                                                "https://static-shop.vivapets.com/media/catalog/product/cache/11fc96e7318a291175a0004e054be56e/f/r/frontline-dog-tri-act-20-40-kg-1-unity-w-3-pipettes.jpg"));
                                productoRepository.saveAll(productos);
                                System.out.println("✅ Productos MongoDB cargados.");
                        }

                        // --- 5. CARGAR SERVICIOS (Mongo) ---
                        if (servicioRepository.count() == 0) {
                                List<Servicio> servicios = Arrays.asList(
                                                crearServicio("Consulta General", "Chequeo completo de salud.",
                                                                "stethoscope", "from-emerald-500", "to-teal-500",
                                                                50.00),
                                                crearServicio("Vacunación", "Aplicación de vacunas anuales.",
                                                                "vaccines", "from-blue-500", "to-cyan-500", 45.00),
                                                crearServicio("Profilaxis Dental", "Limpieza profunda con ultrasonido.",
                                                                "dentistry", "from-purple-500", "to-pink-500", 120.00),
                                                crearServicio("Cirugía Esterilización",
                                                                "Castración y Ovariohisterectomía.", "medical_services",
                                                                "from-orange-500", "to-red-500", 250.00),
                                                // NUEVOS SERVICIOS
                                                crearServicio("Baño y Corte (Grooming)",
                                                                "Corte de raza, baño medicado.", "content_cut",
                                                                "from-yellow-400", "to-orange-400", 60.00),
                                                crearServicio("Ecografía Abdominal", "Imágenes de diagnóstico.",
                                                                "radiology", "from-indigo-500", "to-blue-600", 100.00),
                                                crearServicio("Rayos X", "Placas radiográficas digitales.", "skeleton",
                                                                "from-gray-600", "to-gray-800", 80.00),
                                                crearServicio("Análisis de Sangre", "Hemograma y Bioquímica.",
                                                                "science", "from-red-400", "to-red-600", 70.00),
                                                crearServicio("Hospedaje Diario", "Cuidado por día incluye paseos.",
                                                                "home", "from-green-400", "to-emerald-600", 40.00));
                                servicioRepository.saveAll(servicios);
                                System.out.println("✅ Servicios MongoDB cargados.");
                        }

                        // --- 6. CARGAR CITAS ---
                        if (citaRepository.count() == 0 && mascotaRepository.count() > 0
                                        && servicioRepository.count() > 0) {
                                List<Mascota> m = mascotaRepository.findAll();
                                List<Trabajador> t = trabajadorRepository.findAll(); // t0=Carlos, t1=Maria, t2=Jorge...
                                                                                     // t6=Sofia
                                List<Servicio> s = servicioRepository.findAll();

                                List<Cita> citas = Arrays.asList(
                                                // Citas Pasadas
                                                crearCita(m.get(0), t.get(0), s.get(0),
                                                                LocalDateTime.now().minusDays(5).withHour(10),
                                                                "Chequeo rutina", "REALIZADA"),
                                                crearCita(m.get(2), t.get(1), s.get(2),
                                                                LocalDateTime.now().minusDays(3).withHour(16),
                                                                "Limpieza dental", "REALIZADA"),
                                                crearCita(m.get(5), t.get(6), s.get(0),
                                                                LocalDateTime.now().minusDays(1).withHour(11),
                                                                "Alergia en piel", "REALIZADA"), // Con Sofia
                                                                                                 // (Dermatologa)

                                                // Citas Hoy
                                                crearCita(m.get(1), t.get(0), s.get(1),
                                                                LocalDateTime.now().withHour(9).withMinute(0),
                                                                "Vacuna anual", "PENDIENTE"),
                                                crearCita(m.get(3), t.get(2), s.get(4),
                                                                LocalDateTime.now().withHour(11).withMinute(30),
                                                                "Corte de verano", "CONFIRMADA"), // Grooming
                                                crearCita(m.get(12), t.get(0), s.get(0),
                                                                LocalDateTime.now().withHour(15).withMinute(0),
                                                                "Dificultad respirar", "PENDIENTE"),

                                                // Citas Futuras
                                                crearCita(m.get(4), t.get(6), s.get(5),
                                                                LocalDateTime.now().plusDays(1).withHour(10),
                                                                "Ecografía control", "CONFIRMADA"),
                                                crearCita(m.get(6), t.get(0), s.get(3),
                                                                LocalDateTime.now().plusDays(2).withHour(9),
                                                                "Esterilización programada", "CONFIRMADA"),
                                                crearCita(m.get(11), t.get(0), s.get(0),
                                                                LocalDateTime.now().plusDays(3).withHour(17),
                                                                "Revisión dientes conejo", "PENDIENTE"));
                                citaRepository.saveAll(citas);
                                System.out.println("✅ Citas cargadas.");
                        }

                        // --- 7. CARGAR VENTAS (PAGOS) ---
                        if (ventaRepository.count() == 0 && productoRepository.count() > 0
                                        && clienteRepository.count() > 0) {
                                List<Cliente> c = clienteRepository.findAll();
                                List<Producto> p = productoRepository.findAll();

                                // Venta 1: Juan compra Alimento + Shampoo
                                crearVenta(ventaRepository, c.get(2), "YAPE", "123456", "COMPLETADA",
                                                Arrays.asList(p.get(1), p.get(2)), Arrays.asList(1, 2));

                                // Venta 2: Maria compra Snacks (Pendiente)
                                crearVenta(ventaRepository, c.get(3), "PLIN", "987654", "POR_VALIDAR",
                                                Arrays.asList(p.get(7)), Arrays.asList(5));

                                // Venta 3: Esmeralda compra Bravecto
                                crearVenta(ventaRepository, c.get(4), "TRANSFERENCIA", "BCP-555", "COMPLETADA",
                                                Arrays.asList(p.get(8)), Arrays.asList(1));

                                // Venta 4: Laura compra Heno/Arena (simulado)
                                crearVenta(ventaRepository, c.get(11), "EFECTIVO", "Caja-001", "COMPLETADA",
                                                Arrays.asList(p.get(12)), Arrays.asList(2));

                                // Venta 5: Diego compra Hills
                                crearVenta(ventaRepository, c.get(12), "YAPE", "778899", "COMPLETADA",
                                                Arrays.asList(p.get(10), p.get(6)), Arrays.asList(1, 1));

                                // --- NUEVAS VENTAS ADICIONALES ---

                                // Venta 6: Patricia compra un Rascador Torre
                                crearVenta(ventaRepository, c.get(15), "TARJETA", "STRIPE-001", "COMPLETADA",
                                                Arrays.asList(p.get(13)), Arrays.asList(1));

                                // Venta 7: Fernando compra Cama Ortopédica y Suplementos
                                crearVenta(ventaRepository, c.get(14), "TRANSFERENCIA", "BBVA-123", "COMPLETADA",
                                                Arrays.asList(p.get(14), p.get(3)), Arrays.asList(1, 2));

                                // Venta 8: Admin compra varios items para reponer stock interno (Simulado)
                                crearVenta(ventaRepository, c.get(0), "EFECTIVO", "Caja-002", "COMPLETADA",
                                                Arrays.asList(p.get(0), p.get(5), p.get(15)), Arrays.asList(5, 10, 5));

                                // Venta 9: Sofia compra Collar Reflectivo (Pendiente)
                                crearVenta(ventaRepository, c.get(10), "YAPE", "456123", "POR_VALIDAR",
                                                Arrays.asList(p.get(6)), Arrays.asList(1));

                                // Venta 10: Miguel compra Alimento Premium
                                crearVenta(ventaRepository, c.get(9), "PLIN", "321654", "COMPLETADA",
                                                Arrays.asList(p.get(1)), Arrays.asList(2));

                                System.out.println("✅ Ventas cargadas (10 registros).");
                        }

                        // --- 8. CARGAR TESTIMONIOS (Mongo)---
                        if (testimonioRepository.count() == 0) {
                                List<Testimonio> tests = Arrays.asList(
                                                crearTestimonio("Maria Gonzalez",
                                                                "¡Excelente atención! Trataron a mi gato con mucho cariño.",
                                                                5),
                                                crearTestimonio("Carlos Ruiz",
                                                                "Muy profesionales, la cirugía de mi perro salió perfecta.",
                                                                5),
                                                crearTestimonio("Ana Torres",
                                                                "Buenos precios y el lugar es muy limpio. Recomendado.",
                                                                4),
                                                crearTestimonio("Jorge P.",
                                                                "Me atendieron rápido por una emergencia. Gracias.", 5),
                                                crearTestimonio("Luisa M.",
                                                                "El servicio de grooming es el mejor, mi perrita quedó hermosa.",
                                                                5),
                                                crearTestimonio("Pedro S.",
                                                                "Compré alimentos y me llegaron al toque. Buen servicio.",
                                                                4),
                                                crearTestimonio("Carmen V.",
                                                                "La doctora fue muy paciente explicando el tratamiento.",
                                                                5),
                                                crearTestimonio("Fernando C.",
                                                                "Tienen de todo para mascotas exóticas también.", 5));
                                testimonioRepository.saveAll(tests);
                                System.out.println("✅ Testimonios cargados en MongoDB.");
                        }

                        // --- 9. CARGAR ENFERMEDADES (Mongo) ---
                        if (enfermedadRepository.count() == 0) {
                                enfermedadRepository.saveAll(Arrays.asList(
                                                crearEnfermedad("Parvovirus",
                                                                "Enfermedad viral grave que afecta intestinos.",
                                                                "Perro", "Alta"),
                                                crearEnfermedad("Distemper (Moquillo)",
                                                                "Afecta sistemas respiratorio, gastrointestinal y nervioso.",
                                                                "Perro", "Alta"),
                                                crearEnfermedad("Otitis Externa", "Inflamación del conducto auditivo.",
                                                                "Ambos", "Baja"),
                                                crearEnfermedad("Leucemia Felina",
                                                                "Enfermedad viral que afecta el sistema inmune.",
                                                                "Gato", "Alta"),
                                                // NUEVAS
                                                crearEnfermedad("Ehrlichia (Garrapata)",
                                                                "Infección bacteriana transmitida por garrapatas, causa anemia.",
                                                                "Perro", "Media"),
                                                crearEnfermedad("Sarna Sarcóptica",
                                                                "Ácaros que causan picazón intensa y pérdida de pelo.",
                                                                "Ambos", "Media"),
                                                crearEnfermedad("Insuficiencia Renal",
                                                                "Fallo en la función de los riñones.", "Gato", "Alta"),
                                                crearEnfermedad("Rabia",
                                                                "Virus mortal que afecta el sistema nervioso central.",
                                                                "Ambos", "Alta")));
                        }

                        // --- 10. CARGAR VACUNAS (Mongo) ---
                        if (vacunaCatalogoRepository.count() == 0) {
                                List<VacunaCatalogo> vacunas = Arrays.asList(
                                                // PERROS
                                                crearVacunaCat("Nobivac Puppy DP", "MSD Animal Health",
                                                                "Parvovirus y Distemper", 1, 1, 45.00),
                                                crearVacunaCat("Vanguard Plus 5 (Quíntuple)", "Zoetis",
                                                                "Distemper, Adenovirus, Parvovirus, Parainfluenza", 2,
                                                                1, 55.00),
                                                crearVacunaCat("Recombitek C6 (Séptuple)", "Boehringer Ingelheim",
                                                                "Quíntuple + Lepto", 3, 1, 65.00),
                                                crearVacunaCat("Bronchi-Shield (KC)", "Zoetis",
                                                                "Tos de las Perreras (Bordetella)", 3, 1, 40.00),
                                                crearVacunaCat("GiardiaVax", "Zoetis", "Giardia Lamblia", 4, 2, 50.00),

                                                // GATOS
                                                crearVacunaCat("Felocell 3 (Triple Felina)", "Zoetis",
                                                                "Rinotraqueitis, Calicivirus, Panleucopenia", 2, 1,
                                                                45.00),
                                                crearVacunaCat("Leucogen", "Virbac", "Leucemia Felina", 3, 1, 60.00),
                                                crearVacunaCat("Nobivac Tricat Trio", "MSD",
                                                                "Calicivirus, Herpesvirus, Panleucopenia", 2, 1, 50.00),

                                                // AMBOS
                                                crearVacunaCat("Rabisin", "Boehringer Ingelheim", "Rabia", 4, 1,
                                                                35.00));
                                vacunaCatalogoRepository.saveAll(vacunas);
                                System.out.println("✅ Catálogo de Vacunas cargado en MongoDB.");
                        }

                };
        }

        // --- MÉTODOS AUXILIARES ---

        private Enfermedad crearEnfermedad(String nom, String desc, String esp, String grav) {
                Enfermedad e = new Enfermedad();
                e.setNombre(nom);
                e.setDescripcion(desc);
                e.setEspecie(esp);
                e.setGravedad(grav);
                return e;
        }

        private VacunaCatalogo crearVacunaCat(String nom, String fab, String enf, int edad, int dosis, double precio) {
                VacunaCatalogo v = new VacunaCatalogo();
                v.setNombre(nom);
                v.setFabricante(fab);
                v.setEnfermedadAsociada(enf);
                v.setEdadRecomendada(edad);
                v.setDosis(dosis);
                v.setPrecio(precio);
                return v;
        }

        private Testimonio crearTestimonio(String autor, String texto, int estrellas) {
                Testimonio t = new Testimonio();
                t.setAutor(autor);
                t.setContenido(texto);
                t.setEstrellas(estrellas);
                t.setFecha(LocalDate.now());
                return t;
        }

        private void crearVenta(VentaRepository repo, Cliente cli, String metodo, String cod, String estado,
                        List<Producto> prods, List<Integer> cants) {
                Venta v = new Venta();
                v.setCliente(cli);
                v.setFecha(LocalDateTime.now().minusHours((long) (Math.random() * 48)));
                v.setMetodoPago(metodo);
                v.setCodigoOperacion(cod);
                v.setEstado(estado);

                double total = 0;
                // Inicializar la lista de detalles si es null
                if (v.getDetalles() == null) {
                        v.setDetalles(new ArrayList<>());
                }

                for (int i = 0; i < prods.size(); i++) {
                        DetalleVenta d = new DetalleVenta();
                        d.setProductoId(prods.get(i).getId());
                        d.setNombreProducto(prods.get(i).getNombre());
                        d.setPrecioUnitario(prods.get(i).getPrecio());
                        d.setCantidad(cants.get(i));
                        d.setSubtotal(d.getPrecioUnitario() * d.getCantidad());
                        d.setVenta(v);
                        v.getDetalles().add(d);
                        total += d.getSubtotal();
                }
                v.setTotal(total);
                repo.save(v);
        }

        private Cliente crearCliente(String nom, String ape, String dni, String correo, String tel, String dir,
                        String rol) {
                Cliente c = new Cliente();
                c.setNombres(nom);
                c.setApellidos(ape);
                c.setDni(dni);
                c.setCorreo(correo);
                c.setTelefono(tel);
                c.setDireccion(dir);
                c.setRol(rol);
                c.setPassword(passwordEncoder.encode("123456"));
                return c;
        }

        private Mascota crearMascota(String nom, String esp, String raza, String sexo, int edad, String obs,
                        Cliente cliente) {
                Mascota m = new Mascota();
                m.setNombre(nom);
                m.setEspecie(esp);
                m.setRaza(raza);
                m.setSexo(sexo);
                m.setEdad(edad);
                m.setObservaciones(obs);
                m.setCliente(cliente);
                return m;
        }

        private Trabajador crearTrabajador(String dni, String nom, String ape, String correo, String tel, String esp,
                        String horario, String est) {
                Trabajador t = new Trabajador();
                t.setDni(dni);
                t.setNombres(nom);
                t.setApellidos(ape);
                t.setCorreo(correo);
                t.setTelefono(tel);
                t.setEspecialidad(esp);
                t.setHorarioDisponible(horario);
                t.setEstadoLaboral(est);
                return t;
        }

        private Producto crearProd(String nombre, String desc, Double precio, Integer stock, String img) {
                Producto p = new Producto();
                p.setNombre(nombre);
                p.setDescripcion(desc);
                p.setPrecio(precio);
                p.setStock(stock);
                p.setImagenUrl(img);
                return p;
        }

        private Servicio crearServicio(String nom, String desc, String ico, String cIni, String cFin, Double precio) {
                Servicio s = new Servicio();
                s.setNombre(nom);
                s.setDescripcion(desc);
                s.setIcono(ico);
                s.setColorInicio(cIni);
                s.setColorFin(cFin);
                s.setPrecio(precio);
                s.setEstado("ACTIVO");
                return s;
        }

        private Cita crearCita(Mascota mascota, Trabajador vet, Servicio servicio, LocalDateTime fecha, String motivo,
                        String estado) {
                Cita c = new Cita();
                c.setMascota(mascota);
                c.setVeterinario(vet);
                c.setFechaHora(fecha);
                c.setMotivo(motivo);
                c.setServicioId(servicio.getId());
                c.setServicioNombre(servicio.getNombre());
                c.setPrecioAcordado(servicio.getPrecio());
                c.setEstado(Cita.EstadoCita.valueOf(estado));
                return c;
        }
}