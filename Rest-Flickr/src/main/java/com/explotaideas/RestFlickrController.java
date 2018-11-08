package com.explotaideas;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Controller
public class RestFlickrController {
	//Agrega tu API KEY Solicitada de FLickr.
	//Please use your own Flickr API Key. This is a fake API key
	private static final String FLICKR_API_KEY = "XXXXX";
	@RequestMapping("/flickr-search")
	public String flickrSearch(Model model, @RequestParam String search) throws UnsupportedEncodingException, RestClientException, URISyntaxException { 
		
		String url = "https://api.flickr.com/services/rest/"+
				"?api_key="+FLICKR_API_KEY+"&format=json"+
				"&method=flickr.photos.search&text="+URLEncoder.encode(search, "UTF-8")+"&nojsoncallback=?";
		
		RestTemplate rest = new RestTemplate();
		
		ObjectNode data = rest.getForObject(new URI(url), ObjectNode.class);
		
		model.addAttribute("search",search);
				
		//System.out.println(data);
		
		List<String> photos = new ArrayList<>();
		model.addAttribute("photos",photos);
		
		//https://api.flickr.com/services/rest/?api_key=74b8e65f0fd5a678b50935314c3bdb5e&format=json&method=flickr.photos.search&text=cat&nojsoncallback=?
		
		ObjectNode photosObj = (ObjectNode) data.get("photos");
		ArrayNode photosArray = (ArrayNode) photosObj.get("photo");
				
		for (int i = 0; i < photosArray.size(); i++) {
			JsonNode photo = photosArray.get(i);
			
			String farm = photo.get("farm").asText();
			String server = photo.get("server").asText();
			String id = photo.get("id").asText();
			String secret = photo.get("secret").asText();
			
			String photoUrl = "https://farm"+farm+".staticflickr.com/"+server+"/"+id+"_"+secret+"_m.jpg";
			if(photos.size()<9) {
				photos.add(photoUrl);
			}else {
				break;
			}
						
		}
		
		return "rest-flickr-search";
	}

}
