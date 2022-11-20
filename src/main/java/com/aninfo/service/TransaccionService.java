package com.aninfo.service;

import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.model.*;
import com.aninfo.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.HeadersBuilder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Iterator;
import java.util.Optional;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    public Optional<Transaccion> findById(Long id) {
        return transaccionRepository.findById(id);
    }

    public Transaccion depositar(Transaccion transaccion) {

        return transaccion;
    }

    public Transaccion createTransaccion(Transaccion transaccion) {
        return transaccionRepository.save(transaccion);
    }

    public Collection<Transaccion> getTransacciones() {
        return transaccionRepository.findAll();
    }

    public Long getCbu(Transaccion transaccion) {
        return transaccion.getCbu();
    }

    public Double getImporte(Transaccion transaccion) {
        return transaccion.getImporte();
    }

    public void deleteById(Long id) {
        transaccionRepository.deleteById(id);
    }

    public void setImporte(Transaccion transaccion, double importe) {
        transaccion.setImporte(importe);
    }

    public Collection<Transaccion> getTransaccionesByCBU(Long cbu) {
        List<Transaccion> transacciones = transaccionRepository.findAll();

        Iterator iter = transacciones.iterator();

        while(iter.hasNext()) {
            Transaccion transaccion = (Transaccion) iter.next();

            if(!Objects.equals(transaccion.getCbu(), cbu)) iter.remove();
        }

        return transacciones;
    }
}