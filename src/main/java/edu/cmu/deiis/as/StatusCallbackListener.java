package edu.cmu.deiis.as;


import java.util.List;

import org.apache.uima.aae.client.UimaAsBaseCallbackListener;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.EntityProcessStatus;


  public class StatusCallbackListener extends UimaAsBaseCallbackListener
  {
    //Method called when the processing of a Document is completed.
    public void entityProcessComplete(CAS aCas, EntityProcessStatus aStatus) {
     if (aStatus != null && aStatus.isException()) 
     {
       List exceptions = aStatus.getExceptions();
       for (int i = 0; i < exceptions.size(); i++) 
       {
         ((Throwable) exceptions.get(i)).printStackTrace();
       }
       //uimaAsEngine.stop();
       return;
     }
    
     //Process the retrieved Cas here
  }
}
