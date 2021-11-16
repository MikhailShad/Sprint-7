package ru.sber.springmvc.controller

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
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
import ru.sber.springmvc.vo.Person
import java.util.concurrent.ConcurrentHashMap


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureEmbeddedDatabase(refresh = AutoConfigureEmbeddedDatabase.RefreshMode.BEFORE_EACH_TEST_METHOD)
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
        request.set("username", "admin@admin.com")
        request.set("password", "test")

        val response = restTemplate.postForEntity(url("login"), HttpEntity(request, HttpHeaders()), String::class.java)

        return response!!.headers["Set-Cookie"]!![0]
    }

    private fun url(s: String?): String {
        return "http://localhost:${port}/${s}"
    }

    @BeforeEach
    fun setUp() {
        headers.add("Cookie", getAuthCookie())

        records.forEach { testRecord ->
            testRecord.id = addressBookService.create(testRecord)
            testRecord.people = addressBookService.get(testRecord.id!!).people.map { Person(it.id, it.name, it.email) }
        }
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
        assertEquals(addressBookRecord.people, response.body!!.people)
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
            HttpEntity(mapOf("name" to addressBookRecord.people.first().name), headers),
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
            AddressBookRecord(people = listOf(Person(name = "A", email = "A@test.com")), address = "Улица Пушкина"),
            AddressBookRecord(people = listOf(Person(name = "B", email = "B@test.com")), address = "Дом Колотушкина"),
            AddressBookRecord(people = listOf(Person(name = "C", email = "C@test.com")), address = "Квартира Вольного"),
            AddressBookRecord(people = listOf(Person(name = "D", email = "D@test.com")), address = "Спросите любого")
        )

        @JvmStatic
        fun `created records`() = records
    }
}