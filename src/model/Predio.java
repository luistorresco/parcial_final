package model;

/**
 * Clase Modelo que representa un Predio (Registro Catastral).
 * 
 * Arquitectura MVC: MODELO.
 * Encapsula los datos de un predio: npn, municipio, direccion y numeroFicha.
 */
public class Predio {
    private String npn;
    private String municipio;
    private String direccion;
    private String numeroFicha;

    /**
     * Constructor completo.
     * 
     * @param npn Número Predial Nacional
     * @param municipio Nombre del Municipio
     * @param direccion Dirección del Predio
     * @param numeroFicha Número de Ficha Catastral
     */
    public Predio(String npn, String municipio, String direccion, String numeroFicha) {
        this.npn = npn != null ? npn.trim() : "";
        this.municipio = municipio != null ? municipio.trim() : "";
        this.direccion = direccion != null ? direccion.trim() : "";
        this.numeroFicha = numeroFicha != null ? numeroFicha.trim() : "";
    }

    // Getters y Setters aplicando encapsulamiento completo

    public String getNpn() {
        return npn;
    }

    public void setNpn(String npn) {
        this.npn = npn;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNumeroFicha() {
        return numeroFicha;
    }

    public void setNumeroFicha(String numeroFicha) {
        this.numeroFicha = numeroFicha;
    }

    @Override
    public String toString() {
        return "Predio{" +
                "npn='" + npn + '\'' +
                ", municipio='" + municipio + '\'' +
                ", direccion='" + direccion + '\'' +
                ", numeroFicha='" + numeroFicha + '\'' +
                '}';
    }
}
