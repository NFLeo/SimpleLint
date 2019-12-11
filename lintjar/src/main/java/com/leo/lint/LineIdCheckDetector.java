package com.leo.lint;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector.XmlScanner;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintFix;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;
import com.leo.tools.LintTools;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;

import java.util.Collection;
import java.util.Collections;

import static com.android.SdkConstants.VALUE_ID;

/**
 * desc：detect lint layoutId must start with "view" in xml</br>
 * time: 2019/10/10-11:22</br>
 * author：Leo </br>
 * since V 1.0.0 </br>
 */
public class LineIdCheckDetector extends ResourceXmlDetector implements XmlScanner {

    private static final String reportMSG = "分割线布局控件Id需view开头，且只含小写字母和下划线";

    public static final Issue ISSUE = Issue.create(
            "LineIdNamedInXml", "Line Id Named",
            "Line Id named with lowercase",
            Category.LINT, 5, Severity.WARNING,
            new Implementation(LineIdCheckDetector.class, Scope.RESOURCE_FILE_SCOPE)
    );

    @Override
    public boolean appliesTo(@NotNull ResourceFolderType folderType) {
        return folderType == ResourceFolderType.LAYOUT;
    }

    @Nullable
    @Override
    public Collection<String> getApplicableAttributes() {
        return Collections.singletonList(VALUE_ID);
    }

    @Override
    public void visitAttribute(@NotNull XmlContext context, @NotNull Attr attribute) {
        String prnMain = context.getMainProject().getDir().getPath();
        String prnCur = context.getProject().getDir().getPath();

        if (!prnMain.equals(prnCur)) {
            return;
        }

        // @+id/  substring(5)
        String idName = attribute.getValue().substring(5);
        if (LintTools.isContainUpperCase(idName)
                || !LintTools.startWithChar(idName)
                || !idName.startsWith("view")) {

            String result = "view_" + idName.toLowerCase();
            LintFix lintFix = LintFix.create().replace().text(idName).with(result).build();
            context.report(ISSUE, attribute, context.getLocation(attribute), reportMSG, lintFix);
        }
    }
}
