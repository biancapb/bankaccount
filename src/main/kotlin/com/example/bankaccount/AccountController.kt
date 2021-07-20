package com.example.bankaccount

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException

//porta de entrada da aplicação
@RestController
@RequestMapping("accounts")
class AccountController(val repository: AccountRepository) {

    //para criar
    @PostMapping
    fun createAccount(@RequestBody account: Account) = ResponseEntity.ok(repository.save(account))

    @GetMapping
    fun getAccount() = ResponseEntity.ok(repository.findAll()) /*200*/

    @PutMapping("{document}")
    fun updateAccount(@PathVariable document: String, @RequestBody account: Account): ResponseEntity<Account> {
        val accountDBOptional = repository.findByDocument(document)
        val toSave = accountDBOptional
            .orElseThrow{ RuntimeException("Account document: $document not found") }.copy(name = account.name, balance = account.balance)

        return ResponseEntity.ok(repository.save(toSave))
    }

    @DeleteMapping("{document}")
    fun deleteAccount(@PathVariable document: String) = repository
        .findByDocument(document).ifPresent{ repository.delete(it) }
}