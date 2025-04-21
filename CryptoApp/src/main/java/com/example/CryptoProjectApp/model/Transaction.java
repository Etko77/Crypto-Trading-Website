package com.example.cryptoprojectapp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "cryptocurrency_id", nullable = false)
    private Cryptocurrency cryptocurrency;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private double amount;
    private double price;
    private LocalDateTime timestamp;

    public enum TransactionType {
        BUY, SELL
    }
} 