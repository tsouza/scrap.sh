// Generated from /home/tsouza/scrap.sh/scrapper/src/main/antlr4/sh/scrap/scrapper/parser/Scrap.g4 by ANTLR 4.2.2
package sh.scrap.scrapper.parser;

import java.util.Collection;
import java.util.Arrays;
import static sh.scrap.scrapper.DataScrapperBuilder.FieldType;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ScrapParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ScrapVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ScrapParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(@NotNull ScrapParser.ExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#singleExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleExpression(@NotNull ScrapParser.SingleExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#fieldName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldName(@NotNull ScrapParser.FieldNameContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#functionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionName(@NotNull ScrapParser.FunctionNameContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#jsonValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonValue(@NotNull ScrapParser.JsonValueContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#annotations}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotations(@NotNull ScrapParser.AnnotationsContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#scrapOutput}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScrapOutput(@NotNull ScrapParser.ScrapOutputContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#expressions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressions(@NotNull ScrapParser.ExpressionsContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#reservedWord}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReservedWord(@NotNull ScrapParser.ReservedWordContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(@NotNull ScrapParser.LiteralContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#identifierName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifierName(@NotNull ScrapParser.IdentifierNameContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#typeCast}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeCast(@NotNull ScrapParser.TypeCastContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#localName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLocalName(@NotNull ScrapParser.LocalNameContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#jsonObjectEntry}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonObjectEntry(@NotNull ScrapParser.JsonObjectEntryContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#annotationsList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnnotationsList(@NotNull ScrapParser.AnnotationsListContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#fieldExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldExpression(@NotNull ScrapParser.FieldExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#namespace}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamespace(@NotNull ScrapParser.NamespaceContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#iterationExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIterationExpression(@NotNull ScrapParser.IterationExpressionContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#arguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArguments(@NotNull ScrapParser.ArgumentsContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#jsonObject}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonObject(@NotNull ScrapParser.JsonObjectContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#argumentsList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentsList(@NotNull ScrapParser.ArgumentsListContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#mainArgument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMainArgument(@NotNull ScrapParser.MainArgumentContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#numericLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericLiteral(@NotNull ScrapParser.NumericLiteralContext ctx);

	/**
	 * Visit a parse tree produced by {@link ScrapParser#jsonArray}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJsonArray(@NotNull ScrapParser.JsonArrayContext ctx);
}