package com.bee.domain;


import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A First.
 */
@Entity
@Table(name = "first")
public class First implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "no")
    private Integer no;

    @Column(name = "name")
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNo() {
        return no;
    }

    public First no(Integer no) {
        this.no = no;
        return this;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public First name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        First first = (First) o;
        if (first.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), first.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "First{" +
            "id=" + getId() +
            ", no=" + getNo() +
            ", name='" + getName() + "'" +
            "}";
    }
}
