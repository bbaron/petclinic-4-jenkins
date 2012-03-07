package com.example.petclinic.ui.mvc;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.example.petclinic.domain.Owner;
import com.example.petclinic.domain.Pet;
import com.example.petclinic.reference.PetType;

@RequestMapping("/pets")
@Controller
public class PetController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Pet pet, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, pet);
            return "pets/create";
        }
        uiModel.asMap().clear();
        pet.persist();
        return "redirect:/pets/" + encodeUrlPathSegment(pet.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Pet());
        return "pets/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("pet", Pet.findPet(id));
        uiModel.addAttribute("itemId", id);
        return "pets/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("pets", Pet.findPetEntries(firstResult, sizeNo));
            float nrOfPages = (float) Pet.countPets() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("pets", Pet.findAllPets());
        }
        return "pets/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Pet pet, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, pet);
            return "pets/update";
        }
        uiModel.asMap().clear();
        pet.merge();
        return "redirect:/pets/" + encodeUrlPathSegment(pet.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Pet.findPet(id));
        return "pets/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Pet pet = Pet.findPet(id);
        pet.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/pets";
    }

	void populateEditForm(Model uiModel, Pet pet) {
        uiModel.addAttribute("pet", pet);
        uiModel.addAttribute("owners", Owner.findAllOwners());
        uiModel.addAttribute("pettypes", Arrays.asList(PetType.values()));
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }

	@RequestMapping(params = { "find=ByNameAndWeight", "form" }, method = RequestMethod.GET)
    public String findPetsByNameAndWeightForm(Model uiModel) {
        return "pets/findPetsByNameAndWeight";
    }

	@RequestMapping(params = "find=ByNameAndWeight", method = RequestMethod.GET)
    public String findPetsByNameAndWeight(@RequestParam("name") String name, @RequestParam("weight") Float weight, Model uiModel) {
        uiModel.addAttribute("pets", Pet.findPetsByNameAndWeight(name, weight).getResultList());
        return "pets/list";
    }

	@RequestMapping(params = { "find=ByOwner", "form" }, method = RequestMethod.GET)
    public String findPetsByOwnerForm(Model uiModel) {
        uiModel.addAttribute("owners", Owner.findAllOwners());
        return "pets/findPetsByOwner";
    }

	@RequestMapping(params = "find=ByOwner", method = RequestMethod.GET)
    public String findPetsByOwner(@RequestParam("owner") Owner owner, Model uiModel) {
        uiModel.addAttribute("pets", Pet.findPetsByOwner(owner).getResultList());
        return "pets/list";
    }

	@RequestMapping(params = { "find=BySendRemindersAndWeightLessThan", "form" }, method = RequestMethod.GET)
    public String findPetsBySendRemindersAndWeightLessThanForm(Model uiModel) {
        return "pets/findPetsBySendRemindersAndWeightLessThan";
    }

	@RequestMapping(params = "find=BySendRemindersAndWeightLessThan", method = RequestMethod.GET)
    public String findPetsBySendRemindersAndWeightLessThan(@RequestParam(value = "sendReminders", required = false) boolean sendReminders, @RequestParam("weight") Float weight, Model uiModel) {
        uiModel.addAttribute("pets", Pet.findPetsBySendRemindersAndWeightLessThan(sendReminders, weight).getResultList());
        return "pets/list";
    }

	@RequestMapping(params = { "find=ByTypeAndNameLike", "form" }, method = RequestMethod.GET)
    public String findPetsByTypeAndNameLikeForm(Model uiModel) {
        uiModel.addAttribute("pettypes", java.util.Arrays.asList(PetType.class.getEnumConstants()));
        return "pets/findPetsByTypeAndNameLike";
    }

	@RequestMapping(params = "find=ByTypeAndNameLike", method = RequestMethod.GET)
    public String findPetsByTypeAndNameLike(@RequestParam("type") PetType type, @RequestParam("name") String name, Model uiModel) {
        uiModel.addAttribute("pets", Pet.findPetsByTypeAndNameLike(type, name).getResultList());
        return "pets/list";
    }
}
