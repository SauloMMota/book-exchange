package com.bookexchange.mslivros.assembler;

import com.bookexchange.mslivros.domain.Livro;
import com.bookexchange.mslivros.domain.representation.LivroInput;
import com.bookexchange.mslivros.domain.representation.LivroOutput;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LivroAssembler {

    private final ModelMapper modelMapper;
    public Livro toModel(LivroInput livroInput) {
        return modelMapper.map(livroInput, Livro.class);
    }
    public LivroOutput toRepresentation(Livro livro) {
        return modelMapper.map(livro, LivroOutput.class);
    }
    public Page<LivroOutput> toPageRepresentation(Page<Livro> livros) {
        return livros.map(this::toRepresentation);
    }
}
