package edu.cmu.deiis.as;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.adapter.jms.client.BaseUIMAAsynchronousEngine_impl;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JFSIndexRepository;
import org.apache.uima.jcas.cas.TOP;
import org.cleartk.ne.type.NamedEntityMention;

import edu.cmu.deiis.types.Answer;

public class scnlpClient extends JCasAnnotator_ImplBase
{
  public void process(JCas aJCas) {
    
    //create Asynchronous Client API
    UimaAsynchronousEngine uimaAsEngine = new BaseUIMAAsynchronousEngine_impl();
    uimaAsEngine.addStatusCallbackListener(new StatusCallbackListener()); 
    
    //create Map to pass server URI and Endpoint parameters
    Map<String,Object> appCtx = new HashMap<String,Object>();
    // Add Broker URI on local machine
    appCtx.put(UimaAsynchronousEngine.ServerUri, "tcp://mu.lti.cs.cmu.edu:61616");
    // Add Queue Name
    appCtx.put(UimaAsynchronousEngine.Endpoint, "ScnlpQueue");
    // Add the Cas Pool Size
    appCtx.put(UimaAsynchronousEngine.CasPoolSize, 2);
    try
    {
      //initialize
      uimaAsEngine.initialize(appCtx);
      //send request and receive answer
      System.out.println("******UIMA AS client send request to service********");
      uimaAsEngine.sendAndReceiveCAS(aJCas.getCas());
      System.out.println("*****UIMA AS client receive HW2 AEE Annotations*****");
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
      
  }
  
}
