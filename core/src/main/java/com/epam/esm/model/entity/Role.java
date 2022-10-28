package com.epam.esm.model.entity;

import com.epam.esm.constant.database.TableConstant;
import com.epam.esm.model.security.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = TableConstant.ROLES_TABLE)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(
            mappedBy = "roles"
    )
    private Set<User> users;

    public Role(String name){
        this.name = name;
    }

}
