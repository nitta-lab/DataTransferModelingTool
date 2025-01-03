package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import algorithms.*;
import code.ast.CompilationUnit;
import code.ast.TypeDeclaration;
import generators.DataTransferMethodAnalyzer;
import generators.JavaCodeGenerator;
import generators.JavaMethodBodyGenerator;
import models.dataFlowModel.*;
import parser.*;
import parser.exceptions.ExpectedAssignment;
import parser.exceptions.ExpectedChannel;
import parser.exceptions.ExpectedChannelName;
import parser.exceptions.ExpectedDoubleQuotation;
import parser.exceptions.ExpectedEquals;
import parser.exceptions.ExpectedInOrOutOrRefKeyword;
import parser.exceptions.ExpectedLeftCurlyBracket;
import parser.exceptions.ExpectedRHSExpression;
import parser.exceptions.ExpectedRightBracket;
import parser.exceptions.ExpectedStateTransition;
import parser.exceptions.WrongLHSExpression;
import parser.exceptions.WrongRHSExpression;

public class CodeGeneratorTest {
	public static void main(String[] args) {
		File file = new File("models/POS.model");
		try {
			Parser parser = new Parser(new BufferedReader(new FileReader(file)));
			DataTransferModel model;
			try {
				model = parser.doParse();
				DataFlowGraph graph = DataTransferModelAnalyzer.createDataFlowGraphWithStateStoringAttribute(model);
				DataTransferModelAnalyzer.annotateWithSelectableDataTransferAttiribute(graph);
				DataTransferMethodAnalyzer.decideToStoreResourceStates(graph);
				ArrayList<CompilationUnit> codetree = JavaMethodBodyGenerator.doGenerate(graph, model, JavaCodeGenerator.doGenerate(graph, model));
				System.out.println(codetree);
			} catch (ExpectedChannel | ExpectedChannelName | ExpectedLeftCurlyBracket | ExpectedInOrOutOrRefKeyword
					| ExpectedStateTransition | ExpectedEquals | ExpectedRHSExpression | WrongLHSExpression
					| WrongRHSExpression | ExpectedRightBracket | ExpectedAssignment | ExpectedDoubleQuotation e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
