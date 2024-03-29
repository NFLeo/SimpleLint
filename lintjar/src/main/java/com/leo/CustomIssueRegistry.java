package com.leo;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.ApiKt;
import com.android.tools.lint.detector.api.Issue;
import com.leo.lint.ArTextStringGravityCheckDetector;
import com.leo.lint.ColorCheckDetector;
import com.leo.lint.ContractStartDetector;
import com.leo.lint.LayoutIdCheckDetector;
import com.leo.lint.LeftOrRightCheckDetector;
import com.leo.lint.LineIdCheckDetector;
import com.leo.lint.ParseCheckDetector;
import com.leo.lint.VisibilityCheckDetector;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class CustomIssueRegistry extends IssueRegistry {

    @NotNull
    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(
                ContractStartDetector.ISSUE,
                ArTextStringGravityCheckDetector.ISSUE,
                LeftOrRightCheckDetector.ISSUE,
                ParseCheckDetector.ISSUE,
                ColorCheckDetector.ISSUE,
                VisibilityCheckDetector.ISSUE,
                LineIdCheckDetector.ISSUE,
                LayoutIdCheckDetector.ISSUE);
    }

    @Override
    public int getApi() {
        return ApiKt.CURRENT_API;
    }
}
