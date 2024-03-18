package com.avl.tree.data;

public class Imagen implements Comparable<Imagen>{
    private String tipo;
    private String nombre;
    private long peso;

    public Imagen(String tipo, String nombre, long peso) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.peso= peso;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public long getPeso() {
        return peso;
    }

    @Override
    public int compareTo(Imagen o) {
        if(o == null) {
            return 1;
        }

        return this.nombre.compareTo(o.nombre);
    }
}