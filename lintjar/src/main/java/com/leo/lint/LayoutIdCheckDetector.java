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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;

import java.util.Collection;
import java.util.Collections;

import static com.android.SdkConstants.VALUE_ID;

/**
 * desc：detect layoutId is contain uppercase char in xml</br>
 * time: 2019/10/10-11:22</br>
 * author：Leo </br>
 * since V 1.0.0 </br>
 */
public class LayoutIdCheckDetector extends ResourceXmlDetector implements XmlScanner {

    public static final Issue ISSUE = Issue.create(
            "IdNamedInXml", "Id Named",
            "id named with lowercase",
            Category.LINT, 5, Severity.ERROR,
            new Implementation(LayoutIdCheckDetector.class, Scope.RESOURCE_FILE_SCOPE)
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
        if (isContainUpperCase(idName)) {
            LintFix lintFix = LintFix.create().replace().text(idName)
                    .with(idName.toLowerCase()).build();
            context.report(ISSUE, attribute, context.getLocation(attribute),
                    "id named with lowercase", lintFix);
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
