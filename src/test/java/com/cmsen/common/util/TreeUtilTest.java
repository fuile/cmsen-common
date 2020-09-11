package com.cmsen.common.util;

import java.util.ArrayList;
import java.util.List;

public class TreeUtilTest {
    public void tree() {
        List<MyEntity> trees = new ArrayList<>();
        trees.add(new MyEntity() {{
            setLabel("一级 1");
            setId(1L);
            setParentId(0L);

        }});
        trees.add(new MyEntity() {{
            setLabel("一级 1-1");
            setId(2L);
            setParentId(1L);

        }});
        trees.add(new MyEntity() {{
            setLabel("一级 2");
            setId(3L);
            setParentId(0L);

        }});
        List<MyEntity> convert = TreeUtil.convert(trees);
        System.err.println(JsonUtil.toString(convert));
    }

    public static class MyEntity extends TreeUtil.Tree<MyEntity> {
        private String label;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}