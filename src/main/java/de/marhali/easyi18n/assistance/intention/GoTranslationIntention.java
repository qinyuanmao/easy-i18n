package de.marhali.easyi18n.assistance.intention;

import com.goide.psi.GoStringLiteral;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Go specific translation intention.
 * Detects and processes Go string literals for translation keys or content extraction.
 */
public class GoTranslationIntention extends AbstractTranslationIntention {
    @Override
    protected @Nullable String extractText(@NotNull PsiElement element) {
        // Check if the given element or its parent is a Go string literal
        if (!(element instanceof GoStringLiteral) && !(element.getParent() instanceof GoStringLiteral)) {
            return null;
        }

        // Ensure we are working with the correct GoStringLiteral element
        GoStringLiteral stringLiteral = element instanceof GoStringLiteral
                ? (GoStringLiteral) element
                : (GoStringLiteral) element.getParent();

        // Extract the raw text, including quotes/backticks
        String rawText = stringLiteral.getText();

        // Remove the enclosing quotes/backticks
        if (rawText.startsWith("\"") && rawText.endsWith("\"") ||
                rawText.startsWith("`") && rawText.endsWith("`")) {
            return rawText.substring(1, rawText.length() - 1);
        }

        return null; // Return null if it is not a valid string literal
    }
}
