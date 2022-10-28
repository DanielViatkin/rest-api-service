package com.epam.esm.model.entity;

import com.epam.esm.constant.database.ColumnConstant;
import com.epam.esm.constant.database.TableConstant;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.math.BigDecimal;

import java.time.Instant;
import java.util.List;
import java.util.Set;


@Component
@Getter
@Setter
@Entity
@Table(name = TableConstant.GIFT_CERTIFICATE_TABLE)
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @JoinTable(
            name = TableConstant.GIFT_CERTIFICATE_HAS_TAG_TABLE,
            joinColumns = @JoinColumn(name = ColumnConstant.CERTIFICATE_ID),
            inverseJoinColumns = @JoinColumn(name = ColumnConstant.TAG_ID)
    )
    private Set<Tag> tags;

    @OneToMany(mappedBy = "certificate", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Order> orders;

    @Column(length = 45 , nullable = false)
    private String name;

    @Column(length = 45 , nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer duration;

    @Column(name = "create_date", nullable = false, updatable = false)
    private Instant createDate;

    @Column(name = "last_update_date", nullable = false)
    private Instant lastUpdateDate;

    public GiftCertificate(GiftCertificate certificate){
        this.id = certificate.getId();
        this.name = certificate.getName();
        this.price = certificate.getPrice();
        this.duration = certificate.getDuration();
        this.description = certificate.getDescription();
        this.createDate = certificate.getCreateDate();
        this.lastUpdateDate = certificate.getLastUpdateDate();
        this.tags = certificate.getTags();
        this.orders = certificate.getOrders();
    }

    public GiftCertificate(String name, String description, BigDecimal price,
                           Integer duration, Instant createDate, Instant lastUpdateDate){
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }
    public GiftCertificate(long id, String name, String description, BigDecimal price,
                           Integer duration, Instant createDate, Instant lastUpdateDate){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }

    public void addTag(Tag tag){
        this.tags.add(tag);
    }

    public void removeTag(Tag tag){
        if (tag != null) {
            this.tags.remove(tag);
            if(tag.getCertificates() != null){
                tag.getCertificates().remove(this);
            }
        }
    }
}
