package br.ufal.ic.p2.myfood.models.users;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;
import br.ufal.ic.p2.myfood.models.empresas.Empresa;
import br.ufal.ic.p2.myfood.models.enums.EntregadorStatus;
import br.ufal.ic.p2.myfood.models.enums.Permissoes;
import br.ufal.ic.p2.myfood.models.enums.TipoUsuario;

import java.util.ArrayList;
import java.util.List;

import static br.ufal.ic.p2.myfood.models.enums.Permissoes.ENTREGAR_PEDIDO;

public class Entregador extends Usuario {
    private Veiculo veiculo;
    private List<Empresa> empresas;
    private EntregadorStatus status;

    public Entregador() {
    }

    public Entregador(String nome, String email, String senha, String endereco, Veiculo veiculo) throws AtributoInvalidoException {
        super(nome, email, senha, endereco);
        this.veiculo = veiculo;
        this.empresas = new ArrayList<>();
        this.status = EntregadorStatus.DISPONIVEL;
    }

    public void adicionarEmpresa(Empresa empresa) {
        empresas.add(empresa);
    }

    @Override
    public String getAtributo(String atributo) throws AtributoInvalidoException {
        if (atributo == null)
            throw new AtributoInvalidoException("Atributo invalido");

        return switch (atributo) {
            case "veiculo" -> veiculo.getTipo();
            case "placa" -> veiculo.getPlaca();
            default -> super.getAtributo(atributo);
        };
    }

    public EntregadorStatus getStatus() {
        return status;
    }

    public void setStatus(EntregadorStatus status) {
        this.status = status;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public List<Empresa> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(List<Empresa> empresas) {
        this.empresas = empresas;
    }

    @Override
    public TipoUsuario getTipo() {
        return TipoUsuario.ENTREGADOR;
    }

    @Override
    public List<Permissoes> getPermissoes() {
        return List.of(ENTREGAR_PEDIDO);
    }
}
