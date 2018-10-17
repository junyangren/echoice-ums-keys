package org.echoice.ums.quartz;

import org.echoice.ums.service.UmsServiceFactory;

public class QuartzTriggerRunner {
	private static int jobSeq=0;
	public static void runGroupSyncTask(){
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				UmsServiceFactory.getUmsCommonService().commitSyncGroup();
			}
		});
		
		thread.start();
	}
}
