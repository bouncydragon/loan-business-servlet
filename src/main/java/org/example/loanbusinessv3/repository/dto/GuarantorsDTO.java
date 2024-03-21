package org.example.loanbusinessv3.repository.dto;

public class GuarantorsDTO {
    private Long guarantor_id;
    private String full_name;
    private String email;
    private String phone;
    
    public Long getGuarantor_id() {
        return guarantor_id;
    }
    public void setGuarantor_id(Long guarantor_id) {
        this.guarantor_id = guarantor_id;
    }
    public String getFull_name() {
        return full_name;
    }
    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    
}
