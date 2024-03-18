package com.avl.tree.ui;

import com.avl.tree.data.Imagen;
import com.avl.tree.tree.ArbolAvl;

import javax.swing.*;
import java.awt.*;

public class ArbolAvlNode extends JLabel {
    private static final String FMT = "<html><b>%s</b><br/><span style =\"color:red;\">%s</span><br/>%d</html>";
    private ArbolAvl<Imagen>.TreeNode node;
    private boolean selected;

    public ArbolAvlNode(ArbolAvl<Imagen>.TreeNode node, int xPos, int yPos, int width) {
        super("", SwingConstants.CENTER);
        this.setBounds(xPos - (width / 2), yPos, width, 50);
        this.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1, true));
        this.node = node;
        this.selected = false;
        Imagen imagen = node.getValue();
        this.setText(String.format(FMT, imagen.getNombre(), imagen.getTipo(), imagen.getPeso()));
    }

    public void setSelected(boolean value) {
        this.selected = value;
        if (selected) {
            this.setBorder(BorderFactory.createLineBorder(Color.RED, 3, true));
        } else {
            this.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1, true));
        }
    }

    public boolean isNode(ArbolAvl<Imagen>.TreeNode node) {
        return this.node == node;
    }
}
