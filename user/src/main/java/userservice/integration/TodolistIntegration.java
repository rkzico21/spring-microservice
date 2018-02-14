package userservice.integration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
//@ConfigurationProperties("integration.stores")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodolistIntegration {

	private final LoadBalancerClient loadBalancer;

	private final DiscoveryClient discoveryClient;

	
	@Getter
	@Setter
	private String uri = "http://localhost:8081/todolist";

	public Link getTodolistsByUserLink(Map<String, Object> parameters, String host, String prefix) {

		
		ServiceInstance instance = null;

		List<ServiceInstance> instances = discoveryClient.getInstances("todolistservice");
		
		if(instances.size() > 0 ) {
			instance = instances.get(0);
		 }
		
		

		// TODO: all of the above could be replaced with restTemplate/ribbon
		// The uri would be http://stores
		// traverson.setRestOperations and stuff from Traverson.createDefaultTemplate
		
		
		/*log.info("accessing the todolist at {}…", uri);
		Traverson traverson = new Traverson(uri, MediaTypes.HAL_JSON);
        Link link = traverson.follow("stores", "search", "by-location")
				.withTemplateParameters(parameters).asLink();*/
		
		if(instance != null) {
			URI uri = UriComponentsBuilder.fromHttpUrl( instance.getUri().toString()).path("/todolist/item").
			    query("userid={keyword}").buildAndExpand(parameters.get("userid"))
                .toUri();
			
			String href = uri.toString();
 		    if (host!=null && instance != null) {
			   href = reconstructURI(host, prefix, href);
		    }
 		    
 		  
 		
		    log.info("Found todolist link pointing to {}.", href);

		   return new Link(href, "todolist");
		}
		
		return null;
	}

	private String reconstructURI(String host, String prefix, String href) {
		URI original;
		try {
			original = new URI(href);
		}
		catch (URISyntaxException e) {
			throw new IllegalArgumentException("Cannot create URI from: " + href);
		}
		int port = 80;
		if ("https".equals(original.getScheme())) {
			port = 443;
		}
		if (host.contains(":")) {
			String[] pair = host.split(":");
			host = pair[0];
			port = Integer.valueOf(pair[1]);
		}
		if (host.equals(original.getHost()) && port == original.getPort()) {
			return href;
		}
		String scheme = original.getScheme();
		if (scheme == null) {
			scheme = port == 443 ? "https" : "http";
		}

		StringBuilder sb = new StringBuilder();
		sb.append(scheme).append("://");
		if (StringUtils.hasText(original.getRawUserInfo())) {
			sb.append(original.getRawUserInfo()).append("@");
		}
		sb.append(host);
		if (port >= 0) {
			sb.append(":").append(port);
		}
		
		if(!prefix.isEmpty()){
			sb.append(prefix);
		}
		
		sb.append(original.getRawPath());
		
		if (StringUtils.hasText(original.getRawQuery())) {
			sb.append("?").append(original.getRawQuery());
		}
		if (StringUtils.hasText(original.getRawFragment())) {
			sb.append("#").append(original.getRawFragment());
		}
		return sb.toString();
	}

	public Link defaultLink(Map<String, Object> parameters, String host) {
		return null;
	}
}