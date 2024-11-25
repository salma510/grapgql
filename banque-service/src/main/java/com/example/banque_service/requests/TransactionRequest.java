package com.example.banque_service.requests;


import com.example.banque_service.entities.TypeTransaction;
import lombok.Data;

import java.util.Date;

@Data
public class TransactionRequest {
    private Long compteId;
    private double montant;
    private String  date;
    private TypeTransaction type; // This can also be TypeTransaction if you prefer enums
}
