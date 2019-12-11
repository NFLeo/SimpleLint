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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_GRAVITY;
import static com.android.SdkConstants.ATTR_TEXT_ALIGNMENT;
import static com.android.SdkConstants.TEXT_VIEW;
import static com.android.SdkConstants.TextAlignment.VIEW_END;

/**
 * desc：detect text gravity and textAlignment</br>
 * time: 2019/10/10-11:22</br>
 * author：Leo </br>
 * since V 1.0.1 </br>
 */
public class ArTextStringGravityCheckDetector extends ResourceXmlDetector {

    private static final String reportAlignment = "阿拉伯/波斯语 TextView 控件需设置textAlignment属性";
    private static final String reportRight = "阿拉伯/波斯语 需将right替换成end ";

    public static final Issue ISSUE = Issue.create(
            "Ar text container settings", "Ar text should setting text direction",
            "Ar text should setting text direction",
            Category.LINT, 5, Severity.WARNING,
            new Implementation(ArTextStringGravityCheckDetector.class, Scope.RESOURCE_FILE_SCOPE)
    );

    @Override
    public boolean appliesTo(@NotNull ResourceFolderType folderType) {
        return folderType == ResourceFolderType.LAYOUT;
    }

    @Nullable
    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singletonList(TEXT_VIEW);
    }

    @Override
    public void visitElement(@NotNull XmlContext context, @NotNull Element element) {

        // 存在gravity属性，则以属性值为准，否则默认gravity值为start
        if (element.hasAttributeNS(ANDROID_URI, ATTR_GRAVITY)) {
            Attr gravityAttr = element.getAttributeNodeNS(ANDROID_URI, ATTR_GRAVITY);
            if (gravityAttr == null) return;
            if (gravityAttr.getValue().contains("end")) {
                Attr textAlignment = element.getAttributeNodeNS(ANDROID_URI, ATTR_TEXT_ALIGNMENT);
                if (textAlignment != null) return;
                LintFix lintFix = LintFix.create().set(ANDROID_URI, ATTR_TEXT_ALIGNMENT, VIEW_END).build();
                context.report(ISSUE, element, context.getNameLocation(element), reportAlignment, lintFix);
            } else if (gravityAttr.getValue().contains("right")) {
                LintFix lintFix = LintFix.create().replace().text("right").with("end").build();
                context.report(ISSUE, gravityAttr, context.getLocation(gravityAttr), reportRight, lintFix);
                context.report(ISSUE, element, context.getNameLocation(element), reportAlignment);
            }
        }
    }
}
