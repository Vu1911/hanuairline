package com.se2.hanuairline.payload;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DiscountEventPayload {

    @Id
    private Long id;

    @NotBlank
    @NotNull
    private int discount_rate_percentage;

    public DiscountEventPayload(Long id, @NotBlank @NotNull int discount_rate_percentage) {
        this.id = id;
        this.discount_rate_percentage = discount_rate_percentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDiscount_rate_percentage() {
        return discount_rate_percentage;
    }

    public void setDiscount_rate_percentage(int discount_rate_percentage) {
        this.discount_rate_percentage = discount_rate_percentage;
    }
}
