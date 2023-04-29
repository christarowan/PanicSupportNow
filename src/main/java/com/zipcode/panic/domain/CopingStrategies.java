package com.zipcode.panic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CopingStrategies.
 */
@Entity
@Table(name = "coping_strategies")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CopingStrategies implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "contents")
    private String contents;

    @Column(name = "name")
    private String name;

    @JsonIgnoreProperties(value = { "soundtrack", "copingStrategies", "phoneLinks" }, allowSetters = true)
    @OneToOne(mappedBy = "copingStrategies")
    private ActionPlan actionPlan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CopingStrategies id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContents() {
        return this.contents;
    }

    public CopingStrategies contents(String contents) {
        this.setContents(contents);
        return this;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getName() {
        return this.name;
    }

    public CopingStrategies name(String name) {
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
            this.actionPlan.setCopingStrategies(null);
        }
        if (actionPlan != null) {
            actionPlan.setCopingStrategies(this);
        }
        this.actionPlan = actionPlan;
    }

    public CopingStrategies actionPlan(ActionPlan actionPlan) {
        this.setActionPlan(actionPlan);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CopingStrategies)) {
            return false;
        }
        return id != null && id.equals(((CopingStrategies) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CopingStrategies{" +
            "id=" + getId() +
            ", contents='" + getContents() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
