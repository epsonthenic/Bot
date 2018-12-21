package com.spt.engine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of={"id"})
public class AppUserData {
    private @Id
    @GeneratedValue
    Long id;

     private String firstName;
     private String lastName;
     private String deseription;

}
