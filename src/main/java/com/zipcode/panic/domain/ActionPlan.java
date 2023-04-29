package com.zipcode.panic.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ActionPlan.
 */
@Entity
@Table(name = "action_plan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActionPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "actionPlan" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Soundtrack soundtrack;

    @JsonIgnoreProperties(value = { "actionPlan" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private CopingStrategies copingStrategies;

    @OneToMany(mappedBy = "actionPlan")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "actionPlan" }, allowSetters = true)
    private Set<PhoneLink> phoneLinks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ActionPlan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Soundtrack getSoundtrack() {
        return this.soundtrack;
    }

    public void setSoundtrack(Soundtrack soundtrack) {
        this.soundtrack = soundtrack;
    }

    public ActionPlan soundtrack(Soundtrack soundtrack) {
        this.setSoundtrack(soundtrack);
        return this;
    }

    public CopingStrategies getCopingStrategies() {
        return this.copingStrategies;
    }

    public void setCopingStrategies(CopingStrategies copingStrategies) {
        this.copingStrategies = copingStrategies;
    }

    public ActionPlan copingStrategies(CopingStrategies copingStrategies) {
        this.setCopingStrategies(copingStrategies);
        return this;
    }

    public Set<PhoneLink> getPhoneLinks() {
        return this.phoneLinks;
    }

    public void setPhoneLinks(Set<PhoneLink> phoneLinks) {
        if (this.phoneLinks != null) {
            this.phoneLinks.forEach(i -> i.setActionPlan(null));
        }
        if (phoneLinks != null) {
            phoneLinks.forEach(i -> i.setActionPlan(this));
        }
        this.phoneLinks = phoneLinks;
    }

    public ActionPlan phoneLinks(Set<PhoneLink> phoneLinks) {
        this.setPhoneLinks(phoneLinks);
        return this;
    }

    public ActionPlan addPhoneLink(PhoneLink phoneLink) {
        this.phoneLinks.add(phoneLink);
        phoneLink.setActionPlan(this);
        return this;
    }

    public ActionPlan removePhoneLink(PhoneLink phoneLink) {
        this.phoneLinks.remove(phoneLink);
        phoneLink.setActionPlan(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActionPlan)) {
            return false;
        }
        return id != null && id.equals(((ActionPlan) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActionPlan{" +
            "id=" + getId() +
            "}";
    }
}
