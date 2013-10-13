package edu.cmu.deiis.annotators;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Question;



public class QuestionAnnotator extends JCasAnnotator_ImplBase{
  
  public void process(JCas aJCas) {
    String docText = aJCas.getDocumentText();
   
    if(docText.startsWith("Q "))
    {
      Question annotation = new Question(aJCas);
      annotation.setBegin(2); 
      annotation.setEnd(docText.indexOf("\n"));
      annotation.setConfidence(1);
      annotation.setCasProcessorId("cmu.edu.deiis.annotators.QuestionAnnotator");
      annotation.addToIndexes();      
      
    }
      
   
    
  }
}