package org.gwtproject.resource.processor;

import javax.tools.JavaFileObject;

import org.junit.Test;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;

import static com.google.testing.compile.Compiler.*;
import static com.google.testing.compile.CompilationSubject.*;

public class ResourceProcessorIT {
  
  private static final String BASE_DIR = "org/gwtproject/resource/processor/test";

  @Test
  public void process_WrongAnnotationPlacement() {
    // given
    JavaFileObject input = 
        JavaFileObjects.forResource(BASE_DIR + "/WrongAnnotationPlacementBundle.java");
    
    //when
    Compilation compilation = javac().withProcessors(new ResourceProcessor()).compile(input);
    
    //then
    assertThat(compilation).hadWarningContaining("applied on a class instead of an interface");
  }
  
  @Test
  public void process_Empty() {
    // given
    JavaFileObject input = 
        JavaFileObjects.forResource(BASE_DIR + "/EmptyBundle.java");
    
    JavaFileObject output = 
        JavaFileObjects.forResource(BASE_DIR + "/EmptyBundle_ResourceBundle.expected.java");
    
    //when
    Compilation compilation = javac().withProcessors(new ResourceProcessor()).compile(input);
    
    //then
    assertThat(compilation).succeeded();
    assertThat(compilation)
      .generatedSourceFile("org.gwtproject.resource.processor.test.EmptyBundle_ResourceBundle")
      .hasSourceEquivalentTo(output);
  }
}
