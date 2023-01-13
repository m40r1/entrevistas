package com.gerencimento.pessoa;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
interface pessoaRepo extends PagingAndSortingRepository<pessoaVo, Long> {
    Optional<pessoaVo> findByNome(String nome);

    Stream<pessoaVo> findAll();

    Optional<pessoaVo> findById(long id);

    pessoaVo save(pessoaVo novaPes);
}
