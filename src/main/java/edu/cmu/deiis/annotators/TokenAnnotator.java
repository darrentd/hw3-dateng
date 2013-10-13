package edu.cmu.deiis.annotators;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Token;

public class TokenAnnotator extends JCasAnnotator_ImplBase{
  
  public void process(JCas aJCas) 
  {
    String docText = aJCas.getDocumentText();
    int current_index = 0; 
    int end_index;
    String sentence;
    while(!docText.isEmpty())
    {
      if (docText.startsWith("Q "))
      {
        current_index += 2;//pass prefix "Q 1/0 " and
        sentence = docText.substring(2, docText.indexOf("\n")+1);
      }
      else// if(docText.startsWith("A "))
      {
        current_index += 4; //pass prefix "Q 1/0 " and "A 1/0 "
        sentence = docText.substring(4, docText.indexOf("\n")+1);
      }
      while(sentence.indexOf(" ")>0)
      {
        
        Token annotation = new Token(aJCas);
        annotation.setBegin(current_index);
      //Eliminate punctuation
        end_index = current_index+sentence.indexOf(" ");
        String token  = sentence.substring(0, sentence.indexOf(" "));
        while(!((token.charAt(end_index-current_index-1)>='a' 
                && token.charAt(end_index-current_index-1)<='z')||
                (token.charAt(end_index-current_index-1)>='A' 
                && token.charAt(end_index-current_index-1)<='Z')||
                (token.charAt(end_index-current_index-1)>='0' 
                && token.charAt(end_index-current_index-1)<='9')))
        {
          end_index--;
        }
        annotation.setEnd(end_index);
        annotation.setConfidence(1);
        annotation.setCasProcessorId("cmu.edu.deiis.annotators.TokenAnnotator");
        annotation.addToIndexes();
        
        current_index+=sentence.indexOf(" ")+1;
        sentence = sentence.substring(sentence.indexOf(" ")+1);        
      }
      Token annotation = new Token(aJCas);
      annotation.setBegin(current_index);
      //Eliminate punctuation
      end_index = current_index+sentence.indexOf("\n");
      String token  = sentence.substring(0, sentence.indexOf("\n"));
      while(!((token.charAt(end_index-current_index-1)>='a' 
        && token.charAt(end_index-current_index-1)<='z')||
        (token.charAt(end_index-current_index-1)>='A' 
        && token.charAt(end_index-current_index-1)<='Z')||
        (token.charAt(end_index-current_index-1)>='0' 
        && token.charAt(end_index-current_index-1)<='9')))
      {
        end_index--;
      }
      annotation.setEnd(end_index);
      annotation.setConfidence(1);
      annotation.setCasProcessorId("cmu.edu.deiis.annotators.TokenAnnotator");
      annotation.addToIndexes();
      
      current_index += sentence.indexOf("\n")+1;
      docText = docText.substring(docText.indexOf("\n")+1);
    }
    
  }
}