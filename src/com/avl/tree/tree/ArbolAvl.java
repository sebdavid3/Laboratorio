package com.avl.tree.tree;

import java.util.Comparator;

public class ArbolAvl<T extends Comparable<T>> {
    private Comparator<T> comparator;
    private TreeNode root;

    public ArbolAvl(Comparator<T> comparator) {
        this.comparator = comparator;
        this.root = null;
    }

    public void insert(T value) {
        if (root == null) {
            root = new TreeNode(value);
            return;
        }

        root.insert(value);
        root = root.updateHeight();
    }

    public void delete(T value) {
        if (root != null) {
            root = root.delete(value);
            root = root.updateHeight();
        }
    }

    public TreeNode getRoot() {
        return root;
    }

    public TreeNode find(T oth){
        return root.find(oth);
    }


    public int calcularNivel(TreeNode node) {
        if (root == null) {
            return 0;
        }

        return root.calcularNivel(node, 1);
    }

    public TreeNode findParent(TreeNode node) {
        if (root == null || root == node) {
            return null;
        }

        return root.findParent(node);
    }

    public class TreeNode {
        private T value;
        private TreeNode left;
        private TreeNode right;

        protected TreeNode(T val) {
            left = null;
            right = null;
            value = val;
        }

        public T getValue() {
            return value;
        }

        public TreeNode getLeft() {
            return left;
        }

        public TreeNode getRight() {
            return right;
        }

        public int getHeight() {
            int leftDeep = left == null ? 0 : left.getHeight();
            int rightDeep = right == null ? 0 : right.getHeight();
            return Math.max(leftDeep, rightDeep) + 1;
        }

        public void insert(T newValue) {
            if (comparator.compare(value, newValue) > 0) {
                if (left == null) {
                    left = new TreeNode(newValue);
                } else {
                    left.insert(newValue);
                }
            } else {
                if (right == null) {
                    right = new TreeNode(newValue);
                } else {
                    right.insert(newValue);
                }
            }
        }

        public TreeNode delete(T value) {
            int resultadoComparacion = comparator.compare(this.value, value);
            if (resultadoComparacion == 0) {
                if (left == null) {
                    return right;
                } else if (right == null) {
                    return left;
                }

                if (left.right == null) {
                    left.right = right;
                    return left;
                }

                TreeNode padreMayorIzq = left, mayorIzq = left.right;
                while (mayorIzq.right != null) {
                    padreMayorIzq = mayorIzq;
                    mayorIzq = padreMayorIzq.right;
                }

                this.value = mayorIzq.value;
                padreMayorIzq.right = mayorIzq.left;

            } else if (resultadoComparacion < 0 && right != null) {
                right = right.delete(value);
            } else if (resultadoComparacion > 0 && left != null) {
                left = left.delete(value);
            }

            return this;
        }

        protected TreeNode find(T value){
            int resultadoComparacion = comparator.compare(this.value, value);
            if (resultadoComparacion ==0){
                return this;
            }
            if (resultadoComparacion < 0 && right != null){
                return right.find(value);
            }
            if (resultadoComparacion > 0 && left != null){
                return left.find(value);
            }

            return null;
        }

        private TreeNode updateHeight() {
            int factor = 0;
            if (left != null) {
                left = left.updateHeight();
                factor -= left.getHeight();
            }

            if (right != null) {
                right = right.updateHeight();
                factor += right.getHeight();
            }

            if (Math.abs(factor) < 2) {
                return this;
            }

            return factor > 0 ? this.equilibrateRightSide() : this.equilibrateLeftSide();
        }

        private TreeNode equilibrateRightSide() {
            TreeNode _aux = right;
            if (_aux.left == null || _aux.right == null) {
                return this.rotateLeftNull(_aux);
            }

            if (_aux.right.getHeight() < _aux.left.getHeight()) {
                // Doble rotacion
                TreeNode auxRight = _aux.right;
                _aux.right = null;
                _aux = _aux.rotateRightNull(_aux.left);
                _aux.right.right = auxRight;
            }

            this.right = _aux.left;
            _aux.left = this;
            return _aux;
        }

        private TreeNode equilibrateLeftSide() {
            TreeNode _aux = left;
            if (_aux.left == null || _aux.right == null) {
                return rotateRightNull(_aux);
            }

            if (_aux.right.getHeight() > _aux.left.getHeight()) {
                // Doble rotacion
                TreeNode auxLeft = _aux.left;
                _aux.left = null;
                _aux = _aux.rotateLeftNull(_aux.right);
                _aux.left.left = auxLeft;
            }

            left = _aux.right;
            _aux.right = this;
            return _aux;
        }

        private TreeNode rotateLeftNull(TreeNode _aux) {
            if (_aux.left == null) {
                // Caso 1: no hay hijo izquierdo (derecho - derecho)
                this.right = null;
                _aux.left = this;
                return _aux;
            }

            // Caso 2: el hijo es izquierdo (derecho - izquierdo)
            TreeNode newRoot = _aux.left;
            this.right = null;
            _aux.left = null;
            newRoot.left = this;
            newRoot.right = _aux;
            return newRoot;
        }

        private TreeNode rotateRightNull(TreeNode _aux) {
            if (_aux.right == null) {
                // Caso 1 no hay hijo derecho (solo izquierdo - izquierdo)
                this.left = null;
                _aux.right = this;
                return _aux;
            }

            // Caso 2: el hijo es derecho (izquierdo - derecho)
            TreeNode newRoot =  _aux.right;
            this.left = null;
            _aux.right = null;
            newRoot.right = this;
            newRoot.left = _aux;
            return newRoot;
        }

        protected int calcularNivel(TreeNode node, int nivel) {
            if (this == node) { return nivel; };

            if (value.compareTo(node.value) > 0) {
                return left.calcularNivel(node, nivel + 1);
            }

            return right.calcularNivel(node, nivel + 1);
        }

        protected TreeNode findParent(TreeNode node) {
            if (left == node || right == node) return this;

            if (value.compareTo(node.value) > 0) {
                return left.findParent(node);
            }

            return right.findParent(node);
        }

        public int getFactorBalanceo() {
            int factor = 0;
            if (left != null) {
                factor -= left.getHeight();
            }

            if (right != null) {
                factor += right.getHeight();
            }

            return factor;
        }
    }
}
