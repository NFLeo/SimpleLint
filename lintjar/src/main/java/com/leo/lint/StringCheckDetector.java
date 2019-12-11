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
import com.leo.tools.LintTools;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;

import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.TAG_STRING;

/**
 * desc：detect string.xml name contain uppercase</br>
 * time: 2019/10/10-11:20</br>
 * author：Leo </br>
 * since V 1.0.0 </br>
 */
public class StringCheckDetector extends ResourceXmlDetector {

    private static final String reportMSG = "文字资源名称应使用小写命名";

    public static final Issue ISSUE = Issue.create(
            "String Uppercase", "please use lowercase"
            , "please use lowercase", Category.LINT, 5, Severity.WARNING,
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
        Attr attributeNode = element.getAttributeNode(ATTR_NAME);
        if (attributeNode != null) {
            final String val = attributeNode.getValue();
            if (LintTools.isContainUpperCase(val)) {
                LintFix lintFix = LintFix.create().replace().text(val).with(val.toLowerCase()).build();
                context.report(ISSUE, attributeNode, context.getLocation(attributeNode),
                        reportMSG, lintFix);
            }
        }
    }
}