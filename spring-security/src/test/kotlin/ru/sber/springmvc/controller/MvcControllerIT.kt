package ru.sber.springmvc.controller

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.context.WebApplicationContext
import ru.sber.springmvc.service.AddressBookService
import ru.sber.springmvc.vo.AddressBookRecord
import ru.sber.springmvc.vo.Person


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureEmbeddedDatabase(refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD)
class MvcControllerIT {

    @Autowired
    private val context: WebApplicationContext? = null
    private lateinit var mockMvc: MockMvc


    @Autowired
    private lateinit var addressBookService: AddressBookService

    @BeforeEach
    fun setUp() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context!!)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()

        records.forEach { testRecord ->
            testRecord.id = addressBookService.create(testRecord)
            testRecord.people = addressBookService.get(testRecord.id!!).people.map { Person(it.id, it.name, it.email) }
        }
    }

    @Test
    @WithMockUser(username = "user", password = "test", authorities = ["ROLE_USER"])
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
    @WithMockUser(username = "user", password = "test", authorities = ["ROLE_USER"])
    fun `test list all`() {
        mockMvc.perform(get("/app/list"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
    }

    @ParameterizedTest
    @MethodSource("created records")
    @WithMockUser(username = "user", password = "test", authorities = ["ROLE_USER"])
    fun `test list with query`(addressBookRecord: AddressBookRecord) {
        mockMvc.perform(get("/app/list?name=${addressBookRecord.people.first().name}&address=${addressBookRecord.address}"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("list"))
    }

    @ParameterizedTest
    @MethodSource("created records")
    @WithMockUser(username = "user", password = "test", authorities = ["ROLE_USER"])
    fun `test edit`(addressBookRecord: AddressBookRecord) {
        val note: MultiValueMap<String, String> = LinkedMultiValueMap()

        note.add("name", "J")
        note.add(
            "address",
            "Лондон. Ну это там, где рыба, чипсы, дрянная еда, отвратная погода, Мэри «та самая» Поппинс!"
        )

        mockMvc.perform(post("/app/${addressBookRecord.id}/edit").params(note))
            .andDo(print())
            .andExpect(status().is3xxRedirection)
    }

    @ParameterizedTest
    @MethodSource("created records")
    @WithMockUser(username = "user", password = "test", authorities = ["ROLE_USER"])
    fun `test delete by id`(addressBookRecord: AddressBookRecord) {
        mockMvc.perform(get("/app/${addressBookRecord.id}/delete"))
            .andDo(print())
            .andExpect(status().is3xxRedirection)

        // Post-actions
        addressBookRecord.id = null
    }

    @ParameterizedTest
    @MethodSource("created records")
    @WithMockUser(username = "user", password = "test", authorities = ["ROLE_USER"])
    fun `test get by id`(addressBookRecord: AddressBookRecord) {
        mockMvc.perform(get("/app/${addressBookRecord.id}/view"))
            .andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    @WithMockUser(username = "user", password = "test", authorities = ["ROLE_USER"])
    fun `test open add page`() {
        mockMvc.perform(get("/app/add"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(view().name("create"))
    }

    companion object {
        var records = listOf(
            AddressBookRecord(people = listOf(Person(name = "A", email = "A@test.com")), address = "Улица Пушкина"),
            AddressBookRecord(people = listOf(Person(name = "B", email = "B@test.com")), address = "Дом Колотушкина"),
            AddressBookRecord(people = listOf(Person(name = "C", email = "C@test.com")), address = "Квартира Вольного"),
            AddressBookRecord(people = listOf(Person(name = "D", email = "D@test.com")), address = "Спросите любого")
        )

        @JvmStatic
        fun `created records`() = records
    }
}