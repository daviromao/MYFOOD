package br.ufal.ic.p2.myfood.models.empresas;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;
import br.ufal.ic.p2.myfood.models.users.Dono;

public class Farmacia extends Empresa {
    private boolean aberto24h;
    private int numeroDeFuncionarios;

    public Farmacia(){
    }

    public Farmacia(String tipoEmpresa, Dono dono, String nome, String endereco, boolean aberto24h, int numeroDeFuncionarios) throws AtributoInvalidoException {
        super(tipoEmpresa, dono, nome, endereco);
        this.aberto24h = aberto24h;
        this.numeroDeFuncionarios = numeroDeFuncionarios;
    }

    @Override
    public String getAtributo(String atributo) throws AtributoInvalidoException {
        if(atributo == null)
            throw new AtributoInvalidoException("Atributo invalido");

        return switch (atributo) {
            case "aberto24Horas" -> String.valueOf(this.aberto24h);
            case "numeroFuncionarios" -> String.valueOf(this.numeroDeFuncionarios);
            default -> super.getAtributo(atributo);
        };
    }

    public boolean isAberto24h() {
        return aberto24h;
    }

    public void setAberto24h(boolean aberto24h) {
        this.aberto24h = aberto24h;
    }

    public int getNumeroDeFuncionarios() {
        return numeroDeFuncionarios;
    }

    public void setNumeroDeFuncionarios(int numeroDeFuncionarios) {
        this.numeroDeFuncionarios = numeroDeFuncionarios;
    }
}
