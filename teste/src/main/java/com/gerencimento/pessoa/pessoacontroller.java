package com.gerencimento.pessoa;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class pessoacontroller {
    private Pessoa pessoa;

    @GetMapping("/pessoa")
    public List<pessoaVo> consultarPessoas() {
        return (List<pessoaVo>) pessoa.consultarPessoas();
    }

    @PostMapping("/cadastro/pessoa")
    public pessoaVo cadastrarPessoa(@RequestBody pessoaVo dados) {
        return pessoa.criarPessoa(dados);
    }

    @GetMapping("/endereco")
    public List<Endereco> consultarEnderecos() {
        return pessoa.listarEnderecos();
    }

    @GetMapping("/pessoa/{nome}")
    public pessoaVo consultarPessoa(@PathVariable String nome) {
        return pessoa.acharNome(nome).get();
    }

    @PostMapping("/cadastro/endereco")
    public List<Endereco> cadastrarEndereco(@RequestBody Endereco endereco, @RequestParam(value = "nome") String nome) {
        // NOTE false by default
        return pessoa.criarEndereco(endereco, nome);
    }

    @GetMapping("/endereco/{nome}")
    public Endereco enderecoPrincipal(@PathVariable String nome) {
        return pessoa.enderecoPrincipal(nome).get();
    }

    @PutMapping("/pessoa/{nome}")
    public pessoaVo mudarNome(@PathVariable String nomeAntigo, @RequestParam(value = "nome") String nome) {
        return pessoa.mudarNome(nomeAntigo, nome);
    }

    @PutMapping("/endereco/{id}")
    public void mudarEndereco(@RequestBody Endereco endereco, @PathVariable long id) {
        pessoa.mudarEndereco(id, endereco);
    }

}
