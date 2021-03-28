package com.se2.hanuairline.model;

import com.se2.hanuairline.model.audit.DateAudit;

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
    private int discountRate;

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

    public int getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }

    public Set<Flight> getFlight() {
        return flight;
    }

    public void setFlight(Set<Flight> flight) {
        this.flight = flight;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
