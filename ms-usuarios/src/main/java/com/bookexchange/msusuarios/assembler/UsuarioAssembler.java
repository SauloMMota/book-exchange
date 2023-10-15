package com.bookexchange.msusuarios.assembler;

import com.bookexchange.msusuarios.domain.Usuario;
import com.bookexchange.msusuarios.domain.representation.UsuarioInput;
import com.bookexchange.msusuarios.domain.representation.UsuarioOutput;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioAssembler {

    private final ModelMapper modelMapper;
    public UsuarioOutput toRepresentationOutput(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioOutput.class);
    }
    public Usuario toModel(UsuarioInput usuarioInput) {
        return modelMapper.map(usuarioInput, Usuario.class);
    }
}
