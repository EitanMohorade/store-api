package com.store.api.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;

/**
 * Entidad que representa una venta en el sistema.
 * 
 * Una venta registra la transacción de un producto vendido a un cliente, incluyendo
 * la cantidad, el precio unitario y la fecha de realización. Se relaciona con la
 * entidad Producto para mantener referencia al artículo vendido.
 * 
 * Características principales:
 * - Registro automático de fecha y hora de la venta
 * - Cálculo automático del total (cantidad × precio unitario)
 * - Relación obligatoria con Producto
 * - Cantidad y precio unitario requeridos
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

    /** Precio unitario del producto al momento de la venta. */
    private BigDecimal precioUnitario;

    /**
     * Constructor vacío.
     * Inicializa la fecha actual automáticamente.
     */
    public Venta() {
        this.fecha = LocalDateTime.now();
    }

    /**
     * Constructor con parámetros.
     * Inicializa una venta con producto, cantidad y precio unitario.
     * La fecha se establece a la hora actual.
     * 
     * @param producto Producto vendido
     * @param cantidad Cantidad de unidades vendidas
     * @param precioUnitario Precio por unidad
     */
    public Venta(Producto producto, Integer cantidad, BigDecimal precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
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

    public BigDecimal getPrecioUnitario() {return precioUnitario;}
    public void setPrecioUnitario(BigDecimal precioUnitario) {this.precioUnitario = precioUnitario;}

    /**
     * Calcula el total de la venta.
     * El total es el resultado de cantidad × precio unitario.
     * Este método no persiste en la base de datos, solo calcula el valor.
     * 
     * @return BigDecimal con el total de la venta
     */
    @Transient
    public BigDecimal getTotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}
