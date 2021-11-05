package ru.sber.springmvc.filter

import java.time.Instant
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletContext
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@WebFilter(urlPatterns = ["/*"])
class AuthFilter : HttpFilter() {

    private lateinit var context: ServletContext

    override fun init(filterConfig: FilterConfig) {
        context = filterConfig.servletContext
        context.log("Authentication filter is initialized")
    }

    override fun doFilter(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?) {
        val path = (request as HttpServletRequest).requestURI
        if (path.startsWith("/login")
            || path.startsWith("/authError")
            || path.startsWith("/error")
        ) {
            chain!!.doFilter(request, response)
            return
        }

        val cookies = request.cookies

        if (cookies == null) {
            context.log("No Cookies, Unknown Authorization")
            response!!.sendRedirect("/login")
            return
        }

        val authCookie = cookies.firstOrNull { it.name == "auth" }
        if (authCookie == null) {
            context.log("Cookie Not Found")
            response!!.sendRedirect("/login")
            return
        }

        val currentTime = Instant.now().toString()
        if (currentTime < authCookie.value) {
            context.log("Wrong cookie: Cookie Value - ${authCookie.value}, Current Value - $currentTime")
            response!!.sendRedirect("/login")
            return
        }

        context.log("Valid Cookie")
        chain!!.doFilter(request, response)
    }
}
