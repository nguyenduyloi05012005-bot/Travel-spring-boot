package com.Travel.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name; // ten nguoi
    private String locality; // khu vuc , phuong xa
    private String address; //so nha,ten duong
    private String states; //tinh/thanh pho
    private String mobile; //sdt lien lac

}
