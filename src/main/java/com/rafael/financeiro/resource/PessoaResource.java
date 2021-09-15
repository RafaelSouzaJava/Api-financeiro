package com.rafael.financeiro.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.rafael.financeiro.event.RecursoCriadoEvent;
import com.rafael.financeiro.model.Pessoa;
import com.rafael.financeiro.repository.PessoaRepository;
import com.rafael.financeiro.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaService pessoaService;
	
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public ResponseEntity<List<Pessoa>> lista() {
		List<Pessoa> pessoa = pessoaRepository.findAll();
		return ResponseEntity.ok().body(pessoa);
	}
	
	@GetMapping("/{codigo}")
	public ResponseEntity<Pessoa> buscar(@PathVariable Long codigo) {
		Optional<Pessoa> pessoa = pessoaRepository.findById(codigo);
		
		return pessoa.isPresent() ? ResponseEntity.ok().body(pessoa.get()) :
			ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@PostMapping
	public ResponseEntity<Pessoa> salva(@RequestBody @Valid Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pessoaRepository.save(pessoa);
		
		publisher.publishEvent(new RecursoCriadoEvent(this, response, pessoaSalva.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		pessoaRepository.deleteById(codigo);
	}
	
	@PutMapping("/{codigo}")
	public ResponseEntity<Pessoa> atualizar(@PathVariable Long codigo, @Valid @RequestBody Pessoa pessoa) {
		Pessoa pessoaAtualiza = pessoaService.atualizar(codigo, pessoa);
		return ResponseEntity.ok(pessoaAtualiza);
	}
	
	@PutMapping("/{codigo}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizarPropriedadeAtivo(@PathVariable Long codigo, @RequestBody Boolean ativo) {
		pessoaService.atualizarPropriedadeAtivo(codigo, ativo);
	}
}
