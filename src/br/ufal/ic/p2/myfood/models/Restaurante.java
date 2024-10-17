package br.ufal.ic.p2.myfood.models;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;

public class Restaurante extends Empresa {
    private String tipoCozinha;

    public Restaurante() {
    }

    public Restaurante(String tipoEmpresa, Dono dono, String nome, String endereco, String tipoCozinha) throws AtributoInvalidoException {
        super(tipoEmpresa, dono, nome, endereco);
        if (tipoCozinha == null || tipoCozinha.isEmpty())
            throw new AtributoInvalidoException("Tipo de cozinha invalido");
        this.tipoCozinha = tipoCozinha;
    }

    @Override
    public String getAtributo(String atributo) throws AtributoInvalidoException {
        if(atributo == null)
            throw new AtributoInvalidoException("Atributo invalido");

        if (atributo.equals("tipoCozinha"))
            return tipoCozinha;

        return super.getAtributo(atributo);
    }

    public String getTipoCozinha() {
        return tipoCozinha;
    }

    public void setTipoCozinha(String tipoCozinha) {
        this.tipoCozinha = tipoCozinha;
    }
}
