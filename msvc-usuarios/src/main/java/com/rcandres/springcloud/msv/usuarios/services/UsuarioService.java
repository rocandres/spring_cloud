package com.rcandres.springcloud.msv.usuarios.services;

import com.rcandres.springcloud.msv.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> listar();
    Optional<Usuario> porId(Long id);
    Usuario guardar(Usuario usuario);
    void elminar(Long Id);

    Optional<Usuario> porEmail(String email);

    List<Usuario> listarPorIds(Iterable<Long> ids);


}
