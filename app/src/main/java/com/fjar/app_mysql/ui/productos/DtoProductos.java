package com.fjar.app_mysql.ui.productos;

public class DtoProductos {
    private int idProducto;
    private String nombreProducto;
    private String descProducto;
    private float stock;
    private float precio;
    private String unidadMedida;
    private int estadoProducto;
    private int categoria;
    private String fechaEntrada;

    public DtoProductos() {
    }

    public DtoProductos(int idProducto, String nombreProducto, String descProducto, float stock, float precio, String unidadMedida, int estadoProducto, int categoria, String fechaEntrada) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.descProducto = descProducto;
        this.stock = stock;
        this.precio = precio;
        this.unidadMedida = unidadMedida;
        this.estadoProducto = estadoProducto;
        this.categoria = categoria;
        this.fechaEntrada = fechaEntrada;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescProducto() {
        return descProducto;
    }

    public void setDescProducto(String descProducto) {
        this.descProducto = descProducto;
    }

    public float getStock() {
        return stock;
    }

    public void setStock(float stock) {
        this.stock = stock;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public int getEstadoProducto() {
        return estadoProducto;
    }

    public void setEstadoProducto(int estadoProducto) {
        this.estadoProducto = estadoProducto;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }
}
