/*******************************************************************************
* Copyright (c) 2006, 2008 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl_v10.html
*
* Contributors:
*     IBM Corporation - initial API and implementation
*********************************************************************************/

// This file was generated by LPG

package org.eclipse.cdt.internal.core.dom.lrparser.cpp;

public interface CPPTemplateTypeParameterParsersym {
    public final static int
      TK_asm = 65,
      TK_auto = 26,
      TK_bool = 11,
      TK_break = 77,
      TK_case = 78,
      TK_catch = 119,
      TK_char = 12,
      TK_class = 39,
      TK_const = 23,
      TK_const_cast = 43,
      TK_continue = 79,
      TK_default = 80,
      TK_delete = 67,
      TK_do = 81,
      TK_double = 13,
      TK_dynamic_cast = 44,
      TK_else = 122,
      TK_enum = 57,
      TK_explicit = 27,
      TK_export = 87,
      TK_extern = 28,
      TK_false = 45,
      TK_float = 14,
      TK_for = 82,
      TK_friend = 29,
      TK_goto = 83,
      TK_if = 84,
      TK_inline = 30,
      TK_int = 15,
      TK_long = 16,
      TK_mutable = 31,
      TK_namespace = 60,
      TK_new = 68,
      TK_operator = 8,
      TK_private = 103,
      TK_protected = 104,
      TK_public = 105,
      TK_register = 32,
      TK_reinterpret_cast = 46,
      TK_return = 85,
      TK_short = 17,
      TK_signed = 18,
      TK_sizeof = 47,
      TK_static = 33,
      TK_static_cast = 48,
      TK_struct = 58,
      TK_switch = 86,
      TK_template = 41,
      TK_this = 49,
      TK_throw = 62,
      TK_try = 75,
      TK_true = 50,
      TK_typedef = 34,
      TK_typeid = 51,
      TK_typename = 10,
      TK_union = 59,
      TK_unsigned = 19,
      TK_using = 63,
      TK_virtual = 22,
      TK_void = 20,
      TK_volatile = 24,
      TK_wchar_t = 21,
      TK_while = 76,
      TK_integer = 52,
      TK_floating = 53,
      TK_charconst = 54,
      TK_stringlit = 40,
      TK_identifier = 1,
      TK_Completion = 2,
      TK_EndOfCompletion = 7,
      TK_Invalid = 123,
      TK_LeftBracket = 64,
      TK_LeftParen = 3,
      TK_Dot = 120,
      TK_DotStar = 92,
      TK_Arrow = 106,
      TK_ArrowStar = 90,
      TK_PlusPlus = 37,
      TK_MinusMinus = 38,
      TK_And = 9,
      TK_Star = 6,
      TK_Plus = 35,
      TK_Minus = 36,
      TK_Tilde = 5,
      TK_Bang = 42,
      TK_Slash = 93,
      TK_Percent = 94,
      TK_RightShift = 88,
      TK_LeftShift = 89,
      TK_LT = 55,
      TK_GT = 69,
      TK_LE = 95,
      TK_GE = 96,
      TK_EQ = 97,
      TK_NE = 98,
      TK_Caret = 99,
      TK_Or = 100,
      TK_AndAnd = 101,
      TK_OrOr = 102,
      TK_Question = 107,
      TK_Colon = 73,
      TK_ColonColon = 4,
      TK_DotDotDot = 91,
      TK_Assign = 70,
      TK_StarAssign = 108,
      TK_SlashAssign = 109,
      TK_PercentAssign = 110,
      TK_PlusAssign = 111,
      TK_MinusAssign = 112,
      TK_RightShiftAssign = 113,
      TK_LeftShiftAssign = 114,
      TK_AndAssign = 115,
      TK_CaretAssign = 116,
      TK_OrAssign = 117,
      TK_Comma = 71,
      TK_RightBracket = 118,
      TK_RightParen = 74,
      TK_RightBrace = 72,
      TK_SemiColon = 25,
      TK_LeftBrace = 66,
      TK_ERROR_TOKEN = 61,
      TK_0 = 56,
      TK_EOF_TOKEN = 121;

      public final static String orderedTerminalSymbols[] = {
                 "",
                 "identifier",
                 "Completion",
                 "LeftParen",
                 "ColonColon",
                 "Tilde",
                 "Star",
                 "EndOfCompletion",
                 "operator",
                 "And",
                 "typename",
                 "bool",
                 "char",
                 "double",
                 "float",
                 "int",
                 "long",
                 "short",
                 "signed",
                 "unsigned",
                 "void",
                 "wchar_t",
                 "virtual",
                 "const",
                 "volatile",
                 "SemiColon",
                 "auto",
                 "explicit",
                 "extern",
                 "friend",
                 "inline",
                 "mutable",
                 "register",
                 "static",
                 "typedef",
                 "Plus",
                 "Minus",
                 "PlusPlus",
                 "MinusMinus",
                 "class",
                 "stringlit",
                 "template",
                 "Bang",
                 "const_cast",
                 "dynamic_cast",
                 "false",
                 "reinterpret_cast",
                 "sizeof",
                 "static_cast",
                 "this",
                 "true",
                 "typeid",
                 "integer",
                 "floating",
                 "charconst",
                 "LT",
                 "0",
                 "enum",
                 "struct",
                 "union",
                 "namespace",
                 "ERROR_TOKEN",
                 "throw",
                 "using",
                 "LeftBracket",
                 "asm",
                 "LeftBrace",
                 "delete",
                 "new",
                 "GT",
                 "Assign",
                 "Comma",
                 "RightBrace",
                 "Colon",
                 "RightParen",
                 "try",
                 "while",
                 "break",
                 "case",
                 "continue",
                 "default",
                 "do",
                 "for",
                 "goto",
                 "if",
                 "return",
                 "switch",
                 "export",
                 "RightShift",
                 "LeftShift",
                 "ArrowStar",
                 "DotDotDot",
                 "DotStar",
                 "Slash",
                 "Percent",
                 "LE",
                 "GE",
                 "EQ",
                 "NE",
                 "Caret",
                 "Or",
                 "AndAnd",
                 "OrOr",
                 "private",
                 "protected",
                 "public",
                 "Arrow",
                 "Question",
                 "StarAssign",
                 "SlashAssign",
                 "PercentAssign",
                 "PlusAssign",
                 "MinusAssign",
                 "RightShiftAssign",
                 "LeftShiftAssign",
                 "AndAssign",
                 "CaretAssign",
                 "OrAssign",
                 "RightBracket",
                 "catch",
                 "Dot",
                 "EOF_TOKEN",
                 "else",
                 "Invalid"
             };

    public final static boolean isValidForParser = true;
}
