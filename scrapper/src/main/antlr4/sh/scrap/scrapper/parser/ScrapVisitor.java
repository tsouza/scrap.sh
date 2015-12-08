// Generated from /Users/thiago/scrap.sh/scrapper/src/main/antlr4/sh/scrap/scrapper/parser/Scrap.g4 by ANTLR 4.2.2
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
	 * Visit a parse tree produced by {@link ScrapParser#argument}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgument(@NotNull ScrapParser.ArgumentContext ctx);

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
	 * Visit a parse tree produced by {@link ScrapParser#argumentList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentList(@NotNull ScrapParser.ArgumentListContext ctx);

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
	 * Visit a parse tree produced by {@link ScrapParser#fieldExpression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldExpression(@NotNull ScrapParser.FieldExpressionContext ctx);

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
	 * Visit a parse tree produced by {@link ScrapParser#numericLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericLiteral(@NotNull ScrapParser.NumericLiteralContext ctx);
}