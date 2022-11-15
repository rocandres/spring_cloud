package com.rcandres.springcloud.msv.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name ="msv-cursos" ,url = "${msvc.cursos.url}")
public interface ICursoClienteRest {

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    void elimiarCursoUsuarioPorId(@PathVariable Long id);

}
