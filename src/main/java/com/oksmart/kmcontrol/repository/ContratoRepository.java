package com.oksmart.kmcontrol.repository;

import com.oksmart.kmcontrol.model.ContratoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratoRepository extends JpaRepository<ContratoModel, Long> {
}
