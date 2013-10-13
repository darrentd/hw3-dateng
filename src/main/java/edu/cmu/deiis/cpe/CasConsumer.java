/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package edu.cmu.deiis.cpe;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.impl.XmiCasSerializer;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.examples.SourceDocumentInformation;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JFSIndexRepository;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.ProcessTrace;
import org.apache.uima.util.XMLSerializer;
import org.cleartk.ne.type.NamedEntityMention;
import org.xml.sax.SAXException;

import edu.cmu.deiis.types.AnswerScore;

/**
 * A simple CAS consumer that writes the CAS to XMI format.
 * <p>
 * This CAS Consumer takes one parameter:
 * <ul>
 * <li><code>OutputDirectory</code> - path to directory into which output files will be written</li>
 * </ul>
 */
public class CasConsumer extends CasConsumer_ImplBase {

  private double precision_sum;
  private int precision_count;

  public void initialize() throws ResourceInitializationException {
    precision_sum=0;
    precision_count=0;
  }

  /**
   * Processes the CAS which was populated by the TextAnalysisEngines. <br>
   * In this case, the CAS is converted to XMI and written into the output file .
   * 
   * @param aCAS
   *          a CAS which has been populated by the TAEs
   * 
   * @throws ResourceProcessException
   *           if there is an error in processing the Resource
   * 
   * @see org.apache.uima.collection.base_cpm.CasObjectProcessor#processCas(org.apache.uima.cas.CAS)
   */
  public void processCas(CAS aCAS) throws ResourceProcessException {
    Comparator<AnswerScore> comparator = new Comparator<AnswerScore>(){ 
      public int compare(AnswerScore a1, AnswerScore a2) {
        if(a1.getScore()<a2.getScore())
          return 1;
        return 0;
        }
     };
    
    JCas jcas;
    try {
      jcas = aCAS.getJCas();
    } catch (CASException e) {
      throw new ResourceProcessException(e);
    }

    AnswerScore ans_score;
    Iterator it = jcas.getAnnotationIndex(AnswerScore.type).iterator();
    ArrayList<AnswerScore> ans_score_list = new ArrayList<AnswerScore>();
    int correct_ans_count = 0;
     
    while(it.hasNext())
    {
      ans_score = (AnswerScore) it.next();
      ans_score_list.add(ans_score);      
      if(ans_score.getAnswer().getIsCorrect())
        correct_ans_count++;
    }
    
    Collections.sort(ans_score_list,comparator);
    int correct_ans_atN=0;
    for(int i=0; i<ans_score_list.size(); i++)
    {
      ans_score = ans_score_list.get(i);
      String answer = jcas.getDocumentText().substring(ans_score.getBegin(),ans_score.getEnd()-1);
      boolean isCorrect = ans_score.getAnswer().getIsCorrect();
      if(isCorrect)
      {
        System.out.print("+");
        if(i<correct_ans_count)
        {
          correct_ans_atN++;
        }
      }
      else
        System.out.print("-");
      System.out.println(answer + ": " + ans_score.getScore());
    }
    System.out.println("Precision at " + correct_ans_count+ ": "+correct_ans_atN/(double)correct_ans_count+"\n\n");
    
    precision_sum += correct_ans_atN/(double)correct_ans_count;
    precision_count++;
  }

  public void collectionProcessComplete(ProcessTrace arg0) 
  {
    System.out.println("Average Precision: " + precision_sum/precision_count);
  }
}
