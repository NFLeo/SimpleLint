package com.leo.tools;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LintTools {

    /**
     * 判断字符串中是否包含大写
     *
     * @param value target value
     * @return true=包含大写
     */
    public static boolean isContainUpperCase(String value) {
        if (isEmptyString(value)) {
            return false;
        }

        for (int i = 0; i < value.length(); i++) {
            if (Character.isUpperCase(value.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    /**
     * 以小写字母开头
     *
     * @param value target value
     * @return true=以小写字母开头
     */
    public static boolean startWithChar(String value) {
        return !isEmptyString(value) && Character.isLowerCase(value.charAt(0));
    }

    private static boolean isEmptyString(String value) {
        return value == null || value.length() == 0;
    }

    /**
     * 获取元素
     * @param element
     * @return
     */
    /**
     * 获取元素指定节点下Node的value值
     * @param element       布局节点元素（如TextView）
     * @param nodeIndex
     * @return 指定节点下Node的value值
     */
    public static String getElementNodeValue(Element element, int nodeIndex) {
        // todo
        NodeList childNodes = element.getChildNodes();
        if (childNodes == null || childNodes.getLength() == 0) {
            return "";
        }

        Node child = childNodes.item(nodeIndex);
        if (child == null || child.getNodeValue() == null) {
            return "";
        }

        String nodeValue = child.getNodeValue();
        return isEmptyString(nodeValue) ? "" : nodeValue;
    }
}
