package com.rafael.financeiro.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.rafael.financeiro.model.Pessoa;
import com.rafael.financeiro.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaAtualiza = buscarPessoaPeloCodigo(codigo);		
		BeanUtils.copyProperties(pessoa, pessoaAtualiza, "codigo"); 
		return pessoaRepository.save(pessoaAtualiza);
	}


	public void atualizarPropriedadeAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaAtualiza = buscarPessoaPeloCodigo(codigo);
		pessoaAtualiza.setAtivo(ativo);
		pessoaRepository.save(pessoaAtualiza);
		
	}

	private Pessoa buscarPessoaPeloCodigo(Long codigo) {
		Pessoa pessoaAtualiza = pessoaRepository.findById(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));
		return pessoaAtualiza;
	}
	
}
