package com.gerencimento.pessoa;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.annotation.Id;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ch.qos.logback.core.joran.util.StringToObjectConverter;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

//TODO teste cep validationo
record Cep(@NotNull @Min(8) @Max(8) @Pattern(regexp = "/^d{5}(-d{3})?$/") long cep) {
}

record Endereco(@NotBlank String logradouro, Cep cep, @NotNull long numero, boolean principal) {
}

@Repository
interface pessoaRepo extends CrudRepository<pessoaVo, Long> {
    Optional<pessoaVo> findByNome(String nome);
}

record pessoaVo(@Id long id, @NotBlank String nome, @NotNull LocalDateTime nascimento, Collection<Endereco> enderecos) {
}

public class pessoa {

    private final pessoaRepo pessoaRepo;

    public pessoa(final com.gerencimento.pessoa.pessoaRepo pessoaRepo) {
        this.pessoaRepo = pessoaRepo;
    }

    void mudarEndereco(final long id, final Endereco endereco) {
        // Pegar endereco que a pessoa quer mudar,colocar o novo endereco passado
        // passar o endereco como principal
        // FIXME checar e mudar caso haja outro principal
        pessoaRepo.findById(id).ifPresent(pessoa -> pessoa.enderecos().add(endereco));
    }

    Optional<pessoaVo> acharNome(final String nome) {
        // devolver uma pessoa caso o nome exista
        return pessoaRepo.findByNome(nome);
    };

    Iterable<pessoaVo> consultarPessoas() {
        // pegar todas as pessoas da db
        return pessoaRepo.findAll();
    };

    pessoaVo mudarNome(final pessoaVo pessoa, final String nome) {
        // pegar nome da pessoa,criar uma novo com o nome passado
        final var novaPes = new pessoaVo(pessoa.id(), nome, pessoa.nascimento(), pessoa.enderecos());
        return pessoaRepo.save(novaPes);
    };

    void cadastrarEndereco(final Endereco endereco, final pessoaVo pessoa) {
        pessoaRepo.findById(pessoa.id()).ifPresent(pes -> pessoa.enderecos().add(endereco));
    };

    pessoaVo cadastrarPessoa(final String nome, final LocalDateTime nascimento) {
        // REVIEW redo this
        // deal with constructor right?
        final var pessoa = new pessoaVo(0, nome, nascimento, null);
        return pessoaRepo.save(pessoa);
    };

    void listarEnderecos() {
        pessoaRepo.findAll().forEach(pessoas -> System.out.println(pessoas.enderecos()));
    };

    void enderecoPrincipal(final pessoaVo pessoa) {
        // Pegar enderecos da pessoa
        // REVIEW refazer isso
        System.out.println(pessoa.enderecos().stream().anyMatch(ender -> ender.principal()));
    };
}
