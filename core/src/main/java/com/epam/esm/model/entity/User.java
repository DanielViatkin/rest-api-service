package com.epam.esm.model.entity;

import com.epam.esm.constant.database.ColumnConstant;
import com.epam.esm.constant.database.TableConstant;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = TableConstant.USERS_TABLE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 45, nullable = false)
    private String login;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String status;

    @ManyToMany(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST}
    )
    @JoinTable(
            name = TableConstant.USER_ROLES_TABLE,
            joinColumns = @JoinColumn(name = ColumnConstant.USER_ID),
            inverseJoinColumns = @JoinColumn(name = ColumnConstant.ROLE_ID)
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders = new HashSet<>();

    public User(){
    }

    public User(Long id , String login, String password, String status, Set<Role> roles, Set<Order> orders){
        this.id = id;
        this.login = login;
        this.password = password;
        this.status = status;
        this.roles = roles;
        this.orders = orders;
    }
    public User(Long id, String login, String password, String status, Order order){
        this.id = id;
        this.login = login;
        this.password = password;
        this.status = status;
        orders.add(order);
    }

    public void addOrder(Order order){
        orders.add(order);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public Set<String> getRolesNames(){
        return roles.stream().
                map(Role::getName).
                collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", login=" + login + ", password =" + password + "]";
    }
}
