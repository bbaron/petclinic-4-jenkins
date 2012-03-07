package com.example.petclinic.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class Visit {

    @Size(max = 255)
    private String description;

    @NotNull
    @Past
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date visitDate;

    @NotNull
    @ManyToOne
    private Pet pet;

    @ManyToOne
    private Vet vet;

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new Visit().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countVisits() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Visit o", Long.class).getSingleResult();
    }

	public static List<Visit> findAllVisits() {
        return entityManager().createQuery("SELECT o FROM Visit o", Visit.class).getResultList();
    }

	public static Visit findVisit(Long id) {
        if (id == null) return null;
        return entityManager().find(Visit.class, id);
    }

	public static List<Visit> findVisitEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Visit o", Visit.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Visit attached = Visit.findVisit(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public Visit merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Visit merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public static TypedQuery<Visit> findVisitsByDescriptionAndVisitDate(String description, Date visitDate) {
        if (description == null || description.length() == 0) throw new IllegalArgumentException("The description argument is required");
        if (visitDate == null) throw new IllegalArgumentException("The visitDate argument is required");
        EntityManager em = Visit.entityManager();
        TypedQuery<Visit> q = em.createQuery("SELECT o FROM Visit AS o WHERE o.description = :description AND o.visitDate = :visitDate", Visit.class);
        q.setParameter("description", description);
        q.setParameter("visitDate", visitDate);
        return q;
    }

	public static TypedQuery<Visit> findVisitsByDescriptionLike(String description) {
        if (description == null || description.length() == 0) throw new IllegalArgumentException("The description argument is required");
        description = description.replace('*', '%');
        if (description.charAt(0) != '%') {
            description = "%" + description;
        }
        if (description.charAt(description.length() - 1) != '%') {
            description = description + "%";
        }
        EntityManager em = Visit.entityManager();
        TypedQuery<Visit> q = em.createQuery("SELECT o FROM Visit AS o WHERE LOWER(o.description) LIKE LOWER(:description)", Visit.class);
        q.setParameter("description", description);
        return q;
    }

	public static TypedQuery<Visit> findVisitsByVisitDateBetween(Date minVisitDate, Date maxVisitDate) {
        if (minVisitDate == null) throw new IllegalArgumentException("The minVisitDate argument is required");
        if (maxVisitDate == null) throw new IllegalArgumentException("The maxVisitDate argument is required");
        EntityManager em = Visit.entityManager();
        TypedQuery<Visit> q = em.createQuery("SELECT o FROM Visit AS o WHERE o.visitDate BETWEEN :minVisitDate AND :maxVisitDate", Visit.class);
        q.setParameter("minVisitDate", minVisitDate);
        q.setParameter("maxVisitDate", maxVisitDate);
        return q;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	public Date getVisitDate() {
        return this.visitDate;
    }

	public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

	public Pet getPet() {
        return this.pet;
    }

	public void setPet(Pet pet) {
        this.pet = pet;
    }

	public Vet getVet() {
        return this.vet;
    }

	public void setVet(Vet vet) {
        this.vet = vet;
    }
}
