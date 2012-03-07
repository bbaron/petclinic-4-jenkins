package com.example.petclinic.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

import com.example.petclinic.reference.PetType;

@Configurable
@Entity
public class Pet {

    @NotNull
    private boolean sendReminders;

    @NotNull
    @Size(min = 1)
    private String name;

    @NotNull
    @Min(0L)
    private Float weight;

    @ManyToOne
    private Owner owner;

    @NotNull
    @Enumerated
    private PetType type;

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public boolean isSendReminders() {
        return this.sendReminders;
    }

	public void setSendReminders(boolean sendReminders) {
        this.sendReminders = sendReminders;
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public Float getWeight() {
        return this.weight;
    }

	public void setWeight(Float weight) {
        this.weight = weight;
    }

	public Owner getOwner() {
        return this.owner;
    }

	public void setOwner(Owner owner) {
        this.owner = owner;
    }

	public PetType getType() {
        return this.type;
    }

	public void setType(PetType type) {
        this.type = type;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new Pet().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countPets() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Pet o", Long.class).getSingleResult();
    }

	public static List<Pet> findAllPets() {
        return entityManager().createQuery("SELECT o FROM Pet o", Pet.class).getResultList();
    }

	public static Pet findPet(Long id) {
        if (id == null) return null;
        return entityManager().find(Pet.class, id);
    }

	public static List<Pet> findPetEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Pet o", Pet.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Pet attached = Pet.findPet(this.id);
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
    public Pet merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Pet merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public static TypedQuery<Pet> findPetsByNameAndWeight(String name, Float weight) {
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        if (weight == null) throw new IllegalArgumentException("The weight argument is required");
        EntityManager em = Pet.entityManager();
        TypedQuery<Pet> q = em.createQuery("SELECT o FROM Pet AS o WHERE o.name = :name AND o.weight = :weight", Pet.class);
        q.setParameter("name", name);
        q.setParameter("weight", weight);
        return q;
    }

	public static TypedQuery<Pet> findPetsByOwner(Owner owner) {
        if (owner == null) throw new IllegalArgumentException("The owner argument is required");
        EntityManager em = Pet.entityManager();
        TypedQuery<Pet> q = em.createQuery("SELECT o FROM Pet AS o WHERE o.owner = :owner", Pet.class);
        q.setParameter("owner", owner);
        return q;
    }

	public static TypedQuery<Pet> findPetsBySendRemindersAndWeightLessThan(boolean sendReminders, Float weight) {
        if (weight == null) throw new IllegalArgumentException("The weight argument is required");
        EntityManager em = Pet.entityManager();
        TypedQuery<Pet> q = em.createQuery("SELECT o FROM Pet AS o WHERE o.sendReminders = :sendReminders AND o.weight < :weight", Pet.class);
        q.setParameter("sendReminders", sendReminders);
        q.setParameter("weight", weight);
        return q;
    }

	public static TypedQuery<Pet> findPetsByTypeAndNameLike(PetType type, String name) {
        if (type == null) throw new IllegalArgumentException("The type argument is required");
        if (name == null || name.length() == 0) throw new IllegalArgumentException("The name argument is required");
        name = name.replace('*', '%');
        if (name.charAt(0) != '%') {
            name = "%" + name;
        }
        if (name.charAt(name.length() - 1) != '%') {
            name = name + "%";
        }
        EntityManager em = Pet.entityManager();
        TypedQuery<Pet> q = em.createQuery("SELECT o FROM Pet AS o WHERE o.type = :type AND LOWER(o.name) LIKE LOWER(:name)", Pet.class);
        q.setParameter("type", type);
        q.setParameter("name", name);
        return q;
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
}
