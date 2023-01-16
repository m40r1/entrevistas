package com.gerencimento.pessoa;

import java.util.List;
import java.util.Optional;

class Pessoa {

    private Pessoa(final com.gerencimento.pessoa.pessoaRepo pessoaRepo) {
        this.pessoaRepo = pessoaRepo;
    }

    private final pessoaRepo pessoaRepo;

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

    pessoaVo mudarNome(final String nome, final String nomeAntigo) {
        // REVIEW test if map persists the change to the db
        return pessoaRepo.findByNome(nomeAntigo).map(
                pessoa -> new pessoaVo(pessoa.id(), nome, pessoa.nascimento(), pessoa.enderecos(), pessoa.cidade()))
                .get();
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
