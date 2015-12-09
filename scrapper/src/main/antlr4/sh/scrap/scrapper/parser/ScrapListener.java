// Generated from /Users/thiago/scrap.sh/scrapper/src/main/antlr4/sh/scrap/scrapper/parser/Scrap.g4 by ANTLR 4.2.2
package sh.scrap.scrapper.parser;

import java.util.Collection;
import java.util.Arrays;
import static sh.scrap.scrapper.DataScrapperBuilder.FieldType;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ScrapParser}.
 */
public interface ScrapListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ScrapParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(@NotNull ScrapParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(@NotNull ScrapParser.ExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void enterSingleExpression(@NotNull ScrapParser.SingleExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#singleExpression}.
	 * @param ctx the parse tree
	 */
	void exitSingleExpression(@NotNull ScrapParser.SingleExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void enterFieldName(@NotNull ScrapParser.FieldNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void exitFieldName(@NotNull ScrapParser.FieldNameContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#functionName}.
	 * @param ctx the parse tree
	 */
	void enterFunctionName(@NotNull ScrapParser.FunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#functionName}.
	 * @param ctx the parse tree
	 */
	void exitFunctionName(@NotNull ScrapParser.FunctionNameContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#jsonValue}.
	 * @param ctx the parse tree
	 */
	void enterJsonValue(@NotNull ScrapParser.JsonValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#jsonValue}.
	 * @param ctx the parse tree
	 */
	void exitJsonValue(@NotNull ScrapParser.JsonValueContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#annotations}.
	 * @param ctx the parse tree
	 */
	void enterAnnotations(@NotNull ScrapParser.AnnotationsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#annotations}.
	 * @param ctx the parse tree
	 */
	void exitAnnotations(@NotNull ScrapParser.AnnotationsContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#scrapOutput}.
	 * @param ctx the parse tree
	 */
	void enterScrapOutput(@NotNull ScrapParser.ScrapOutputContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#scrapOutput}.
	 * @param ctx the parse tree
	 */
	void exitScrapOutput(@NotNull ScrapParser.ScrapOutputContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#expressions}.
	 * @param ctx the parse tree
	 */
	void enterExpressions(@NotNull ScrapParser.ExpressionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#expressions}.
	 * @param ctx the parse tree
	 */
	void exitExpressions(@NotNull ScrapParser.ExpressionsContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#reservedWord}.
	 * @param ctx the parse tree
	 */
	void enterReservedWord(@NotNull ScrapParser.ReservedWordContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#reservedWord}.
	 * @param ctx the parse tree
	 */
	void exitReservedWord(@NotNull ScrapParser.ReservedWordContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(@NotNull ScrapParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(@NotNull ScrapParser.LiteralContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#identifierName}.
	 * @param ctx the parse tree
	 */
	void enterIdentifierName(@NotNull ScrapParser.IdentifierNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#identifierName}.
	 * @param ctx the parse tree
	 */
	void exitIdentifierName(@NotNull ScrapParser.IdentifierNameContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#typeCast}.
	 * @param ctx the parse tree
	 */
	void enterTypeCast(@NotNull ScrapParser.TypeCastContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#typeCast}.
	 * @param ctx the parse tree
	 */
	void exitTypeCast(@NotNull ScrapParser.TypeCastContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#localName}.
	 * @param ctx the parse tree
	 */
	void enterLocalName(@NotNull ScrapParser.LocalNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#localName}.
	 * @param ctx the parse tree
	 */
	void exitLocalName(@NotNull ScrapParser.LocalNameContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#jsonObjectEntry}.
	 * @param ctx the parse tree
	 */
	void enterJsonObjectEntry(@NotNull ScrapParser.JsonObjectEntryContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#jsonObjectEntry}.
	 * @param ctx the parse tree
	 */
	void exitJsonObjectEntry(@NotNull ScrapParser.JsonObjectEntryContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#annotationsList}.
	 * @param ctx the parse tree
	 */
	void enterAnnotationsList(@NotNull ScrapParser.AnnotationsListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#annotationsList}.
	 * @param ctx the parse tree
	 */
	void exitAnnotationsList(@NotNull ScrapParser.AnnotationsListContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#fieldExpression}.
	 * @param ctx the parse tree
	 */
	void enterFieldExpression(@NotNull ScrapParser.FieldExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#fieldExpression}.
	 * @param ctx the parse tree
	 */
	void exitFieldExpression(@NotNull ScrapParser.FieldExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#namespace}.
	 * @param ctx the parse tree
	 */
	void enterNamespace(@NotNull ScrapParser.NamespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#namespace}.
	 * @param ctx the parse tree
	 */
	void exitNamespace(@NotNull ScrapParser.NamespaceContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#iterationExpression}.
	 * @param ctx the parse tree
	 */
	void enterIterationExpression(@NotNull ScrapParser.IterationExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#iterationExpression}.
	 * @param ctx the parse tree
	 */
	void exitIterationExpression(@NotNull ScrapParser.IterationExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#arguments}.
	 * @param ctx the parse tree
	 */
	void enterArguments(@NotNull ScrapParser.ArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#arguments}.
	 * @param ctx the parse tree
	 */
	void exitArguments(@NotNull ScrapParser.ArgumentsContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#jsonObject}.
	 * @param ctx the parse tree
	 */
	void enterJsonObject(@NotNull ScrapParser.JsonObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#jsonObject}.
	 * @param ctx the parse tree
	 */
	void exitJsonObject(@NotNull ScrapParser.JsonObjectContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#argumentsList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentsList(@NotNull ScrapParser.ArgumentsListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#argumentsList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentsList(@NotNull ScrapParser.ArgumentsListContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#mainArgument}.
	 * @param ctx the parse tree
	 */
	void enterMainArgument(@NotNull ScrapParser.MainArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#mainArgument}.
	 * @param ctx the parse tree
	 */
	void exitMainArgument(@NotNull ScrapParser.MainArgumentContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void enterNumericLiteral(@NotNull ScrapParser.NumericLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void exitNumericLiteral(@NotNull ScrapParser.NumericLiteralContext ctx);

	/**
	 * Enter a parse tree produced by {@link ScrapParser#jsonArray}.
	 * @param ctx the parse tree
	 */
	void enterJsonArray(@NotNull ScrapParser.JsonArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#jsonArray}.
	 * @param ctx the parse tree
	 */
	void exitJsonArray(@NotNull ScrapParser.JsonArrayContext ctx);
}