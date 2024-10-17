package br.ufal.ic.p2.myfood.services;

import br.ufal.ic.p2.myfood.exceptions.CredenciaisInvalidasException;
import br.ufal.ic.p2.myfood.exceptions.EmailExistenteException;
import br.ufal.ic.p2.myfood.exceptions.ObjetoNaoEncontradoException;
import br.ufal.ic.p2.myfood.models.users.Usuario;
import br.ufal.ic.p2.myfood.persistence.Persistencia;
import br.ufal.ic.p2.myfood.persistence.PersistenciaXML;

import java.util.List;

public class UsuarioService {
    private final Persistencia<Usuario> usuarios = new PersistenciaXML<>("db/usuarios.xml");

    public void salvar(Usuario usuario) throws EmailExistenteException {
        for (Usuario u : usuarios.buscarTodos()) {
            if (u.getEmail().equals(usuario.getEmail())) {
                throw new EmailExistenteException();
            }
        }

        usuarios.salvar(usuario);
    }

    public List<Usuario> buscarTodos() {
        return usuarios.buscarTodos();
    }

    public int login(String email, String senha) {
        for (Usuario usuario : usuarios.buscarTodos()) {
            if (usuario.getEmail().equals(email) && usuario.getSenha().equals(senha)) {
                return usuario.getId();
            }
        }

        throw new CredenciaisInvalidasException();
    }

    public Usuario buscar(int id) throws ObjetoNaoEncontradoException {
        Usuario usuario = usuarios.buscar(id);

        if(usuario == null)
            throw new ObjetoNaoEncontradoException("Usuario nao cadastrado.");

        return usuarios.buscar(id);
    }

    public void deletarTodos() {
        usuarios.deletarTodos();
    }

    public void salvarTodos() {
        usuarios.salvarTodos();
    }
}
