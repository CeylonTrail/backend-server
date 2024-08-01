package com.ceylontrail.backend_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "traveller")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EntityListeners(AuditingEntityListener.class)
public class TravellerEntity {

    @Id
    @Column(name = "traveller_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int travellerId;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;

    @ElementCollection
    @CollectionTable(name = "traveller_interests", joinColumns = @JoinColumn(name = "traveller_id"))
    @Column(name = "interests")
    private List<String> interests = new ArrayList<>();
    @OneToMany(mappedBy = "traveller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpenseEntity> expenses = new ArrayList<>();


}
