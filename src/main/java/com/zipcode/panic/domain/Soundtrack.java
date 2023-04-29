package com.zipcode.panic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Soundtrack.
 */
@Entity
@Table(name = "soundtrack")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Soundtrack implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "name")
    private String name;

    @JsonIgnoreProperties(value = { "soundtrack", "copingStrategies", "phoneLinks" }, allowSetters = true)
    @OneToOne(mappedBy = "soundtrack")
    private ActionPlan actionPlan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Soundtrack id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Soundtrack fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getName() {
        return this.name;
    }

    public Soundtrack name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActionPlan getActionPlan() {
        return this.actionPlan;
    }

    public void setActionPlan(ActionPlan actionPlan) {
        if (this.actionPlan != null) {
            this.actionPlan.setSoundtrack(null);
        }
        if (actionPlan != null) {
            actionPlan.setSoundtrack(this);
        }
        this.actionPlan = actionPlan;
    }

    public Soundtrack actionPlan(ActionPlan actionPlan) {
        this.setActionPlan(actionPlan);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Soundtrack)) {
            return false;
        }
        return id != null && id.equals(((Soundtrack) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Soundtrack{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
