package com.rcandres.springcloud.msvc.cursos.services;

import com.rcandres.springcloud.msvc.cursos.models.Usuario;
import com.rcandres.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface ICursoService {

    List<Curso> listar();

    Optional<Curso> porId(Long id);

    Curso guardar(Curso curso);

    void elminar(Long id);

    Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId);

    Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId);

    Optional<Usuario> eliminarUsuarioCurso(Usuario usuario, Long cursoId);

    Optional<Curso> porIdConUsuarios(Long id);

    void eliminarCursoUsuarioPorId(Long id);


}
