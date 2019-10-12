package com.leo.lint;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintFix;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Collection;
import java.util.Collections;

import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.TAG_COLOR;

/**
 * desc：Color.xml named format with c_xxxxxx</br>
 * time: 2019/10/11-10:29</br>
 * author：Leo </br>
 * since V  1.0.0 </br>
 */
public class ColorCheckDetector extends ResourceXmlDetector {

    public static final Issue ISSUE = Issue.create(
            "Color c_xxxxxx", "named format with c_xxxxxx"
            , "named format with c_xxxxxx"
            , Category.LINT, 5, Severity.WARNING
            , new Implementation(ColorCheckDetector.class, Scope.RESOURCE_FILE_SCOPE));

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return ResourceFolderType.VALUES == folderType;
    }

    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singletonList(TAG_COLOR);
    }

    @Override
    public void visitElement(XmlContext context, Element element) {

        final Attr attributeNode = element.getAttributeNode(ATTR_NAME);

        if (attributeNode != null) {
            String val = attributeNode.getValue();
            if (val != null && (!val.matches("c_[A-Fa-f0-9]{6,8}"))) {
                LintFix lintFix = LintFix.create().replace()
                        .text(val).with(getReplaceValue(element)).build();
                context.report(ISSUE, attributeNode, context.getLocation(attributeNode),
                        "named format with c_xxxxxx", lintFix);
            }
        }
    }

    private String getReplaceValue(Element element) {
        NodeList childNodes = element.getChildNodes();
        if (childNodes == null || childNodes.getLength() == 0) {
            return "";
        }

        Node child = childNodes.item(0);
        if (child == null || child.getNodeValue() == null) {
            return "";
        }

        return child.getNodeValue().replace("#", "");
    }
}