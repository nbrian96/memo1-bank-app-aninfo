package com.aninfo.repository;

import com.aninfo.model.Transaccion;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TransaccionRepository extends CrudRepository<Transaccion, Long> {

    Transaccion findTransaccionByTransaccionID(Long transaccionID);

    @Override
    List<Transaccion> findAll();

}
