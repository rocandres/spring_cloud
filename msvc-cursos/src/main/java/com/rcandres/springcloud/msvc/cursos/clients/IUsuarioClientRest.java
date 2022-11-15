package com.rcandres.springcloud.msvc.cursos.clients;

import com.rcandres.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios",url = "${msvc.usuarios.url}")
public interface IUsuarioClientRest {

    @GetMapping("/{id}")
    Usuario detalle(@PathVariable Long id);

    @PostMapping("/")
    Usuario crearUsuario(@RequestBody Usuario usuario);

    @GetMapping("/usuarios-por-curso")
    List<Usuario> obtenerUsusariosPorCurso(@RequestParam(name = "ids") Iterable<Long> ids);

}
