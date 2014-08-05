package jobs;

import models.ErrorType;
import models.TransactionType;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

/**
 * Created by renatosierra on 7/24/14.
 */
@OnApplicationStart
public class InitialValues extends Job {
    public void doJob(){
        if(TransactionType.count()==0)
            Fixtures.loadModels("TransactionType.yml");

        if(ErrorType.count()==0)
            Fixtures.loadModels("ErrorType.yml");

    }
}
