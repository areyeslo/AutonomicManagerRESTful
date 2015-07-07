package RESTful.clientLibrary.MAPEK;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServlet;


//@Path("/crontab")
public class crontabMonitor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private Timer timer;
    /**
     * Default constructor. 
     */
    public crontabMonitor() {
        
    	timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run()
			{
				System.out.print("Test");// TODO: we need to replace it with an appropriate function 
			}
		}, 5*60*1000, 5*60*1000);
    }
}

