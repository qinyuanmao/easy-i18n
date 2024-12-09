package de.marhali.easyi18n.assistance.reference;

import com.goide.psi.GoStringLiteral;
import com.intellij.openapi.project.Project;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Go specific key reference binding.
 */
public class GoKeyReferenceContributor extends AbstractKeyReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(GoStringLiteral.class),
                getProvider());
    }

    private PsiReferenceProvider getProvider() {
        return new PsiReferenceProvider() {
            @Override
            public PsiReference @NotNull [] getReferencesByElement(
                    @NotNull PsiElement element, @NotNull ProcessingContext context) {

                // Ensure the element is a Go string literal
                if (!(element instanceof GoStringLiteral)) {
                    return PsiReference.EMPTY_ARRAY;
                }

                // Cast the element to GoStringLiteral
                GoStringLiteral stringLiteral = (GoStringLiteral) element;

                // Extract the actual string content (remove quotes/backticks)
                String content = extractStringContent(stringLiteral);

                if (content == null || content.isEmpty()) {
                    return PsiReference.EMPTY_ARRAY;
                }

                // Create references for the extracted string content
                Project project = element.getProject();
                return getReferences(project, element, content);
            }
        };
    }

    /**
     * Extracts the content of a Go string literal, removing quotes or backticks.
     *
     * @param stringLiteral The Go string literal.
     * @return The unwrapped string content, or null if not valid.
     */
    private String extractStringContent(@NotNull GoStringLiteral stringLiteral) {
        String rawText = stringLiteral.getText();
        if ((rawText.startsWith("\"") && rawText.endsWith("\"")) ||
                (rawText.startsWith("`") && rawText.endsWith("`"))) {
            return rawText.substring(1, rawText.length() - 1);
        }
        return null;
    }
}
