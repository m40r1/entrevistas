package com.gerencimento.pessoa;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class pessoaController {

    @Autowired
    private final Pessoa pessoa;

    public pessoaController(final Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @PutMapping("/endereco/{id}")
    void mudarEndereco(@PathVariable final long id, @RequestBody final Endereco endereco) {
        // Pegar endereco que a pessoa quer mudar,colocar o novo endereco passado
        // passar o endereco como principal
        // FIXME checar e mudar caso haja outro principal
        pessoa.mudarEndereco(id, endereco);
    }

    @GetMapping("/pessoa/{nome}")
    Optional<pessoaVo> acharNome(@PathVariable final String nome) {
        return pessoa.acharNome(nome);
    };

    @GetMapping("/pessoa")
    List<pessoaVo> consultarPessoas() {
        return pessoa.consultarPessoas();
    };

    @PutMapping("/pessoa/{nome}")
    pessoaVo mudarNome(@RequestParam(value = "nome") final String nome, @PathVariable final String nomeAntigo) {
        // REVIEW test if map persists the change to the db
        return pessoa.mudarNome(nome, nomeAntigo);
    };

    @PostMapping("/cadastro/endereco")
    List<Endereco> criarEndereco(@RequestBody final Endereco endereco,
            @RequestParam(value = "nome") final String nome) {
        // false by default
        return pessoa.criarEndereco(endereco, nome);
    };

    @PostMapping("/cadastro/pessoa")
    pessoaVo criarPessoa(@RequestBody final pessoaVo dados) {
        // REVIEW deal with constructor right?
        return pessoa.criarPessoa(dados);
    };

    @GetMapping("/endereco")
    List<Endereco> listarEnderecos() {
        return pessoa.listarEnderecos();
    };

    @GetMapping("/endereco/{nome}")
    Optional<Endereco> enderecoPrincipal(@PathVariable final String nome) {
        // REVIEW testar uma forma mais resiliente
        return pessoa.enderecoPrincipal(nome);
    };
}
