package br.ufal.ic.p2.myfood.models.empresas;

import br.ufal.ic.p2.myfood.exceptions.AtributoInvalidoException;
import br.ufal.ic.p2.myfood.models.users.Dono;

import java.util.List;

public class Mercado extends Empresa {
    private static final List<String> tiposMercado = List.of("supermercado", "minimercado", "atacadista");
    private String tipoMercado;
    private String abre;
    private String fecha;

    public Mercado() {
    }

    public Mercado(String tipoEmpresa, Dono dono, String nome, String endereco, String abre, String fecha, String tipoMercado) throws AtributoInvalidoException {
        super(tipoEmpresa, dono, nome, endereco);
        if(tipoMercado == null || !tiposMercado.contains(tipoMercado))
            throw new AtributoInvalidoException("Tipo de mercado invalido");

        this.tipoMercado = tipoMercado;

        validarHorario(abre, fecha);

        this.abre = abre;
        this.fecha = fecha;
    }

    @Override
    public String getAtributo(String atributo) throws AtributoInvalidoException {
        if(atributo == null)
            throw new AtributoInvalidoException("Atributo invalido");

        if (atributo.equals("tipoMercado"))
            return tipoMercado;
        if (atributo.equals("abre"))
            return abre;
        if (atributo.equals("fecha"))
            return fecha;
        return super.getAtributo(atributo);
    }

    // valida hora formato HH:MM
    private void validarHorario(String abre, String fecha) throws AtributoInvalidoException {
        if (abre == null || fecha == null)
            throw new AtributoInvalidoException("Horario invalido");

        if (abre.length() != 5 || fecha.length() != 5)
            throw new AtributoInvalidoException("Formato de hora invalido");

        if (abre.charAt(2) != ':' || fecha.charAt(2) != ':')
            throw new AtributoInvalidoException("Formato de hora invalido");

        int horaAbre = Integer.parseInt(abre.substring(0, 2));
        int minAbre = Integer.parseInt(abre.substring(3, 5));

        int horaFecha = Integer.parseInt(fecha.substring(0, 2));
        int minFecha = Integer.parseInt(fecha.substring(3, 5));

        if (horaAbre < 0 || horaAbre > 23 || horaFecha < 0 || horaFecha > 23)
            throw new AtributoInvalidoException("Horario invalido");

        if (minAbre < 0 || minAbre > 59 || minFecha < 0 || minFecha > 59)
            throw new AtributoInvalidoException("Horario invalido");

        if (horaAbre > horaFecha)
            throw new AtributoInvalidoException("Horario invalido");

        if (horaAbre == horaFecha && minAbre >= minFecha)
            throw new AtributoInvalidoException("Horario invalido");
    }

    public void alterarHorario(String abre, String fecha) throws AtributoInvalidoException {
        validarHorario(abre, fecha);

        this.abre = abre;
        this.fecha = fecha;
    }

    public String getTipoMercado() {
        return tipoMercado;
    }

    public void setTipoMercado(String tipoMercado) {
        this.tipoMercado = tipoMercado;
    }

    public String getAbre() {
        return abre;
    }

    public void setAbre(String abre) {
        this.abre = abre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
