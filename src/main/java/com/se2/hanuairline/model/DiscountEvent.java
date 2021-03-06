package com.se2.hanuairline.model;

import com.se2.hanuairline.model.audit.DateAudit;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "discount_event")
public class DiscountEvent extends DateAudit implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    private double discountRate;

    @ManyToMany(mappedBy = "discount",cascade = {CascadeType.REMOVE})
    private Set<Flight> flight;

    public DiscountEvent(@NotBlank @NotNull int discountRate) {
        this.discountRate = discountRate;
    }

    public DiscountEvent(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public String toString() {
        return "DiscountEvent{" +
                "id=" + id +
                ", discountRate=" + discountRate +

                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        System.out.println("OK");
        return super.clone();
    }
}
