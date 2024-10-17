package br.ufal.ic.p2.myfood.models.users;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;

public class Veiculo {
    String tipo;
    String placa;

    public Veiculo(String tipo, String placa) throws AtributoInvalidoException {
        if(tipo == null || tipo.isEmpty())
            throw new AtributoInvalidoException("Veiculo invalido");

        if(placa == null || placa.isEmpty())
            throw new AtributoInvalidoException("Placa invalido");

        this.tipo = tipo;
        this.placa = placa;
    }

    public Veiculo() {
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}
