package de.marhali.easyi18n.assistance.completion;

import com.goide.psi.GoStringLiteral;
import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.PlatformPatterns;

public class GoCompletionContributor extends CompletionContributor {
    public GoCompletionContributor() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement().inside(GoStringLiteral.class),
                new KeyCompletionProvider());
    }
}