package com.example.petclinic.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class VisitDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Visit> data;

	@Autowired
    private PetDataOnDemand petDataOnDemand;

	@Autowired
    private VetDataOnDemand vetDataOnDemand;

	public Visit getNewTransientVisit(int index) {
        Visit obj = new Visit();
        setDescription(obj, index);
        setPet(obj, index);
        setVet(obj, index);
        setVisitDate(obj, index);
        return obj;
    }

	public void setDescription(Visit obj, int index) {
        String description = "description_" + index;
        if (description.length() > 255) {
            description = description.substring(0, 255);
        }
        obj.setDescription(description);
    }

	public void setPet(Visit obj, int index) {
        Pet pet = petDataOnDemand.getRandomPet();
        obj.setPet(pet);
    }

	public void setVet(Visit obj, int index) {
        Vet vet = vetDataOnDemand.getRandomVet();
        obj.setVet(vet);
    }

	public void setVisitDate(Visit obj, int index) {
        Date visitDate = new Date(new Date().getTime() - 10000000L);
        obj.setVisitDate(visitDate);
    }

	public Visit getSpecificVisit(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Visit obj = data.get(index);
        Long id = obj.getId();
        return Visit.findVisit(id);
    }

	public Visit getRandomVisit() {
        init();
        Visit obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Visit.findVisit(id);
    }

	public boolean modifyVisit(Visit obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = Visit.findVisitEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Visit' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Visit>();
        for (int i = 0; i < 10; i++) {
            Visit obj = getNewTransientVisit(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
}
