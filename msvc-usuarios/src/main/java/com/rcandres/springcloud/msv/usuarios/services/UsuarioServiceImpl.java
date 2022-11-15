package com.rcandres.springcloud.msv.usuarios.services;

import com.rcandres.springcloud.msv.usuarios.clients.ICursoClienteRest;
import com.rcandres.springcloud.msv.usuarios.models.entity.Usuario;
import com.rcandres.springcloud.msv.usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements  UsuarioService{

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private ICursoClienteRest cursoClienteRest;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return (List<Usuario>) userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> porId(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return userRepository.save(usuario);
    }

    @Override
    @Transactional
    public void elminar(Long Id) {
        userRepository.deleteById(Id);
        cursoClienteRest.elimiarCursoUsuarioPorId(Id);
    }

    @Override
    public Optional<Usuario> porEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarPorIds(Iterable<Long> ids) {
        return (List<Usuario>) userRepository.findAllById(ids);
    }
}
