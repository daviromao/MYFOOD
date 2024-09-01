package br.ufal.ic.p2.myfood.services;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;
import br.ufal.ic.p2.myfood.exceptions.EmpresaInvalidaException;
import br.ufal.ic.p2.myfood.exceptions.ObjetoNaoEncontradoException;
import br.ufal.ic.p2.myfood.models.Dono;
import br.ufal.ic.p2.myfood.models.Empresa;
import br.ufal.ic.p2.myfood.persistence.Persistencia;
import br.ufal.ic.p2.myfood.persistence.PersistenciaXML;

import java.util.ArrayList;
import java.util.List;

public class EmpresaService {
    private final Persistencia<Empresa> empresas = new PersistenciaXML<>("db/empresas.xml");

    public int salvar(Empresa novaEmpresa) throws EmpresaInvalidaException {
        for (Empresa e : empresas.buscarTodos()) {
            if(e.getNome().equals(novaEmpresa.getNome())){
                if(!e.getDono().equals(novaEmpresa.getDono())) {
                    throw new EmpresaInvalidaException("Empresa com esse nome ja existe");
                }
                if(e.getDono().equals(novaEmpresa.getDono()) && e.getEndereco().equals(novaEmpresa.getEndereco())) {
                    throw new EmpresaInvalidaException("Proibido cadastrar duas empresas com o mesmo nome e local");
                }
            }
        }

        empresas.salvar(novaEmpresa);

        return novaEmpresa.getId();
    }

    public List<Empresa> buscarEmpresasPorDono(Dono dono) {
        List<Empresa> empresasPorDono = new ArrayList<>();

        for (Empresa e : empresas.buscarTodos()) {
            if(e.getDono().getId() == dono.getId()) {
                empresasPorDono.add(e);
            }
        }

        return empresasPorDono;
    }

    public Empresa buscarIdEmpresa(Dono dono, String nome, int indice) throws AtributoInvalidoException, ObjetoNaoEncontradoException {
        List<Empresa> empresasPorDonoENome = buscarEmpresasPorDono(dono).stream()
                                                                        .filter(e -> e.getNome().equals(nome))
                                                                        .toList();

        if(indice < 0)
            throw new AtributoInvalidoException("Indice invalido");

        if(nome == null || nome.isBlank())
            throw new AtributoInvalidoException("Nome invalido");

        if(empresasPorDonoENome.size()==0)
            throw new ObjetoNaoEncontradoException("Nao existe empresa com esse nome");

        if(indice >= empresasPorDonoENome.size())
            throw new AtributoInvalidoException("Indice maior que o esperado");

        return empresasPorDonoENome.get(indice);
    }

    public Empresa buscarEmpresaPorId(int id) throws ObjetoNaoEncontradoException {
        return empresas.buscar(id);
    }

    public void deletarTodos() {
        empresas.deletarTodos();
    }

    public void salvarTodos() {
        empresas.salvarTodos();
    }

}
