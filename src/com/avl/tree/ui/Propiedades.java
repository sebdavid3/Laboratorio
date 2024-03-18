package com.avl.tree.ui;

import com.avl.tree.data.Imagen;
import com.avl.tree.tree.ArbolAvl;

import javax.swing.*;

public class Propiedades extends JDialog {
    private String[] columnNames = { "Propiedad", "Valor" };
    private JTable table;

    public Propiedades(ArbolAvl<Imagen> arbol, ArbolAvl<Imagen>.TreeNode node) {
        super();
        Imagen imagen = node.getValue();
        setTitle("Propiedades de " + imagen.getNombre());
        setSize(500, 500);
        JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        ArbolAvl<Imagen>.TreeNode padre = arbol.findParent(node);
        ArbolAvl<Imagen>.TreeNode abuelo = padre != null ? arbol.findParent(padre) : null;
        ArbolAvl<Imagen>.TreeNode tio = abuelo != null ? (abuelo.getLeft() == padre ? abuelo.getRight() : abuelo.getLeft()) : null;
        int nivel = arbol.calcularNivel(node);
        int factor = node.getFactorBalanceo();

        Object[][] datos = {
                { "Nivel", nivel },
                { "Factor", factor },
                { "Padre", padre == null ? "No tiene" : padre.getValue().getNombre() },
                { "Abuelo", abuelo == null ? "No tiene" : abuelo.getValue().getNombre() },
                { "Tio", tio == null ? "No tiene" : tio.getValue().getNombre() }
        };
        scrollPane.getViewport().add(table = new JTable(datos, columnNames));
        getContentPane().add(scrollPane);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}
