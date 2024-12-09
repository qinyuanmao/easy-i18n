package de.marhali.easyi18n.assistance.folding;

import com.goide.psi.GoStringLiteral;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Go specific translation key folding.
 */
public class GoFoldingBuilder extends AbstractFoldingBuilder {
    @Override
    @NotNull List<Pair<String, PsiElement>> extractRegions(@NotNull PsiElement root) {
        // Find all string literals in the PSI tree
        return PsiTreeUtil.findChildrenOfType(root, GoStringLiteral.class).stream()
                .map(literal -> Pair.pair(extractStringContent(literal), (PsiElement) literal))
                .collect(Collectors.toList());
    }

    @Override
    @Nullable String extractText(@NotNull ASTNode node) {
        // Extract content from a single string literal AST node
        GoStringLiteral literal = node.getPsi(GoStringLiteral.class);
        return extractStringContent(literal);
    }

    /**
     * Extracts the content of a Go string literal, removing quotes or backticks.
     *
     * @param literal The Go string literal.
     * @return The unwrapped string content, or null if invalid.
     */
    private String extractStringContent(@Nullable GoStringLiteral literal) {
        if (literal == null) {
            return null;
        }
        String rawText = literal.getText();
        if ((rawText.startsWith("\"") && rawText.endsWith("\"")) ||
                (rawText.startsWith("`") && rawText.endsWith("`"))) {
            return rawText.substring(1, rawText.length() - 1);
        }
        return null;
    }
}
