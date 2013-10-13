package edu.cmu.deiis.annotators;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.NGram;
import edu.cmu.deiis.types.Question;
import edu.cmu.deiis.types.Token;



public class NGramAnnotator extends JCasAnnotator_ImplBase{
  
  public void process(JCas aJCas) {
    
    Comparator<Token> comparator = new Comparator<Token>()
    {
      public int compare(Token t1, Token t2) 
      {
        if(t1.getBegin()>t2.getBegin())
          return 1;
        return 0;
      }
     };
    //Add Answer NGram
    //Add Answer NGram
    //Add Answer NGram
    FSIndex ansIndex = aJCas.getAnnotationIndex(Answer.type);
    Iterator ansIter = ansIndex.iterator();   
    while (ansIter.hasNext()) 
    {
      Answer ans = (Answer)ansIter.next();
      int sentence_begin = ans.getBegin();
      int sentence_end = ans.getEnd();
      NGram ng = new NGram(aJCas);
      ng.setBegin(sentence_begin);
      ng.setEnd(sentence_end);
      
      FSIndex tokenIndex = aJCas.getAnnotationIndex(Token.type);
      Iterator tokenIter = tokenIndex.iterator();
      ArrayList<Token> toks_in_sentece = new ArrayList<Token>();
      ArrayList<Token> expand_toks = new ArrayList<Token>();
      while(tokenIter.hasNext())
      {
        Token tok = (Token)tokenIter.next();
        if(tok.getBegin()>=sentence_begin && tok.getEnd()<=sentence_end)
          toks_in_sentece.add(tok);
      }
      Collections.sort(toks_in_sentece, comparator);
      for(int i=0; i<toks_in_sentece.size(); i++)
      {
        for(int j=1; j<3 && (i+j<toks_in_sentece.size()); j++)
        {
          Token ng_tok = new Token(aJCas);
          ng_tok.setBegin(toks_in_sentece.get(i).getBegin());
          ng_tok.setEnd(toks_in_sentece.get(i+j).getEnd());
          ng_tok.setConfidence(1);
          ng_tok.setCasProcessorId("cmu.edu.deiis.annotators.NGramAnnotator");
          expand_toks.add(ng_tok);
        }
      }
      
      FSArray NGram_array = new FSArray(aJCas,toks_in_sentece.size()+expand_toks.size());
      for(int i=0; i<toks_in_sentece.size(); i++)
      {
        NGram_array.set(i, toks_in_sentece.get(i));
      }
      for(int i=0; i<expand_toks.size(); i++)
      {
        NGram_array.set(toks_in_sentece.size()+i, expand_toks.get(i));
      }
      ng.setElements(NGram_array);
      ng.setElementType("NGram");
      ng.setConfidence(1);
      ng.setCasProcessorId("cmu.edu.deiis.annotators.NGramAnnotator");
      
      ng.addToIndexes();
    }
    
    //Add Question NGram
    //Add Question NGram
    //Add Question NGram
    FSIndex quesIndex = aJCas.getAnnotationIndex(Question.type);
    Iterator quesIter = quesIndex.iterator();
    Question ques = (Question)quesIter.next();
    int sentence_begin = ques.getBegin();
    int sentence_end = ques.getEnd();
    NGram ng = new NGram(aJCas);
    ng.setBegin(sentence_begin);
    ng.setEnd(sentence_end);
    
    FSIndex tokenIndex = aJCas.getAnnotationIndex(Token.type);
    Iterator tokenIter = tokenIndex.iterator();
    ArrayList<Token> toks_in_sentece = new ArrayList<Token>();
    ArrayList<Token> expand_toks = new ArrayList<Token>();
    while(tokenIter.hasNext())
    {
      Token tok = (Token)tokenIter.next();
      if(tok.getBegin()>=sentence_begin && tok.getEnd()<=sentence_end)
        toks_in_sentece.add(tok);
    }
    Collections.sort(toks_in_sentece, comparator);
    for(int i=0; i<toks_in_sentece.size(); i++)
    {
      for(int j=1; j<3 && (i+j<toks_in_sentece.size()); j++)
      {
        Token ng_tok = new Token(aJCas);
        ng_tok.setBegin(toks_in_sentece.get(i).getBegin());
        ng_tok.setEnd(toks_in_sentece.get(i+j).getEnd());
        ng_tok.setConfidence(1);
        ng_tok.setCasProcessorId("cmu.edu.deiis.annotators.NGramAnnotator");
        expand_toks.add(ng_tok);
      }
    }
    FSArray NGram_array = new FSArray(aJCas,toks_in_sentece.size()+expand_toks.size());
    for(int i=0; i<toks_in_sentece.size(); i++)
    {
      NGram_array.set(i, toks_in_sentece.get(i));
    }
    for(int i=0; i<expand_toks.size(); i++)
    {
      NGram_array.set(toks_in_sentece.size()+i, expand_toks.get(i));
    }
    ng.setElements(NGram_array);
    ng.setElementType("NGram");
    ng.setConfidence(1);
    ng.setCasProcessorId("cmu.edu.deiis.annotators.NGramAnnotator");
    
    ng.addToIndexes();
  
   
    
  }
}