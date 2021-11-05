package ru.sber.springmvc.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.view.RedirectView

@Controller
@RequestMapping
class DefaultPageController() {
    @GetMapping
    fun getDefaultPage(): RedirectView {
        return RedirectView("/app/list")
    }
}
