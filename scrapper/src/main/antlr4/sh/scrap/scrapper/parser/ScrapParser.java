// Generated from /home/tsouza/scrap.sh/scrapper/src/main/antlr4/sh/scrap/scrapper/parser/Scrap.g4 by ANTLR 4.2.2
package sh.scrap.scrapper.parser;

import java.util.Collection;
import java.util.Arrays;
import static sh.scrap.scrapper.DataScrapperBuilder.FieldType;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ScrapParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__4=1, T__3=2, T__2=3, T__1=4, T__0=5, LineTerminator=6, NullLiteral=7, 
		BooleanLiteral=8, DecimalLiteral=9, HexIntegerLiteral=10, OctalIntegerLiteral=11, 
		Identifier=12, StringLiteral=13, WhiteSpaces=14, MultiLineComment=15, 
		SingleLineComment=16, UnexpectedCharacter=17;
	public static final String[] tokenNames = {
		"<INVALID>", "'->'", "':'", "';'", "','", "'array'", "LineTerminator", 
		"'null'", "BooleanLiteral", "DecimalLiteral", "HexIntegerLiteral", "OctalIntegerLiteral", 
		"Identifier", "StringLiteral", "WhiteSpaces", "MultiLineComment", "SingleLineComment", 
		"UnexpectedCharacter"
	};
	public static final int
		RULE_scrapOutput = 0, RULE_fieldExpression = 1, RULE_expressions = 2, 
		RULE_expression = 3, RULE_singleExpression = 4, RULE_iterationExpression = 5, 
		RULE_arguments = 6, RULE_argumentList = 7, RULE_argument = 8, RULE_fieldName = 9, 
		RULE_literal = 10, RULE_numericLiteral = 11, RULE_identifierName = 12, 
		RULE_reservedWord = 13, RULE_typeCast = 14, RULE_functionName = 15;
	public static final String[] ruleNames = {
		"scrapOutput", "fieldExpression", "expressions", "expression", "singleExpression", 
		"iterationExpression", "arguments", "argumentList", "argument", "fieldName", 
		"literal", "numericLiteral", "identifierName", "reservedWord", "typeCast", 
		"functionName"
	};

	@Override
	public String getGrammarFileName() { return "Scrap.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


	    private ScrapParser.FunctionNameValidator functionNameValidator;

	    public ScrapParser(TokenStream input, FunctionNameValidator functionNameValidator) {
	        this(input);
	        this.functionNameValidator = functionNameValidator;
	    }

	    private boolean isValidFunction(String text) {
	        return functionNameValidator.isValidFunctionName(text);
	    }

	    private boolean isValidTypeCast(String text) {
	        try {
	            FieldType.valueOf(text.toUpperCase());
	            return true;
	        } catch (Exception e) {
	            return false;
	        }
	    }

	    public static interface FunctionNameValidator {
	        boolean isValidFunctionName(String name);
	    }

	public ScrapParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ScrapOutputContext extends ParserRuleContext {
		public List<FieldExpressionContext> fieldExpression() {
			return getRuleContexts(FieldExpressionContext.class);
		}
		public FieldExpressionContext fieldExpression(int i) {
			return getRuleContext(FieldExpressionContext.class,i);
		}
		public ScrapOutputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_scrapOutput; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterScrapOutput(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitScrapOutput(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitScrapOutput(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ScrapOutputContext scrapOutput() throws RecognitionException {
		ScrapOutputContext _localctx = new ScrapOutputContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_scrapOutput);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(32); fieldExpression();
			setState(36);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NullLiteral) | (1L << BooleanLiteral) | (1L << Identifier))) != 0)) {
				{
				{
				setState(33); fieldExpression();
				}
				}
				setState(38);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldExpressionContext extends ParserRuleContext {
		public FieldNameContext fieldName() {
			return getRuleContext(FieldNameContext.class,0);
		}
		public ExpressionsContext expressions() {
			return getRuleContext(ExpressionsContext.class,0);
		}
		public TypeCastContext typeCast() {
			return getRuleContext(TypeCastContext.class,0);
		}
		public FieldExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterFieldExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitFieldExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitFieldExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldExpressionContext fieldExpression() throws RecognitionException {
		FieldExpressionContext _localctx = new FieldExpressionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_fieldExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(39); fieldName();
			setState(40); match(2);
			setState(44);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(41); typeCast();
				setState(42); match(1);
				}
				break;
			}
			setState(46); expressions();
			setState(47); match(3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionsContext extends ParserRuleContext {
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterExpressions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitExpressions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitExpressions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionsContext expressions() throws RecognitionException {
		ExpressionsContext _localctx = new ExpressionsContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_expressions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49); expression();
			setState(54);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==1) {
				{
				{
				setState(50); match(1);
				setState(51); expression();
				}
				}
				setState(56);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public IterationExpressionContext iterationExpression() {
			return getRuleContext(IterationExpressionContext.class,0);
		}
		public SingleExpressionContext singleExpression() {
			return getRuleContext(SingleExpressionContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_expression);
		try {
			setState(59);
			switch (_input.LA(1)) {
			case NullLiteral:
			case BooleanLiteral:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(57); singleExpression();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 2);
				{
				setState(58); iterationExpression();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SingleExpressionContext extends ParserRuleContext {
		public FunctionNameContext functionName() {
			return getRuleContext(FunctionNameContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public SingleExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_singleExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterSingleExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitSingleExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitSingleExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SingleExpressionContext singleExpression() throws RecognitionException {
		SingleExpressionContext _localctx = new SingleExpressionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_singleExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61); functionName();
			setState(62); arguments();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IterationExpressionContext extends ParserRuleContext {
		public FunctionNameContext functionName() {
			return getRuleContext(FunctionNameContext.class,0);
		}
		public ArgumentsContext arguments() {
			return getRuleContext(ArgumentsContext.class,0);
		}
		public IterationExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iterationExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterIterationExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitIterationExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitIterationExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IterationExpressionContext iterationExpression() throws RecognitionException {
		IterationExpressionContext _localctx = new IterationExpressionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_iterationExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64); match(5);
			setState(65); functionName();
			setState(66); arguments();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentsContext extends ParserRuleContext {
		public ArgumentListContext argumentList() {
			return getRuleContext(ArgumentListContext.class,0);
		}
		public ArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitArguments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsContext arguments() throws RecognitionException {
		ArgumentsContext _localctx = new ArgumentsContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_arguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NullLiteral) | (1L << BooleanLiteral) | (1L << DecimalLiteral) | (1L << HexIntegerLiteral) | (1L << OctalIntegerLiteral) | (1L << StringLiteral))) != 0)) {
				{
				setState(68); argumentList();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentListContext extends ParserRuleContext {
		public List<ArgumentContext> argument() {
			return getRuleContexts(ArgumentContext.class);
		}
		public ArgumentContext argument(int i) {
			return getRuleContext(ArgumentContext.class,i);
		}
		public ArgumentListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterArgumentList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitArgumentList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitArgumentList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentListContext argumentList() throws RecognitionException {
		ArgumentListContext _localctx = new ArgumentListContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_argumentList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71); argument();
			setState(76);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==4) {
				{
				{
				setState(72); match(4);
				setState(73); argument();
				}
				}
				setState(78);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public ArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentContext argument() throws RecognitionException {
		ArgumentContext _localctx = new ArgumentContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_argument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79); literal();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldNameContext extends ParserRuleContext {
		public IdentifierNameContext identifierName() {
			return getRuleContext(IdentifierNameContext.class,0);
		}
		public FieldNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterFieldName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitFieldName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitFieldName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldNameContext fieldName() throws RecognitionException {
		FieldNameContext _localctx = new FieldNameContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_fieldName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(81); identifierName();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public NumericLiteralContext numericLiteral() {
			return getRuleContext(NumericLiteralContext.class,0);
		}
		public TerminalNode StringLiteral() { return getToken(ScrapParser.StringLiteral, 0); }
		public TerminalNode NullLiteral() { return getToken(ScrapParser.NullLiteral, 0); }
		public TerminalNode BooleanLiteral() { return getToken(ScrapParser.BooleanLiteral, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_literal);
		int _la;
		try {
			setState(85);
			switch (_input.LA(1)) {
			case NullLiteral:
			case BooleanLiteral:
			case StringLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(83);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NullLiteral) | (1L << BooleanLiteral) | (1L << StringLiteral))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				}
				break;
			case DecimalLiteral:
			case HexIntegerLiteral:
			case OctalIntegerLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(84); numericLiteral();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumericLiteralContext extends ParserRuleContext {
		public TerminalNode DecimalLiteral() { return getToken(ScrapParser.DecimalLiteral, 0); }
		public TerminalNode OctalIntegerLiteral() { return getToken(ScrapParser.OctalIntegerLiteral, 0); }
		public TerminalNode HexIntegerLiteral() { return getToken(ScrapParser.HexIntegerLiteral, 0); }
		public NumericLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numericLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterNumericLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitNumericLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitNumericLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumericLiteralContext numericLiteral() throws RecognitionException {
		NumericLiteralContext _localctx = new NumericLiteralContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_numericLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DecimalLiteral) | (1L << HexIntegerLiteral) | (1L << OctalIntegerLiteral))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdentifierNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(ScrapParser.Identifier, 0); }
		public ReservedWordContext reservedWord() {
			return getRuleContext(ReservedWordContext.class,0);
		}
		public IdentifierNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifierName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterIdentifierName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitIdentifierName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitIdentifierName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierNameContext identifierName() throws RecognitionException {
		IdentifierNameContext _localctx = new IdentifierNameContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_identifierName);
		try {
			setState(91);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(89); match(Identifier);
				}
				break;
			case NullLiteral:
			case BooleanLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(90); reservedWord();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReservedWordContext extends ParserRuleContext {
		public TerminalNode NullLiteral() { return getToken(ScrapParser.NullLiteral, 0); }
		public TerminalNode BooleanLiteral() { return getToken(ScrapParser.BooleanLiteral, 0); }
		public ReservedWordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reservedWord; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterReservedWord(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitReservedWord(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitReservedWord(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReservedWordContext reservedWord() throws RecognitionException {
		ReservedWordContext _localctx = new ReservedWordContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_reservedWord);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			_la = _input.LA(1);
			if ( !(_la==NullLiteral || _la==BooleanLiteral) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeCastContext extends ParserRuleContext {
		public IdentifierNameContext identifierName;
		public IdentifierNameContext identifierName() {
			return getRuleContext(IdentifierNameContext.class,0);
		}
		public TypeCastContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeCast; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterTypeCast(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitTypeCast(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitTypeCast(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeCastContext typeCast() throws RecognitionException {
		TypeCastContext _localctx = new TypeCastContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_typeCast);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95); ((TypeCastContext)_localctx).identifierName = identifierName();
			setState(96);
			if (!(isValidTypeCast((((TypeCastContext)_localctx).identifierName!=null?_input.getText(((TypeCastContext)_localctx).identifierName.start,((TypeCastContext)_localctx).identifierName.stop):null)))) throw new FailedPredicateException(this, "isValidTypeCast($identifierName.text)");
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionNameContext extends ParserRuleContext {
		public IdentifierNameContext identifierName;
		public IdentifierNameContext identifierName() {
			return getRuleContext(IdentifierNameContext.class,0);
		}
		public FunctionNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterFunctionName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitFunctionName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitFunctionName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionNameContext functionName() throws RecognitionException {
		FunctionNameContext _localctx = new FunctionNameContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_functionName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98); ((FunctionNameContext)_localctx).identifierName = identifierName();
			setState(99);
			if (!(isValidFunction((((FunctionNameContext)_localctx).identifierName!=null?_input.getText(((FunctionNameContext)_localctx).identifierName.start,((FunctionNameContext)_localctx).identifierName.stop):null)))) throw new FailedPredicateException(this, "isValidFunction($identifierName.text)");
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 14: return typeCast_sempred((TypeCastContext)_localctx, predIndex);

		case 15: return functionName_sempred((FunctionNameContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean typeCast_sempred(TypeCastContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return isValidTypeCast((((TypeCastContext)_localctx).identifierName!=null?_input.getText(((TypeCastContext)_localctx).identifierName.start,((TypeCastContext)_localctx).identifierName.stop):null));
		}
		return true;
	}
	private boolean functionName_sempred(FunctionNameContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1: return isValidFunction((((FunctionNameContext)_localctx).identifierName!=null?_input.getText(((FunctionNameContext)_localctx).identifierName.start,((FunctionNameContext)_localctx).identifierName.stop):null));
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\23h\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3\2\7\2%\n"+
		"\2\f\2\16\2(\13\2\3\3\3\3\3\3\3\3\3\3\5\3/\n\3\3\3\3\3\3\3\3\4\3\4\3\4"+
		"\7\4\67\n\4\f\4\16\4:\13\4\3\5\3\5\5\5>\n\5\3\6\3\6\3\6\3\7\3\7\3\7\3"+
		"\7\3\b\5\bH\n\b\3\t\3\t\3\t\7\tM\n\t\f\t\16\tP\13\t\3\n\3\n\3\13\3\13"+
		"\3\f\3\f\5\fX\n\f\3\r\3\r\3\16\3\16\5\16^\n\16\3\17\3\17\3\20\3\20\3\20"+
		"\3\21\3\21\3\21\3\21\2\2\22\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \2"+
		"\5\4\2\t\n\17\17\3\2\13\r\3\2\t\n_\2\"\3\2\2\2\4)\3\2\2\2\6\63\3\2\2\2"+
		"\b=\3\2\2\2\n?\3\2\2\2\fB\3\2\2\2\16G\3\2\2\2\20I\3\2\2\2\22Q\3\2\2\2"+
		"\24S\3\2\2\2\26W\3\2\2\2\30Y\3\2\2\2\32]\3\2\2\2\34_\3\2\2\2\36a\3\2\2"+
		"\2 d\3\2\2\2\"&\5\4\3\2#%\5\4\3\2$#\3\2\2\2%(\3\2\2\2&$\3\2\2\2&\'\3\2"+
		"\2\2\'\3\3\2\2\2(&\3\2\2\2)*\5\24\13\2*.\7\4\2\2+,\5\36\20\2,-\7\3\2\2"+
		"-/\3\2\2\2.+\3\2\2\2./\3\2\2\2/\60\3\2\2\2\60\61\5\6\4\2\61\62\7\5\2\2"+
		"\62\5\3\2\2\2\638\5\b\5\2\64\65\7\3\2\2\65\67\5\b\5\2\66\64\3\2\2\2\67"+
		":\3\2\2\28\66\3\2\2\289\3\2\2\29\7\3\2\2\2:8\3\2\2\2;>\5\n\6\2<>\5\f\7"+
		"\2=;\3\2\2\2=<\3\2\2\2>\t\3\2\2\2?@\5 \21\2@A\5\16\b\2A\13\3\2\2\2BC\7"+
		"\7\2\2CD\5 \21\2DE\5\16\b\2E\r\3\2\2\2FH\5\20\t\2GF\3\2\2\2GH\3\2\2\2"+
		"H\17\3\2\2\2IN\5\22\n\2JK\7\6\2\2KM\5\22\n\2LJ\3\2\2\2MP\3\2\2\2NL\3\2"+
		"\2\2NO\3\2\2\2O\21\3\2\2\2PN\3\2\2\2QR\5\26\f\2R\23\3\2\2\2ST\5\32\16"+
		"\2T\25\3\2\2\2UX\t\2\2\2VX\5\30\r\2WU\3\2\2\2WV\3\2\2\2X\27\3\2\2\2YZ"+
		"\t\3\2\2Z\31\3\2\2\2[^\7\16\2\2\\^\5\34\17\2][\3\2\2\2]\\\3\2\2\2^\33"+
		"\3\2\2\2_`\t\4\2\2`\35\3\2\2\2ab\5\32\16\2bc\6\20\2\3c\37\3\2\2\2de\5"+
		"\32\16\2ef\6\21\3\3f!\3\2\2\2\n&.8=GNW]";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}