package io.w4t3rcs.python.resolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.w4t3rcs.python.exception.SpelythonProcessingException;
import io.w4t3rcs.python.properties.PythonResolverProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Map;

/**
 * {@link PythonResolver} implementation that processes Spring Expression Language (SpEL)
 * expressions embedded within Python scripts.
 *
 * <p>This resolver allows embedding SpEL expressions directly into Python code. During
 * script resolution, it evaluates these expressions at runtime within a configured
 * SpEL evaluation context and replaces them with their JSON-serialized representations
 * for use in Python.</p>
 *
 * <p>The resolver automatically inserts the necessary Python JSON import statement if missing.
 * It supports passing external variables to the SpEL context via the {@code arguments} map.
 * Errors during JSON serialization are wrapped and rethrown as {@link SpelythonProcessingException}.</p>
 *
 * @see PythonResolver
 * @see AbstractPythonResolver
 * @see PythonResolverHolder
 * @see PythonResolverProperties.SpelythonProperties
 * @author w4t3rcs
 * @since 1.0.0
 */
@RequiredArgsConstructor
public class SpelythonResolver extends AbstractPythonResolver {
    private final PythonResolverProperties resolverProperties;
    private final ApplicationContext applicationContext;
    private final ObjectMapper objectMapper;

    /**
     * Resolves SpEL expressions within the given Python script by evaluating each
     * expression and replacing it with its JSON representation.
     *
     * <p>The method uses configured regex patterns and positional parameters to
     * identify expressions, then evaluates them against the SpEL context enriched
     * with the provided {@code arguments} as variables.</p>
     *
     * @param script non-null Python script content possibly containing SpEL expressions
     * @param arguments nullable map of variables for SpEL evaluation context, keys are variable names, values are their corresponding objects. If null or empty, no variables are set.
     * @return non-null-resolved script with SpEL expressions replaced by JSON-wrapped results
     */
    @Override
    public String resolve(String script, Map<String, Object> arguments) {
        StringBuilder resolvedScript = new StringBuilder(script);
        this.insertUniqueLineToStart(resolvedScript, AbstractPythonResolver.IMPORT_JSON);
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        var spelythonProperties = resolverProperties.spelython();
        var spelProperties = spelythonProperties.spel();
        if (arguments != null && !arguments.isEmpty()) {
            arguments.forEach((key, value) ->
                    parser.parseExpression(spelProperties.localVariableIndex() + key)
                            .setValue(context, value));
        }
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));
        this.replaceScriptFragments(resolvedScript, spelythonProperties.regex(),
                spelProperties.positionFromStart(), spelProperties.positionFromEnd(),
                (matcher, fragment, result) -> {
                    try {
                        Expression expression = parser.parseExpression(fragment.toString());
                        Object expressionValue = expression.getValue(context, Object.class);
                        String jsonResult = objectMapper.writeValueAsString(expressionValue).replace("'", "\\'");
                        if (jsonResult.startsWith("\"\\\"") && jsonResult.endsWith("\\\"\"")) {
                            int beginIndex = 3;
                            int endIndex = jsonResult.length() - beginIndex;
                            jsonResult = jsonResult.substring(beginIndex, endIndex);
                            return result.append("'")
                                    .append(jsonResult)
                                    .append("'");
                        }
                        return result.append("json.loads('")
                                .append(jsonResult)
                                .append("')");
                    } catch (JsonProcessingException e) {
                        throw new SpelythonProcessingException(e);
                    }
                });
        return resolvedScript.toString();
    }
}