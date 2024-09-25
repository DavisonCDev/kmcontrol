package com.oksmart.kmcontrol.repository;

import org.springframework.data.jpa.repository.Query;
import com.oksmart.kmcontrol.model.ContratoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratoRepository extends JpaRepository<ContratoModel, Long> {

    //Query para procurar por placa
    @Query("SELECT c FROM ContratoModel c WHERE c.placa = :placa ORDER BY c.id DESC")
    List<ContratoModel> findByPlaca(@Param("placa") String placa);

    // Query para obter o último contrato para cada número de contrato
    @Query("SELECT c FROM ContratoModel c WHERE c.id IN " +
            "(SELECT MAX(c2.id) FROM ContratoModel c2 GROUP BY c2.numeroContrato)")
    List<ContratoModel> findUltimosContratos();

    List<ContratoModel> findByNumeroContrato(String numeroContrato);
}
