package com.rcandres.springcloud.msv.usuarios.controllers;

import com.rcandres.springcloud.msv.usuarios.models.entity.Usuario;
import com.rcandres.springcloud.msv.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/")
    public ResponseEntity<?> lista(){
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){
        Optional<Usuario> usuarioOptional = usuarioService.porId(id);
        if(usuarioOptional.isPresent())
            return ResponseEntity.ok(usuarioOptional.get());
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return validar(bindingResult);
        }

        if(!usuario.getEmail().isEmpty() && usuarioService.porEmail(usuario.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("Error","El email ya existe para el usuario"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario,BindingResult bindingResult, @PathVariable Long id){


        if(bindingResult.hasErrors()){
            return validar(bindingResult);
        }
        Optional<Usuario> usuarioOptional = usuarioService.porId(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuarioDb = usuarioOptional.get();
            if(!usuario.getEmail().isEmpty() && !usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail()) && usuarioService.porEmail(usuario.getEmail()).isPresent()){
                return ResponseEntity.badRequest().body(Collections.singletonMap("Error","El email ya existe para un usuario"));
            }

            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(usuario.getPassword());

            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.guardar(usuarioDb));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Usuario> usuarioOptional = usuarioService.porId(id);
        if (usuarioOptional.isPresent()) {
            usuarioService.elminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerUsusariosPorCurso(@RequestParam(name = "ids") List<Long> ids){
         return ResponseEntity.ok(usuarioService.listarPorIds(ids));
    }


    private static ResponseEntity<Map<String, String>> validar(BindingResult bindingResult) {
        Map<String,String> errores = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err ->{
            errores.put(err.getField(), "Error en el campo "+err.getField()+" "+err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }


}
