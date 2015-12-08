// Generated from /home/tsouza/scrap.sh/scrapper/src/main/antlr4/sh/scrap/scrapper/parser/Scrap.g4 by ANTLR 4.2.2
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
	 * Enter a parse tree produced by {@link ScrapParser#argument}.
	 * @param ctx the parse tree
	 */
	void enterArgument(@NotNull ScrapParser.ArgumentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#argument}.
	 * @param ctx the parse tree
	 */
	void exitArgument(@NotNull ScrapParser.ArgumentContext ctx);

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
	 * Enter a parse tree produced by {@link ScrapParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void enterArgumentList(@NotNull ScrapParser.ArgumentListContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#argumentList}.
	 * @param ctx the parse tree
	 */
	void exitArgumentList(@NotNull ScrapParser.ArgumentListContext ctx);

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
	 * Enter a parse tree produced by {@link ScrapParser#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void enterNumericLiteral(@NotNull ScrapParser.NumericLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link ScrapParser#numericLiteral}.
	 * @param ctx the parse tree
	 */
	void exitNumericLiteral(@NotNull ScrapParser.NumericLiteralContext ctx);
}