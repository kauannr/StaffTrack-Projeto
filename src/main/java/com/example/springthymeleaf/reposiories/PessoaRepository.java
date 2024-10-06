package com.example.springthymeleaf.reposiories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.springthymeleaf.model.Pessoa;

@Repository
@Transactional
public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
    
    @Query("select p from Pessoa p where lower(p.nome) like %?1% or lower(p.sobrenome) like %?1%")
    public Page<Pessoa> consultarPornNome(String nome, Pageable pageable);

    @Query("select p from Pessoa p where lower(p.sexo) like %?1%")
    public Page<Pessoa> consultarPorSexo(String nome, Pageable pageable);
}



