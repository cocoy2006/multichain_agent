package molab;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.azazar.bitcoin.jsonrpcclient.BitcoinJSONRPCClient;

@SpringBootApplication
@Component
public class AgentApplication implements CommandLineRunner {

	@Autowired
	private Properties properties;
	
	public static void main(String[] args) {
		SpringApplication.run(AgentApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(properties.getRpcuser(), properties.getRpcpassword().toCharArray());
			}
		});
		BitcoinJSONRPCClient.getInstance(null, null, properties.getRpcurl());
	}

}
