package com.rcandres.springcloud.msvc.cursos.repositories;

import com.rcandres.springcloud.msvc.cursos.models.entity.Curso;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ICursoRepository extends CrudRepository<Curso,Long> {

    @Modifying
    @Query("delete from CursoUsuario cu where cu.usuario=?1")
    void eliminarCursoUsuarioPorId(Long id);
}
