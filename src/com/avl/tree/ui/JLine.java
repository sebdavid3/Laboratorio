package com.avl.tree.ui;

import javax.swing.*;
import java.awt.*;

public class JLine extends JComponent {
    private boolean isLeft;
    public JLine(int xLeft, int xRight, int yTop, boolean isLeft) {
        setBounds(xLeft, yTop, xRight - xLeft, 30);
        this.isLeft = isLeft;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        if (isLeft) {
            g.drawLine(0, 29, getWidth() - 1, 0);
        } else {
            g.drawLine(0, 0, getWidth() - 1, 29);
        }
    }
}
