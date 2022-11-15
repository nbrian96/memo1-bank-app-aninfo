package com.aninfo.model;

import javax.persistence.*;

@Entity
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transaccionID;

    private Long cbu;

    private Double importe;

    public Transaccion() {
    }

    public Transaccion(Long cbu, Double importe) {
        this.cbu = cbu;
        this.importe = importe;
    }

    public Long getTransaccionID() {
        return transaccionID;
    }

    public void setTransaccionID(Long transaccionID) {
        this.transaccionID = transaccionID;
    }

    public Long getCbu() {
        return cbu;
    }

    public void setCbu(Long cbu) {
        this.cbu = cbu;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }
}
