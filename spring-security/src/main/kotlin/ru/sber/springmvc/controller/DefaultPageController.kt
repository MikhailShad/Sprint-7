package ru.sber.springmvc.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.view.RedirectView
import ru.sber.springmvc.service.PersonService
import ru.sber.springmvc.vo.Person

@Controller
@RequestMapping
class DefaultPageController() {

    @Autowired
    lateinit var personService: PersonService

    @GetMapping
    fun getDefaultPage(): RedirectView {
        return RedirectView("/app/list")
    }

    @GetMapping("/signUp")
    fun getSignUpPage(model: Model): String {
        model.addAttribute("newUser", Person())
        return "signUp"
    }

    @PostMapping("/signUp")
    fun signUp(@ModelAttribute person: Person): RedirectView {
        personService.save(person)
        return RedirectView("/")
    }
}
