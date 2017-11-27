package molab.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ProcessUtil {
	
	private static final String MULTICHAIN_NAME = "huihong";

	public static void restartMultichaind() {
    	if(kill()) {
    		start();
    	} else {
    		// TODO warn is necessary
    	}
    }
    
    private static boolean kill() {
    	String command = "killall -9 -r multichaind*";
		
		try {  
			Process proc = Runtime.getRuntime().exec(command); 
			ArrayList<String> errorOutput = new ArrayList<String>();
            ArrayList<String> stdOutput = new ArrayList<String>();
            int status = grabProcessOutput(proc, errorOutput, stdOutput, true);
			if (status != 0) {
	            return false;
	        }
		} catch (IOException | InterruptedException e) {  
			return false;
		}
		return true;
    }
    
    private static void start() {
    	String command = "multichaind " + MULTICHAIN_NAME + " -daemon";
    	
    	try {  
			Process proc = Runtime.getRuntime().exec(command); 
			ArrayList<String> errorOutput = new ArrayList<String>();
            ArrayList<String> stdOutput = new ArrayList<String>();
            int status = grabProcessOutput(proc, errorOutput, stdOutput, true);
			if (status == 0) {
				// success
			}
		} catch (IOException | InterruptedException e) {  
		}
    }
    
	private static int grabProcessOutput(final Process process, final ArrayList<String> errorOutput,
			final ArrayList<String> stdOutput, boolean waitForReaders) throws InterruptedException {
		// read the lines as they come. if null is returned, it's
		// because the process finished
		Thread t1 = new Thread("") { //$NON-NLS-1$
			@Override
			public void run() {
				// create a buffer to read the stderr output
				InputStreamReader is = new InputStreamReader(process.getErrorStream());
				BufferedReader errReader = new BufferedReader(is);

				try {
					while (true) {
						String line = errReader.readLine();
						if (line != null) {
							errorOutput.add(line);
						} else {
							break;
						}
					}
				} catch (IOException e) {
					// do nothing.
				}
			}
		};

		Thread t2 = new Thread("") { //$NON-NLS-1$
			@Override
			public void run() {
				InputStreamReader is = new InputStreamReader(process.getInputStream());
				BufferedReader outReader = new BufferedReader(is);

				try {
					while (true) {
						String line = outReader.readLine();
						if (line != null) {
							stdOutput.add(line);
						} else {
							break;
						}
					}
				} catch (IOException e) {
					// do nothing.
				}
			}
		};

		t1.start();
		t2.start();

		// it looks like on windows process#waitFor() can return
		// before the thread have filled the arrays, so we wait for both threads
		// and the
		// process itself.
		if (waitForReaders) {
			try {
				t1.join();
			} catch (InterruptedException e) {
			}
			try {
				t2.join();
			} catch (InterruptedException e) {
			}
		}

		// get the return code from the process
		return process.waitFor();
	}

}
