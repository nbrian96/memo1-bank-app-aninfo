package com.aninfo;

import com.aninfo.model.*;
import com.aninfo.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@SpringBootApplication
@EnableSwagger2
public class Memo1BankApp {

	@Autowired
	private AccountService accountService;

	@Autowired
	private TransaccionService transaccionService;

	public static void main(String[] args) {
		SpringApplication.run(Memo1BankApp.class, args);
	}

	/*
	 * Account
	 */
	@PostMapping("/accounts")
	@ResponseStatus(HttpStatus.CREATED)
	public Account createAccount(@RequestBody Account account) {
		return accountService.createAccount(account);
	}

	@GetMapping("/accounts")
	public Collection<Account> getAccounts() {
		return accountService.getAccounts();
	}

	@GetMapping("/accounts/{cbu}")
	public ResponseEntity<Account> getAccount(@PathVariable Long cbu) {
		Optional<Account> accountOptional = accountService.findById(cbu);
		return ResponseEntity.of(accountOptional);
	}

	@PutMapping("/accounts/{cbu}")
	public ResponseEntity<Account> updateAccount(@RequestBody Account account, @PathVariable Long cbu) {
		Optional<Account> accountOptional = accountService.findById(cbu);

		if (!accountOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		account.setCbu(cbu);
		accountService.save(account);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/accounts/{cbu}")
	public void deleteAccount(@PathVariable Long cbu) {
		accountService.deleteById(cbu);
	}

	@PutMapping("/accounts/{cbu}/withdraw")
	public Account withdraw(@PathVariable Long cbu, @RequestParam Double sum) {
		return accountService.withdraw(cbu, sum);
	}

	@PutMapping("/accounts/{cbu}/deposit")
	public Account deposit(@PathVariable Long cbu, @RequestParam Double sum) {
		return accountService.deposit(cbu, sum);
	}

	/*
	 * Transaccion
	 */
	@GetMapping("/transacciones")
	public Collection<Transaccion> getTransacciones() {
		return transaccionService.getTransacciones();
	}

	@GetMapping("/transaccionesCBU/{cbu}")
	public Collection<Transaccion> getTransaccionesByCBU(@PathVariable Long cbu) {
		return transaccionService.getTransaccionesByCBU(cbu);
	}

	@GetMapping("/transacciones/{id}")
	public ResponseEntity<Transaccion> getTransaccion(@PathVariable Long id) {
		Optional<Transaccion> transaccionOptional = transaccionService.findById(id);
		return ResponseEntity.of(transaccionOptional);
	}

	@DeleteMapping("/transacciones/{id}")
	public void deleteTransaccion(@PathVariable Long id) {
		transaccionService.deleteById(id);
	}

	@PostMapping("/depositos")
	public ResponseEntity depositar(@RequestBody Transaccion transaccion) {

		Long cbu = transaccionService.getCbu(transaccion);
		Double importe = transaccionService.getImporte(transaccion);

		Optional<Account> accountOptional = accountService.findById(cbu);

		if (!accountOptional.isPresent())
			return ResponseEntity.notFound().build();

		Account account = accountOptional.get();
		if (!accountService.depositar(account, importe))
			return ResponseEntity.notFound().build();

		double extra = 0;
		if (importe > 2000)
			extra = importe * 0.1;
		if (extra > 500)
			extra = 500;

		importe = importe + extra;

		transaccionService.setImporte(transaccion, importe);

		transaccionService.createTransaccion(transaccion);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PostMapping("/extracciones")
	public ResponseEntity extraer(@RequestBody Transaccion transaccion) {

		Long cbu = transaccionService.getCbu(transaccion);
		Double importe = transaccionService.getImporte(transaccion);

		Optional<Account> accountOptional = accountService.findById(cbu);

		if (!accountOptional.isPresent())
			return ResponseEntity.notFound().build();

		Account account = accountOptional.get();
		if (!accountService.extraer(account, importe))
			return ResponseEntity.notFound().build();

		transaccionService.setImporte(transaccion, -importe);
		transaccionService.createTransaccion(transaccion);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/*
	 * OTROS
	 */
	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}
}
