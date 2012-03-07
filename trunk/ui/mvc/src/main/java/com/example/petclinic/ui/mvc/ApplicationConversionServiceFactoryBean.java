package com.example.petclinic.ui.mvc;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

import com.example.petclinic.domain.Owner;
import com.example.petclinic.domain.Pet;
import com.example.petclinic.domain.Vet;
import com.example.petclinic.domain.Visit;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@SuppressWarnings("deprecation")
	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	public Converter<Owner, String> getOwnerToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.example.petclinic.domain.Owner, java.lang.String>() {
            @Override
			public String convert(Owner owner) {
                return new StringBuilder().append(owner.getFirstName()).append(" ").append(owner.getLastName()).append(" ").append(owner.getAddress()).append(" ").append(owner.getCity()).toString();
            }
        };
    }

	public Converter<Long, Owner> getIdToOwnerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.example.petclinic.domain.Owner>() {
            @Override
			public com.example.petclinic.domain.Owner convert(java.lang.Long id) {
                return Owner.findOwner(id);
            }
        };
    }

	public Converter<String, Owner> getStringToOwnerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.example.petclinic.domain.Owner>() {
            @Override
			public com.example.petclinic.domain.Owner convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Owner.class);
            }
        };
    }

	public Converter<Pet, String> getPetToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.example.petclinic.domain.Pet, java.lang.String>() {
            @Override
			public String convert(Pet pet) {
                return new StringBuilder().append(pet.getName()).append(" ").append(pet.getWeight()).toString();
            }
        };
    }

	public Converter<Long, Pet> getIdToPetConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.example.petclinic.domain.Pet>() {
            @Override
			public com.example.petclinic.domain.Pet convert(java.lang.Long id) {
                return Pet.findPet(id);
            }
        };
    }

	public Converter<String, Pet> getStringToPetConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.example.petclinic.domain.Pet>() {
            @Override
			public com.example.petclinic.domain.Pet convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Pet.class);
            }
        };
    }

	public Converter<Vet, String> getVetToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.example.petclinic.domain.Vet, java.lang.String>() {
            @Override
			public String convert(Vet vet) {
                return new StringBuilder().append(vet.getFirstName()).append(" ").append(vet.getLastName()).append(" ").append(vet.getAddress()).append(" ").append(vet.getCity()).toString();
            }
        };
    }

	public Converter<Long, Vet> getIdToVetConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.example.petclinic.domain.Vet>() {
            @Override
			public com.example.petclinic.domain.Vet convert(java.lang.Long id) {
                return Vet.findVet(id);
            }
        };
    }

	public Converter<String, Vet> getStringToVetConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.example.petclinic.domain.Vet>() {
            @Override
			public com.example.petclinic.domain.Vet convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Vet.class);
            }
        };
    }

	public Converter<Visit, String> getVisitToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<com.example.petclinic.domain.Visit, java.lang.String>() {
            @Override
			public String convert(Visit visit) {
                return new StringBuilder().append(visit.getDescription()).append(" ").append(visit.getVisitDate()).toString();
            }
        };
    }

	public Converter<Long, Visit> getIdToVisitConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, com.example.petclinic.domain.Visit>() {
            @Override
			public com.example.petclinic.domain.Visit convert(java.lang.Long id) {
                return Visit.findVisit(id);
            }
        };
    }

	public Converter<String, Visit> getStringToVisitConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, com.example.petclinic.domain.Visit>() {
            @Override
			public com.example.petclinic.domain.Visit convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Visit.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getOwnerToStringConverter());
        registry.addConverter(getIdToOwnerConverter());
        registry.addConverter(getStringToOwnerConverter());
        registry.addConverter(getPetToStringConverter());
        registry.addConverter(getIdToPetConverter());
        registry.addConverter(getStringToPetConverter());
        registry.addConverter(getVetToStringConverter());
        registry.addConverter(getIdToVetConverter());
        registry.addConverter(getStringToVetConverter());
        registry.addConverter(getVisitToStringConverter());
        registry.addConverter(getIdToVisitConverter());
        registry.addConverter(getStringToVisitConverter());
    }

	@Override
	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
