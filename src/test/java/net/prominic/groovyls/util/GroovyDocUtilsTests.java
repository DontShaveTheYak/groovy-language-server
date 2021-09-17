package net.prominic.groovyls.util;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CompileUnit;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.Phases;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import net.prominic.groovyls.compiler.control.GroovyLSCompilationUnit;
import net.prominic.groovyls.config.CompilationUnitFactory;

class GroovyDocUtilsTests {

  private static final String PATH_WORKSPACE = "./build/test_workspace/";

	@Test
	void testDidRemoveSingleLineComment() {
    String expected = "SomeMethod";
		String docString = "/** " + expected + " */";

    String result = GroovyDocUtils.removeComment(docString);

		Assertions.assertEquals(expected, result);
	}

  @Test
	void testDidRemoveMultiLineComment() {
    String expected = "SomeMethod";

    StringBuilder docString = new StringBuilder();

    docString.append("/**\n");
    docString.append("* " + expected + "\n");
    docString.append("*/");

    String result = GroovyDocUtils.removeComment(docString.toString());

		Assertions.assertEquals(expected + "\n", result.toString());
	}

  @Test
	void testDidGetDocString() {

    CompilationUnitFactory cf = new CompilationUnitFactory();

    FileContentsTracker fileContentsTracker = new FileContentsTracker();

    Path workspaceRoot = Paths.get(System.getProperty("user.dir")).resolve(PATH_WORKSPACE);

    GroovyLSCompilationUnit cp = cf.create(workspaceRoot, fileContentsTracker);

    StringBuilder funcDefinition = new StringBuilder();

    String expected = "SomeFunction";

    funcDefinition.append("class MyClass {\n");
    funcDefinition.append("  /** " + expected + " */" + "\n");
    funcDefinition.append("  def myFunction() {}\n");
    funcDefinition.append("}");

    cp.addSource("test.groovy", funcDefinition.toString());
    cp.compile(Phases.SEMANTIC_ANALYSIS);
    CompileUnit ast = cp.getAST();
    ClassNode parent = ast.getClasses().get(0);
    MethodNode child = parent.getMethods().get(0);

    String result = GroovyDocUtils.getDocString(child);

		Assertions.assertEquals(expected, result);
	}

  @Test
	void testDidNotGetDocString() {

    AnnotatedNode node = new AnnotatedNode();
    String expected = "";

    String docString = GroovyDocUtils.getDocString(node);

		Assertions.assertEquals(expected, docString);
	}

}