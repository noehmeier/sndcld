package sndcld;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.simple.parser.ParseException;

public class Sndcld {
	public static final File WRAPPER_SER = new File("wrapper.ser");

	public static void main(String[] args) throws ClassNotFoundException, IOException, ParseException, JSONException {
		if (args.length < 4) {
            System.err.println("CreateWrapper client_id client_secret login password");
            System.exit(1);
        } else {
            final Wrapper wrapper = new Wrapper(
                    args[0] /* client_id */,
                    args[1] /* client_secret */,
                    args[2] /* user name */,
                    args[3] /* password */
            );
            if (args.length < 6) {
                wrapper.login();
            } else {
            	wrapper.login(args[5] /* scope */);
            }
            
            wrapper.getRessource();

            // in this example the whole wrapper is serialised to disk -
            // in a real application you would just save the tokens and usually have the client_id/client_secret
            // hardcoded in the application, as they rarely change
            //wrapper.toFile(WRAPPER_SER);
            //System.out.println("wrapper serialised to " + WRAPPER_SER);
            
            
        }
	}
}
