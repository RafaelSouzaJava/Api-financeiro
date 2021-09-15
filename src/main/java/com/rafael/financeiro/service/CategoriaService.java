package com.rafael.financeiro.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.rafael.financeiro.model.Categoria;
import com.rafael.financeiro.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	CategoriaRepository categoriaRepository;
	
	public Categoria atualiza(Long codigo, Categoria categoria) {
		Categoria categoriaAtualiza = categoriaRepository.findById(codigo).orElseThrow(() -> new EmptyResultDataAccessException(1));	
		BeanUtils.copyProperties(categoria, categoriaAtualiza, "codigo");
		return categoriaRepository.save(categoriaAtualiza);
	}

}
