package ru.sber.springmvc.controller

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import ru.sber.springmvc.service.AddressBookService
import ru.sber.springmvc.vo.AddressBookRecord


@SpringBootTest
@AutoConfigureMockMvc
class MvcControllerIT {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var addressBookService: AddressBookService

    @BeforeEach
    fun setUp() {
        records.forEach { addressBookService.create(it) }
    }

    @Test
    fun `test add record`() {
        val note: MultiValueMap<String, String> = LinkedMultiValueMap()

        note.add("name", "J")
        note.add(
            "address",
            "Лондон. Ну это там, где рыба, чипсы, дрянная еда, отвратная погода, Мэри «та самая» Поппинс!"
        )

        mockMvc.perform(post("/app/add").params(note))
            .andExpect(status().is3xxRedirection)
    }

    @Test
    fun `test list all`() {
        mockMvc.perform(get("/app/list"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
    }

    @Test
    fun `test list with query`() {
        mockMvc.perform(get("/app/list?name=B"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
    }

    @Test
    fun `test edit`() {
        val note: MultiValueMap<String, String> = LinkedMultiValueMap()

        note.add("name", "J")
        note.add(
            "address",
            "Лондон. Ну это там, где рыба, чипсы, дрянная еда, отвратная погода, Мэри «та самая» Поппинс!"
        )

        mockMvc.perform(post("/app/0/edit").params(note))
            .andDo(print())
            .andExpect(status().is3xxRedirection)
    }

    @ParameterizedTest
    @MethodSource("created records")
    fun `test delete by id`(addressBookRecord: AddressBookRecord) {
        mockMvc.perform(get("/app/${addressBookRecord.id}/delete"))
            .andDo(print())
            .andExpect(status().is3xxRedirection)
    }

    @ParameterizedTest
    @MethodSource("created records")
    fun `test get by id`(addressBookRecord: AddressBookRecord) {
        mockMvc.perform(get("/app/${addressBookRecord.id}/view"))
            .andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    fun `test open add page`() {
        mockMvc.perform(get("/app/add"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("create"))
    }

    companion object {
        val records = listOf(
            AddressBookRecord("A", "Улица Пушкина"),
            AddressBookRecord("B", "Дом Колотушкина"),
            AddressBookRecord("C", "Квартира Вольного"),
            AddressBookRecord("D", "Спросите любого")
        )

        @JvmStatic
        fun `created records`() = records
    }
}