package sndcld;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import main.java.com.soundcloud.api.ApiWrapper;
import main.java.com.soundcloud.api.Http;
import main.java.com.soundcloud.api.Request;
import main.java.com.soundcloud.api.Token;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Wrapper {
	final String clientId;
	final String clientSecret;
	final String userName;
	final String password;
	String scope = null;
	ApiWrapper wrapper = null;
	Token token = null;
	
	public Wrapper(String clientIdInput, String clientSecretInput, String userNameInput, String passwordInput) {
		this.clientId = clientIdInput;
		this.clientSecret = clientSecretInput;
		this.userName = userNameInput;
		this.password = passwordInput;
		this.createWrapper();
	}
	
	public void createWrapper() {
		this.wrapper = new ApiWrapper(
				clientId,
				clientSecret,
                null    /* redirect URI */,
                null    /* token */);
	}
	
	public Token login() throws IOException {
		this.token = this.wrapper.login(this.userName, this.password);
        System.out.println("got token from server: " + this.token);
		return this.token;
	}
	
	public Token login(String scopeInput) throws IOException {
		this.scope = scopeInput;
		this.token = this.wrapper.login(this.userName, this.password, this.scope);
        System.out.println("got token from server: " + this.token);
		return this.token;
	}
	
	public void getRessource() throws IOException, ParseException {
		String streamUrl = "https://api.soundcloud.com/tracks/150419500/stream";
		final Request resource = Request.to("tracks/150419500/stream");
		
		
		// get a specific sound
		//final Request resource = Request.to("tracks/150419500");
		/*		.with("track[user]", 198921422)
				.withFile("track[asset_data]", new File("track.mp3"));*/
		
		// getting my favorites
		//final Request resource = Request.to("me/favorites");
        System.out.println("GET " + resource);
        
        HttpResponse resp = this.wrapper.get(resource);
        if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            System.out.println("\n" + Http.formatJSON(Http.getString(resp)));
        } else {
        	
            System.err.println("Invalid status received: " + resp.getStatusLine());
            
            HttpEntity entity = resp.getEntity();
            System.out.println("Entity:"+entity.getContentType());
            String content = Http.formatJSON(Http.getString(resp));
            System.out.println("\n" + content);
            
            //resp.getEntity().getContent()
            //formatJSON(Http.getString(resp);
            
            
            JSONObject json = (JSONObject) new JSONParser().parse(content);
            System.out.println("location=" + json.get("location"));
            
            String song_dir = "C:\\Users\\Stephan\\songs\\";
            URLConnection conn = new URL(json.get("location").toString()).openConnection();
            InputStream is = conn.getInputStream();

            OutputStream outstream = new FileOutputStream(new File(song_dir + "song.mp3"));
            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) > 0) {
                outstream.write(buffer, 0, len);
            }
            outstream.close();
            
            
            /*URL url = new URL(streamUrl);
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            String body = IOUtils.toString(in, encoding);
            System.out.println(body);*/
            
        }
	}
}
