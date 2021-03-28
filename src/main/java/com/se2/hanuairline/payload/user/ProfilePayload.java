package com.se2.hanuairline.payload.user;

import com.se2.hanuairline.model.user.User;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ProfilePayload {

    @Id
    private Long id;

    @NotBlank
    private Long user_id;


    @Size(max = 40)
    private String id_card_number;

    @Size(max = 15)
    private String credit_card_number;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    private String phoneNumber;

    public ProfilePayload(Long id, Long user_id, @NotBlank @Size(max = 40) String id_card_number, @NotBlank @Size(max = 15) String credit_card_number, @NotBlank @Size(max = 40) String phoneNumber) {

        this.id = id;
        this.user_id = user_id;
        this.id_card_number = id_card_number;
        this.credit_card_number = credit_card_number;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getId_card_number() {
        return id_card_number;
    }

    public void setId_card_number(String id_card_number) {
        this.id_card_number = id_card_number;
    }

    public String getCredit_card_number() {
        return credit_card_number;
    }

    public void setCredit_card_number(String credit_card_number) {
        this.credit_card_number = credit_card_number;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
