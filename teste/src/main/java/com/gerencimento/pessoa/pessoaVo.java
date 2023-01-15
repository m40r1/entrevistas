package com.gerencimento.pessoa;

import java.time.LocalDateTime;
import java.util.List;

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

record pessoaVo(@Id Long id, @NotBlank String nome, @NotNull LocalDateTime nascimento, List<Endereco> enderecos,
        String cidade) {
}
