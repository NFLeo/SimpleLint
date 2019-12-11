package com.leo.lint;

import com.android.tools.lint.client.api.UElementHandler;
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
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UElement;

import java.util.Collections;
import java.util.List;

/**
 * desc：detect contract named error</br>
 * time: 2019/10/12-17:26</br>
 * author：Leo </br>
 * since V 1.0.0 </br>
 */
public class ContractStartDetector extends Detector implements Detector.UastScanner {

    private static final String reportMSG = "把Contact修改成Contract";

    public static final Issue ISSUE = Issue.create(
            "ContractNameStartDetect", "ContractNameStartDetect",
            "Contract Class name should start with Contact",
            Category.LINT, 5, Severity.ERROR,
            new Implementation(ContractStartDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    @Nullable
    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.singletonList(UClass.class);
    }

    @Nullable
    @Override
    public UElementHandler createUastHandler(@NotNull JavaContext context) {
        return new UElementHandler() {
            @Override
            public void visitClass(@NotNull UClass node) {
                if (node.getName().contains("Contact")) {
                    LintFix lintFix = LintFix.create().replace().text(node.getName())
                            .with("Contract").build();
                    context.report(ISSUE, node, context.getNameLocation(node), reportMSG, lintFix);
                }
            }
        };
    }
}
