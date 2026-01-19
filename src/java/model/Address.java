package model;

import Hibernate.User;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "address")
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "line_01", length = 45, nullable = false)
    private String line_01;

    @Column(name = "line_02", length = 45, nullable = false)
    private String line_02;

    @Column(name = "postalCode", length = 5, nullable = false)
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "mobile", length = 10)
    private String mobile;

    @Column(name = "fname", length = 45)
    private String fname;

    @Column(name = "lname", length = 45)
    private String lname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLine_01() {
        return line_01;
    }

    public void setLine_01(String line_01) {
        this.line_01 = line_01;
    }

    public String getLine_02() {
        return line_02;
    }

    public void setLine_02(String line_02) {
        this.line_02 = line_02;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

}
