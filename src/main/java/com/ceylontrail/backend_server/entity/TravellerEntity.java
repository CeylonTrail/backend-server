package com.ceylontrail.backend_server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", unique = true)
    private UserEntity user;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    public TravellerEntity(UserEntity user, String firstname, String lastname) {
        this.user = user;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
