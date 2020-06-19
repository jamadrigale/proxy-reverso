package ar.com.mercadolibre.services;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import ar.com.mercadolibre.model.ProxyLog;

public class ProxyRouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		
		from("netty-http:http://0.0.0.0:8180/?matchOnUriPrefix=true")
			.setHeader("bodyBkp", simple("${bodyAs(String)}"))
			.process(new Processor() {
		        public void process(Exchange exchange) throws Exception {
		        	String ip = exchange.getIn().getHeader("CamelNettyRemoteAddress").toString();
		        	ProxyLog pl = new ProxyLog(ip.substring(ip.indexOf("/")+1, ip.indexOf(":")), exchange.getIn().getHeader("CamelHttpPath").toString());
		        	exchange.getIn().setBody(pl);
		       }
		    })
			.to("mongodb://proxydb?database={{mongo.dbname}}&collection=control&operation=insert")
			.log("mongodb= ${body}")
			.setBody(simple("${header.bodyBkp}"))
			.to("http4://{{proxy.host}}/?bridgeEndpoint=true&throwExceptionOnFailure=false");
		
		from("netty-http:http://0.0.0.0:8180/favicon.ico")
			.to("http4://camel.apache.org/favicon-196x196.png?bridgeEndpoint=true");
		
		from("netty-http:http://0.0.0.0:8280/metrics/findall")
			.to("mongodb://proxydb?database={{mongo.dbname}}&collection=control&operation=findAll")
			.marshal().json(JsonLibrary.Jackson, List.class);
	}

}
