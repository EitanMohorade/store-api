package com.store.api.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;

/**
 * Entidad que representa una venta en el sistema.
 * 
 * Una venta registra la transacción de un producto vendido a un cliente, incluyendo
 * la cantidad y la fecha de realización. Se relaciona con la entidad Producto para
 * obtener el precio unitario al momento de la venta.
 * 
 * Características principales:
 * - Registro automático de fecha y hora de la venta
 * - Cálculo automático del total (cantidad × precio del producto)
 * - Relación obligatoria con Producto
 * - Obtiene el precio unitario del Producto asociado
 * 
 */
@Entity
public class Venta {
    
    /** Identificador único de la venta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Fecha y hora en que se realizó la venta. Por defecto es la fecha actual. */
    @Column(nullable = false)
    private LocalDateTime fecha;

    /** Producto que fue vendido. Relación obligatoria. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    /** Cantidad de unidades vendidas del producto. */
    @Column(nullable = false)
    private Integer cantidad;

    /**
     * Constructor vacío.
     * Inicializa la fecha actual automáticamente.
     */
    public Venta() {
        this.fecha = LocalDateTime.now();
    }

    /**
     * Constructor con parámetros.
     * Inicializa una venta con producto y cantidad.
     * La fecha se establece a la hora actual.
     * El precio se obtiene del Producto asociado.
     * 
     * @param producto Producto vendido
     * @param cantidad Cantidad de unidades vendidas
     */
    public Venta(Producto producto, Integer cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() {return id;}
    public void setId(Long id) { this.id = id; }


    public LocalDateTime getFecha() {return fecha;}
    public void setFecha(LocalDateTime fecha) {this.fecha = fecha;}

    public Producto getProducto() {return producto;}
    public void setProducto(Producto producto) {this.producto = producto;}

    public Integer getCantidad() {return cantidad;}
    public void setCantidad(Integer cantidad) {this.cantidad = cantidad;}

    /**
     * Obtiene el precio unitario del producto asociado a la venta.
     * Este es un método calculado que no persiste en la base de datos.
     * 
     * @return Integer con el precio unitario del producto
     */
    @Transient
    public Integer getPrecioUnitario() {
        return producto != null ? producto.getPrecioUnitario() : 0;
    }

    /**
     * Obtiene el precio del producto asociado a la venta.
     * Este es un método calculado que no persiste en la base de datos.
     * 
     * @return Integer con el precio unitario del producto
     */
    @Transient
    public Integer getPrecio() {
        return producto != null ? producto.getPrecio() : 0;
    }


    /**
     * Calcula el total de la venta.
     * El total es el resultado de cantidad × precio unitario del producto.
     * Este método no persiste en la base de datos, solo calcula el valor.
     * 
     * @return Integer con el total de la venta del precio unitario
     */
    @Transient
    public Integer getTotalPrecioUnitario() {
        return getTotalPrecioUnitario() * cantidad;
    }

    /**
     * Calcula el total de la venta.
     * El total es el resultado de cantidad × precio del producto.
     * Este método no persiste en la base de datos, solo calcula el valor.
     * 
     * @return Integer con el total de la venta
     */
    @Transient
    public Integer getTotalPrecioCliente() {
        return getTotalPrecioCliente() * cantidad;
    }
}
