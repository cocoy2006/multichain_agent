package molab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;

import com.azazar.bitcoin.jsonrpcclient.BitcoinException;
import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;

public class RPCTester {

	public static void main(String[] args) throws BitcoinException, IOException, InterruptedException {
		
		int i = 100, j = 1;
		
		//// rpc everytime
		long rpcEStart = System.currentTimeMillis();
		while(j-- > 0) {
			BitcoinJSONRPCClient client2 = getClient();
			System.out.println("" + (System.currentTimeMillis() - rpcEStart));
			client2.sendAssetFrom("1H1pYEkqM8PopYrvv5gHzA988PxM93ZdRVL9k2", "1HzVwzmjYgrJa3er2xFtaBHyWGzRTV2sSPR8Gy", "huihong", 1);
		}
		long rpcEDone = System.currentTimeMillis();
		System.out.println("rpc everytime need " + (rpcEDone - rpcEStart));
			
		//// rpc
		long rpcStart = System.currentTimeMillis();
		BitcoinJSONRPCClient client = getClient();
		System.out.println("" + (System.currentTimeMillis() - rpcStart));
		while(i-- > 0) {
			client.sendAssetFrom("1H1pYEkqM8PopYrvv5gHzA988PxM93ZdRVL9k2", "1HzVwzmjYgrJa3er2xFtaBHyWGzRTV2sSPR8Gy", "huihong", 1);
		}
		long rpcDone = System.currentTimeMillis();
		System.out.println("rpc need " + (rpcDone - rpcStart));
		
		
		
		
		//// console
//		long consoleStart = System.currentTimeMillis();
//		while(j-- > 0) {
//			String command = "multichain-cli hmchain publishfrom 1KDa69m4E2Tbuu8tHwFqycLPhbRotEFnEeFRnH 3230313730363131 3230313730363131";
//			Process proc = Runtime.getRuntime().exec(command); 
//			ArrayList<String> errorOutput = new ArrayList<String>();
//            ArrayList<String> stdOutput = new ArrayList<String>();
//            int status = grabProcessOutput(proc, errorOutput, stdOutput, true);
//			if (status != 0) {
//	            
//	        }
//		}
//		long consoleDone = System.currentTimeMillis();
//		System.out.println("console need " + (consoleDone - consoleStart));
		
		System.exit(0);
	}
	
	private static BitcoinJSONRPCClient getClient() {
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("multichainrpc", "Hf8QuWU3VEWqHDyM9aqB7TDo8QjLkGaU6tmVeVt2SECC".toCharArray());
			}
		});
		try {
			return new BitcoinJSONRPCClient(new URL("http://localhost:2912"));
		} catch (MalformedURLException e) {}
		return null;
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
