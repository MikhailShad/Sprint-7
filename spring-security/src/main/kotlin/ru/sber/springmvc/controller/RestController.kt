package ru.sber.springmvc.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import ru.sber.springmvc.service.AddressBookService
import ru.sber.springmvc.vo.AddressBookRecord

@RestController
@RequestMapping("/api")
class RestController @Autowired constructor(val addressBookService: AddressBookService) {

    @PostMapping("/add")
    fun addPage(
        @RequestBody
        addressBookRecord: AddressBookRecord
    ): ResponseEntity<AddressBookRecord> {
        addressBookService.create(addressBookRecord)
        return ResponseEntity(addressBookRecord, HttpStatus.CREATED)
    }

    @PostMapping("/list")
    fun getPage(@RequestBody query: Map<String, String>?): ResponseEntity<List<AddressBookRecord>> {
        return ResponseEntity(
            addressBookService.get(query),
            HttpStatus.OK
        )
    }

    @GetMapping("/{id}/view")
    fun getPageById(@PathVariable id: String): ResponseEntity<AddressBookRecord> {
        return ResponseEntity(
            addressBookService.get(id.toLong()),
            HttpStatus.OK
        )
    }

    @PostMapping("/{id}/edit")
    fun editPage(
        @PathVariable id: String,
        @RequestBody addressBookRecord: AddressBookRecord
    ): ResponseEntity<AddressBookRecord> {
        addressBookService.update(id.toLong(), addressBookRecord)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/{id}/delete")
    fun deletePage(@PathVariable id: String): ResponseEntity<AddressBookRecord> {
        addressBookService.delete(id.toLong())
        return ResponseEntity(HttpStatus.OK)
    }
}
