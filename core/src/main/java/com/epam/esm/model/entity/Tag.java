package com.epam.esm.model.entity;

import com.epam.esm.constant.database.TableConstant;
import lombok.*;
import org.hibernate.annotations.*;
import org.springframework.hateoas.RepresentationModel;
import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "tags")
@Data
@NoArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
            mappedBy = "tags")
    @JsonIgnore
    Set<GiftCertificate> certificates;

    @Column(nullable = false)
    private String name;

    public Tag(long id, Set<GiftCertificate> certificates, String name){
        this.id = id;
        this.certificates = certificates;
        this.name = name;
    }

    public void addCertificate(GiftCertificate certificate){
        certificates.add(certificate);
    }

    public void removeCertificate(GiftCertificate certificate){
        if (certificate != null){
            this.certificates.remove(certificate);
            certificate.getTags().remove(this);
        }
    }

    @PreRemove
    public void deleteTag(){
        certificates.forEach(certificate -> certificate.getTags().remove(this));
    }
}
