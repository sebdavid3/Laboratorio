package com.avl.tree.ui;

import com.avl.tree.data.Imagen;
import com.avl.tree.tree.ArbolAvl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class ArbolAvlFrame extends JFrame implements ActionListener {
    private ArbolAvl<Imagen> arbolAvl;
   // private JPanel drawPanel;
    private JScrollPane scrollPane;

    private JPanel drawPanel;

    private JMenuBar menuBar;

    public ArbolAvlFrame() {
        super("Ejemplo Arbol AVL");
        menuBar = createMainMenuBar();
        arbolAvl = new ArbolAvl<>(Imagen::compareTo);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setResizable(true);
        drawPanel = new JPanel(null);
        drawPanel.setBackground(Color.WHITE);
        scrollPane = new JScrollPane(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().add(drawPanel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        setJMenuBar(menuBar);

        cargarArbol();
        this.setVisible(true);
        refreshNodes();
    }

    private void cargarArbol() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione Carpeta del DataSet");
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fileChooser.showDialog(this,"Abrir") == JFileChooser.APPROVE_OPTION){
            File carpeta = fileChooser.getSelectedFile();
            File[] tipos = carpeta.listFiles(File::isDirectory);
            if (tipos == null || tipos.length== 0){
                JOptionPane.showMessageDialog(this,"No se encontraron carpetas para los tipos");
                return;
            }
            for (File tipo: tipos){
                File[] archivos = tipo.listFiles();
                if (archivos != null){
                    for (File archivo: archivos) {
                        if (!archivo.isDirectory()){
                            Imagen imagen = new Imagen(tipo.getName(), archivo.getName(), archivo.length());
                            arbolAvl.insert(imagen);
                        }
                    }
                }
            }
        }
    }

    private void deseleccionarTodos() {
        Arrays.stream(drawPanel.getComponents()).filter(comp -> comp instanceof ArbolAvlNode)
                .forEach(comp -> { ((ArbolAvlNode) comp).setSelected(false);});
    }

    private void seleccionarNodo(final ArbolAvl<Imagen>.TreeNode nodo) {
        var label = Arrays.stream(drawPanel.getComponents()).filter(comp -> (comp instanceof ArbolAvlNode) && ((ArbolAvlNode)comp).isNode(nodo)).findFirst();
        if (label.isPresent()) {
            ArbolAvlNode labelNode = (ArbolAvlNode) label.get();
            labelNode.setSelected(true);
            scrollPane.getViewport().setViewPosition(labelNode.getLocation());
        }
    }
    private List<String> recorridoPorNiveles() {
        List<String> recorrido = new ArrayList<>();
        ArbolAvl<Imagen>.TreeNode raiz = arbolAvl.getRoot();
        int niveles = raiz.getHeight();
        for (int i = niveles; i > 0; i--) {
            recorridoPorNiveles(raiz, 1, i, recorrido);
        }

        return recorrido;
    }

    private void recorridoPorNiveles(ArbolAvl<Imagen>.TreeNode nodo, int nivel, int curNivel, List<String> lista) {
        if (nivel == curNivel) {
            Imagen imagen = nodo.getValue();
            lista.add(imagen.getNombre());
            return;
        }

        if (nodo.getLeft() != null) {
            recorridoPorNiveles(nodo.getLeft(), nivel + 1, curNivel, lista);
        }

        if (nodo.getRight() != null) {
            recorridoPorNiveles(nodo.getRight(), nivel + 1, curNivel, lista);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("ADD_ONE".equals(e.getActionCommand())) {
            String tipo = JOptionPane.showInputDialog("Ingrese tipo para el nuevo nodo:");

            if (tipo == null || tipo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No ingreso tipo. Operacion cancelada");
                return;
            }
            String nombre = JOptionPane.showInputDialog("Ingrese nombre del nuevo archivo:");

            if (nombre == null || nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No ingreso nombre. Operacion cancelada");
                return;
            }

            String pesoString = JOptionPane.showInputDialog("Ingrese peso del nuevo archivo:");
            long peso = 0;
            try {
                peso = Long.parseLong(pesoString);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Peso no es un numero. Operacion cancelada");
                return;
            }

            Imagen imagen = new Imagen(tipo.trim(), nombre.trim(), peso);
            arbolAvl.insert(imagen);
            refreshNodes();
            ArbolAvl<Imagen>.TreeNode nodo = arbolAvl.find(new Imagen("",nombre, 0));
            deseleccionarTodos();
            seleccionarNodo(nodo);
        }

        if ("DELETE_ONE".equals(e.getActionCommand())) {
            String nombre = JOptionPane.showInputDialog("Digite nombre del nodo a eliminar");

            ArbolAvl<Imagen>.TreeNode nodo = arbolAvl.find(new Imagen("",nombre, 0));
            if (nodo == null) {
                JOptionPane.showMessageDialog(this, "No se encontró con nombre : "+ nombre);
                return;
            }

            arbolAvl.delete(nodo.getValue());
            refreshNodes();
        }

        if ("FIND_BY_NAME".equals(e.getActionCommand())) {

            String nombre = JOptionPane.showInputDialog("Digite nombre del nodo a buscar");

            ArbolAvl<Imagen>.TreeNode nodo = arbolAvl.find(new Imagen("",nombre, 0));
            if (nodo == null) {
                JOptionPane.showMessageDialog(this, "No se encontró con nombre : "+ nombre);
                return;
            }

            deseleccionarTodos();
            seleccionarNodo(nodo);
        }

        if ("FIND_BY_CRIT".equals(e.getActionCommand())) {

            String tipo = JOptionPane.showInputDialog("Ingrese tipo para la busqueda:");

            if (tipo == null || tipo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "No ingreso tipo. Operacion cancelada");
                return;
            }

            String pesoString = JOptionPane.showInputDialog("Ingrese peso minimo para la busqueda:");
            long pesoMin = 0;
            try {
                pesoMin  = Long.parseLong(pesoString);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Peso no es un numero. Operacion cancelada");
                return;
            }

            pesoString = JOptionPane.showInputDialog("Ingrese peso maximo para la busqueda:");
            long pesoMax = 0;
            try {
                pesoMax  = Long.parseLong(pesoString);
            } catch (Exception exception) {
                JOptionPane.showMessageDialog(this, "Peso no es un numero. Operacion cancelada");
                return;
            }

            deseleccionarTodos();
            boolean encontrado = buscarNodos(tipo, pesoMin, pesoMax);
            if (!encontrado) {
                JOptionPane.showMessageDialog(this, "No se encontraron nodos con ese criterio");
            }
        }

        if ("REC_NIVEL".equals(e.getActionCommand())) {
            var lista = recorridoPorNiveles();
            var dialog = new ListaRecorridoNiveles(lista);
            dialog.setModal(true);
            dialog.setVisible(true);
        }
    }

    private boolean buscarNodos(String tipo, long pesoMin, long pesoMax) {
        if (arbolAvl.getRoot() != null) {
            return buscarNodos(arbolAvl.getRoot(), tipo, pesoMin, pesoMax);
        }

        return false;
    }

    public boolean buscarNodos(
                            ArbolAvl<Imagen>.TreeNode nodo,
                            String tipo, long pesoMin, long pesoMax) {
        boolean encontrado = false;
        if (nodo.getLeft() != null) {
            encontrado = buscarNodos(nodo.getLeft(), tipo, pesoMin, pesoMax);
        }

        if (nodo.getRight() != null) {
            encontrado |= buscarNodos(nodo.getRight(), tipo, pesoMin, pesoMax);
        }

        Imagen imagen = nodo.getValue();
        if (tipo.equals(imagen.getTipo()) &&
                pesoMin <= imagen.getPeso() &&
                imagen.getPeso() < pesoMax) {
            seleccionarNodo(nodo);
            encontrado = true;
        }

        return encontrado;
    }

    public void refreshNodes() {
        drawPanel.removeAll();
        var rootNode = arbolAvl.getRoot();
        if (rootNode == null) {
            drawPanel.setSize(100, 100);
            return;
        }

        var fontMetrics = this.getFontMetrics(this.getFont());
        int nodeWidth = calculateMaxNodeWidth(rootNode, fontMetrics, this.getGraphics());
        int height = rootNode.getHeight() * 80;
        int maxNodesInLine = ((Double)Math.pow(2, rootNode.getHeight() - 1)).intValue();
        int width = maxNodesInLine * (nodeWidth + 30);
        int rootXCenter = width / 2;
        addNode(rootNode, rootXCenter, 15, nodeWidth, width / 4);

        drawPanel.setMinimumSize(new Dimension(width, height));
        drawPanel.setPreferredSize(drawPanel.getMinimumSize());

        synchronized (scrollPane.getTreeLock()) {
            scrollPane.updateUI();
        }
    }

    private int calculateMaxNodeWidth(ArbolAvl<Imagen>.TreeNode node, FontMetrics metrics, Graphics context) {
        int width = ((Double)metrics.getStringBounds(node.getValue().getNombre(), context).getWidth()).intValue() + 10;
        if (node.getLeft() != null) {
            width = Math.max(width, calculateMaxNodeWidth(node.getLeft(), metrics, context));
        }

        if (node.getRight() != null) {
            width = Math.max(width, calculateMaxNodeWidth(node.getRight(), metrics, context));
        }

        return width;
    }

    private void addNode(ArbolAvl<Imagen>.TreeNode node, int xCenter, int yPos, int width, int childSep) {
        Imagen imagen = node.getValue();
        if (node.getLeft() != null) {
            drawPanel.add(new JLine(xCenter - childSep, xCenter, yPos + 50, true));
            addNode(node.getLeft(), xCenter - childSep, yPos + 80, width, childSep / 2);
        }

        if (node.getRight() != null) {
            drawPanel.add(new JLine(xCenter, xCenter + childSep, yPos + 50, false));
            addNode(node.getRight(), xCenter + childSep, yPos + 80, width, childSep / 2);
        }

        ArbolAvlNode comp = new ArbolAvlNode(node, xCenter, yPos, width);
        comp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                var props = new Propiedades(arbolAvl, node);
                props.setModal(true);
                props.setVisible(true);
            }
        });
        drawPanel.add(comp);
    }

    private JMenuBar createMainMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createMainMenu());
        return menuBar;
    }

    private JMenu createMainMenu() {
        JMenu menu = new JMenu("Principal");
        JMenuItem addOneMenu = new JMenuItem(" 1. Insertar nodo");
        addOneMenu.setActionCommand("ADD_ONE");
        addOneMenu.addActionListener(this);
        menu.add(addOneMenu);

        JMenuItem deleteOneMenu = new JMenuItem("2. Eliminar nodo por nombre");
        deleteOneMenu.setActionCommand("DELETE_ONE");
        deleteOneMenu.addActionListener(this);
        menu.add(deleteOneMenu);

        JMenuItem addMultiMenu = new JMenuItem("3. Buscar nodo por nombre");
        addMultiMenu.setActionCommand("FIND_BY_NAME");
        addMultiMenu.addActionListener(this);
        menu.add(addMultiMenu);

        JMenuItem findWeird = new JMenuItem("4. Buscar por criterio");
        findWeird.setActionCommand("FIND_BY_CRIT");
        findWeird.addActionListener(this);
        menu.add(findWeird);

        JMenuItem recorridoMenu = new JMenuItem("5. Recorrido por Niveles");
        recorridoMenu.setActionCommand("REC_NIVEL");
        recorridoMenu.addActionListener(this);
        menu.add(recorridoMenu);

        return menu;
    }
}
