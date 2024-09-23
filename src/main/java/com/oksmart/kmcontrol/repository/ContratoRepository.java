package com.oksmart.kmcontrol.repository;

import org.springframework.data.jpa.repository.Query;
import com.oksmart.kmcontrol.model.ContratoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContratoRepository extends JpaRepository<ContratoModel, Long> {
    ContratoModel findByNumeroContrato(String numeroContrato);

    @Query("SELECT c FROM ContratoModel c WHERE c.placa = :placa ORDER BY c.id DESC")
    List<ContratoModel> findByPlaca(@Param("placa") String placa);
}
