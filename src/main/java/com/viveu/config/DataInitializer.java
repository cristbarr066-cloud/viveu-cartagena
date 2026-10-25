package com.viveu.config;

import com.viveu.model.Alojador;
import com.viveu.model.Alojamiento;
import com.viveu.repository.AlojadorRepository;
import com.viveu.repository.AlojamientoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(AlojamientoRepository repository, AlojadorRepository alojadorRepository) {
        return args -> {
            if (repository.count() > 0) return;

            // Arrendadora verificada inicial para pruebas de inicio de sesión
            Alojador rosalba = new Alojador(
                "Doña Rosalba Méndez",
                "rosalba@viveu.com",
                "123456",
                "+57 312 458 9231",
                "Cédula de Ciudadanía",
                "45892104",
                "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=400&q=80",
                "https://images.unsplash.com/photo-1589330694653-dad6bc0140ad?auto=format&fit=crop&w=500&q=80",
                "Calle 30 # 50-25, Barrio Zaragocilla, Cartagena"
            );
            rosalba = alojadorRepository.save(rosalba);
            Long idRosalba = rosalba.getId();

            Alojamiento a1 = new Alojamiento(
                "Habitación Estudiantil a 4 min de Medicina Zaragocilla",
                "Habitación independiente y fresca en casa familiar tranquila. Ambiente exclusivo para estudiantes de ciencias de la salud. Incluye escritorio de estudio, armario empotrado y acceso a cocina integral compartida.",
                390000.0,
                "Zaragocilla",
                "Sede Zaragocilla (Salud)",
                4,
                "Individual con baño privado",
                true,
                true,
                false,
                true,
                true,
                true,
                false,
                true,
                true,
                "https://images.unsplash.com/photo-1595526114035-0d45ed16cfbf?auto=format&fit=crop&w=800&q=80",
                "Doña Rosalba Méndez",
                "+57 312 458 9231",
                "Horario de llegada flexible hasta las 11:00 PM. No fumadores. Ambiente de silencio para estudio.",
                4.9
            );
            a1.setDireccion("Calle 30 # 50-25, Zaragocilla");
            a1.setLatitud(10.3995);
            a1.setLongitud(-75.4975);
            a1.setAlojadorId(idRosalba);

            Alojamiento a2 = new Alojamiento(
                "Cuarto Universitario Económico cerca a Piedra de Bolívar",
                "Habitación acogedora a pasos de la sede de Ingenierías. Excelente ventilación natural, cama individual, closet y mesa de trabajo. Zona residencial segura con fácil acceso a la Avenida Pedro de Heredia y estación de Transcaribe.",
                340000.0,
                "Los Cerros",
                "Sede Piedra de Bolívar (Ingeniería)",
                6,
                "Individual con baño compartido",
                true,
                true,
                false,
                true,
                true,
                false,
                true,
                true,
                true,
                "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?auto=format&fit=crop&w=800&q=80",
                "Carlos Gómez",
                "+57 301 789 4412",
                "Convivencia con dos estudiantes de Ingeniería de Sistemas. Horarios flexibles.",
                4.7
            );
            a2.setDireccion("Cra 49 # 28-14, Los Cerros");
            a2.setLatitud(10.4085);
            a2.setLongitud(-75.5012);

            Alojamiento a3 = new Alojamiento(
                "Suite Estudiantil con Aire Acondicionado en Amberes",
                "Espacio moderno y climatizado para sobrellevar las tardes cálidas de Cartagena. Baño privado recién remodelado, mini refrigerador y cama semi-doble. Ideal para estudiantes de San Pablo o Piedra de Bolívar.",
                480000.0,
                "Amberes",
                "Sede Piedra de Bolívar (Ingeniería)",
                10,
                "Individual con baño privado",
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?auto=format&fit=crop&w=800&q=80",
                "Mariana Restrepo",
                "+57 320 654 1198",
                "Acepta visitas los fines de semana. Mascotas no permitidas. Contrato semestral renovable.",
                4.8
            );
            a3.setDireccion("Calle 29B # 44-18, Amberes");
            a3.setLatitud(10.4050);
            a3.setLongitud(-75.5090);

            Alojamiento a4 = new Alojamiento(
                "Habitación en Casa Colonial cerca a Claustro San Agustín",
                "Ubicación inmejorable en el Centro Histórico a 3 calles de la Facultad de Derecho y Ciencias Sociales. Habitación amplia con techos altos, piso republicano y ambiente propicio para lectura e investigación.",
                460000.0,
                "Centro Histórico",
                "Sede Claustro San Agustín (Centro)",
                5,
                "Individual con baño compartido",
                true,
                true,
                false,
                true,
                true,
                true,
                false,
                false,
                true,
                "https://images.unsplash.com/photo-1513694203232-719a280e022f?auto=format&fit=crop&w=800&q=80",
                "Fernando Castro",
                "+57 314 223 8871",
                "Uso de áreas comunes (patio colonial y sala de lectura). Silencio después de las 10:00 PM.",
                4.9
            );
            a4.setDireccion("Calle del Curato # 38-42, San Diego");
            a4.setLatitud(10.4255);
            a4.setLongitud(-75.5478);

            Alojamiento a5 = new Alojamiento(
                "Cuarto Compartido Doble Ultra-Económico en Pie de la Popa",
                "Solución pensada para estudiantes con presupuesto ajustado que deseen compartir gastos. Incluye dos camas individuales, dos escritorios, ventilador de techo y servicios públicos incluidos. A 1 ruta de Transcaribe a Zaragocilla y Piedra de Bolívar.",
                300000.0,
                "Pie de la Popa",
                "Sede Zaragocilla (Salud)",
                12,
                "Compartida doble",
                true,
                true,
                false,
                true,
                true,
                true,
                true,
                false,
                true,
                "https://images.unsplash.com/photo-1555854877-bab0e564b8d5?auto=format&fit=crop&w=800&q=80",
                "Marta Lucía De Ávila",
                "+57 300 912 3345",
                "Precio por persona ($300.000 COP c/u). Ideal para compañeros que vengan del mismo municipio.",
                4.6
            );
            a5.setDireccion("Calle 30 # 21-35, Pie de la Popa");
            a5.setLatitud(10.4150);
            a5.setLongitud(-75.5310);

            Alojamiento a6 = new Alojamiento(
                "Estudio Cómodo cerca a la Sede San Pablo en San Diego",
                "Alojamiento privado a 7 minutos a pie de la Sede San Pablo. Ventilación cruzada, vista a la calle y cerca a supermercados y librerías universitarias.",
                420000.0,
                "Torices",
                "Sede San Pablo",
                7,
                "Individual con baño privado",
                true,
                true,
                false,
                true,
                true,
                false,
                false,
                false,
                true,
                "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?auto=format&fit=crop&w=800&q=80",
                "Javier Morales",
                "+57 318 667 9043",
                "Incluye internet de 300 megas simétrico para clases virtuales y trabajos.",
                4.8
            );
            a6.setDireccion("Paseo Bolívar # 41-10, Torices");
            a6.setLatitud(10.4280);
            a6.setLongitud(-75.5380);

            Alojamiento a7 = new Alojamiento(
                "Habitación en Casa Estudiantil Familiar en Blas de Lezo",
                "Excelente opción en el sur de Cartagena con acceso directo a rutas alimentadoras de Transcaribe. Muy segura, ambiente acogedor con derecho a cocinar y lavar.",
                320000.0,
                "Blas de Lezo",
                "Sede Piedra de Bolívar (Ingeniería)",
                15,
                "Individual con baño compartido",
                true,
                true,
                false,
                true,
                true,
                true,
                true,
                false,
                true,
                "https://images.unsplash.com/photo-1598928506311-c55ded91a20c?auto=format&fit=crop&w=800&q=80",
                "Teresa Jiménez",
                "+57 311 554 2210",
                "Estrictamente para estudiantes activos demostrables con carné vigente.",
                4.7
            );
            a7.setDireccion("Mz 15 Lote 8, Blas de Lezo");
            a7.setLatitud(10.3890);
            a7.setLongitud(-75.4850);

            Alojamiento a8 = new Alojamiento(
                "Habitación Premium con A/A y Balcón en Manga",
                "Habitación para estudiante con altos estándares. Aire acondicionado inverter silencioso, balcón con brisa de la bahía, internet de alta velocidad y baño privado.",
                520000.0,
                "Manga",
                "Sede Claustro San Agustín (Centro)",
                10,
                "Individual con baño privado",
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                true,
                "https://images.unsplash.com/photo-1540518614846-7ede433c4ef0?auto=format&fit=crop&w=800&q=80",
                "Ignacio Valdés",
                "+57 310 882 1765",
                "Zona residencial de máxima seguridad en Cartagena. Edificio con celaduría 24 horas.",
                5.0
            );
            a8.setDireccion("3ra Avenida Calle 26 # 22-15, Manga");
            a8.setLatitud(10.4120);
            a8.setLongitud(-75.5390);

            repository.saveAll(List.of(a1, a2, a3, a4, a5, a6, a7, a8));
            System.out.println(">>> 8 Alojamientos con direcciones y coordenadas GPS precargados exitosamente en ViveU!");
        };
    }
}
