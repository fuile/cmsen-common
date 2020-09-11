package com.cmsen.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jared.Yan (yanhuaiwen@163.com)
 */
public class TreeUtil<T> {

    public static <T extends Tree<T>> List<T> convert(List<T> tList) {
        List<T> trees = new ArrayList<>();
        for (T t : tList) {
            if (t.getParentId() != null && t.getParentId() == 0) {
                if (t.getChildren() == null) {
                    t.setChildren(new ArrayList<>());
                }
                trees.add(t);
            }
            for (T tNode : tList) {
                if (tNode.getParentId() != null && tNode.getParentId().equals(t.getId())) {
                    if (t.getChildren() == null) {
                        t.setChildren(new ArrayList<>());
                    }
                    tNode.setLevel(t.getLevel() + 1);
                    t.getChildren().add(tNode);
                }
            }
        }
        return trees;
    }

    /**
     * Tree attributes
     *
     * <br>Long id
     * <br>Long parentId
     * <br>List Children
     */
    public static class Tree<T> {
        private Long id;
        private Long parentId;
        private int level;
        private List<T> Children;

        public Long getId() {
            return id;
        }

        public Tree<T> setId(Long id) {
            this.id = id;
            return this;
        }

        public Long getParentId() {
            return parentId;
        }

        public Tree<T> setParentId(Long parentId) {
            this.parentId = parentId;
            return this;
        }

        public int getLevel() {
            return level;
        }

        public Tree<T> setLevel(int level) {
            this.level = level;
            return this;
        }

        public List<T> getChildren() {
            return Children;
        }

        public Tree<T> setChildren(List<T> children) {
            Children = children;
            return this;
        }
    }
}
