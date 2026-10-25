package com.viveu.repository;

import com.viveu.model.Alojamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlojamientoRepository extends JpaRepository<Alojamiento, Long> {

    List<Alojamiento> findByDestacadoTrue();

    List<Alojamiento> findByAlojadorId(Long alojadorId);

    void deleteByAlojadorId(Long alojadorId);

    List<Alojamiento> findBySedeCercanaContainingIgnoreCase(String sede);

    List<Alojamiento> findByPrecioMensualLessThanEqual(Double precioMax);

    @Query("SELECT a FROM Alojamiento a WHERE " +
           "(:sede IS NULL OR :sede = '' OR LOWER(a.sedeCercana) LIKE LOWER(CONCAT('%', :sede, '%'))) AND " +
           "(:precioMax IS NULL OR a.precioMensual <= :precioMax) AND " +
           "(:soloVerificados IS NULL OR :soloVerificados = false OR a.verificadoViveU = true) AND " +
           "(:aireAcondicionado IS NULL OR :aireAcondicionado = false OR a.aireAcondicionado = true) AND " +
           "(:incluyeServicios IS NULL OR :incluyeServicios = false OR a.incluyeServicios = true)")
    List<Alojamiento> filtrarAlojamientos(@Param("sede") String sede,
                                         @Param("precioMax") Double precioMax,
                                         @Param("soloVerificados") Boolean soloVerificados,
                                         @Param("aireAcondicionado") Boolean aireAcondicionado,
                                         @Param("incluyeServicios") Boolean incluyeServicios);
}
