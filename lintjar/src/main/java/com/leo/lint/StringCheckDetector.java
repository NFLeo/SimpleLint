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

import java.util.Collection;
import java.util.Collections;

import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.TAG_STRING;

/**
 * desc：检测string.xml文件name是否包含大写</br>
 * time: 2019/10/10-11:20</br>
 * author：Leo </br>
 * since V 1.0.0 </br>
 */
public class StringCheckDetector extends ResourceXmlDetector {

    public static final Issue ISSUE = Issue.create(
            "String Uppercase", "please use lowercase"
            , "please use lowercase", Category.LINT, 5, Severity.ERROR,
            new Implementation(StringCheckDetector.class, Scope.RESOURCE_FILE_SCOPE));

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return ResourceFolderType.VALUES == folderType;
    }

    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singletonList(TAG_STRING);
    }

    @Override
    public void visitElement(XmlContext context, Element element) {

        final Attr attributeNode = element.getAttributeNode(ATTR_NAME);
        if (attributeNode != null) {
            final String val = attributeNode.getValue();
            if (isContainUpperCase(val)) {
                LintFix lintFix = LintFix.create().replace().text(val).with(val.toLowerCase()).build();
                context.report(ISSUE, attributeNode, context.getLocation(attributeNode),
                        "please use lowercase", lintFix);
            }
        }
    }

    private boolean isContainUpperCase(String value) {
        if (value == null || value.length() == 0) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (Character.isUpperCase(value.charAt(i))) {
                return true;
            }
        }

        return false;
    }
}