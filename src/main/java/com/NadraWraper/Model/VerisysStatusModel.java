package com.NadraWraper.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "verisys_management")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerisysStatusModel {
    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "CNIC")
    private String cnic;

    @Column(name = "status")
    private String status;
}
