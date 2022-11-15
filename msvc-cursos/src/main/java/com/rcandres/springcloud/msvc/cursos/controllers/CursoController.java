package com.rcandres.springcloud.msvc.cursos.controllers;

import com.rcandres.springcloud.msvc.cursos.models.Usuario;
import com.rcandres.springcloud.msvc.cursos.models.entity.Curso;
import com.rcandres.springcloud.msvc.cursos.services.ICursoService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class CursoController {

    @Autowired
    private ICursoService cursoService;

    @GetMapping
    private ResponseEntity<?> listar(){
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/{id}")
    private ResponseEntity<?> porId(@PathVariable Long id){
        Optional<Curso> cursoOptional = cursoService.porIdConUsuarios(id);//cursoService.porId(id);
        if (cursoOptional.isPresent())
            return ResponseEntity.ok(cursoOptional.get());

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    private ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return validar(bindingResult);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(curso));

    }

    @PutMapping("/{id}")
    private ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult bindingResult, @PathVariable Long id){
        if(bindingResult.hasErrors()){
            return validar(bindingResult);
        }
        Optional<Curso> cursoOptional = cursoService.porId(id);
        if (cursoOptional.isPresent()){
            Curso cursoDb= cursoOptional.get();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(cursoDb));
        }
        return ResponseEntity.notFound().build();

    }

    @DeleteMapping("/{id}")
    private ResponseEntity eliminar(@PathVariable Long id){
        Optional<Curso> usuarioOptional = cursoService.porId(id);
        if (usuarioOptional.isPresent()) {
            cursoService.elminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();

    }

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> o ;
        try {
           o = cursoService.asignarUsuario(usuario, cursoId);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(Collections.singletonMap("Mensaje:","No existe el usuario por el id o error " +
                            "en la comunicacion"+e.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> o ;
        try {
            o = cursoService.crearUsuario(usuario, cursoId);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(Collections.singletonMap("Mensaje:","No se pudo crear el usuario o error " +
                            "en la comunicacion"+e.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?>elimarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        Optional<Usuario> o ;
        try {
            o = cursoService.eliminarUsuarioCurso(usuario, cursoId);
        }catch (FeignException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).
                    body(Collections.singletonMap("Mensaje:","No existe el usuario por el id o error " +
                            "en la comunicacion"+e.getMessage()));
        }
        if(o.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }

        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> elimiarCursoUsuarioPorId(@PathVariable Long id){
        cursoService.eliminarCursoUsuarioPorId(id);
        return ResponseEntity.noContent().build();
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult bindingResult) {
        Map<String,String> errores = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err ->{
            errores.put(err.getField(), "Error en el campo "+err.getField()+" "+err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }



}
