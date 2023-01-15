package com.gerencimento.pessoa;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Pessoa {
    private Pessoa(final com.gerencimento.pessoa.pessoaRepo pessoaRepo) {
        this.pessoaRepo = pessoaRepo;
    }

    private final pessoaRepo pessoaRepo;

    @PutMapping("/endereco/{id}")
    void mudarEndereco(@PathVariable final long id, @RequestBody final Endereco endereco) {
        // Pegar endereco que a pessoa quer mudar,colocar o novo endereco passado
        // passar o endereco como principal
        // FIXME checar e mudar caso haja outro principal
        pessoaRepo.findById(id).ifPresent(pessoa -> pessoa.enderecos().add(endereco));
    }

    @GetMapping("/pessoa/{nome}")
    Optional<pessoaVo> acharNome(@PathVariable final String nome) {
        return pessoaRepo.findByNome(nome);
    };

    @GetMapping("/pessoa")
    List<pessoaVo> consultarPessoas() {
        return pessoaRepo.findAll().toList();
    };

    @PutMapping("/pessoa/{nome}")
    pessoaVo mudarNome(@RequestParam(value = "nome") final String nome, @PathVariable final String nomeAntigo) {
        // REVIEW test if map persists the change to the db
        return pessoaRepo.findByNome(nomeAntigo).map(
                pessoa -> new pessoaVo(pessoa.id(), nome, pessoa.nascimento(), pessoa.enderecos(), pessoa.cidade()))
                .get();
    };

    @PostMapping("/cadastro/endereco")
    List<Endereco> criarEndereco(@RequestBody final Endereco endereco,
            @RequestParam(value = "nome") final String nome) {
        // false by default
        pessoaRepo.findByNome(nome).ifPresent(pes -> pes.enderecos().add(endereco));
        return pessoaRepo.findByNome(nome).get().enderecos();
    };

    @PostMapping("/cadastro/pessoa")
    pessoaVo criarPessoa(@RequestBody final pessoaVo pessoa) {
        // REVIEW deal with constructor right?
        return pessoaRepo.save(pessoa);
    };

    @GetMapping("/endereco")
    List<Endereco> listarEnderecos() {
        return pessoaRepo.findAll().flatMap(pes -> pes.enderecos().parallelStream()).toList();
    };

    @GetMapping("/endereco/{nome}")
    Optional<Endereco> enderecoPrincipal(@PathVariable final String nome) {
        // REVIEW testar uma forma mais resiliente
        return pessoaRepo.findByNome(nome)
                .flatMap(pes -> pes.enderecos().stream().filter(endereco -> endereco.principal()).findFirst());
    };
}
