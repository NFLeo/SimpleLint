package com.leo.lint;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Lint;
import com.android.tools.lint.detector.api.LintFix;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiMethod;

import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UTryExpression;
import org.jetbrains.uast.UastUtils;
import org.jetbrains.uast.util.UastExpressionUtils;

import java.util.Collections;
import java.util.List;

import static com.android.tools.lint.client.api.JavaEvaluatorKt.TYPE_DOUBLE_WRAPPER;
import static com.android.tools.lint.client.api.JavaEvaluatorKt.TYPE_FLOAT_WRAPPER;
import static com.android.tools.lint.client.api.JavaEvaluatorKt.TYPE_INTEGER_WRAPPER;
import static com.android.tools.lint.client.api.JavaEvaluatorKt.TYPE_LONG_WRAPPER;

/**
 * desc：detect is contain visibility using</br>
 * ignore detect when in try-catch body
 * time: 2019/10/10-11:21</br>
 * author：Leo </br>
 * since V 1.0.0 </br>
 */
public class VisibilityCheckDetector extends Detector implements Detector.UastScanner {

    private static final String reportMSG = "建议避免使用visibility直接赋值方法";

    public static final Issue ISSUE = Issue.create(
            "VisibilityUse", "Visibility Usage"
            , "*.visibility =", Category.SECURITY, 5, Severity.WARNING
            , new Implementation(VisibilityCheckDetector.class, Scope.JAVA_FILE_SCOPE));

    @Nullable
    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.singletonList(UCallExpression.class);
    }

    @Nullable
    @Override
    public UElementHandler createUastHandler(@NotNull JavaContext context) {
        return new ParseUElementHandler(context);
    }

    class ParseUElementHandler extends UElementHandler {

        private JavaContext context;

        ParseUElementHandler(JavaContext context) {
            this.context = context;
        }

        @Override
        public void visitCallExpression(@NotNull UCallExpression node) {

            if (!UastExpressionUtils.isMethodCall(node)) {
                return;
            }

            PsiMethod resolve = node.resolve();
            String className = resolve.getContainingClass().getName();

            if (needDetectInKotlin(node) || needDetectInJava(node, className)) {
                String methodName = node.getMethodName();
                LintFix lintFix;
                lintFix = LintFix.create().replace()
                        .text(methodName)
                        .with(methodName.replace("visibility", "setVisibility")).build();
                context.report(ISSUE, node, context.getLocation(node), reportMSG, lintFix);
            }
        }
    }

    // java类中调用
    private boolean needDetectInJava(UCallExpression node, String className) {
        if (Lint.isKotlin(node.getSourcePsi())) {
            return false;
        }

        String methodName = node.getMethodName();
        return !TextUtils.isEmpty(methodName) && (!className.equals("ToolView")
                || methodName.equals("setVisibility"));
    }

    // kotlin中调用
    private boolean needDetectInKotlin(UCallExpression node) {
        if (!Lint.isKotlin(node.getSourcePsi())) {
            return false;
        }

        String methodName = node.getMethodName();
        return "setVisibility".equals(methodName) || "visibility".equals(methodName);
    }
}
