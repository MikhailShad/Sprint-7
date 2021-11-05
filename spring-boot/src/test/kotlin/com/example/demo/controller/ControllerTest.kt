package com.example.demo.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(Controller::class)
internal class ControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun greeting() {
        var response = mockMvc.perform(MockMvcRequestBuilders.get("/hello"))

        response
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().string("hello my fellow friend"))
    }
}