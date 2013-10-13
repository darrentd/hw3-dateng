package edu.cmu.deiis.as;

import java.util.HashMap;
import java.util.Map;

import org.apache.uima.aae.client.UimaAsynchronousEngine;
import org.apache.uima.adapter.jms.client.BaseUIMAAsynchronousEngine_impl;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;

public class Hw2AEEClient extends JCasAnnotator_ImplBase
{
  public void process(JCas aJCas) {
    //create Asynchronous Client API
    UimaAsynchronousEngine uimaAsEngine = new BaseUIMAAsynchronousEngine_impl();
    uimaAsEngine.addStatusCallbackListener(new StatusCallbackListener()); 
    
    //create Map to pass server URI and Endpoint parameters
    Map<String,Object> appCtx = new HashMap<String,Object>();
    // Add Broker URI on local machine
    appCtx.put(UimaAsynchronousEngine.ServerUri, "tcp://tengdatekiMacBook-Air.local:61616");
    // Add Queue Name
    appCtx.put(UimaAsynchronousEngine.Endpoint, "hw2AAEqueue");
    // Add the Cas Pool Size
    appCtx.put(UimaAsynchronousEngine.CasPoolSize, 2);
    
    try
    {
      //initialize
      uimaAsEngine.initialize(appCtx);
      //send request and receive answer
      System.out.println("*********UIMA AS client send request to service**********");
      uimaAsEngine.sendAndReceiveCAS(aJCas.getCas());
      System.out.println("***UIMA AS client receive Answerscore Annotation***");
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
    
  }
}