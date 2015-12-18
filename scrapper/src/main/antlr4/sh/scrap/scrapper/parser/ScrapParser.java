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
		T__9=1, T__8=2, T__7=3, T__6=4, T__5=5, T__4=6, T__3=7, T__2=8, T__1=9, 
		T__0=10, LineTerminator=11, NullLiteral=12, BooleanLiteral=13, DecimalLiteral=14, 
		HexIntegerLiteral=15, OctalIntegerLiteral=16, Identifier=17, StringLiteral=18, 
		WhiteSpaces=19, MultiLineComment=20, SingleLineComment=21, UnexpectedCharacter=22;
	public static final String[] tokenNames = {
		"<INVALID>", "'->'", "'foreach'", "':'", "'['", "'{'", "';'", "','", "'array'", 
		"']'", "'}'", "LineTerminator", "'null'", "BooleanLiteral", "DecimalLiteral", 
		"HexIntegerLiteral", "OctalIntegerLiteral", "Identifier", "StringLiteral", 
		"WhiteSpaces", "MultiLineComment", "SingleLineComment", "UnexpectedCharacter"
	};
	public static final int
		RULE_scrapOutput = 0, RULE_fieldExpression = 1, RULE_expressions = 2, 
		RULE_expression = 3, RULE_singleExpression = 4, RULE_iterationExpression = 5, 
		RULE_arguments = 6, RULE_argumentsList = 7, RULE_annotations = 8, RULE_annotationsList = 9, 
		RULE_mainArgument = 10, RULE_fieldName = 11, RULE_jsonValue = 12, RULE_jsonObjectEntry = 13, 
		RULE_jsonObject = 14, RULE_jsonArray = 15, RULE_literal = 16, RULE_numericLiteral = 17, 
		RULE_identifierName = 18, RULE_reservedWord = 19, RULE_typeCast = 20, 
		RULE_functionName = 21, RULE_namespace = 22, RULE_localName = 23;
	public static final String[] ruleNames = {
		"scrapOutput", "fieldExpression", "expressions", "expression", "singleExpression", 
		"iterationExpression", "arguments", "argumentsList", "annotations", "annotationsList", 
		"mainArgument", "fieldName", "jsonValue", "jsonObjectEntry", "jsonObject", 
		"jsonArray", "literal", "numericLiteral", "identifierName", "reservedWord", 
		"typeCast", "functionName", "namespace", "localName"
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

	    private boolean isValidFunction(String namespace, String localName) {
	        return functionNameValidator.isValidFunctionName(namespace == null ?
	            localName : namespace + "." + localName);
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
			setState(48); fieldExpression();
			setState(52);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NullLiteral) | (1L << BooleanLiteral) | (1L << Identifier))) != 0)) {
				{
				{
				setState(49); fieldExpression();
				}
				}
				setState(54);
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
			setState(55); fieldName();
			setState(56); match(3);
			setState(60);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				{
				setState(57); typeCast();
				setState(58); match(1);
				}
				break;
			}
			setState(62); expressions();
			setState(63); match(6);
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
			setState(65); expression();
			setState(70);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==1) {
				{
				{
				setState(66); match(1);
				setState(67); expression();
				}
				}
				setState(72);
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
			setState(75);
			switch (_input.LA(1)) {
			case NullLiteral:
			case BooleanLiteral:
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(73); singleExpression();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(74); iterationExpression();
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
			setState(77); functionName();
			setState(78); arguments();
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
			setState(80); match(2);
			setState(81); functionName();
			setState(82); arguments();
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
		public ArgumentsListContext argumentsList() {
			return getRuleContext(ArgumentsListContext.class,0);
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
			setState(85);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NullLiteral) | (1L << BooleanLiteral) | (1L << DecimalLiteral) | (1L << HexIntegerLiteral) | (1L << OctalIntegerLiteral) | (1L << StringLiteral))) != 0)) {
				{
				setState(84); argumentsList();
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

	public static class ArgumentsListContext extends ParserRuleContext {
		public MainArgumentContext mainArgument() {
			return getRuleContext(MainArgumentContext.class,0);
		}
		public AnnotationsContext annotations() {
			return getRuleContext(AnnotationsContext.class,0);
		}
		public ArgumentsListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentsList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterArgumentsList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitArgumentsList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitArgumentsList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentsListContext argumentsList() throws RecognitionException {
		ArgumentsListContext _localctx = new ArgumentsListContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_argumentsList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87); mainArgument();
			setState(88); annotations();
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

	public static class AnnotationsContext extends ParserRuleContext {
		public AnnotationsListContext annotationsList() {
			return getRuleContext(AnnotationsListContext.class,0);
		}
		public AnnotationsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotations; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterAnnotations(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitAnnotations(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitAnnotations(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationsContext annotations() throws RecognitionException {
		AnnotationsContext _localctx = new AnnotationsContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_annotations);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(91);
			_la = _input.LA(1);
			if (_la==5) {
				{
				setState(90); annotationsList();
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

	public static class AnnotationsListContext extends ParserRuleContext {
		public JsonObjectContext jsonObject() {
			return getRuleContext(JsonObjectContext.class,0);
		}
		public AnnotationsListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_annotationsList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterAnnotationsList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitAnnotationsList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitAnnotationsList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnnotationsListContext annotationsList() throws RecognitionException {
		AnnotationsListContext _localctx = new AnnotationsListContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_annotationsList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93); jsonObject();
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

	public static class MainArgumentContext extends ParserRuleContext {
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public MainArgumentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_mainArgument; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterMainArgument(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitMainArgument(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitMainArgument(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MainArgumentContext mainArgument() throws RecognitionException {
		MainArgumentContext _localctx = new MainArgumentContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_mainArgument);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95); literal();
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
		enterRule(_localctx, 22, RULE_fieldName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97); identifierName();
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

	public static class JsonValueContext extends ParserRuleContext {
		public JsonArrayContext jsonArray() {
			return getRuleContext(JsonArrayContext.class,0);
		}
		public JsonObjectContext jsonObject() {
			return getRuleContext(JsonObjectContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public JsonValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsonValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterJsonValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitJsonValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitJsonValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonValueContext jsonValue() throws RecognitionException {
		JsonValueContext _localctx = new JsonValueContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_jsonValue);
		try {
			setState(102);
			switch (_input.LA(1)) {
			case 5:
				enterOuterAlt(_localctx, 1);
				{
				setState(99); jsonObject();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 2);
				{
				setState(100); jsonArray();
				}
				break;
			case NullLiteral:
			case BooleanLiteral:
			case DecimalLiteral:
			case HexIntegerLiteral:
			case OctalIntegerLiteral:
			case StringLiteral:
				enterOuterAlt(_localctx, 3);
				{
				setState(101); literal();
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

	public static class JsonObjectEntryContext extends ParserRuleContext {
		public JsonValueContext jsonValue() {
			return getRuleContext(JsonValueContext.class,0);
		}
		public IdentifierNameContext identifierName() {
			return getRuleContext(IdentifierNameContext.class,0);
		}
		public JsonObjectEntryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsonObjectEntry; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterJsonObjectEntry(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitJsonObjectEntry(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitJsonObjectEntry(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonObjectEntryContext jsonObjectEntry() throws RecognitionException {
		JsonObjectEntryContext _localctx = new JsonObjectEntryContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_jsonObjectEntry);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104); identifierName();
			setState(105); match(3);
			setState(106); jsonValue();
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

	public static class JsonObjectContext extends ParserRuleContext {
		public List<JsonObjectEntryContext> jsonObjectEntry() {
			return getRuleContexts(JsonObjectEntryContext.class);
		}
		public JsonObjectEntryContext jsonObjectEntry(int i) {
			return getRuleContext(JsonObjectEntryContext.class,i);
		}
		public JsonObjectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsonObject; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterJsonObject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitJsonObject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitJsonObject(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonObjectContext jsonObject() throws RecognitionException {
		JsonObjectContext _localctx = new JsonObjectContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_jsonObject);
		int _la;
		try {
			setState(121);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(108); match(5);
				setState(109); match(10);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(110); match(5);
				setState(111); jsonObjectEntry();
				setState(116);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==7) {
					{
					{
					setState(112); match(7);
					setState(113); jsonObjectEntry();
					}
					}
					setState(118);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(119); match(10);
				}
				break;
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

	public static class JsonArrayContext extends ParserRuleContext {
		public List<JsonValueContext> jsonValue() {
			return getRuleContexts(JsonValueContext.class);
		}
		public JsonValueContext jsonValue(int i) {
			return getRuleContext(JsonValueContext.class,i);
		}
		public JsonArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsonArray; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterJsonArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitJsonArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitJsonArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsonArrayContext jsonArray() throws RecognitionException {
		JsonArrayContext _localctx = new JsonArrayContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_jsonArray);
		try {
			setState(132);
			switch ( getInterpreter().adaptivePredict(_input,9,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(123); match(4);
				setState(124); match(9);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(125); match(4);
				setState(126); jsonValue();
				{
				setState(127); match(7);
				setState(128); jsonValue();
				}
				setState(130); match(10);
				}
				break;
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
		enterRule(_localctx, 32, RULE_literal);
		int _la;
		try {
			setState(136);
			switch (_input.LA(1)) {
			case NullLiteral:
			case BooleanLiteral:
			case StringLiteral:
				enterOuterAlt(_localctx, 1);
				{
				setState(134);
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
				setState(135); numericLiteral();
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
		enterRule(_localctx, 34, RULE_numericLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
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
		enterRule(_localctx, 36, RULE_identifierName);
		try {
			setState(142);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(140); match(Identifier);
				}
				break;
			case NullLiteral:
			case BooleanLiteral:
				enterOuterAlt(_localctx, 2);
				{
				setState(141); reservedWord();
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
		enterRule(_localctx, 38, RULE_reservedWord);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
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
		enterRule(_localctx, 40, RULE_typeCast);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146); ((TypeCastContext)_localctx).identifierName = identifierName();
			setState(148);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				setState(147); match(8);
				}
				break;
			}
			setState(150);
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
		public NamespaceContext namespace;
		public LocalNameContext localName;
		public NamespaceContext namespace() {
			return getRuleContext(NamespaceContext.class,0);
		}
		public LocalNameContext localName() {
			return getRuleContext(LocalNameContext.class,0);
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
		enterRule(_localctx, 42, RULE_functionName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(155);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(152); ((FunctionNameContext)_localctx).namespace = namespace();
				setState(153); match(3);
				}
				break;
			}
			setState(157); ((FunctionNameContext)_localctx).localName = localName();
			setState(158);
			if (!(isValidFunction((((FunctionNameContext)_localctx).namespace!=null?_input.getText(((FunctionNameContext)_localctx).namespace.start,((FunctionNameContext)_localctx).namespace.stop):null), (((FunctionNameContext)_localctx).localName!=null?_input.getText(((FunctionNameContext)_localctx).localName.start,((FunctionNameContext)_localctx).localName.stop):null)))) throw new FailedPredicateException(this, "isValidFunction($namespace.text, $localName.text)");
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

	public static class NamespaceContext extends ParserRuleContext {
		public IdentifierNameContext identifierName() {
			return getRuleContext(IdentifierNameContext.class,0);
		}
		public NamespaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterNamespace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitNamespace(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitNamespace(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceContext namespace() throws RecognitionException {
		NamespaceContext _localctx = new NamespaceContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_namespace);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(160); identifierName();
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

	public static class LocalNameContext extends ParserRuleContext {
		public IdentifierNameContext identifierName() {
			return getRuleContext(IdentifierNameContext.class,0);
		}
		public LocalNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).enterLocalName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ScrapListener ) ((ScrapListener)listener).exitLocalName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ScrapVisitor ) return ((ScrapVisitor<? extends T>)visitor).visitLocalName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LocalNameContext localName() throws RecognitionException {
		LocalNameContext _localctx = new LocalNameContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_localName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(162); identifierName();
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
		case 20: return typeCast_sempred((TypeCastContext)_localctx, predIndex);

		case 21: return functionName_sempred((FunctionNameContext)_localctx, predIndex);
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
		case 1: return isValidFunction((((FunctionNameContext)_localctx).namespace!=null?_input.getText(((FunctionNameContext)_localctx).namespace.start,((FunctionNameContext)_localctx).namespace.stop):null), (((FunctionNameContext)_localctx).localName!=null?_input.getText(((FunctionNameContext)_localctx).localName.start,((FunctionNameContext)_localctx).localName.stop):null));
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\30\u00a7\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\3\2\3\2\7\2\65\n\2\f\2\16\28\13\2\3\3\3\3\3\3\3\3\3\3\5\3?\n\3\3\3\3"+
		"\3\3\3\3\4\3\4\3\4\7\4G\n\4\f\4\16\4J\13\4\3\5\3\5\5\5N\n\5\3\6\3\6\3"+
		"\6\3\7\3\7\3\7\3\7\3\b\5\bX\n\b\3\t\3\t\3\t\3\n\5\n^\n\n\3\13\3\13\3\f"+
		"\3\f\3\r\3\r\3\16\3\16\3\16\5\16i\n\16\3\17\3\17\3\17\3\17\3\20\3\20\3"+
		"\20\3\20\3\20\3\20\7\20u\n\20\f\20\16\20x\13\20\3\20\3\20\5\20|\n\20\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u0087\n\21\3\22\3\22"+
		"\5\22\u008b\n\22\3\23\3\23\3\24\3\24\5\24\u0091\n\24\3\25\3\25\3\26\3"+
		"\26\5\26\u0097\n\26\3\26\3\26\3\27\3\27\3\27\5\27\u009e\n\27\3\27\3\27"+
		"\3\27\3\30\3\30\3\31\3\31\3\31\2\2\32\2\4\6\b\n\f\16\20\22\24\26\30\32"+
		"\34\36 \"$&(*,.\60\2\5\4\2\16\17\24\24\3\2\20\22\3\2\16\17\u009d\2\62"+
		"\3\2\2\2\49\3\2\2\2\6C\3\2\2\2\bM\3\2\2\2\nO\3\2\2\2\fR\3\2\2\2\16W\3"+
		"\2\2\2\20Y\3\2\2\2\22]\3\2\2\2\24_\3\2\2\2\26a\3\2\2\2\30c\3\2\2\2\32"+
		"h\3\2\2\2\34j\3\2\2\2\36{\3\2\2\2 \u0086\3\2\2\2\"\u008a\3\2\2\2$\u008c"+
		"\3\2\2\2&\u0090\3\2\2\2(\u0092\3\2\2\2*\u0094\3\2\2\2,\u009d\3\2\2\2."+
		"\u00a2\3\2\2\2\60\u00a4\3\2\2\2\62\66\5\4\3\2\63\65\5\4\3\2\64\63\3\2"+
		"\2\2\658\3\2\2\2\66\64\3\2\2\2\66\67\3\2\2\2\67\3\3\2\2\28\66\3\2\2\2"+
		"9:\5\30\r\2:>\7\5\2\2;<\5*\26\2<=\7\3\2\2=?\3\2\2\2>;\3\2\2\2>?\3\2\2"+
		"\2?@\3\2\2\2@A\5\6\4\2AB\7\b\2\2B\5\3\2\2\2CH\5\b\5\2DE\7\3\2\2EG\5\b"+
		"\5\2FD\3\2\2\2GJ\3\2\2\2HF\3\2\2\2HI\3\2\2\2I\7\3\2\2\2JH\3\2\2\2KN\5"+
		"\n\6\2LN\5\f\7\2MK\3\2\2\2ML\3\2\2\2N\t\3\2\2\2OP\5,\27\2PQ\5\16\b\2Q"+
		"\13\3\2\2\2RS\7\4\2\2ST\5,\27\2TU\5\16\b\2U\r\3\2\2\2VX\5\20\t\2WV\3\2"+
		"\2\2WX\3\2\2\2X\17\3\2\2\2YZ\5\26\f\2Z[\5\22\n\2[\21\3\2\2\2\\^\5\24\13"+
		"\2]\\\3\2\2\2]^\3\2\2\2^\23\3\2\2\2_`\5\36\20\2`\25\3\2\2\2ab\5\"\22\2"+
		"b\27\3\2\2\2cd\5&\24\2d\31\3\2\2\2ei\5\36\20\2fi\5 \21\2gi\5\"\22\2he"+
		"\3\2\2\2hf\3\2\2\2hg\3\2\2\2i\33\3\2\2\2jk\5&\24\2kl\7\5\2\2lm\5\32\16"+
		"\2m\35\3\2\2\2no\7\7\2\2o|\7\f\2\2pq\7\7\2\2qv\5\34\17\2rs\7\t\2\2su\5"+
		"\34\17\2tr\3\2\2\2ux\3\2\2\2vt\3\2\2\2vw\3\2\2\2wy\3\2\2\2xv\3\2\2\2y"+
		"z\7\f\2\2z|\3\2\2\2{n\3\2\2\2{p\3\2\2\2|\37\3\2\2\2}~\7\6\2\2~\u0087\7"+
		"\13\2\2\177\u0080\7\6\2\2\u0080\u0081\5\32\16\2\u0081\u0082\7\t\2\2\u0082"+
		"\u0083\5\32\16\2\u0083\u0084\3\2\2\2\u0084\u0085\7\f\2\2\u0085\u0087\3"+
		"\2\2\2\u0086}\3\2\2\2\u0086\177\3\2\2\2\u0087!\3\2\2\2\u0088\u008b\t\2"+
		"\2\2\u0089\u008b\5$\23\2\u008a\u0088\3\2\2\2\u008a\u0089\3\2\2\2\u008b"+
		"#\3\2\2\2\u008c\u008d\t\3\2\2\u008d%\3\2\2\2\u008e\u0091\7\23\2\2\u008f"+
		"\u0091\5(\25\2\u0090\u008e\3\2\2\2\u0090\u008f\3\2\2\2\u0091\'\3\2\2\2"+
		"\u0092\u0093\t\4\2\2\u0093)\3\2\2\2\u0094\u0096\5&\24\2\u0095\u0097\7"+
		"\n\2\2\u0096\u0095\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0098\3\2\2\2\u0098"+
		"\u0099\6\26\2\3\u0099+\3\2\2\2\u009a\u009b\5.\30\2\u009b\u009c\7\5\2\2"+
		"\u009c\u009e\3\2\2\2\u009d\u009a\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u009f"+
		"\3\2\2\2\u009f\u00a0\5\60\31\2\u00a0\u00a1\6\27\3\3\u00a1-\3\2\2\2\u00a2"+
		"\u00a3\5&\24\2\u00a3/\3\2\2\2\u00a4\u00a5\5&\24\2\u00a5\61\3\2\2\2\20"+
		"\66>HMW]hv{\u0086\u008a\u0090\u0096\u009d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}