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

import java.util.Arrays;
import java.util.Collection;

import static com.android.SdkConstants.ATTR_DRAWABLE_LEFT;
import static com.android.SdkConstants.ATTR_DRAWABLE_RIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_GONE_MARGIN_LEFT;
import static com.android.SdkConstants.ATTR_LAYOUT_GONE_MARGIN_RIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_LEFT_TO_LEFT_OF;
import static com.android.SdkConstants.ATTR_LAYOUT_LEFT_TO_RIGHT_OF;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_LEFT;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_RIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_RIGHT_TO_LEFT_OF;
import static com.android.SdkConstants.ATTR_LAYOUT_RIGHT_TO_RIGHT_OF;
import static com.android.SdkConstants.ATTR_PADDING_LEFT;
import static com.android.SdkConstants.ATTR_PADDING_RIGHT;

/**
 * desc：检查xml中是否包含left、right</br>
 * time: 2019/10/10-11:24</br>
 * author：Leo </br>
 * since V 1.0.0 </br>
 */
public class LeftOrRightCheckDetector extends ResourceXmlDetector {

    public static final Issue ISSUE = Issue.create(
            "LeftOrRightInXml", "LeftOrRight Id Named"
            , "replace left right with start end"
            , Category.LINT, 9, Severity.WARNING
            , new Implementation(LeftOrRightCheckDetector.class, Scope.RESOURCE_FILE_SCOPE)
    );

    @Override
    public boolean appliesTo(@NotNull ResourceFolderType folderType) {
        return folderType == ResourceFolderType.LAYOUT;
    }

    @Nullable
    @Override
    public Collection<String> getApplicableAttributes() {
        return Arrays.asList(ATTR_LAYOUT_MARGIN_LEFT, ATTR_LAYOUT_MARGIN_RIGHT
                , ATTR_PADDING_LEFT, ATTR_PADDING_RIGHT, ATTR_DRAWABLE_LEFT, ATTR_DRAWABLE_RIGHT
                , ATTR_LAYOUT_GONE_MARGIN_LEFT, ATTR_LAYOUT_GONE_MARGIN_RIGHT
                , ATTR_LAYOUT_LEFT_TO_LEFT_OF, ATTR_LAYOUT_LEFT_TO_RIGHT_OF
                , ATTR_LAYOUT_RIGHT_TO_LEFT_OF, ATTR_LAYOUT_RIGHT_TO_RIGHT_OF);
    }

    @Override
    public void visitAttribute(@NotNull XmlContext context, @NotNull Attr attribute) {
        LintFix lintFix = LintFix.create().replace().text(attribute.getName())
                .with(attribute.getName()
                        .replace("Left", "Start")
                        .replace("Right", "End"))
                .build();
        context.report(ISSUE, attribute, context.getLocation(attribute)
                , "replace left/right with start/end in oversea version", lintFix
        );
    }
}
