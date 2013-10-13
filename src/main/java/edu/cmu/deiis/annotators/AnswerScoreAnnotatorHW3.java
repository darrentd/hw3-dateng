package edu.cmu.deiis.annotators;

import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JFSIndexRepository;
import org.apache.uima.jcas.cas.TOP;
import org.cleartk.ne.type.NamedEntity;
import org.cleartk.ne.type.NamedEntityMention;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.NGram;
import edu.cmu.deiis.types.Question;

public class AnswerScoreAnnotatorHW3 extends JCasAnnotator_ImplBase{
  
  public void process(JCas aJCas) 
  {
    //get Quesion
    FSIndex quesIndex = aJCas.getAnnotationIndex(Question.type);
    Iterator quesIter = quesIndex.iterator();
    Question ques = (Question) quesIter.next();
    
    //find Question NGram
    FSIndex qNGramIndex = aJCas.getAnnotationIndex(NGram.type);
    Iterator qNGramIter = qNGramIndex.iterator();
    NGram qNGram = new NGram(aJCas); //keep javac quiet
    while (qNGramIter.hasNext()) 
    {
      qNGram = (NGram) qNGramIter.next();
      if(qNGram.getBegin()==ques.getBegin())
        break;
    }
    
    //assign score to answer
    FSIndex ansIndex = aJCas.getAnnotationIndex(Answer.type);
    Iterator ansIter = ansIndex.iterator();
    Answer ans; 
    while(ansIter.hasNext())
    {
      //get an Answer
      ans = (Answer) ansIter.next();
      //find corresponding aNGram
      FSIndex aNGramIndex = aJCas.getAnnotationIndex(NGram.type);
      Iterator aNGramIter = aNGramIndex.iterator();
      NGram aNGram = new NGram(aJCas); //keep javac quiet
      while (aNGramIter.hasNext()) 
      {
        aNGram = (NGram) aNGramIter.next();
        if(aNGram.getBegin()==ans.getBegin())
          break;
      }
      
      //count score
      double match_count = 0;
      String ques_ng;
      String ans_ng;
      for(int i=0; i<qNGram.getElements().size(); i++)
      {
        ques_ng = aJCas.getDocumentText().substring(qNGram.getElements(i).getBegin(), qNGram.getElements(i).getEnd());
        for(int j=0; j<aNGram.getElements().size(); j++)
        {
          ans_ng = aJCas.getDocumentText().substring(aNGram.getElements(j).getBegin(), aNGram.getElements(j).getEnd());
          if(ans_ng.equals(ques_ng))
          {
            match_count++;
            break;
          }
        }
      }
      double answer_score = match_count/aNGram.getElements().size();
      AnswerScore as_annotation = new AnswerScore(aJCas);
      as_annotation.setBegin(ans.getBegin());
      as_annotation.setEnd(ans.getEnd());
      as_annotation.setAnswer(ans);
      as_annotation.setScore(answer_score+0.1);
      as_annotation.setConfidence(1);
      as_annotation.setCasProcessorId("cmu.edu.deiis.annotators.AnswerScoreAnnotator");
      
      as_annotation.addToIndexes();
      
      
      //Use Name Entity to help scoring
      JFSIndexRepository repo = aJCas.getJFSIndexRepository();
      FSIterator<TOP> iter = repo.getAllIndexedFS(NamedEntityMention.type);
      while(iter.hasNext())
      {
        NamedEntityMention nem = (NamedEntityMention) iter.next();
        //System.out.println(nem.toString());
        if(nem.getMentionType()!=null && nem.getMentionType().equals("PERSON"))
        {
          String name = aJCas.getDocumentText().substring(nem.getBegin(), nem.getEnd());
          //System.out.println(name);
        }  
      }
      
      
    }
  }
}



