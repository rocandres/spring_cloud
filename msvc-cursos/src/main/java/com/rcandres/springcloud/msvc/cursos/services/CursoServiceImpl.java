package com.rcandres.springcloud.msvc.cursos.services;

import com.rcandres.springcloud.msvc.cursos.clients.IUsuarioClientRest;
import com.rcandres.springcloud.msvc.cursos.models.Usuario;
import com.rcandres.springcloud.msvc.cursos.models.entity.Curso;
import com.rcandres.springcloud.msvc.cursos.models.entity.CursoUsuario;
import com.rcandres.springcloud.msvc.cursos.repositories.ICursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements ICursoService {

    @Autowired
    private ICursoRepository cursoRepository;

    @Autowired
    private IUsuarioClientRest usuarioClientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>)cursoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return cursoRepository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void elminar(Long id) {
        cursoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = cursoRepository.findById(cursoId);

        if(o.isPresent()){
            Usuario usuarioMsvc = usuarioClientRest.detalle(usuario.getId());

            Curso curso = o.get();
            CursoUsuario cursoUsuario= new CursoUsuario();
            cursoUsuario.setUsuario(usuarioMsvc.getId());
            curso.addCursoUsuario(cursoUsuario);

            cursoRepository.save(curso);
            return Optional.of(usuarioMsvc);
        }
         return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = cursoRepository.findById(cursoId);

        if(o.isPresent()){
            Usuario usuarioNuevo = usuarioClientRest.crearUsuario(usuario);

            Curso curso = o.get();
            CursoUsuario cursoUsuario= new CursoUsuario();
            cursoUsuario.setUsuario(usuarioNuevo.getId());
            curso.addCursoUsuario(cursoUsuario);

            cursoRepository.save(curso);
            return Optional.of(usuarioNuevo);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuarioCurso(Usuario usuario, Long cursoId) {
        Optional<Curso> o = cursoRepository.findById(cursoId);

        if(o.isPresent()){
            Usuario usuarioMsvc = usuarioClientRest.detalle(usuario.getId());

            Curso curso = o.get();
            CursoUsuario cursoUsuario= new CursoUsuario();
            cursoUsuario.setUsuario(usuarioMsvc.getId());
            curso.removeCursoUsuario(cursoUsuario);

            cursoRepository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porIdConUsuarios(Long id) {
        Optional<Curso> o = cursoRepository.findById(id);
        if(o.isPresent()){
            Curso curso= o.get();
            if(!curso.getCursoUsuarios().isEmpty()){
                List<Long> idsUsuarios= curso.getCursoUsuarios().stream().map(cu -> cu.getUsuario()).collect(Collectors.toList());

                List<Usuario> usuarios = usuarioClientRest.obtenerUsusariosPorCurso(idsUsuarios);
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public void eliminarCursoUsuarioPorId(Long id) {
        cursoRepository.eliminarCursoUsuarioPorId(id);
    }
}
