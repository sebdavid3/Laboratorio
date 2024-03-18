package com.avl.tree.ui;

import javax.swing.*;

public class ListaRecorridoNiveles extends JDialog {
    private JList<String> list;

    public ListaRecorridoNiveles(java.util.List<String> lista) {
        super();
        setTitle("Recorrido por niveles");
        setSize(200, 500);
        JScrollPane scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().add(list = new JList<>());
        getContentPane().add(scrollPane);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        list.setListData(lista.toArray(new String[0]));
    }
}
