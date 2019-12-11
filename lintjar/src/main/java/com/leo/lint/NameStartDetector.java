package com.leo.lint;

import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintFix;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;

import java.util.Arrays;
import java.util.List;

/**
 * desc：Detect class named start with Contract、Activity、Fragment </br>
 * time: 2019/10/11-14:08</br>
 * author：Leo </br>
 * since V 1.0.0 </br>
 */
public class NameStartDetector extends Detector implements Detector.UastScanner {

    private static final String reportMSG = "Activity、Fragment、DialogFragment类" +
            "应该以Activity、Fragment、DialogFragment开头命名";

    public static final Issue ISSUE = Issue.create(
            "NameStartDetect", "NameStartDetect",
            "Class name should start with Activity、Fragment...",
            Category.LINT, 5, Severity.WARNING,
            new Implementation(NameStartDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    @Nullable
    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return super.getApplicableUastTypes();
    }

    @Nullable
    @Override
    public List<String> applicableSuperClasses() {
        return returnSuperClasses();
    }

    @Override
    public void visitClass(@NotNull JavaContext context, @NotNull UClass declaration) {
        String name = declaration.getName();
        if (!name.startsWith("Activity") && !name.startsWith("Fragment")
                && !name.startsWith("DialogFragment")) {
            LintFix lintFix = LintFix.create().replace().text(name)
                    .with(replaceName(declaration, name)).build();
            context.report(ISSUE, declaration, context.getNameLocation(declaration)
                    , reportMSG, lintFix);
        }
    }

    private String replaceName(UClass declaration, String className) {
        String superClassName = declaration.getSuperClass().getName();
        String headName = "";
        if (superClassName.contains("Activity")) {
            headName = "Activity";
        } else if (superClassName.contains("DialogFragment")) {
            headName = "DialogFragment";
        } else if (superClassName.contains("Fragment")) {
            headName = "Fragment";
        }

        return headName + className.replace(headName, "");
    }

    private List<String> returnSuperClasses() {
        return Arrays.asList(
                "androidx.fragment.app.Fragment"
                , "androidx.fragment.app.DialogFragment"
                , "androidx.appcompat.app.AppCompatActivity");
    }
}
