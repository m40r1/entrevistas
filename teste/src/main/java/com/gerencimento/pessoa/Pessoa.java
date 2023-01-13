package com.gerencimento.pessoa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.Id;

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

record pessoaVo(@Id Long id, @NotBlank String nome, @NotNull LocalDateTime nascimento, List<Endereco> enderecos) {
}

public class Pessoa {

    private final pessoaRepo pessoaRepo;

    private Pessoa(final com.gerencimento.pessoa.pessoaRepo pessoaRepo) {
        this.pessoaRepo = pessoaRepo;
    }

    void mudarEndereco(final long id, final Endereco endereco) {
        // Pegar endereco que a pessoa quer mudar,colocar o novo endereco passado
        // passar o endereco como principal
        // FIXME checar e mudar caso haja outro principal
        pessoaRepo.findById(id).ifPresent(pessoa -> pessoa.enderecos().add(endereco));
    }

    Optional<pessoaVo> acharNome(final String nome) {
        return pessoaRepo.findByNome(nome);
    };

    List<pessoaVo> consultarPessoas() {
        return pessoaRepo.findAll().toList();
    };

    pessoaVo mudarNome(final String nomeAntigo, final String nome) {
        // REVIEW test if map persists the change to the db
        return pessoaRepo.findByNome(nomeAntigo)
                .map(pessoa -> new pessoaVo(pessoa.id(), nome, pessoa.nascimento(), pessoa.enderecos())).get();
    };

    List<Endereco> criarEndereco(final Endereco endereco, final String nome) {
        // false by default
        pessoaRepo.findByNome(nome).ifPresent(pes -> pes.enderecos().add(endereco));
        return pessoaRepo.findByNome(nome).get().enderecos();
    };

    pessoaVo criarPessoa(final pessoaVo pessoa) {
        // REVIEW deal with constructor right?
        return pessoaRepo.save(pessoa);
    };

    List<Endereco> listarEnderecos() {
        return pessoaRepo.findAll().flatMap(pes -> pes.enderecos().parallelStream()).toList();
    };

    Optional<Endereco> enderecoPrincipal(final String nome) {
        // REVIEW testar uma forma mais resiliente
        return pessoaRepo.findByNome(nome)
                .flatMap(pes -> pes.enderecos().stream().filter(endereco -> endereco.principal()).findFirst());
    };
}
