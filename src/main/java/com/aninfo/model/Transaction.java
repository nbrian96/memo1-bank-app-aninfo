package com.aninfo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long cbu;
    private TransactionType type;
    private Double sum;

    public Long getCbu() {
        return cbu;
    }

    public TransactionType getType() {
        return type;
    }

    public Double getSum() {
        return sum;
    }

}
