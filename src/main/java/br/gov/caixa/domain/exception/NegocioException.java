package br.gov.caixa.domain.exception;

public class NegocioException extends RuntimeException {
    private final int codigo;

    public NegocioException(int codigo, String mensagem) {
        super(mensagem);
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

}
