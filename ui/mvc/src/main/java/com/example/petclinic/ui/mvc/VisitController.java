package com.example.petclinic.ui.mvc;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

import com.example.petclinic.domain.Pet;
import com.example.petclinic.domain.Vet;
import com.example.petclinic.domain.Visit;

@RequestMapping("/visits")
@Controller
public class VisitController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Visit visit, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, visit);
            return "visits/create";
        }
        uiModel.asMap().clear();
        visit.persist();
        return "redirect:/visits/" + encodeUrlPathSegment(visit.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Visit());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (Pet.countPets() == 0) {
            dependencies.add(new String[] { "pet", "pets" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "visits/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("visit", Visit.findVisit(id));
        uiModel.addAttribute("itemId", id);
        return "visits/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("visits", Visit.findVisitEntries(firstResult, sizeNo));
            float nrOfPages = (float) Visit.countVisits() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("visits", Visit.findAllVisits());
        }
        addDateTimeFormatPatterns(uiModel);
        return "visits/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Visit visit, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, visit);
            return "visits/update";
        }
        uiModel.asMap().clear();
        visit.merge();
        return "redirect:/visits/" + encodeUrlPathSegment(visit.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Visit.findVisit(id));
        return "visits/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Visit visit = Visit.findVisit(id);
        visit.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/visits";
    }

	void addDateTimeFormatPatterns(Model uiModel) {
        uiModel.addAttribute("visit_visitdate_date_format", org.joda.time.format.DateTimeFormat.patternForStyle("M-", LocaleContextHolder.getLocale()));
    }

	void populateEditForm(Model uiModel, Visit visit) {
        uiModel.addAttribute("visit", visit);
        addDateTimeFormatPatterns(uiModel);
        uiModel.addAttribute("pets", Pet.findAllPets());
        uiModel.addAttribute("vets", Vet.findAllVets());
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

	@RequestMapping(params = { "find=ByDescriptionAndVisitDate", "form" }, method = RequestMethod.GET)
    public String findVisitsByDescriptionAndVisitDateForm(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        return "visits/findVisitsByDescriptionAndVisitDate";
    }

	@RequestMapping(params = "find=ByDescriptionAndVisitDate", method = RequestMethod.GET)
    public String findVisitsByDescriptionAndVisitDate(@RequestParam("description") String description, @RequestParam("visitDate") @DateTimeFormat(style = "M-") Date visitDate, Model uiModel) {
        uiModel.addAttribute("visits", Visit.findVisitsByDescriptionAndVisitDate(description, visitDate).getResultList());
        addDateTimeFormatPatterns(uiModel);
        return "visits/list";
    }

	@RequestMapping(params = { "find=ByDescriptionLike", "form" }, method = RequestMethod.GET)
    public String findVisitsByDescriptionLikeForm(Model uiModel) {
        return "visits/findVisitsByDescriptionLike";
    }

	@RequestMapping(params = "find=ByDescriptionLike", method = RequestMethod.GET)
    public String findVisitsByDescriptionLike(@RequestParam("description") String description, Model uiModel) {
        uiModel.addAttribute("visits", Visit.findVisitsByDescriptionLike(description).getResultList());
        return "visits/list";
    }

	@RequestMapping(params = { "find=ByVisitDateBetween", "form" }, method = RequestMethod.GET)
    public String findVisitsByVisitDateBetweenForm(Model uiModel) {
        addDateTimeFormatPatterns(uiModel);
        return "visits/findVisitsByVisitDateBetween";
    }

	@RequestMapping(params = "find=ByVisitDateBetween", method = RequestMethod.GET)
    public String findVisitsByVisitDateBetween(@RequestParam("minVisitDate") @DateTimeFormat(style = "M-") Date minVisitDate, @RequestParam("maxVisitDate") @DateTimeFormat(style = "M-") Date maxVisitDate, Model uiModel) {
        uiModel.addAttribute("visits", Visit.findVisitsByVisitDateBetween(minVisitDate, maxVisitDate).getResultList());
        addDateTimeFormatPatterns(uiModel);
        return "visits/list";
    }
}
