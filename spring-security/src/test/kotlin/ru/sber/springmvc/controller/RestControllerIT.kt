package ru.sber.springmvc.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import ru.sber.springmvc.service.AddressBookService
import ru.sber.springmvc.vo.AddressBookRecord
import java.util.concurrent.ConcurrentHashMap


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestControllerIT {

    private val headers = HttpHeaders()

    @LocalServerPort
    private var port: Int = 8080

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var addressBookService: AddressBookService

    private fun getAuthCookie(): String? {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap()
        request.set("login", "admin")
        request.set("password", "admin")

        val response = restTemplate.postForEntity(url("login"), HttpEntity(request, HttpHeaders()), String::class.java)

        return response!!.headers["Set-Cookie"]!![0]
    }

    private fun url(s: String?): String {
        return "http://localhost:${port}/${s}"
    }

    @BeforeEach
    fun setUp() {
        headers.add("Cookie", getAuthCookie())

        records.forEach { addressBookService.create(it) }
    }

    @ParameterizedTest
    @MethodSource("created records")
    fun `test add new records`(addressBookRecord: AddressBookRecord) {
        val response = restTemplate.exchange(
            url("api/add"),
            HttpMethod.POST,
            HttpEntity(addressBookRecord, headers),
            AddressBookRecord::class.java
        )

        assertEquals(HttpStatus.CREATED, response.statusCode)
        assertNotNull(response.body)
        assertEquals(addressBookRecord.name, response.body!!.name)
    }

    @ParameterizedTest
    @MethodSource("created records")
    fun `test view existing records`(addressBookRecord: AddressBookRecord) {
        val response = restTemplate.exchange(
            url("api/${addressBookRecord.id}/view"),
            HttpMethod.GET,
            HttpEntity(null, headers),
            ConcurrentHashMap::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @ParameterizedTest
    @MethodSource("created records")
    fun `test list records by query`(addressBookRecord: AddressBookRecord) {
        val response = restTemplate.exchange(
            url("api/list"),
            HttpMethod.POST,
            HttpEntity(mapOf("name" to addressBookRecord.name), headers),
            List::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @Test
    fun `test list all records`() {
        val response = restTemplate.exchange(
            url("api/list"),
            HttpMethod.POST,
            HttpEntity(emptyMap<String, String>(), headers),
            List::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
    }

    @ParameterizedTest
    @MethodSource("created records")
    fun `test delete by id`(addressBookRecord: AddressBookRecord) {
        val response = restTemplate.exchange(
            url("api/${addressBookRecord.id}/delete"),
            HttpMethod.DELETE,
            HttpEntity(emptyMap<String, String>(), headers),
            ConcurrentHashMap::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @ParameterizedTest
    @MethodSource("created records")
    fun `test get by id`(addressBookRecord: AddressBookRecord) {
        val response = restTemplate.exchange(
            url("api/${addressBookRecord.id}/edit"),
            HttpMethod.POST,
            HttpEntity(addressBookRecord, headers),
            ConcurrentHashMap::class.java
        )

        assertEquals(HttpStatus.OK, response.statusCode)
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